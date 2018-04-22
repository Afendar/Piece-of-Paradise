package world.item;

import entities.Entity;
import entities.ItemEntity;
import entities.Player;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import world.World;
import world.tile.Tile;

public abstract class Item
{
    protected BufferedImage m_sprite;
    
    public BufferedImage getSprite()
    {
        return m_sprite;
    }
    
    public void onTake(ItemEntity itemEntity){}
    
    public boolean interact(Player player, Entity entity, int atkDir)
    {
        return false;
    }
    
    public boolean interactOn(Tile tile, World world, int x, int y, Player player, int atkDir)
    {
        return false;
    }
    
    public void renderInventory(Graphics2D g){}
    
    public boolean canAttack()
    {
        return false;
    }
    public int getAttackDamagesBonus(Entity e)
    {
        return 0;
    }
    
    public boolean matches(Item item)
    {
        return item.getClass() == getClass();
    }
    
    public String getName()
    {
        return "";
    }
}
