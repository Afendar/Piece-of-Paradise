package states;

import core.Camera;
import core.ResourceManager;
import core.state.BaseState;
import core.state.StateManager;
import core.state.StateType;
import entities.Player;
import entities.Villager;
import entities.Zombie;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import world.World;

public class GameState extends BaseState
{
    private World m_world;
    private Player m_player;
    private Camera m_camera;
    private boolean m_displayReport;
    
    public GameState(StateManager stateManager)
    {
        super(stateManager);
    }
    
    @Override
    public void onCreate()
    {
        m_world = new World(64, 64);
        
        m_camera = new Camera(0, 0, 800, 600, m_world);
        
        m_player = new Player(m_world, m_stateManager.getContext().m_inputsListeners);
        if(m_player.findStartPos(m_world))
        {
            m_world.add(m_player);
        }
        
        for(int i = 0; i < 5 ; i++)
        {
            Villager v = new Villager();
            int attempt = 0;
            while(!v.findStartPos(m_world))
            {
                //System.out.println("attempt find pos Villager: " + attempt);
                attempt++;
            }
            m_world.add(v);
        }
    }

    @Override
    public void onDestroy()
    {
        
    }

    @Override
    public void activate() 
    {
        m_displayReport = false;
        m_world.m_mins++;
    }

    @Override
    public void desactivate()
    {
        m_stateManager.getContext().population = m_world.getPopulation();
        m_stateManager.getContext().nbWood = m_player.getWoodStock();
        m_stateManager.getContext().nbFood = m_player.getResources();
    }

    @Override
    public void update(double dt) 
    {
        if(m_displayReport)
        {
            m_stateManager.switchTo(StateType.REPORT);
        }
        
        if(m_player.getWoodStock() >= m_stateManager.getContext().objWood && 
                m_player.getResources() >= m_stateManager.getContext().objFood)
        {
            m_stateManager.switchTo(StateType.WIN);
        }
        
        if(m_world.getPopulation() == 0 || m_player.getResources() <= 0)
        {
            m_stateManager.switchTo(StateType.LOSE);
        }

        m_world.update(dt);
        
        m_camera.update(m_player);
        
        if(m_world.getTimeString().equals("07:00"))
        {
            m_displayReport = true;
        }
    }

    @Override
    public void render(Graphics2D g)
    {
        ResourceManager rm = m_stateManager.getContext().m_resourceManager;
        Font f = rm.getFont("anotherDayInParadise").deriveFont(Font.PLAIN, 24.0f);
        
        int scaling = m_stateManager.getContext().SCALING;
        
        g.translate(-m_camera.getX(), -m_camera.getY());
        
        m_world.renderTiles(g, m_camera, scaling);
        m_world.renderEntities(g, m_camera, scaling);

        g.translate(m_camera.getX(), m_camera.getY());
        
        g.setColor(new Color(0, 0, 0, m_world.m_brightness));
        g.fillRect(-5, -5, 810, 610);
        
        g.setColor(Color.BLACK);
        g.setFont(f);
        g.drawString("Food: " + m_player.getResources(), 15, 30);
        g.drawString("Wood: " + m_player.getWoodStock(), 15, 60);
        g.drawString("Population: " + m_world.getPopulation(), 15, 90);
        
        g.drawString("" + m_world.getTimeString(), 800 - 80, 30);
        
    }
    
    public void pause()
    {
        
    }
}
