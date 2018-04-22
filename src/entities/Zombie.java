package entities;

import core.Defines;
import core.ResourceManager;
import java.awt.Graphics2D;

public class Zombie extends Mob
{
    private int m_xa, m_ya;
    private int m_lvl;
    private int m_randomWalkTime = 0;
    
    public Zombie(int lvl)
    {
        m_lvl = lvl;
        m_x = 200;
        m_y = 150;
        m_health = m_maxHealth = m_lvl * m_lvl * 10;
    }
    
    @Override
    public void update(double dt)
    {
        super.update(dt);
        
        Villager v = m_world.getVillager();
        if(v != null && m_randomWalkTime == 0)
        {
            int xd = (v.m_x - m_x) / (Defines.TILESIZE * 3);
            int yd = (v.m_y - m_y) / (Defines.TILESIZE * 3);
            if(xd * xd + yd * yd < 50 * 50)
            {
                m_xa = 0;
                m_ya = 0;
                if (xd < 0) m_xa = -2;
                if (xd > 0) m_xa = +2;
                if (yd < 0) m_ya = -2;
                if (yd > 0) m_ya = +2;
            }
        }
        
        if(!move(m_xa, m_ya))
        {            
            m_randomWalkTime = 60;
        }
        if(m_randomWalkTime > 0)
        {
            m_randomWalkTime -= dt;
        }
    }
    
    @Override
    public void render(Graphics2D g, int scaling)
    {        
        ResourceManager rm = ResourceManager.getInstance();
        g.drawImage(
                rm.getSpritesheets("spritesheet").getSubimage(261 + 16 * (m_walkDist % 20 / 5), 80 + 16 * m_dir, 16, 16), 
                m_x, 
                m_y,
                Defines.TILESIZE * scaling, 
                Defines.TILESIZE * scaling,
                null
            );
    }
    
    @Override
    protected void touchedBy(Entity e)
    {
        if(e instanceof Villager)
        {
            e.hurt(this, m_lvl + 1, m_dir);
        }
    }
    
    @Override
    protected void die()
    {
        super.die();
        
        if(m_world.getPlayer() != null)
        {
            m_world.getPlayer().addScore(50 * m_lvl);
        }
    }
}
