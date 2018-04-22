package world.tile;

import core.Defines;
import core.ResourceManager;
import entities.ItemEntity;
import entities.Mob;
import java.awt.Graphics2D;
import world.World;
import world.item.ResourceItem;
import world.item.resource.Resource;

class WheatTile extends Tile
{    
    public WheatTile(int id)
    {
        super(id);
    }
    
    @Override
    public void render(Graphics2D g, World world, int x, int y, int scaling)
    {
        ResourceManager rm = ResourceManager.getInstance();
        int age = world.getData(x, y);
        
        g.drawImage(
                rm.getSpritesheets("spritesheet").getSubimage(309 + (16 * (age / 10)), 0, 16, 16), 
                x * Defines.TILESIZE * scaling, 
                y * Defines.TILESIZE * scaling, 
                Defines.TILESIZE * scaling, 
                Defines.TILESIZE * scaling,
                null
            );
    }
    
    @Override
    public void update(double dt, World world, int x, int y)
    {
        if (m_random.nextInt(2) == 0)
            return;
        
        int age = world.getData(x, y);
        if(age < 30)
        {
            world.setData(x, y, age + 1);
        }
    }
    
    @Override
    public void hurt(World world, int x, int y, Mob source, int dmg, int atkDir)
    {
        harvest(world, x, y);
    }
    
    public void harvest(World world, int x, int y)
    {
        int age = world.getData(x, y);
        int count = m_random.nextInt(2) + 1;
        
        for(int i = 0 ; i < count ; i++)
        {
            world.add(new ItemEntity(
                    new ResourceItem(Resource.SEEDS),
                    x * 16 * 3 + 4,
                    y * 16 * 3 + 4
            ));
        }
        
        count = 0;
        if(age == 30)
        {
            count = m_random.nextInt(2) + 1;
        }
        
        for(int i = 0 ; i < count ; i++)
        {
            world.add(new ItemEntity(
                    new ResourceItem(Resource.WHEAT),
                    x * 16 * 3 + 4,
                    y * 16 * 3 + 4
            ));
        }
        
        world.setTile(x, y, Tile.FARM, 0);
    }
}
