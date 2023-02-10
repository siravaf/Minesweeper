package edu.ufp.sd.sdminesweeper.server;

import java.net.InetAddress;
import java.rmi.AccessException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.logging.Level;
import java.util.logging.Logger;

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
public class MineSweeperServer {

    public static String serviceName = "rmi://localhost:1099/MineSweeperService";

    public MineSweeperServer(String hostIP) {
        System.out.println("CURRENT WORKING DIRECTORY:" + System.getProperty("user.dir"));
        try {
            // Create and install a security manager
            if (System.getSecurityManager() == null) {
                System.out.println("MineSweeperServer - Constructor(): set security manager");
                System.setSecurityManager(new SecurityManager());
            }
            // Get referencefor Registry
            InetAddress inetAddr = InetAddress.getLocalHost();
            //String serviceName = "rmi://localhost:1099/MineSweeperService";
            String hostName = inetAddr.getHostName();
            String hostAddress = inetAddr.getHostAddress();

            MineSweeperServer.serviceName = (hostIP == null ? "rmi://" + hostAddress + ":1099/MineSweeperService" : "rmi://" + hostIP + ":1099/MineSweeperService");

            System.out.println("MineSweeperServer - Constructor(): Local host is " + hostName + " at IP address " + hostAddress);
            System.out.println("MineSweeperServer - Constructor(): get registry on " + hostAddress + " - default port 1099");
            //Registry registry = LocateRegistry.getRegistry();
            Registry registry = LocateRegistry.getRegistry(inetAddr.getHostAddress(), 1099);
            if (registry != null) {
                String[] srvList = registry.list();
                System.out.println("MineSweeperServer - Constructor(): list of servervices svrList.length = " + srvList.length);

                for (int i = 0; i < srvList.length; i++) {
                    System.out.println("MineSweeperServer - Constructor(): service svrLis[" + i + "] = " + srvList[i]);
                }

                System.out.println("MineSweeperServer - Constructor(): try register service " + serviceName + "...");
                MineSweeperServerRI bdsRI = (MineSweeperServerRI) new MineSweeperServerImpl();

                try {
                    registry.rebind(MineSweeperServer.serviceName, bdsRI);
                    System.out.println("MineSweeperServer - Constructor(): service bound and running!");
                } catch (AccessException e) {
                    Logger.getLogger(MineSweeperServerImpl.class.getName()).log(Level.SEVERE, null, e);
                }

            } else {
                System.out.println("MineSweeperServer - Constructor(): create registry on port 1099");
                registry = LocateRegistry.createRegistry(1099);
            }
        } catch (Exception e) {
            e.printStackTrace();
            Logger.getLogger(MineSweeperServerImpl.class.getName()).log(Level.SEVERE, null, e);
        }
    }

    public static void main(String[] args) {
        for (int i = 0; args != null && i < args.length; i++) {
            System.out.println("MineSweeperServer - main(): args[" + i + "] = " + args[i]);
        }
        MineSweeperServer hws = new MineSweeperServer((args != null && args.length > 0 ? args[0] : null));
    }
}
