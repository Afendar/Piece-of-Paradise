package world.tile;

import core.Defines;
import java.awt.Color;
import java.awt.Graphics2D;
import world.World;

class HouseTile extends Tile
{
    public HouseTile(int id)
    {
        super(id);
    }
    
    @Override
    public void render(Graphics2D g, World world, int x, int y, int scaling)
    {                
        g.setColor(Color.RED);
        g.fillRect(x * Defines.TILESIZE * scaling, y * Defines.TILESIZE * scaling, Defines.TILESIZE * scaling, Defines.TILESIZE * scaling);
    }
}
