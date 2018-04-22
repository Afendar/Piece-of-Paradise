package entities;

import audio.Sound;
import java.awt.Graphics2D;
import world.item.Item;

public class ItemEntity extends Entity
{
    private int m_lifeTime;
    protected int m_walkDist = 0;
    protected int m_dir = 0;
    public int m_hurtTime = 0;
    protected int m_xKnockback, m_yKnockback;
    public double m_xa, m_ya, m_za;
    public double m_xx, m_yy, m_zz;
    public Item m_item;
    private int m_time = 0;
    
    public ItemEntity(Item item, int x, int y)
    {
        m_item = item;
        m_xx = m_x = x;
        m_yy = m_y = y;
        m_w = 3;
        m_h = 3;
        
        m_zz = 2;
        m_xa = m_random.nextGaussian() * 0.3 * 3;
        m_ya = m_random.nextGaussian() * 0.2 * 3;
        m_za = m_random.nextFloat() * 0.7 * 3 + 1;
        
        m_lifeTime = 60 * 10 + m_random.nextInt(60);
    }
    
    @Override
    public void update(double dt)
    {
        m_time += dt;
        if(m_time >= m_lifeTime)
        {
            remove();
            return;
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
        int ox = m_x;
        int oy = m_y;
        int nx = (int)m_xx;
        int ny = (int)m_yy;
        int expectedx = nx - m_x;
        int expectedy = ny - m_y;
        
        move(nx - m_x, ny - m_y);
        
        int gotx = m_x - ox;
        int goty = m_y - oy;
        m_xx += gotx - expectedx;
        m_yy += goty - expectedy;
        
        if(m_hurtTime > 0)
            m_hurtTime -= dt;
    }
    
    @Override
    public void render(Graphics2D g, int scaling)
    {
        if(m_time >= m_lifeTime - 6 * 20)
        {
            if (m_time / 6 % 2 == 0)
                return;
        }
        g.drawImage(
                m_item.getSprite(), 
                m_x, 
                m_y, 
                8 * scaling, 
                8 * scaling,
                null
            );
    }
    
    @Override
    protected void touchedBy(Entity e)
    {        
        if(m_time > 30)
        {
            e.touchItem(this);
        }
    }
    
    public void take(Player player)
    {
        new Thread(Sound.pickup::play).start();
        player.addScore(1);
        if(m_item.getName().equals("Wheat"))
        {
            player.setResource(1);
        }
        if(m_item.getName().equals("Wood"))
        {
            player.setWoodStock(1);
        }
        m_item.onTake(this);
        remove();
    }
}
