package world.tile;

import entities.Entity;
import entities.Mob;
import entities.Player;
import java.awt.Graphics2D;
import java.util.Random;
import world.World;
import world.item.Item;

public class Tile 
{
    protected Random m_random = new Random();
    
    public static Tile[] m_tiles = new Tile[255];
    
    public static Tile GRASS = new GrassTile(0);
    public static Tile WATER = new WaterTile(1);
    public static Tile FLOWER = new FlowerTile(2);
    public static Tile TREE = new TreeTile(3);
    public static Tile SAND = new SandTile(4);
    public static Tile HOUSE = new HouseTile(5);
    public static Tile FARM = new FarmTile(6);
    public static Tile WHEAT = new WheatTile(7);
    
    public final int m_id;
    
    public Tile(int id)
    {
        m_id = id;
        
        if(m_tiles[id] != null)
        {
            throw new RuntimeException("Duplicate tile with id: " + id);
        }
        
        m_tiles[m_id] = this;
    }
    
    public void render(Graphics2D g, World wolrd, int x, int y, int scaling){}
    
    public boolean mayPass(World world, int x, int y, Entity e)
    {
        return true;
    }
    
    public void hurt(World world, int x, int y, Mob source, int damages, int atkDir){}
    
    public void bumpedInto(World world, int xt, int yt, Entity e){}
    
    public void update(double dt, World world, int x, int y){}
    
    public void stepOn(World world, int xt, int yt, Entity e){}
    
    public boolean interact(World world, int xt, int yt, Player player, Item item, int atkDir)
    {
        return false;
    }
}
