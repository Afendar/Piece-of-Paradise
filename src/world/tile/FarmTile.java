package world.tile;

import core.Defines;
import core.ResourceManager;
import java.awt.Graphics2D;
import world.World;

class FarmTile extends Tile
{
    public FarmTile(int id)
    {
        super(id);
    }
    
    @Override
    public void render(Graphics2D g, World world, int x, int y, int scaling)
    {                
        ResourceManager rm = ResourceManager.getInstance();
        g.drawImage(
                rm.getSpritesheets("spritesheet").getSubimage(293, 0, 16, 16), 
                x * Defines.TILESIZE * scaling, 
                y * Defines.TILESIZE * scaling, 
                Defines.TILESIZE * scaling, 
                Defines.TILESIZE * scaling,
                null
            );
    }
}
