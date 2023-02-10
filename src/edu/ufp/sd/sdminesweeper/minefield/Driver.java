package edu.ufp.sd.sdminesweeper.minefield;

import edu.ufp.sd.sdminesweeper.timers.SystemTimeUpdater;
import edu.ufp.sd.sdminesweeper.minefield.jgui.MainFrame;
import edu.ufp.sd.sdminesweeper.minefield.jgui.MineSweeperMenuBar;
import edu.ufp.sd.sdminesweeper.models.GameModeManager;
import edu.ufp.sd.sdminesweeper.server.MineSweeperServerRI;

/**
 * The main class. This class starts the application.
 *
 * @author Sorin ( soriniulus@yahoo.com ) At: Apr 8, 2007, 8:23:57 PM
 */
public class Driver {
    
    public static final String absPathToResourceIcons = "/edu/ufp/sd/sdminesweeper/resources/icons";
    
      public Driver(MineSweeperServerRI mineSweeperServerRI, int roomID) {
        MainFrame.getInstance().setMineSweeperServerRI(mineSweeperServerRI);
        MainFrame.getInstance().setRoomID(roomID);
        MainFrame.getInstance().startNewGame();
        MainFrame.getInstance().setLocationRelativeTo(null);
        MainFrame.getInstance().setJMenuBar(MineSweeperMenuBar.getInstance());
        GameModeManager.getInstance().addGameModeListener(MainFrame.getInstance());
        SystemTimeUpdater.getInstance();
        MainFrame.getInstance().pack();
        
    }
}
