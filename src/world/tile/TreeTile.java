package world.tile;

import core.Defines;
import core.ResourceManager;
import core.particles.TextParticle;
import entities.Entity;
import entities.ItemEntity;
import entities.Mob;
import java.awt.Color;
import java.awt.Graphics2D;
import world.World;
import world.item.ResourceItem;
import world.item.resource.Resource;

class TreeTile extends Tile
{
    private int m_life = 10;
    
    public TreeTile(int id)
    {
        super(id);
    }
    
    @Override
    public boolean mayPass(World world, int x, int y, Entity e)
    {
        return false;
    }
    
    @Override
    public void render(Graphics2D g, World world, int x, int y, int scaling)
    {                
        ResourceManager rm = ResourceManager.getInstance();
        g.drawImage(
                rm.getSpritesheets("spritesheet").getSubimage(373, 0, 16, 16), 
                x * Defines.TILESIZE * scaling, 
                y * Defines.TILESIZE * scaling, 
                Defines.TILESIZE * scaling, 
                Defines.TILESIZE * scaling,
                null
            );
    }
    
    @Override
    public void hurt(World world, int x, int y, Mob source, int dmg, int atkDir)
    {
        world.add(new TextParticle("" + dmg, x * Defines.TILESIZE * 3, y * Defines.TILESIZE * 3, Color.GREEN));
        int damages = world.getData(x, y) + dmg;
        if(damages >= 20)
        {
            int count = m_random.nextInt(2) + 1;
            for(int i = 0; i < count ; i++)
            {
                world.add(new ItemEntity(
                        new ResourceItem(Resource.WOOD), 
                        x * 16 * 3 + 4,
                        y * 16 * 3 + 4
                ));
            }
            world.setTile(x, y, Tile.GRASS, 0);
        }
        else
        {
            world.setData(x, y, damages);
        }
    }
}
