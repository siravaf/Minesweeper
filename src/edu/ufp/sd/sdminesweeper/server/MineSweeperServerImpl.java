package edu.ufp.sd.sdminesweeper.server;


import edu.ufp.sd.sdminesweeper.client.MineSweeperClientRI;
import edu.ufp.sd.sdminesweeper.minefield.generator.MineFieldGenerator;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.rmi.server.UnicastRemoteObject;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;


public class MineSweeperServerImpl extends UnicastRemoteObject implements MineSweeperServerRI {
    
    private Object state;
    

    protected ArrayList<MineSweeperClientRI> clients = new ArrayList<>();
    private ArrayList<Game>rooms = new ArrayList<>();
    public static String PATH_USERS = "/Users/pedrocoutinho/NetBeansProjects/SDMineSweeper/data/";
    public static String PATH_LEVELS = "../../res/levels/";
    // Uses RMI-default sockets-based transport
    // Runs forever (do not passivates) - Do not needs rmid (activation deamon)
    // Constructor must throw RemoteException due to export()
    public MineSweeperServerImpl() throws RemoteException {
        // Invokes UnicastRemoteObject constructor which exports remote object
        super();
        this.clients = new ArrayList<>();
        this.rooms = new ArrayList<>();
        this.rooms.add(new Game(30,20,130));
    }
    

    
    @Override
    public synchronized int login(MineSweeperClientRI client, String username, String password) throws RemoteException {
        System.out.println("MineSweeperServerImpl - login(): " + username + " " + password);
        if (clientAlreadyLoggedin(username)) {
            System.out.println("MineSweeperServerImpl - login(): " + username + " is already logged in.");
            return 0;
        }
        
        String user_path = PATH_USERS + username + ".txt";
        System.out.println("MineSweeperServerImpl - var userpath: " + user_path);
        BufferedReader br = null;
        try {
            br = new BufferedReader(new FileReader(new File(user_path)));
            String file_password = br.readLine();
            if (file_password.compareTo(password) == 0) {
                client.sendMessage("Your password is correct!");
                System.out.println("MineSweeperServerImpl - login(): " + username + " is now logged in.");
                clients.add(client);
                return 1;
            } else {
                client.sendMessage("Your password is incorrect!");
                System.out.println("MineSweeperServerImpl - login(): " + username + " has failed the password.");
                return 0;
            }
            
        } catch (FileNotFoundException ex) {
            client.sendMessage("Your account is not found, going to register this one.");
            return 0;
        } catch (IOException ex) {
            return 0;
        } finally {
            try {
                if (br != null) {
                    br.close();
                }
            } catch (IOException ex) {
                Logger.getLogger(MineSweeperServerImpl.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    @Override
    public synchronized int logout(String username) throws RemoteException {
        System.out.println("MineSweeperServerImpl - logout(): " + username);
        for (MineSweeperClientRI user : this.clients) {
            if (user.getClientUsername().compareTo(username) == 0) {
                clients.remove(user);
                return 1;
            }
        }
        return 0;
    }
    
    @Override
    public int register(MineSweeperClientRI client, String username, String password) throws RemoteException {
        String user_path = PATH_USERS + username + ".txt";
        File f = new File(user_path);
        
        if (f.exists()) {
            client.sendMessage("This username is already registed!");
            return 0;
        }
        
        BufferedWriter bw = null;
        try {
            bw = new BufferedWriter(new FileWriter(new File(user_path)));
            bw.write(password);
        } catch (IOException e) {
            Logger.getLogger(MineSweeperServerImpl.class.getName()).log(Level.SEVERE, null, e);
            return 0;
        } finally {
            try {
                if (bw != null) {
                    bw.close();
                    client.sendMessage("Your account has been created!");
                }
            } catch (IOException e) {
                Logger.getLogger(MineSweeperServerImpl.class.getName()).log(Level.SEVERE, null, e);
            }
        }
        return 1;
    }
    
   
    
    
    @Override
    public Object getState() throws RemoteException {
        return this.state;
    }
    
    @Override
    public void setState(Object s) throws RemoteException {
        System.out.println("MineSweeperServerImpl - setState(): " + s.getClass().getName());
        this.state = s;
        if (!clients.isEmpty()) {
            notifyAllObservers();
        }
    }
    
    public void notifyAllObservers() throws RemoteException {
        System.out.println("MineSweeperServerImpl - notifyAllObservers()");
        for (MineSweeperClientRI client : clients) {
            client.update();
        }
    }
    
    @Override
    public int countConnectedClients() throws RemoteException {
        return this.clients.size();
    }
    
    @Override
    public int createGameRoom(MineSweeperClientRI client, String level) throws RemoteException {
       for(MineSweeperClientRI c : clients){
           if(client.equals(c)){
               Game newServer=null;
        
               if(level.equals("Expert Mode")){
                    newServer = new Game(30,20,130);
                    System.out.println("Modo():................Expert");
       
                }else if(level.equals("Medium Mode")){
                    newServer = new Game(20,15,60);
                    System.out.println("Modo():...............Medium");
                }else{
                    newServer = new Game(15,10,15);
                    System.out.println("Modo():................Junior");
                }
       
                this.rooms.add(newServer);
                newServer.setRoomID(rooms.indexOf(newServer));
                String name = rooms.size() - 1 + "# MineSweeper Room"
                + " - Level: " + level;
                newServer.setRoomName(name);
        
                this.setState(new State().new NewRoom(false, name));
                return rooms.size() - 1;
                }
        }
        System.out.println("Cliente nao fez login!");
        return 0;
        
    }
    

    @Override
    public String[] fetchAvaliableLevels() throws RemoteException {
        List<String> stockList = new ArrayList<>();
        
        File directory = new File(PATH_LEVELS);
        File[] fileList = directory.listFiles();
        String fileName, fileNameExtValue;
        int fileNameExtIndex;
        
        for (File file : fileList) {
            fileName = file.getName();
            fileNameExtIndex = fileName.lastIndexOf('.');
            
            if (fileNameExtIndex > 0) {
                fileNameExtValue = fileName.substring(fileNameExtIndex, fileName.length());
                
                if (fileNameExtValue.equals(".xml")) {
                    fileName = fileName.substring(0, fileNameExtIndex);
                    System.out.println(fileName);
                    stockList.add(fileName);
                }
            }
        }

        // Convert to String[] (required)
        String[] itemsArr = new String[stockList.size()];
        itemsArr = stockList.toArray(itemsArr);
        
        return itemsArr;
    }
    
    @Override
    public String[] fetchAvaliableRooms() throws RemoteException {
        
        String[] itemsArr = new String[rooms.size()];
        if (!rooms.isEmpty()) {
            for (int i = 0; i < rooms.size(); i++) {
                itemsArr[i] = rooms.get(i).getRoomName();
            }
        }
        
        return itemsArr;
    }
    
   
    
    private boolean clientAlreadyLoggedin(String username) {
        System.out.println("clientAlreadyLoggedin: " + username);
        try {
            for (MineSweeperClientRI client : clients) {
                if (client.getClientUsername().compareTo(username) == 0) {
                    return true;
                }
            }
        } catch (RemoteException ex) {
            Logger.getLogger(MineSweeperServerImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
        
    }
    
    
    
    public MineSweeperClientRI clientFromUsername(String username) {
        try {
            for (MineSweeperClientRI client : clients) {
                if (client.getClientUsername().compareTo(username) == 0) {
                    return client;
                }
            }
        } catch (RemoteException ex) {
            Logger.getLogger(MineSweeperServerImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
    

    
    
    public void shutdown() {
        System.out.println("SHUTDOWN SERVER");
        System.exit(0);
    }
    
   
     @Override
    public void detach(MineSweeperClientRI client) throws RemoteException {
        this.clients.remove(client);
    }
    
    @Override
    public void removeClientFromRoom(MineSweeperClientRI client, int roomID) throws RemoteException {
        Game server = null;
        try {
            server = this.rooms.get(roomID);
            
            server.removeClient(client);
            
        } catch (IndexOutOfBoundsException ex) {
            Logger.getLogger(MineSweeperServerImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
   
    @Override
    public int attachGame(MineSweeperClientRI mineSweeperClientRI, int id) throws RemoteException {
        System.out.println("Added player " + this.rooms.get(id).getNplayers());
        this.rooms.get(id).attachToGame(mineSweeperClientRI);
        
        return this.rooms.get(id).getNplayers();
    }
    
    @Override
    public MineFieldGenerator getGameMap(int id) throws RemoteException {
        System.out.println("Get map " + id);
        return this.rooms.get(id).getMineFieldGenerator();
    }
     
    public void notifyPlayersNewMoviment(int id, int row, int col) throws RemoteException {
        System.out.println("Notify moviment " + id);
        this.rooms.get(id).notifyNewMoviment(row, col);
    }

}
