package edu.ufp.sd.sdminesweeper.models;

import java.util.Vector;

import edu.ufp.sd.sdminesweeper.minefield.jgui.MineSweeperToolbar;
import edu.ufp.sd.sdminesweeper.minefield.listeners.GameModeListener;
import edu.ufp.sd.sdminesweeper.client.MineSweeperClientRI;
import edu.ufp.sd.sdminesweeper.minefield.generator.MineFieldGenerator;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * This class manages the game modes.
 * 
 * @author Sorin ( soriniulus@yahoo.com )
 * At: Apr 11, 2007, 12:12:50 PM
 */
public class GameModeManager {

	private static GameModeManager INSTANCE = new GameModeManager();

	private String gameMode;

	private int bombsNumber;

	private int totalNumberOfBombs;

	private Vector listenerList;

	private boolean questionMarksOn;

        private ArrayList<MineSweeperClientRI> clients = new ArrayList<>(2);

        private int roomID;
        
        private String roomName;
        
        private MineFieldGenerator mineFieldGenerator;
        
        private int width;
        
        private int height;
        
        private int[] score;

        public int getScore(int index) {
            return score[index];
        }    
        public int[] getScore() {
            return score;
        }

        /**
         * Set score
         *
         * @param score
         */
        public void setScore(int[] score) {
            this.score = score;
        }
        /**
	 * Returns the single instance of this class.
	 * <p>
	 * 
	 * @return			the single instance of this class.
	 */
	public static GameModeManager getInstance() {
		return INSTANCE;
	}

	public GameModeManager() {
		listenerList = new Vector();
		gameMode = GameMode.expertMode;
		questionMarksOn = false;
	}
        
        public GameModeManager(int width, int height, int bombsNumber) {
        this.width = width;
        this.height = height;
        this.bombsNumber = bombsNumber;
        this.clients = new ArrayList<>();
        this.mineFieldGenerator = new MineFieldGenerator(
                this.width, this.height, this.bombsNumber
        );
    }

	/**
	 * Registers a listener who is interested in the change of the game mode.
	 * <p>
	 * @param 			listener			the listener who is interested in the change of the game mode.
	 */
	public void addGameModeListener(GameModeListener listener) {
		listenerList.add(listener);
	}

	/**
	 * Notifies all the registered listeners when the game mode changed.
	 *
	 */
	private void gameModeChanged() {
		Object[] listeners = listenerList.toArray();
		if (listeners.length == 0 || listeners == null)
			return;
		for (int i = 0; i < listeners.length; i++) {
			((GameModeListener) listeners[i]).gameModeChanged();
		}
	}

	/**
	 * Reset the game.
	 *
	 */
	public void reset() {
		if (gameMode.equalsIgnoreCase(GameMode.expertMode)) {
			bombsNumber = GameMode.expertBombsNumber;
		}
		if (gameMode.equalsIgnoreCase(GameMode.mediumMode)) {
			bombsNumber = GameMode.mediumBombsNumber;
		}
		if (gameMode.equalsIgnoreCase(GameMode.juniorMode)) {
			bombsNumber = GameMode.juniorBombsNumber;
		}
		totalNumberOfBombs = bombsNumber;
		MineSweeperToolbar.getInstance().setMinesLeft(
				GameModeManager.getInstance().getBombsNumber());
	}

	/**
	 * Returns the bombs left number.
	 * <p>
	 * @return			the bombs left number.
	 */
	public int getBombsNumber() {
		return bombsNumber;
	}

	public void setBombsNumber(int n) {
		bombsNumber = n;
		MineSweeperToolbar.getInstance().setMinesLeft(
				GameModeManager.getInstance().getBombsNumber());
	}

	/**
	 * Sets the game mode.
	 * <p>
	 * @param 			gameMode			the game mode.
	 */
	public void setGameMode(String gameMode) {
		this.gameMode = gameMode;
		gameModeChanged();
	}

	/**
	 * Returns the game mode.
	 * <p>
	 * @return			the game mode.
	 */
	public String getGameMode() {
		return gameMode;
	}

	/**
	 * Returns the minefield height.
	 * <p>
	 * 
	 * @return			the minefield height.
	 */
	public int getMineFieldHeight() {
		if (gameMode.equalsIgnoreCase(GameMode.expertMode))
			return GameMode.expertHeight;
		if (gameMode.equalsIgnoreCase(GameMode.mediumMode))
			return GameMode.mediumHeight;
		if (gameMode.equalsIgnoreCase(GameMode.juniorMode))
			return GameMode.juniorHeight;
		return 0;
	}

	/**
	 * Returns the minefield width.
	 * <p>
	 * 
	 * @return			the minefield width.
	 */
	public int getMineFieldWidth() {
		if (gameMode.equalsIgnoreCase(GameMode.expertMode))
			return GameMode.expertWidth;
		if (gameMode.equalsIgnoreCase(GameMode.mediumMode))
			return GameMode.mediumWidth;
		if (gameMode.equalsIgnoreCase(GameMode.juniorMode))
			return GameMode.juniorWidth;
		return 0;
	}

	/**
	 * Returns the total number of bombs.
	 * <p>
	 * @return			the total number of bombs.
	 */
	public int getTotalNumberOfBombs() {
		return totalNumberOfBombs;
	}

	/**
	 * Set the Question marks mode.
	 * <p>
	 * 
	 * @param 			b			true for Question marks mode on.
	 */
	public void setQuestionMarksOn(boolean b) {
		questionMarksOn = b;
	}

	/**
	 * Returns the Question marks mode.
	 * <p>
	 * 
	 * @return			the Question marks mode.
	 */
	public boolean isQuestionsMarksOn() {
		return questionMarksOn;
	}

    public int getRoomID() {
        return roomID;
    }

    public void setRoomID(int roomID) {
        this.roomID = roomID;
    }

    public String getRoomName() {
        return roomName;
    }

    public void setRoomName(String roomName) {
        this.roomName = roomName;
    }
        
        
        
        public void addClient(MineSweeperClientRI client) {
        try {
            
            System.out.println(client.getClientUsername() + " joined room " + this.roomID);

            if (this.clients.get(0) == null) {
                clients.set(0, client);
            } else {
                clients.set(1, client);
            }
            
            // All clients connected!
            if (this.clients.get(0) != null && this.clients.get(1) != null) {
                System.out.println("Room " + this.roomID + " is now full.");
               }
            
//            updateRoomName();

        } catch (RemoteException ex) {
            Logger.getLogger(GameModeManager.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
        
        public int getConnectedClients() {
        int sum = 0;
        for (MineSweeperClientRI client : this.clients) {
            if (client != null) {
                sum++;
            }
        }
        
        System.out.println("Connected clients in Room # " + this.roomID + " - " + sum);
        return sum;
    }
  
       
}
