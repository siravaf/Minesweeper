package edu.ufp.sd.sdminesweeper.server;

import edu.ufp.sd.sdminesweeper.client.MineSweeperClientRI;
import edu.ufp.sd.sdminesweeper.minefield.generator.MineFieldGenerator;
import edu.ufp.sd.sdminesweeper.models.GameModeManager;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Game {

    private int nplayers;
    private int width;
    private int height;
    private int bombsNumber;
    private ArrayList<MineSweeperClientRI> players;
    private MineFieldGenerator mineFieldGenerator;
    private int roomID;
    private String roomName;
    private GameModeManager gameModeManager;
    
    public Game(int width, int height, int bombsNumber) {
        this.width = width;
        this.height = height;
        this.bombsNumber = bombsNumber;
        this.players = new ArrayList<>();
        this.mineFieldGenerator = new MineFieldGenerator(
                this.width, this.height, this.bombsNumber
        );
    }

     public void removeClient(MineSweeperClientRI client) {
        int index = players.indexOf(client);
        players.set(index, null);                  
        try {
            System.out.println(client.getClientUsername() + " left server " + this.roomID);
            
        } catch (RemoteException ex) {
            Logger.getLogger(Game.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
     
    
    public String getRoomName() {
        return roomName;
    }

    public void setRoomName(String roomName) {
        this.roomName = roomName;
    }

    
    
    public int getRoomID() {
        return roomID;
    }

    public void setRoomID(int roomID) {
        this.roomID = roomID;
    }
    

    public int attachToGame(MineSweeperClientRI mineSweeperClientRI) {
        System.out.println("Adicionado com sucesso!! Jogador numero " + players.size());
        this.players.add(mineSweeperClientRI);
        //System.out.println("Sucesso----> " + players.size());
        nplayers=players.size();
        return players.size();
    }
    
  

    public MineFieldGenerator getMineFieldGenerator() {
        return mineFieldGenerator;
    }

    public void notifyNewMoviment(int row, int col) {
        for (MineSweeperClientRI mineSweeperClientRI : this.players) {
            try {
                mineSweeperClientRI.simulateNewMoviment(row, col);
            } catch (RemoteException e) {
            }
        }
    }
    
   

    public GameModeManager getGameMode() {
        return gameModeManager;
    }

    public void setGameModeManager(GameModeManager gameModeManager) {
        this.gameModeManager = gameModeManager;
    }

    public int getNplayers() {
        return nplayers;
    }

    public void setNplayers(int nplayers) {
        this.nplayers = nplayers;
    }
}
