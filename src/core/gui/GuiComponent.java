package core.gui;

import core.Position;
import core.state.BaseState;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public abstract class GuiComponent
{
    protected String m_label;
    protected Map<Status, BufferedImage> m_appearances = new HashMap<>();
    protected Status m_status;
    protected Position m_position;
    protected Font m_font;
    protected Color m_color;
    protected int m_width, m_height;
    protected int m_scale;
    
    public enum Status {CLICKED, FOCUSED, CHECKED, NEUTRAL};
    
    public class Callback
    {
        private final String m_methodName;
        private final BaseState m_state;
        
        public Callback(String methodName, BaseState state)
        {
            m_methodName = methodName;
            m_state = state;
        }
    }
    
    protected Map<Status, Callback> m_callbacks = new HashMap<>();
    
    public GuiComponent()
    {
        m_status = Status.NEUTRAL;
        m_position = new Position(0, 0);
        m_font = new Font("Arial", Font.PLAIN, 14);
        m_color = Color.BLACK;
        m_scale = 1;
        
        calculateBounds();
    }
    
    public void addCallback(Status status, String methodName, BaseState state)
    {
        m_callbacks.put(status, new Callback(methodName, state));
    }
    
    public void addApearance(Status status, BufferedImage background)
    {
        if(m_appearances.containsKey(status))
        {
            return;
        }
        
        m_appearances.put(status, background);
        
        if(m_status == status)
        {
            calculateBounds();
        }
    }
    
    private void calculateBounds()
    {
        if(m_appearances.containsKey(m_status))
        {
            BufferedImage i = m_appearances.get(m_status);
            m_width = i.getWidth() * m_scale;
            m_height = i.getHeight() * m_scale;
        }
    }
    
    public void setFont(Font font)
    {
        m_font = font;
    }
    
    public void setPosition(Position position)
    {
        m_position = position;
    }
    
    public void setPosition(int x, int y)
    {
        m_position.m_x = x;
        m_position.m_y = y;
    }
    
    public void setStatus(Status status)
    {
        if(m_status == status)
        {
            return;
        }
        
        if(m_callbacks.containsKey(status))
        {
            Callback c = m_callbacks.get(status);
            try
            {
                Method m = c.m_state.getClass().getMethod(c.m_methodName);
                m.invoke(c.m_state);
            } 
            catch(NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException | InvocationTargetException ex)
            {
                Logger.getLogger(GuiComponent.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        m_status = status;
    }
    
    public void setLabel(String label)
    {
        m_label = label;
    }
    
    public String getLabel()
    {
        return m_label;
    }
    
    public Status getStatus()
    {
        return m_status;
    }
    
    public int[] getPosition()
    {
        int[] pos = {m_position.m_x, m_position.m_y};
        return pos;
    }
    
    public int getWidth()
    {
        return m_width;
    }
    
    public int getHeight()
    {
        return m_height;
    }
    
    public boolean isInside(int x, int y)
    {
        return x > m_position.m_x &&
                x < m_position.m_x + m_width * m_scale &&
                y > m_position.m_y &&
                y < m_position.m_y + m_height * m_scale;
    }
    
    public void setScale(int scale)
    {
        m_scale = scale;
    }
    
    public void onClick(){throw new UnsupportedOperationException("Not supported yet.");}
    public void onRelease(){throw new UnsupportedOperationException("Not supported yet.");}
    public void onHover(){throw new UnsupportedOperationException("Not supported yet.");}
    public void onLeave(){throw new UnsupportedOperationException("Not supported yet.");}
    
    public abstract void update(double dt);
    public abstract void render(Graphics2D g);
}
