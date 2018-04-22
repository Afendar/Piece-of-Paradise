package entities;

import audio.Sound;
import core.Defines;
import core.InputsListeners;
import core.ResourceManager;
import java.awt.Graphics2D;
import java.util.ArrayList;
import world.World;
import world.item.Item;
import world.item.ResourceItem;
import world.item.resource.Resource;
import world.tile.Tile;

public class Player extends Mob
{
    private InputsListeners m_inputsListeners;
    private int m_atkTime, m_atkDir;
    
    private World m_world;
    public Item m_atkItem;
    public Item m_activeItem;
    
    public int m_score = 0;
    public int m_invulnerableTime = 0;
    public int m_resources = 75;
    public int m_woodStock = 0;
    
    public Player(World world, InputsListeners inputsListeners)
    {
        m_world = world;
        m_inputsListeners = inputsListeners;
        
        m_x = 0;
        m_y = 0;
        
        m_activeItem = new ResourceItem(Resource.SEEDS);
    }
    
    @Override
    public void update(double dt)
    {
        super.update(dt);
        
        int xa = 0;
        int ya = 0;
        
        if(m_inputsListeners.up.m_enabled){
            ya-=3;
        }
        if(m_inputsListeners.down.m_enabled){
            ya+=3;
        }
        if(m_inputsListeners.left.m_enabled){
            xa-=3;
        }
        if(m_inputsListeners.right.m_enabled){
            xa+=3;
        }
        
        move(xa, ya);
        
        if(m_inputsListeners.attack.m_enabled && m_atkTime <= 0)
        {
            attack();
        }
        
        if(m_atkTime > 0)
        {
            m_atkTime -= dt;
        }
    }
    
    private void attack()
    {
        m_walkDist += 8;
        m_atkDir = m_dir;
        m_atkItem = m_activeItem;
        new Thread(Sound.hit::play).start();

        m_atkTime = 20;
        int yo = -2;
        int range = 20;

        if(m_dir == 0)hurt(m_x - Defines.TILESIZE, m_y + Defines.TILESIZE / 2 + yo, m_x + Defines.TILESIZE, m_y + range + yo);
        if(m_dir == 1)hurt(m_x - Defines.TILESIZE, m_y - range + yo, m_x + Defines.TILESIZE, m_y - Defines.TILESIZE / 2 + yo);
        if(m_dir == 2)hurt(m_x + Defines.TILESIZE / 2, m_y - Defines.TILESIZE + yo, m_x + range, m_y + Defines.TILESIZE + yo);
        if(m_dir == 3)hurt(m_x - range, m_y - Defines.TILESIZE + yo, m_x - Defines.TILESIZE / 2, m_y + Defines.TILESIZE + yo);

        int xt = m_x;
        int yt = m_y + yo;
        int r = 12;

        if(m_atkDir == 0)yt = (m_y + r + yo);
        if(m_atkDir == 1)yt = (m_y - r + yo);
        if(m_atkDir == 2)xt = (m_x - r);
        if(m_atkDir == 3)xt = (m_x + r);
        
        if(xt >= 0 && yt >= 0 && xt / (Defines.TILESIZE * 3) < m_world.getWidth() && yt / (Defines.TILESIZE * 3) < m_world.getHeight())
        {
            xt /= (Defines.TILESIZE * 3);
            yt /= (Defines.TILESIZE * 3);
            m_world.getTile(xt, yt).hurt(m_world, xt, yt, this, m_random.nextInt(3) + 1, m_atkDir);
            if(m_activeItem != null)
            {
                m_activeItem.interactOn(m_world.getTile(xt, yt), m_world, xt, yt, this, m_atkDir);
            }
        }
    }
    
    private boolean use(int x0, int y0, int x1, int y1)
    {
        ArrayList<Entity> entities = m_world.getEntities(x0 / (Defines.TILESIZE * 3), y0 / (Defines.TILESIZE * 3), x1 / (Defines.TILESIZE * 3), y1 / (Defines.TILESIZE * 3));
        for(Entity e : entities)
        {
            if(e != this)
            {
                if(e.use(this, m_atkDir))
                {
                    return true;
                }
            }
        }
        return false;
    }
    
    private boolean interact(int x0, int y0, int x1, int y1)
    {
        ArrayList<Entity> entities = m_world.getEntities(x0 / (Defines.TILESIZE * 3), y0 / (Defines.TILESIZE * 3), x1 / (Defines.TILESIZE * 3), y1 / (Defines.TILESIZE * 3));
        for(Entity e : entities)
        {
            if(e != this)
            {
                if(e.interact(this, m_atkItem, m_atkDir))
                {
                    return true;
                }
            }
        }
        return false;
    }
    
    private void hurt(int x0, int y0, int x1, int y1)
    {
        ArrayList<Entity> entities = m_world.getEntities(x0 / (Defines.TILESIZE * 3), y0 / (Defines.TILESIZE * 3), x1 / (Defines.TILESIZE * 3), y1 / (Defines.TILESIZE * 3));
        for(Entity e : entities)
        {
            if(e != this)
            {
                e.hurt(this, getAttackDamage(e), m_atkDir);
            }
        }
    }
    
    private int getAttackDamage(Entity e)
    {
        int dmg = m_random.nextInt(3) + 1;
        if(m_atkItem != null)
        {
            dmg += m_atkItem.getAttackDamagesBonus(e);
        }
        return dmg;
    }
    
    public int getResources()
    {
        return m_resources;
    }
    
    public void setResource(int nb)
    {
        m_resources += nb;
    }
    
    public int getWoodStock()
    {
        return m_woodStock;
    }
    
    public int setWoodStock(int nb)
    {
        return m_woodStock += nb;
    }
    
    @Override
    public void render(Graphics2D g, int scaling)
    {        
        ResourceManager rm = ResourceManager.getInstance();
        g.drawImage(
                rm.getSpritesheets("spritesheet").getSubimage(261 + 16 * (m_walkDist % 20 / 5), 16 * (m_dir + 1), 16, 16), 
                m_x, 
                m_y,
                Defines.TILESIZE * scaling, 
                Defines.TILESIZE * scaling,
                null
            );
    }
    
    @Override
    public void touchItem(ItemEntity itemEntity)
    {
        itemEntity.take(this);
        m_score += 1;
    }
    
    @Override
    public boolean findStartPos(World world)
    {
        while(true)
        {
            int x = m_random.nextInt(m_world.getWidth());
            int y = m_random.nextInt(m_world.getHeight());
            if(m_world.getTile(x, y) == Tile.FARM)
            {
                m_x = x * Defines.TILESIZE * 3;
                m_y = y * Defines.TILESIZE * 3;
                System.out.println("Starting pos: " + x + ":" + y);
                return true;
            }
        }
    }
    
    public void addScore(int score)
    {
        m_score += score;
    }
    
    @Override
    protected void die()
    {
        super.die();
    }
    
    @Override
    protected void touchedBy(Entity e)
    {
        if(!(e instanceof Player))
        {
            e.touchedBy(this);
        }
    }
    
    @Override
    protected void doHurt(int damages, int atkDir)
    {
        //System.out.println("Aie !");
        //new Thread(Sound.hurt::play).start();
    }
}
