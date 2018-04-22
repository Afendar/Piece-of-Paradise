package world.item.resource;

import core.ResourceManager;
import entities.Player;
import java.awt.image.BufferedImage;
import world.World;
import world.tile.Tile;

public class Resource
{
    public static PlantableResource SEEDS = new PlantableResource("Seeds", ResourceManager.getInstance().getSprite("seeds"), Tile.WHEAT, Tile.FARM);
    
    public static Resource WHEAT = new Resource("Wheat", ResourceManager.getInstance().getSprite("wheat"));
    public static Resource WOOD = new Resource("Wood", ResourceManager.getInstance().getSprite("wood"));
    
    protected String m_name;
    protected BufferedImage m_sprite;
    
    public Resource(String name, BufferedImage sprite)
    {
        m_name = name;
        m_sprite = sprite;
    }
    
    public BufferedImage getSprite()
    {
        return m_sprite;
    }
    
    public String getName()
    {
        return m_name;
    }
    
    public boolean interactOn(Tile tile, World world, int xt, int yt, Player player, int atkDir)
    {
        return false;
    }
}
