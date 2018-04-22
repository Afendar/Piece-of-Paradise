package states;

import core.ResourceManager;
import core.state.BaseState;
import core.state.StateManager;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;

public class WinState extends BaseState
{
    public WinState(StateManager stateManager)
    {
        super(stateManager);
        
        setTransparent(true);
    }

    @Override
    public void onCreate()
    {
        
    }

    @Override
    public void onDestroy()
    {
        
    }

    @Override
    public void activate()
    {
        
    }

    @Override
    public void desactivate()
    {
        
    }

    @Override
    public void update(double dt)
    {
        
    }

    @Override
    public void render(Graphics2D g)
    {
        ResourceManager rm = m_stateManager.getContext().m_resourceManager;
        
        g.setColor(new Color(0, 0, 0, 180));
        g.fillRect(0, 0, 800, 600);
        
        g.drawImage(rm.getSpritesheets("spritesheet").getSubimage(112, 24, 100, 120), (800 - 300) / 2, (600 - 160 * 3) / 2, 100 * 3, 120 * 3, null);
        
        Font f = rm.getFont("anotherDayInParadise").deriveFont(Font.PLAIN, 32.0f);
        FontMetrics fm = g.getFontMetrics(f);
        int w = fm.stringWidth("You win !!");
        
        g.setColor(new Color(0, 115, 164));
        g.setFont(f);
        g.drawString("You win !!", (800 - w) / 2, (600 - fm.getAscent()) / 2 - 140);
        
        g.setColor(Color.BLACK);
        f = f.deriveFont(Font.PLAIN, 26.0f);
        g.setFont(f);
        fm = g.getFontMetrics(f);
        w = fm.stringWidth("You managed to escape");
        g.drawString("You managed to escape", (800 - w) / 2, (600 - fm.getAscent()) / 2 - 40);
        
        w = fm.stringWidth("with the other survivors");
        g.drawString("with the other survivors", (800 - w) / 2, (600 - fm.getAscent()) / 2 - 10);
        
        w = fm.stringWidth("Thanks for playing");
        g.drawString("Thanks for playing", (800 - w) / 2, (600 - fm.getAscent()) / 2 + 100);
        
        g.setColor(Color.WHITE);
        
        f = f.deriveFont(Font.PLAIN, 22.0f);
        g.setFont(f);
        fm = g.getFontMetrics(f);
        w = fm.stringWidth("Game made with love by Afendar");
        g.drawString("Game made with love by Afendar", (800 - w) / 2, 600 - 130);
        g.drawImage(rm.getSpritesheets("afendar"), (800 - 80) / 2, 600 - 120, 60, 60, null);
    }
}
