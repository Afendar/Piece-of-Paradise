package core;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;

public class InputsListeners implements KeyListener, MouseMotionListener, MouseListener
{
    public class Action
    {
        public boolean m_enabled, m_typed;
        public int m_pressCpt, m_absorbCpt;
        
        public Action()
        {
            m_actions.add(this);
        }
        
        public void switched(boolean enabled)
        {
            if(enabled != m_enabled)
            {
                m_enabled = enabled;
            }
            
            if(enabled)
            {
                m_pressCpt++;
            }
        }
        
        public void update()
        {
            if(m_absorbCpt < m_pressCpt)
            {
                m_absorbCpt++;
                m_typed = true;
            }
            else
            {
                m_typed = false;
            }
        }
    }
    
    private static InputsListeners INSTANCE = null;
    
    private final boolean[] m_keys;
    private final ArrayList<Action> m_actions = new ArrayList<>();
    
    public Action up = new Action();
    public Action down = new Action();
    public Action left = new Action();
    public Action right = new Action();
    public Action attack = new Action();
    
    public int m_mouseX, m_mouseY, m_mouseClickCount;
    
    public boolean m_mouseExited, m_mousePressed;
    
    private KeyEvent m_keyEvent;
    
    private InputsListeners(Game game)
    {
        game.addKeyListener(this);
        game.addMouseMotionListener(this);
        game.addMouseListener(this);
        
        m_mouseX = 0;
        m_mouseY = 0;
        m_mouseClickCount = 0;
        m_mouseExited = true;
        m_mousePressed = false;
        m_keys = new boolean[KeyEvent.KEY_LAST];
    }
    
    public static InputsListeners getInstance(Game game)
    {
        if(INSTANCE == null)
        {
            INSTANCE = new InputsListeners(game);
        }
        return INSTANCE;
    }
    
    public void update()
    {
        m_mouseClickCount = 0;
        for (Action m_action : m_actions)
        {
            m_action.update();
        }
    }
    
    private void processKey(KeyEvent e, boolean enabled)
    {
        switch(e.getKeyCode())
        {
            case KeyEvent.VK_NUMPAD8:
            case KeyEvent.VK_W:
            case KeyEvent.VK_UP:
                up.switched(enabled);
                break;
            case KeyEvent.VK_NUMPAD2:
            case KeyEvent.VK_S:
            case KeyEvent.VK_DOWN:
                down.switched(enabled);
                break;
            case KeyEvent.VK_NUMPAD4:
            case KeyEvent.VK_A:
            case KeyEvent.VK_LEFT:
                left.switched(enabled);
                break;
            case KeyEvent.VK_NUMPAD6:
            case KeyEvent.VK_D:
            case KeyEvent.VK_RIGHT:
                right.switched(enabled);
                break;
            case KeyEvent.VK_SPACE:
            case KeyEvent.VK_C:
            case KeyEvent.VK_ENTER:
                attack.switched(enabled);
                break;
        }
    }    
    
    @Override
    public void keyTyped(KeyEvent e)
    {
    }

    @Override
    public void keyPressed(KeyEvent e)
    {
        processKey(e, true);
        m_keyEvent = e;
    }

    @Override
    public void keyReleased(KeyEvent e)
    {
        processKey(e, false);
        m_keyEvent = null;
    }

    @Override
    public void mouseDragged(MouseEvent e)
    {
        m_mouseX = e.getX();
        m_mouseY = e.getY();
    }

    @Override
    public void mouseMoved(MouseEvent e)
    {
        m_mouseX = e.getX();
        m_mouseY = e.getY();
    }

    @Override
    public void mouseClicked(MouseEvent e)
    {
    }

    @Override
    public void mousePressed(MouseEvent e)
    {
        m_mousePressed = true;
        m_mouseClickCount = e.getClickCount();
    }

    @Override
    public void mouseReleased(MouseEvent e)
    {
        m_mousePressed = false;
    }

    @Override
    public void mouseEntered(MouseEvent e) 
    {
        m_mouseExited = false;
    }

    @Override
    public void mouseExited(MouseEvent e)
    {
        m_mouseExited = true;
    }
}
