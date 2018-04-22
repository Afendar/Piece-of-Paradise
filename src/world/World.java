package world;

import core.Camera;
import core.Defines;
import entities.Entity;
import entities.Player;
import entities.Villager;
import entities.Zombie;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.Random;
import world.tile.Tile;

public class World
{
    private Random m_random = new Random();
    
    private int m_w;
    private int m_h;
    
    private int[][] m_tiles;
    private int[][] m_datas;
    
    public int m_mins;
    public int m_hours;
    private int m_timer;
    
    public int m_brightness;
    
    private ArrayList<Entity>[] m_entitiesInTile;
    private ArrayList<Entity> m_entities = new ArrayList<>();
    private ArrayList<Entity> m_entitiesInScreen = new ArrayList<>();
    
    private Player m_player;
    
    private int m_nbVillagers;
    
    public World(int w, int h)
    {
        m_w = w;
        m_h = h;
        
        m_mins = 0;
        m_hours = 9;
        m_timer = 0;
        
        m_brightness = 0;
        
        m_tiles = WorldGenerator.createAndValidateMap(w, h);
        m_datas = new int[w][h];
        
        m_entitiesInTile = new ArrayList[w * h];
        for(int i = 0; i < w * h ; i++)
        {
            m_entitiesInTile[i] = new ArrayList<>();
        }
    }
    
    public void update(double dt)
    {    
        m_timer += dt;
        
        for (int i = 0; i < m_w * m_h / 50; i++)
        {
            int xt = m_random.nextInt(m_w);
            int yt = m_random.nextInt(m_w);
            getTile(xt, yt).update(dt, this, xt, yt);
        }
        
        for(int i = 0 ; i < m_entities.size() ; i++)
        {
            Entity e = m_entities.get(i);
            int xto = e.getX() / (Defines.TILESIZE * 3);
            int yto = e.getY() / (Defines.TILESIZE * 3);
            
            e.update(dt);
            
            if(e.isRemoved())
            {
                m_entities.remove(i);
                removeEntity(e.getX() / (Defines.TILESIZE * 3), e.getY() / (Defines.TILESIZE * 3), e);
            }
            else
            {
                int xt = e.getX() / (Defines.TILESIZE * 3);
                int yt = e.getY() / (Defines.TILESIZE * 3);
                
                if(xto != xt  || yto != yt)
                {
                    removeEntity(xto, yto, e);
                    insertEntity(xt, yt, e);
                }
            }
        }
        
        if(m_timer > 25)
        {
            m_timer -= 25;
            m_mins++;
            if(m_mins == 60)
            {
                m_mins = 0;
                m_hours++;
                m_player.setResource(m_nbVillagers * -5);
                if(m_hours == 24)
                {
                    m_hours = 0;
                }
            }
            
            if(m_hours >= 17 && m_hours < 18)
            {
                m_brightness += 3;
            }
            if(m_hours >= 6 && m_hours < 7)
            {
                m_brightness -= 3;
            }
            
            if((m_hours > 18 && m_hours < 23) || (m_hours > 0 && m_hours < 5))
            {
                if(m_mins % 20 == 0)
                {  
                    trySpawn(1);
                }
            }
        }
    }
    
    public void renderTiles(Graphics2D g, Camera cam, int scaling)
    {     
        int xo = cam.getX() / (Defines.TILESIZE * 3);
        int yo = cam.getY() / (Defines.TILESIZE * 3);
        
        for(int i  = yo ; i < yo + (600 / (Defines.TILESIZE * 3)) + 1; i++)
        {
            for(int j = xo ; j < xo + (800 / (Defines.TILESIZE * 3)) + 2 ; j++)
            {
                getTile(j, i).render(g, this, j, i, scaling);
            }
        }
    }
    
    public void renderEntities(Graphics2D g, Camera cam, int scaling)
    {
        int xo = cam.getX() / (Defines.TILESIZE * 3);
        int yo = cam.getY() / (Defines.TILESIZE * 3);
        
        for(int y = yo ; y < yo + (600 / (Defines.TILESIZE * 3)) + 1 ; y++)
        {
            for(int x = xo ; x < xo + (800 / (Defines.TILESIZE * 3)) + 2 ; x++)
            {
                if(x < 0 || y < 0 || x >= m_w || y >= m_h)
                {
                    continue;
                }
                
                m_entitiesInScreen.addAll(m_entitiesInTile[x + y * m_w]);
            }
            if(m_entitiesInScreen.size() > 0)
            {
                for (Entity entity : m_entitiesInScreen)
                {
                    entity.render(g, scaling);
                }
            }
            m_entitiesInScreen.clear();
        }
    }
    
    public Tile getTile(int x, int y)
    {
        if(x < 0 || y < 0 || x >= m_w || y >= m_h)
        {
            return Tile.WATER;
        }
        return Tile.m_tiles[m_tiles[x][y]];
    }
    
    public void setTile(int x, int y, Tile tile, int value)
    {
        if(x < 0 || y < 0 || x >= m_w || y >= m_h)
        {
            return;
        }
        m_tiles[x][y] = tile.m_id;
        m_datas[x][y] = value;
    }
    
    public int getData(int x, int y)
    {
        if(x < 0 || y < 0 || x >= m_w || y >= m_h)
        {
            return 0;
        }
        return m_datas[x][y];
    }
    
    public void setData(int x, int y, int value)
    {
        if(x < 0 || y < 0 || x >= m_w || y >= m_h)
        {
            return;
        }
        m_datas[x][y] = value;
    }
    
    public int getWidth()
    {
        return m_w;
    }
    
    public int getHeight()
    {
        return m_h;
    }
    
    public int getHours()
    {
        return m_hours;
    }
    
    public ArrayList<Entity> getEntities(int x0, int y0, int x1, int y1)
    {
        ArrayList<Entity> result = new ArrayList<>();
        int xt0 = x0 - 1;
        int yt0 = y0 - 1;
        int xt1 = x1 + 1;
        int yt1 = y1 + 1;
        for(int y = yt0 ; y <= yt1 ; y++)
        {
            for(int x = xt0 ; x <= xt1 ; x++)
            {
                if(x < 0 || y < 0 || x >= m_w || y >= m_h)continue;
                
                ArrayList<Entity> entities = m_entitiesInTile[x + y * m_w];
                for (Entity e : entities)
                {
                    if(e.intersects(x0, y0, x1, y1))
                    {
                        result.add(e);
                    }
                }
            }
        }
        return result;
    }
    
    public void add(Entity e)
    {
        if(e instanceof Player)
        {
            m_player = (Player)e;
        }
        else if(e instanceof Villager)
        {
            m_nbVillagers++;
        }
        
        m_entities.add(e);
        e.init(this);
        
        //System.out.println("add: " + e.getX() / (Defines.TILESIZE * 3) + ":" + e.getY() / (Defines.TILESIZE * 3));
        
        insertEntity(e.getX() / (Defines.TILESIZE * 3), e.getY() / (Defines.TILESIZE * 3), e);
    }
    
    public void remove(Entity e)
    {
        m_entities.remove(e);
        removeEntity(e.getX() / (Defines.TILESIZE * 3), e.getY() / (Defines.TILESIZE * 3), e);
    }
    
    private void insertEntity(int x, int y, Entity e)
    {
        if(x < 0 || y < 0 || x >= m_w || y >= m_h)
        {
            return;
        }
        m_entitiesInTile[x + y * m_w].add(e);
    }
    
    private void removeEntity(int x, int y, Entity e)
    {
        if(x < 0 || y < 0 || x >= m_w || y >= m_h)
        {
            return;
        }
        m_entitiesInTile[x + y * m_w].remove(e);
    }
    
    public Player getPlayer()
    {
        return m_player;
    }
    
    public Villager getVillager()
    {
        for(Entity e : m_entities)
        {
            if(e instanceof Villager)
            {
                return (Villager)e;
            }
        }
        return null;
    }
    
    public int getPopulation()
    {
        return m_nbVillagers;
    }
    
    public String getTimeString()
    {
        return String.format("%02d:%02d", m_hours, m_mins);
    }
    
    public void decreasePopulation()
    {
        m_nbVillagers--;
    }
    
    public void trySpawn(int nb)
    {           
        for(int i = 0 ; i < nb ; i++)
        {
            if(m_random.nextInt(2) == 0)
            {
                Zombie z = new Zombie(1);
                System.out.println("try spawn");
                if(z.findStartPos(this))
                {
                    add(z);
                }
            }
        }
    }

    public int getWoodStock() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
