package states;

import core.ResourceManager;
import core.state.BaseState;
import core.state.StateManager;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;

public class LoseState extends BaseState
{
    public LoseState(StateManager stateManager)
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
        int w = fm.stringWidth("You lose !!");
        
        g.setColor(new Color(136, 0, 21));
        g.setFont(f);
        g.drawString("You lose !!", (800 - w) / 2, (600 - fm.getAscent()) / 2 - 140);
        
        g.setColor(Color.BLACK);
        f = f.deriveFont(Font.PLAIN, 26.0f);
        g.setFont(f);
        fm = g.getFontMetrics(f);
        w = fm.stringWidth("Too bad, you have not managed");
        g.drawString("Too bad, you have not managed", (800 - w) / 2, (600 - fm.getAscent()) / 2 - 40);
        
        w = fm.stringWidth("to fulfill all the objectives");
        g.drawString("to fulfill all the objectives", (800 - w) / 2, (600 - fm.getAscent()) / 2 - 10);
        
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
