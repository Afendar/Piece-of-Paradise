package entities;

import audio.Sound;
import core.Defines;
import core.ResourceManager;
import java.awt.Graphics2D;

public class Villager extends Mob
{
    private int m_xa, m_ya;
    private int m_randomWalkTime = 0;
    
    public Villager()
    {
        m_x = 300;
        m_y = 200;
    }
    
    @Override
    public void update(double dt)
    {
        super.update(dt);
        
        if(m_randomWalkTime <= 0)
        {
            int dir = m_random.nextInt(5);
            switch(dir)
            {
                case 0:
                    m_ya = 2;
                    break;
                case 1:
                    m_ya = -2;
                    break;
                case 2:
                    m_xa = -2;
                    break;
                case 3:
                    m_xa = 2;
                    break;
            }
        }
        
        if(!move(m_xa, m_ya) || m_randomWalkTime == 0)
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
                rm.getSpritesheets("spritesheet").getSubimage(325 + 16 * (m_walkDist % 20 / 5), 16 * (m_dir + 1), 16, 16), 
                m_x, 
                m_y,
                Defines.TILESIZE * scaling, 
                Defines.TILESIZE * scaling,
                null
            );
    }
    
    @Override
    protected void doHurt(int damages, int atkDir)
    {
        new Thread(Sound.hurt::play).start();
        m_health -= damages;
        if(m_health <= 0)
        {
            die();
        }
    }

    
    @Override
    protected void die()
    {
        super.die();
        
        m_world.decreasePopulation();
    }
}
