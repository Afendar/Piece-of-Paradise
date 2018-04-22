package core;

import core.state.StateManager;
import core.state.StateType;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import javax.swing.JPanel;

public class Game extends JPanel implements Runnable
{
    private boolean m_running, m_paused;
    
    private static final int SCREEN_WIDTH = 800;
    private static final int SCREEN_HEIGHT = 600;
    private static final String VERSION = "1.0.0";
    private static final String NAME = "Piece of Paradise";
    
    private Window m_window;
    
    private Context m_context;
    private StateManager m_stateManager;
    
    private Thread m_tgame;
    public int m_elapsedTime;
    private int m_lastTime;
    private int m_pauseTime;
    private int m_fps;
    
    public Game()
    {
        super();
        init();
    }
    
    private void init()
    {
        m_running = false;
        m_paused = false;
        
        setPreferredSize(new Dimension(SCREEN_WIDTH, SCREEN_HEIGHT));
        setMaximumSize(new Dimension(SCREEN_WIDTH, SCREEN_HEIGHT));
        setMinimumSize(new Dimension(SCREEN_WIDTH, SCREEN_HEIGHT));
        setSize(new Dimension(SCREEN_WIDTH, SCREEN_HEIGHT));
        
        m_context = new Context();
        m_context.m_inputsListeners = InputsListeners.getInstance(this);
        m_context.m_resourceManager = ResourceManager.getInstance();
        
        m_stateManager = new StateManager(m_context);
        m_stateManager.switchTo(StateType.START);
        
        m_window = new Window(this);
        m_window.setSize(SCREEN_WIDTH, SCREEN_HEIGHT);
        
        
        start();
    }

    private void start()
    {
        if(m_running)
        {
            return;
        }
        
        m_running = true;
        m_tgame = new Thread(this, "gameThread");
        m_tgame.start();
    }
    
    public void stop()
    {
        m_running = false;
    }
    
    @Override
    public void run()
    {
        long startTime = System.currentTimeMillis();
        long lastTime = System.nanoTime();
        double nsms = 1000000000 / 60;
        int frameCpt = 0;
        
        boolean needUpdate;
        m_lastTime = TimerThread.MILLI;
        m_pauseTime= 0;
        
        while(m_running)
        {
            long current = System.nanoTime(); 
            
            try
            {
                Thread.sleep(2);
            }
            catch(InterruptedException e){}
            
            needUpdate = false;
            double delta = (current - lastTime) / nsms;
            
            if((current - lastTime) / nsms  >= 1)
            {         
                //tick
                frameCpt++;
                lastTime = current;
                needUpdate = true;
            }
            
            repaint();
            
            if(needUpdate)
            {
                update(delta);
            }
            
            if(System.currentTimeMillis() - startTime >= 1000)
            {
                m_fps = frameCpt;
                frameCpt = 0;
                startTime = System.currentTimeMillis();
            }
        }
    }
    
    public void update(double dt)
    {
        m_stateManager.update(dt);
        
        m_context.m_inputsListeners.update();
    }
    
    @Override
    public void paint(Graphics g)
    {
        requestFocus();
        
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        g2d.setColor(Color.BLACK);
        g2d.fillRect(0, 0, 800, 600);
        
        m_stateManager.render(g2d);
    }
    
    public String getVersion()
    {
        return VERSION;
    }
    
    @Override
    public String getName()
    {
        return NAME;
    }
}
