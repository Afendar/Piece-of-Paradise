package entities;

import audio.Sound;
import core.Defines;
import core.particles.TextParticle;
import java.awt.Color;
import world.World;
import world.tile.Tile;

public abstract class Mob extends Entity
{
    protected int m_walkDist = 0;
    protected int m_dir = 0;
    public int m_hurtTime = 0;
    protected int m_xKnockback, m_yKnockback;
    public int m_maxHealth = 10;
    public int m_health = m_maxHealth;
    public int m_swimTimer = 0;
    public double m_time;
    
    public Mob()
    {
        m_x = m_y = 16;
        m_w = Defines.TILESIZE;
        m_h = Defines.TILESIZE;
    }
    
    @Override
    public void update(double dt)
    {
        m_time += dt;
        
        if(m_health <= 0)
        {
            die();
        }
        
        if(m_hurtTime > 0)
        {
            m_hurtTime -= dt;
        }
    }
    
    protected void die()
    {
        remove();
    }
    
    @Override
    public boolean move(int x, int y)
    {        
        if(m_xKnockback < 0)
        {
            move2(-1, 0);
            m_xKnockback++;
        }
        if(m_xKnockback > 0)
        {
            move2(1, 0);
            m_xKnockback--;
        }
        if(m_yKnockback < 0)
        {
            move2(0, -1);
            m_yKnockback++;
        }
        if(m_yKnockback > 0)
        {
            move2(0, 1);
            m_yKnockback--;
        }
        
        if(x != 0 || y != 0)
        {
            m_walkDist++;
            if(x < 0) m_dir = 2;
            if(x > 0) m_dir = 3;
            if(y < 0) m_dir = 1;
            if(y > 0) m_dir = 0;
        }
        
        return super.move(x, y);
    }
    
    @Override
    public boolean stop(Entity e)
    {
        return e.isBlockableBy(this);
    }
    
    @Override
    public void hurt(Tile tile, int x, int y, int damages)
    {
        int atkDir = m_dir ^ 1;
        doHurt(damages, atkDir);
    }
    
    @Override
    public void hurt(Mob mob, int damage, int atkDir)
    {
        doHurt(damage, atkDir);
    }
    
    public void heal(int heal)
    {
        if(m_hurtTime > 0)
        {
            return;
        }
        
        m_world.add(new TextParticle("" + heal, m_x, m_y, Color.GREEN));
    }
    
    protected void doHurt(int damage, int atkDir)
    {
        if(m_hurtTime > 0) 
        {
            return;
        }
        
        if(m_world.getPlayer() != null)
        {
            int xd = (m_world.getPlayer().getX() - m_x) / (Defines.TILESIZE * 3);
            int yd = (m_world.getPlayer().getY() - m_y) / (Defines.TILESIZE * 3);
            
            new Thread(Sound.monsterhurt::play).start();
            
            m_world.add(new TextParticle("" + damage, m_x, m_y, Color.RED));
            m_health -= damage;
            
            if(atkDir == 0) m_yKnockback = 6;
            if(atkDir == 1) m_yKnockback = -6;
            if(atkDir == 2) m_xKnockback = -6;
            if(atkDir == 3) m_xKnockback = 6;
            m_hurtTime = 10;
        }
    }
    
    public boolean findStartPos(World world)
    {
        int x = m_random.nextInt(world.getWidth());
        int y = m_random.nextInt(world.getHeight());
        
        int xx = x + 8;
        int yy = y + 8;
        
        if(world.getTile(x, y).mayPass(world, x, y, this))
        {
            m_x = xx * Defines.TILESIZE * 3;
            m_y = yy * Defines.TILESIZE * 3;
            
            //System.out.println("Start pos mob: " + m_x + ":" + m_y);
            return true;
        }
        
        return false;
    }
}
