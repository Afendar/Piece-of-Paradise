package core;

import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import javax.imageio.ImageIO;

public class ResourceManager
{
    private Map<String, BufferedImage> m_spritesheets = new HashMap<>();
    private Map<String, BufferedImage> m_sprites = new HashMap<>();
    private Map<String, Font> m_fonts = new HashMap<>();
    
    private static ResourceManager INSTANCE = null;
    
    private ResourceManager()
    {
        try
        {
            BufferedImage spritesheet = ImageIO.read(getClass().getResourceAsStream("/gfx/spritesheet.png"));
            m_spritesheets.put("spritesheet", spritesheet);
            
            m_sprites.put("seeds", spritesheet.getSubimage(0, 73, 8, 8));
            m_sprites.put("wheat", spritesheet.getSubimage(0, 65, 8, 8));
            m_sprites.put("wood", spritesheet.getSubimage(8, 65, 8, 8));
            
            spritesheet = ImageIO.read(getClass().getResourceAsStream("/gfx/afendar.png"));
            m_spritesheets.put("afendar", spritesheet);
            
            m_fonts.put("anotherDayInParadise", Font.createFont(Font.TRUETYPE_FONT, getClass().getResourceAsStream("/gfx/fonts/another_day_in_paradise.ttf")));
        }
        catch(IOException | FontFormatException e)
        {
            e.getMessage();
        }
    }
    
    public BufferedImage getSpritesheets(String name)
    {
        return m_spritesheets.get(name);
    }
    
    public Font getFont(String name)
    {
        return m_fonts.get(name);
    }
    
    public BufferedImage getSprite(String name)
    {
        return m_sprites.get(name);
    }
    
    public static ResourceManager getInstance()
    {
        if(INSTANCE == null)
        {
            INSTANCE = new ResourceManager();
        }
        return INSTANCE;
    }
}
