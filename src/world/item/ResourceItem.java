package world.item;

import entities.ItemEntity;
import entities.Player;
import java.awt.image.BufferedImage;
import world.World;
import world.item.resource.Resource;
import world.tile.Tile;

public class ResourceItem extends Item
{
    protected Resource m_resource;
    public int m_stock;
    
    public ResourceItem(Resource resource)
    {
        this(resource, 1);
    }
    
    public ResourceItem(Resource resource, int count)
    {
        m_resource = resource;
        m_stock = count;
    }
    
    @Override
    public BufferedImage getSprite()
    {
        return m_resource.getSprite();
    }
    
    @Override
    public String getName()
    {
        return m_resource.getName();
    }
    
    @Override
    public boolean interactOn(Tile tile, World world, int x, int y, Player player, int atkDir)
    {
        if(m_resource.interactOn(tile, world, x, y, player, atkDir))
        {
            return true;
        }
        return false;
    }
    
    @Override
    public void onTake(ItemEntity itemEntity){}
}
