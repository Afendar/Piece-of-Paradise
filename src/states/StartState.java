package states;

import core.ResourceManager;
import core.gui.Button;
import core.gui.GuiComponent;
import core.maths.Easing;
import core.state.BaseState;
import core.state.StateManager;
import core.state.StateType;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class StartState extends BaseState
{
    private int m_posX;
    private double m_timer;
    private boolean m_displayInfos;
    private String[] m_infos;

    private final ArrayList<GuiComponent> m_guiElements = new ArrayList<>();
    
    public StartState(StateManager stateManager)
    {
        super(stateManager);
        
        setTransparent(true);
    }

    @Override
    public void onCreate()
    {
        ResourceManager rm = m_stateManager.getContext().m_resourceManager;
        Font f = rm.getFont("anotherDayInParadise").deriveFont(Font.PLAIN, 22.0f);
        BufferedImage spritesheet = rm.getSpritesheets("spritesheet");
        
        m_posX = -120 * 3;
        m_timer = 0;
        m_displayInfos = false;
        
        m_infos = new String[]{"Food stock: 100", "Wood stock: 200"};
        
        Button b = new Button("Start");
        b.setFont(f);
        b.setPadding(8, 0);
        b.setTextCenter(true);
        b.addApearance(GuiComponent.Status.NEUTRAL, spritesheet.getSubimage(0, 85, 86, 19));
        b.addApearance(GuiComponent.Status.FOCUSED, spritesheet.getSubimage(0, 104, 86, 19));
        b.setPosition((800 - 86 * 3)/2, ((600 - 120 * 3) / 2) + 290);
        b.setScale(3);
        b.addCallback(GuiComponent.Status.CLICKED, "resume", this);
        
        m_guiElements.add(b);
    }

    @Override
    public void onDestroy()
    {
        
    }

    @Override
    public void activate()
    {
        
    }

    @Override
    public void desactivate()
    {
        
    }

    @Override
    public void update(double dt)
    {
        if(m_posX < (800 - 300) / 2)
        {
            m_timer += dt;
            m_posX = Easing.circEaseOut((float)m_timer, -220, 250 + 221, 80);
        }
        else if(m_posX >= (800 - 300) / 2 && !m_displayInfos)
        {
            m_timer = 0;
            m_displayInfos = true;
        }
        
        if(m_displayInfos)
        {
            m_timer+= dt;
        }
        
        int mouseX = m_stateManager.getContext().m_inputsListeners.m_mouseX;
        int mouseY = m_stateManager.getContext().m_inputsListeners.m_mouseY;
        
        for(GuiComponent element : m_guiElements)
        {
            element.update(dt);
            
            if(element.isInside(mouseX, mouseY))
            {
                if(m_stateManager.getContext().m_inputsListeners.m_mousePressed && m_stateManager.getContext().m_inputsListeners.m_mouseClickCount >= 1)
                {
                    element.onClick();
                }
                
                if(element.getStatus() != GuiComponent.Status.NEUTRAL)
                {
                    continue;
                }

                element.onHover();
            }
            else if(element.getStatus() == GuiComponent.Status.FOCUSED)
            {
                element.onLeave();
            }
            else if(element.getStatus() == GuiComponent.Status.CLICKED)
            {
                element.onRelease();
            }
        }
    }

    @Override
    public void render(Graphics2D g)
    {
        ResourceManager rm = m_stateManager.getContext().m_resourceManager;
        
        g.drawImage(rm.getSpritesheets("spritesheet").getSubimage(112, 24, 100, 120), m_posX, (600 - 120 * 3) / 2, 100 * 3, 120 * 3, null);
        
        if(m_displayInfos)
        {
            g.setColor(Color.BLACK);
            
            Font f = rm.getFont("anotherDayInParadise").deriveFont(Font.PLAIN, 32.0f);
            FontMetrics fm = g.getFontMetrics(f);
            int w = fm.stringWidth("Objectives");
            g.setFont(f);
            g.setColor(new Color(19, 102, 44));
            g.drawString("Objectives", (800 - w) / 2, (600 - 120 * 3) / 2 + 30);
            
            f = f.deriveFont(Font.PLAIN, 24.0f);
            fm = g.getFontMetrics(f);
            g.setFont(f);
            g.setColor(Color.BLACK);
            w = fm.stringWidth("To escape, you must collect:");
            g.drawString("To escape, you must collect:", (800 - w) / 2, (600 - 120 * 3) / 2 + 80);
            
            if(m_timer >= 30)
            {
                g.drawString(m_infos[0], ((800 - 100 * 3) / 2) + 20, ((600 - 120 * 3) / 2) + 130);
            }
            if(m_timer >= 60)
            {
                g.drawString(m_infos[1], ((800 - 100 * 3) / 2) + 20, ((600 - 120 * 3) / 2) + 180);
            }
            if(m_timer >= 90)
            {
                w = fm.stringWidth("You must also leave with");
                g.drawString("You must also leave with", (800 - w) / 2, (600 - 120 * 3) / 2 + 240);
                w = fm.stringWidth("at least one survivor.");
                g.drawString("at least one survivor.", (800 - w) / 2, (600 - 120 * 3) / 2 + 270);
            }
            
            if(m_timer >= 140)
            {
                for(GuiComponent element : m_guiElements)
                {
                    element.render(g);
                }
            }
        }
    }
    
    public void resume()
    {
        m_stateManager.switchTo(StateType.GAME);
    }
}
