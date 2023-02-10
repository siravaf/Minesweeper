package edu.ufp.sd.sdminesweeper.client;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * <p>
 * Title: Distributed Systems Project - MineSweeper</p>
 * <p>
 * Description: MineSweeper Game Multiplayer - Distributed using RMI</p>
 * <p>
 * Copyright: Copyright (c) 2017</p>
 * <p>
 * Company: UFP </p>
 *
 * @author Tiago Cardoso <tiagocardosoweb@gmail.com>
 * @author Miguel Ferreira <migueelfsf@gmail.com>
 * @version 0.1
 */
public interface MineSweeperClientRI extends Remote {

    /**
     * Returns the client username
     * 
     * @return
     * @throws RemoteException 
     */
    public String getClientUsername() throws RemoteException;

    /**
     * Outputs a client message in the console
     * 
     * @param message
     * @throws RemoteException 
     */
    public void sendMessage(String message) throws RemoteException;

    /**
     * Update for receiving states
     * 
     * @throws RemoteException 
     */
    public void update() throws RemoteException;
    

    public void simulateNewMoviment(int row, int col) throws RemoteException;
}
