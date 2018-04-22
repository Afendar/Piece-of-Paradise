package entities;

import core.Defines;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.Random;
import world.World;
import world.item.Item;
import world.tile.Tile;

public abstract class Entity
{
    protected final Random m_random = new Random();
    
    protected int m_x;
    protected int m_y;
    protected int m_w = 6;
    protected int m_h = 6;
    
    protected boolean m_removed = false;
    protected World m_world;
    
    public void render(Graphics2D g, int scaling){}
    public void update(double dt){}
    
    public void remove()
    {
        m_removed = true;
    }
    
    public final void init(World world)
    {
        m_world = world;
    }
    
    public boolean intersects(int x0, int y0, int x1, int y1)
    {       
        return !(m_x / (Defines.TILESIZE * 3) > x1 || 
                m_y  / (Defines.TILESIZE * 3) > y1 || 
                (m_x + m_w) / (Defines.TILESIZE * 3) < x0 || 
                (m_y + m_h) / (Defines.TILESIZE * 3) < y0);
    }
    
    public boolean stop(Entity e)
    {
        return false;
    }
    
    public void hurt(Tile tile, int x, int y, int damages){}
    public void hurt(Mob mob, int damage, int atkDir){}
    
    public boolean move(int x, int y)
    {        
        if(x == 0 && y == 0)
        {
            return true;
        }
                
        boolean stop = true;
        if(move2(x, 0)) stop = false;
        if(move2(0, y)) stop = false;
        if(!stop)
        {
            int xt = x / (Defines.TILESIZE * 3);
            int yt = y / (Defines.TILESIZE * 3);
            m_world.getTile(xt , yt).stepOn(m_world, xt, yt, this);
        }
        return !stop;
    }
    
    protected boolean move2(int x, int y)
    {
        if(x != 0 && y != 0)
        {
            throw new RuntimeException("Move 2 can only move along one axis at the time.");
        }
        
        int mx = m_x;
        int my = m_y;
        
        int xto0 = mx;
        int yto0 = my;
        int xto1 = (mx + m_w * 3);
        int yto1 = (my + m_h * 3);
        
        int xt0 = xto0 + x;
        int yt0 = yto0 + y;
        int xt1 = xto1 + x;
        int yt1 = yto1 + y;
        
        boolean blocked = false;
        
        for(int yt = yt0 ; yt <= yt1 ; yt++)
        {
            for(int xt = xt0 ; xt <= xt1 ; xt++)
            {
                if (xt >= xto0 && xt <= xto1 && yt >= yto0 && yt <= yto1) continue;
                
                int xa = xt / (Defines.TILESIZE * 3);
                int ya = yt / (Defines.TILESIZE * 3);
                
                m_world.getTile(xa, ya).bumpedInto(m_world, xa, ya, this);
                if(!m_world.getTile(xa, ya).mayPass(m_world, xa, ya, this))
                {
                    blocked = true;
                    return false;
                }
            }
        }
        
        if(blocked)
        {
            return false;
        }
        
        int x0 = xto0 / (Defines.TILESIZE * 3);
        int y0 = yto0 / (Defines.TILESIZE * 3);
        int x1 = xto1 / (Defines.TILESIZE * 3);
        int y1 = yto1 / (Defines.TILESIZE * 3);
        
        xt0 = (xto0 + x) / (Defines.TILESIZE * 3);
        yt0 = (yto0 + y) / (Defines.TILESIZE * 3);
        xt1 = (xto1 + x) / (Defines.TILESIZE * 3);
        yt1 = (yto1 + y) / (Defines.TILESIZE * 3);
        
        ArrayList<Entity> wasInside = m_world.getEntities(x0, y0, x1, y1);
        ArrayList<Entity> isInside = m_world.getEntities(xt0, yt0, xt1, yt1);
        
        for(int i = 0 ; i < isInside.size() ; i++)
        {
            Entity e = isInside.get(i);
            if(e == this)continue;
            
            e.touchedBy(this);
        }
        
        isInside.removeAll(wasInside);
        
        for(int i = 0 ; i < isInside.size() ; i++)
        {
            Entity e = isInside.get(i);
            if(e == this)continue;
            
            if(e.stop(this))
            {
                return false;
            }
        }
        
        m_x += x;
        m_y += y;
        
        return true;
    }
    
    protected void touchedBy(Entity e){}
    
    public boolean isBlockableBy(Mob mob)
    {
        return true;
    }
    
    public boolean isRemoved()
    {
        return m_removed;
    }
    
    public void touchItem(ItemEntity itemEntity){}
    
    public boolean canSwim()
    {
        return false;
    }
    
    public boolean interact(Player player, Item item, int atkDir)
    {
        return item.interact(player, this, atkDir);
    }
    
    public boolean use(Player player, int atkDir)
    {
        return false;
    }
    
    public int getX()
    {
        return m_x;
    }
    
    public int getY()
    {
        return m_y;
    }
    
    public int getLightRadius()
    {
        return 0;
    }
    
    public void setPosition(int x, int y)
    {
        m_x = x;
        m_y = y;
    }
}
