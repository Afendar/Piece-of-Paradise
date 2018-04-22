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

public class ReportState extends BaseState
{
    private int m_posX;
    private double m_timer;
    private boolean m_displayInfos;
    private String[] m_infos;

    private final ArrayList<GuiComponent> m_guiElements = new ArrayList<>();
    
    public ReportState(StateManager stateManager)
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
        
        m_infos = new String[]{
            "Population: " + m_stateManager.getContext().population, 
            "Food: " + m_stateManager.getContext().nbFood, 
            "Wood: " + m_stateManager.getContext().nbWood};
        
        Button b = new Button("OK");
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
        m_infos = new String[]{
            "Population: " + m_stateManager.getContext().population, 
            "Food: " + m_stateManager.getContext().nbFood, 
            "Wood: " + m_stateManager.getContext().nbWood};
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
            int w = fm.stringWidth("Daily report");
            g.setFont(f);
            g.setColor(new Color(19, 102, 44));
            g.drawString("Daily report", (800 - w) / 2, (600 - 120 * 3) / 2 + 40);
            
            f = f.deriveFont(Font.PLAIN, 22.0f);
            g.setFont(f);
            g.setColor(Color.BLACK);
            if(m_timer >= 30)
            {
                g.drawString(m_infos[0], ((800 - 100 * 3) / 2) + 20, ((600 - 120 * 3) / 2) + 100);
            }
            if(m_timer >= 60)
            {
                g.drawString(m_infos[1], ((800 - 100 * 3) / 2) + 20, ((600 - 120 * 3) / 2) + 170);
                if(m_stateManager.getContext().nbFood >= m_stateManager.getContext().objFood)
                {
                    g.drawString("Done", ((800 - 100 * 3) / 2) + 200, ((600 - 120 * 3) / 2) + 170);
                }
            }
            if(m_timer >= 90)
            {
                g.drawString(m_infos[2], ((800 - 100 * 3) / 2) + 20, ((600 - 120 * 3) / 2) + 240);
                if(m_stateManager.getContext().nbWood >= m_stateManager.getContext().objWood)
                {
                    g.drawString("Done", ((800 - 100 * 3) / 2) + 200, ((600 - 120 * 3) / 2) + 240);
                }
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
