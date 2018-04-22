package core.particles;

import core.ResourceManager;
import entities.Entity;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;

public class TextParticle extends Entity
{
    private String m_msg;
    private Color m_color;
    private int m_time = 0;
    private double m_xa, m_ya, m_za;
    private double m_xx, m_yy, m_zz;
    
    public TextParticle(String msg, int x, int y, Color color)
    {
        m_x = x;
        m_y = y;
        m_color = color;
        m_msg = msg;
        m_xx = x;
        m_yy = y;
        m_zz = 2;
        m_xa = m_random.nextGaussian() * 0.3 * 3;
        m_ya = m_random.nextGaussian() * 0.2 * 3;
        m_za = m_random.nextFloat() * 0.7 * 3 + 2;
    }
    
    @Override
    public void update(double dt)
    {
        m_time += dt;
        if(m_time > 60)
        {
            remove();
        }
        
        m_xx += m_xa;
        m_yy += m_ya;
        m_zz += m_za;
        if(m_zz < 0)
        {
            m_zz = 0;
            m_za *= -0.5;
            m_xa *= 0.6;
            m_ya *= 0.6;
        }
        m_za -= 0.15;
        m_x = (int) m_xx;
        m_y = (int) m_yy;
    }
    
    @Override
    public void render(Graphics2D g , int scaling)
    {
        ResourceManager rm = ResourceManager.getInstance();
        
        g.setColor(m_color);
        
        Font font = rm.getFont("anotherDayInParadise").deriveFont(Font.PLAIN, 16.0f);
        g.setFont(font);
        g.drawString(m_msg, m_x, m_y);
    }
}
