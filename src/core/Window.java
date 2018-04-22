package core;

import java.awt.Dimension;
import javax.swing.JFrame;

public class Window extends JFrame
{
    private final Game m_game;
    private int m_width;
    private int m_height;
    
    public Window(Game game)
    {
        m_game = game;
        init();
    }
    
    private void init()
    {
        setTitle(m_game.getName() + " - v" + m_game.getVersion());
        add(m_game);
        getContentPane().setPreferredSize(new Dimension(m_width, m_height));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        setLayout(null);
        pack();
        setVisible(true);
        setLocationRelativeTo(null);
    }
}
