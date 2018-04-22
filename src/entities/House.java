package entities;

public class House
{
    private int m_x1;
    private int m_x2;
    private int m_y1;
    private int m_y2;
    
    private int m_w;
    private int m_h;
    
    public House(int x, int y, int w, int h)
    {
        m_x1 = x;
        m_x2 = x + w;
        m_y1 = y;
        m_y2 = y + h;
        
        m_w = w;
        m_h = h;
    }
    
    public boolean intersects(House house)
    {
        return (m_x1 <= house.m_x2 && m_x2 >= house.m_x1 && m_y1 <= house.m_y2 && m_y2 >= house.m_y1);
    }
}
