package edu.ufp.sd.sdminesweeper.server;

import edu.ufp.sd.sdminesweeper.client.MineSweeperClientImpl;
import edu.ufp.sd.sdminesweeper.client.MineSweeperClientRI;
import edu.ufp.sd.sdminesweeper.minefield.generator.MineFieldGenerator;
import java.rmi.Remote;
import java.rmi.RemoteException;

public interface MineSweeperServerRI extends Remote {


    public void detach(MineSweeperClientRI client) throws RemoteException;

    public Object getState() throws RemoteException;

    public void setState(Object s) throws RemoteException;
    //End: Observer Design Pattern
    
   
    /**
     * Method to verify login
     * 
     * @param client
     * @param username
     * @param password
     * @return
     * @throws RemoteException 
     */
    public int login(MineSweeperClientRI client, String username, String password) throws RemoteException;

    /**
     * Method that is used for logout
     * @param username
     * @return
     * @throws RemoteException 
     */
    public int logout(String username) throws RemoteException;

    /**
     * Method used for register
     * @param client
     * @param username
     * @param password
     * @return
     * @throws RemoteException 
     */
    public int register(MineSweeperClientRI client, String username, String password) throws RemoteException;

    /**
     * Returns the current online clients
     * @return
     * @throws RemoteException 
     */
    public int countConnectedClients() throws RemoteException;

    /**
     * Creates a room for play with other player
     * @param client
     * @param level
     * @return
     * @throws RemoteException 
     */
    public int createGameRoom(MineSweeperClientRI client, String level) throws RemoteException;

    /**
     * Fetch the Levels avaliable
     * @return
     * @throws RemoteException 
     */
    public String[] fetchAvaliableLevels() throws RemoteException;

    /**
     * Fetch the Rooms avaliable
     * @return
     * @throws RemoteException 
     */
    public String[] fetchAvaliableRooms() throws RemoteException;

    /**
     * Removes the client from a room
     * @param client
     * @param roomID
     * @throws RemoteException 
     */
    public void removeClientFromRoom(MineSweeperClientRI client, int roomID) throws RemoteException;

    public int attachGame(MineSweeperClientRI mscRI,int id) throws RemoteException;
    
    public MineFieldGenerator getGameMap(int id) throws RemoteException;
    
    public void notifyPlayersNewMoviment(int id, int row, int col) throws RemoteException;
}

