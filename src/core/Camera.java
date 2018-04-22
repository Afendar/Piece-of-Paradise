package core;

import entities.Player;
import world.World;

public class Camera
{
    private int m_x;
    private int m_y;
    private int m_w;
    private int m_h;
    private World m_world;
    
    public Camera(int x, int y, int w, int h, World world)
    {
        m_x = x;
        m_y = y;
        m_w = w;
        m_h = h;
        m_world = world;
    }
    
    public void update(Player p)
    {
        if(p.getX() == m_x && p.getY() == m_y)
        {
            return;
        }
        
        m_x = (int) p.getX() + 4 - 400;
        m_y = (int) p.getY() + 8 - 300;
        
        if(m_x < 0)m_x = 0;
        if(m_y < 0)m_y = 0;
        if(m_x + m_w > m_world.getWidth() * Defines.TILESIZE * 3)m_x = m_world.getWidth() * Defines.TILESIZE * 3 - m_w;
        if(m_y + m_h > m_world.getHeight() * Defines.TILESIZE * 3)m_y = m_world.getHeight() * Defines.TILESIZE * 3 - m_h;
    }
    
    public int getX()
    {
        return m_x;
    }
    
    public int getY()
    {
        return m_y;
    }
    
    public int getWidth()
    {
        return m_w;
    }
    
    public int getHeight()
    {
        return m_h;
    }
}
