package world.tile;

import core.Defines;
import entities.Entity;
import java.awt.Color;
import java.awt.Graphics2D;
import world.World;

class WaterTile extends Tile
{
    public WaterTile(int id) 
    {
        super(id);
    }
    
    @Override
    public void render(Graphics2D g, World world, int x, int y, int scaling)
    {                
        g.setColor(Color.BLUE);
        g.fillRect(x * Defines.TILESIZE * scaling, y * Defines.TILESIZE * scaling, Defines.TILESIZE * scaling, Defines.TILESIZE * scaling);
    }
    
    @Override
    public boolean mayPass(World world, int x, int y, Entity e)
    {
        return false;
    }
}
