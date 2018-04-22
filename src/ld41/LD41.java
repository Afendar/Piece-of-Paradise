package ld41;

import core.Game;
import core.TimerThread;

public class LD41
{
    public static void main(String[] args)
    {
        TimerThread timer = new TimerThread();
        timer.start();
        Game game = new Game();
    }
}
