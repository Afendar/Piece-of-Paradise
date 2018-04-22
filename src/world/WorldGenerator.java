package world;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.util.Random;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import world.tile.Tile;

public class WorldGenerator
{
    private static final Random m_random = new Random();
    public double[] m_values;
    
    private int m_w, m_h;
    
    public WorldGenerator(int w, int h, int noise)
    {
        m_w = w;
        m_h = h;
        
        m_values = new double[w * h];
        
        for(int y = 0 ; y < h ; y += noise)
        {
            for(int x = 0 ; x < w ; x += noise)
            {
                setSample(x, y, m_random.nextFloat() * 2 - 1);
            }
        }
        
        int step = noise;
        double scale = 1 / w;
        double scaleMod = 1;
        do
        {
            int halfStep = step / 2;
            for(int y = 0 ; y < h ; y += step)
            {
                for(int x = 0 ; x < w ; x += step)
                {
                    double a = getSample(x, y);
                    double b = getSample(x + step, y);
                    double c = getSample(x, y + step);
                    double d = getSample(x + step, y + step);
                    
                    double e = (a + b + c + d) / 4 + (m_random.nextFloat() * 2 - 1) * step * scale;
                    setSample(x + halfStep, y + halfStep, e);
                 }
            }
            
            for(int y = 0 ; y < h ; y += step)
            {
                for(int x = 0 ; x < w ; x += step)
                {
                    double a = getSample(x, y);
                    double b = getSample(x + step, y);
                    double c = getSample(x, y + step);
                    double d = getSample(x + halfStep, y + halfStep);
                    double e = getSample(x + halfStep, y - halfStep);
                    double f = getSample(x - halfStep, y + halfStep);
                    
                    double k = (a + b + d + e) / 4 + (m_random.nextFloat() * 2 - 1) * step * scale * 0.5;
                    double l = (a + c + d + f) / 4 + (m_random.nextFloat() * 2 - 1) * step * scale * 0.5;
                    setSample(x + halfStep, y, k);
                    setSample(x, y + halfStep, l);
                }
            }
            
            step /= 2;
            scale *= (scaleMod + 0.8);
            scaleMod *= 0.3;
        }
        while(step > 1);
    }
    
    private double getSample(int x, int y)
    {
        return m_values[(x & (m_w - 1)) + (y & (m_h - 1)) * m_w];
    }
    
    private void setSample(int x, int y, double value)
    {
        m_values[(x & (m_w - 1)) + (y & (m_h - 1)) * m_w] = value;
    }
    
    public static int[][] createAndValidateMap(int w, int h)
    {
        int attempt = 0;
        do
        {
            int[][] result = generateMap(w, h);
            
            int[] count = new int[8];
            
            for(int i = 0; i < w ; i++)
            {
                for (int j = 0 ; j < h ; j++)
                {
                    count[result[i][j]]++;
                }
            }
            
            attempt++;
            
            System.out.println("attempt" + attempt);
            
            if(count[Tile.FARM.m_id] <= 0)continue;
            if(count[Tile.TREE.m_id] < 100)continue;
            
            return result;
        }
        while(true);
    }
    
    private static int[][] generateMap(int w, int h)
    {
        WorldGenerator noise1 = new WorldGenerator(w, h, 32);
        WorldGenerator noise2 = new WorldGenerator(w, h, 32);
        
        int[][] map = new int[w][h];
        int[][] data = new int[w][h];
        
        //generate grass isles
        for(int y = 0 ; y < h ; y++)
        {
            for(int x = 0 ; x < w ; x++)
            {
                int i = x + y * w;
                double val  = Math.abs(noise1.m_values[i] - noise2.m_values[i]) * 3 - 2;
                
                double xd = x / (w - 1.0) * 2 - 1;
                double yd = y / (h - 1.0) * 2 - 1;
                
                if(xd < 0) xd = -xd;
                if(yd < 0) yd = -yd;
                
                double dist = xd >= yd ? xd : yd;
                dist = Math.pow(dist, 6);
                val = val + 1 - dist * 15;
                
                if(val < -0.5)
                {
                    map[x][y] = Tile.WATER.m_id;
                }
                else
                {
                    map[x][y] = Tile.GRASS.m_id;
                }
            }
        }
        
        //geerate sand
        for(int i = 0 ; i < w * h / 2800 ; i++)
        {
            int xs = m_random.nextInt(w);
            int ys = m_random.nextInt(h);
            for(int k = 0 ; k < 10 ; k++)
            {
                int x = xs + m_random.nextInt(21) - 10;
                int y = ys + m_random.nextInt(21) - 10;
                for(int j = 0 ; j < 100 ; j++)
                {
                    int xo = x + m_random.nextInt(5) - m_random.nextInt(5);
                    int yo = y + m_random.nextInt(5) - m_random.nextInt(5);
                    for(int yy = yo - 1 ; yy <= yo + 1 ; yy++)
                    {
                        for(int xx = xo - 1 ; xx <= xo + 1 ; xx++)
                        {
                            if(xx >= 0 && yy >= 0 && xx < w && yy < h && map[xx][yy] == Tile.GRASS.m_id)
                            {
                                map[xx][yy] = Tile.SAND.m_id;
                            }
                        }
                    }
                }
            }
        }
        
        //generate houses
        for(int i = 0 ; i < 15 ; i++)
        {
            /*int x = m_random.nextInt(w - 6);
            int y = m_random.nextInt(h - 2);

            if(map[x][y] == Tile.GRASS.m_id && map[x + 6][y] == Tile.GRASS.m_id && 
                    map[x + 6][y + 2] == Tile.GRASS.m_id && map[x][y + 2] == Tile.GRASS.m_id)
            {
                for(int k = 0; k < 6; k++)
                {
                    for(int l = 0 ; l < 2 ; l++)
                    {
                        map[x + k][y + l] = Tile.HOUSE.m_id;
                    }
                }
            }*/
            
            int x = m_random.nextInt(w - 6);
            int y = m_random.nextInt(h - 2);
            
            if(map[x][y] == Tile.GRASS.m_id && map[x + 6][y] == Tile.GRASS.m_id && 
                    map[x + 6][y + 3] == Tile.GRASS.m_id && map[x][y + 3] == Tile.GRASS.m_id)
            {
                for(int k = 0; k < 6; k++)
                {
                    for(int l = 0 ; l < 3 ; l++)
                    {
                        if(k < 5)
                        {
                            map[x + k][y + l] = Tile.FARM.m_id;
                        }
                        else
                        {
                            map[x + k][y + l] = Tile.WHEAT.m_id;
                        }
                    }
                }
            }
        }
        
        //generate trees
        for(int i = 0 ; i < w * h / 1000 ; i++)
        {
            int x = m_random.nextInt(w);
            int y = m_random.nextInt(h);
            
            for(int j = 0 ; j < 50 ; j++)
            {
                int xx = x + m_random.nextInt(15) - m_random.nextInt(15);
                int yy = y + m_random.nextInt(15) - m_random.nextInt(15);
                if(xx >= 0 && yy >= 0 && xx < w && yy < h && map[xx][yy] == Tile.GRASS.m_id)
                {
                    map[xx][yy] = Tile.TREE.m_id;
                }
            }
        }
        
        return map;
    }
    
    public static void main(String[] args)
    {
        int d = 0;
        while(true)
        {
            int w = 64;
            int h = 64;
            
            int[][] map = WorldGenerator.createAndValidateMap(w, h);
            
            BufferedImage img = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
            int[] pixels = new int[w * h];
            
            for(int y = 0 ; y < h ; y++)
            {
                for(int x = 0 ; x < w ; x++)
                {
                    int i = x + y * h;
                    
                    if(map[x][y] == Tile.WATER.m_id)pixels[i] = 0x000080;
                    if(map[x][y] == Tile.GRASS.m_id)pixels[i] = 0x208020;
                    if(map[x][y] == Tile.SAND.m_id)pixels[i] = 0xA0A040;
                    if(map[x][y] == Tile.HOUSE.m_id)pixels[i] = 0xFF2020;
                    if(map[x][y] == Tile.FARM.m_id)pixels[i] = 0x84613C;
                }
            }
            img.setRGB(0, 0, w, h, pixels, 0, w);
            JOptionPane.showMessageDialog(null, null, "Map", JOptionPane.YES_NO_OPTION, new ImageIcon(img.getScaledInstance(w * 4, h * 4, Image.SCALE_AREA_AVERAGING)));
        }
    }
}
