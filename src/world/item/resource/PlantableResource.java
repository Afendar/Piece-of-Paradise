package world.item.resource;

import entities.Player;
import java.awt.image.BufferedImage;
import java.util.Arrays;
import java.util.List;
import world.World;
import world.tile.Tile;

public class PlantableResource extends Resource
{
    private List<Tile> m_sourceTile;
    private Tile m_targetTile;
    
    public PlantableResource(String name, BufferedImage sprite, Tile targetTile, Tile... sourceTiles1)
    {
        this(name, sprite, targetTile, Arrays.asList(sourceTiles1));
    }
    
    public PlantableResource(String name, BufferedImage sprite, Tile targetTile, List<Tile> sourceTile)
    {
        super(name, sprite);
        
        m_sourceTile = sourceTile;
        m_targetTile = targetTile;
    }
    
    public boolean interactOn(Tile tile, World world, int xt, int yt, Player player, int atkDir)
    {
        if(m_sourceTile.contains(tile))
        {
            world.setTile(xt, yt, m_targetTile, 0);
            return true;
        }
        return false;
    }
}
