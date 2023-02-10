package edu.ufp.sd.sdminesweeper.client;


import edu.ufp.sd.sdminesweeper.minefield.Driver;
import edu.ufp.sd.sdminesweeper.minefield.jgui.MainFrame;
import edu.ufp.sd.sdminesweeper.server.MineSweeperServerImpl;
import edu.ufp.sd.sdminesweeper.server.MineSweeperServerRI;
import edu.ufp.sd.sdminesweeper.server.State;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;


public class MineSweeperClientImpl implements MineSweeperClientRI {

    private Object lastState;

    protected MineSweeperServerRI mineSweeperServerRI;
    private MineSweeperClientUserGUI mineSweeperLoginUI = new MineSweeperClientUserGUI(this);
    private MineSweeperClientHallGUI mineSweeperHallUI = null;
    
    private String username;
    private String password;
    private boolean loggedin;


    
    public MineSweeperClientImpl(MineSweeperServerRI mineSweeperServerRI) throws RemoteException {
        exportObjectMethod();
        this.mineSweeperServerRI = mineSweeperServerRI;
    }

    private void exportObjectMethod() {
        try {
            UnicastRemoteObject.exportObject(this, 0);
        } catch (RemoteException e) {
            System.out.println("MineSweeperClientImpl: " + e.getMessage());
        }
    }

    public String getUsername() {
        return this.username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isLoggedin() {
        return loggedin;
    }

    public void setLoggedin(boolean loggedin) {
        this.loggedin = loggedin;
    }

    public Object getLastState() {
        return lastState;
    }

    public void setLastState(Object lastState) {
        this.lastState = lastState;
    }

    public MineSweeperServerRI getBdsRI() {
        return mineSweeperServerRI;
    }

    public void setBdsRI(MineSweeperServerRI mineSweeperServerRI) {
        this.mineSweeperServerRI = mineSweeperServerRI;
    }

    @Override
    public void sendMessage(String message) throws RemoteException {
        System.out.println("MineSweeperClientImpl - sendMessage(): " + message);
    }

    @Override
    public String getClientUsername() throws RemoteException {
        return this.username;
    }

    @Override
    public void update() throws RemoteException {
        System.out.println("MineSweeperClientImpl - update(): ");
        this.lastState = this.mineSweeperServerRI.getState();
        if (lastState instanceof State.Message) {
            System.out.println("MineSweeperClientImpl - update(): State = MessageState ");
            if (mineSweeperHallUI != null) {
                mineSweeperHallUI.updateMesssages();
            }
        } else if (lastState instanceof State.ConnectedClients) {
            System.out.println("MineSweeperClientImpl - update(): State = ConnectedClients ");
            if (mineSweeperHallUI != null) {
                mineSweeperHallUI.updateClients();
            }
        } else if (lastState instanceof State.NewRoom) {
            System.out.println("MineSweeperClientImpl - update(): State = NewRoom ");
            State.NewRoom nr = (State.NewRoom) lastState;
            if (nr.isRemoveAll()) {
                mineSweeperHallUI.removeAllRooms();
            } else {
                mineSweeperHallUI.addNewRoom(nr);
            }

        } else if (lastState instanceof State.GenericState) {
            State.GenericState state = (State.GenericState) lastState;
            String type = state.getType();
            System.out.println("MineSweeperClientImpl - update(): State = GenericState(" + type + ")");
            switch (type) {
                case "RoomsUpdate": {
                    mineSweeperHallUI.updateAllRooms();
                    break;
                }
            }
        } else if (lastState instanceof State.Disconnect) {
            System.out.println("MineSweeperClientImpl - update(): State = Disconnect ");
            disconnect();
        }
    }
    
    
    public void disconnect() throws RemoteException {
        System.exit(0);
    }

    public void triggeredLogin(String username, String password) {
        this.setUsername(username);
        this.setPassword(password);
        System.out.println("triggeredLogin");
        try {
            int login = mineSweeperServerRI.login(this, username, password);
            
            System.out.println("int login " + login);
            if (login != 0) {
                System.out.println("You are now logged in!");
                this.setLoggedin(true);
                mineSweeperLoginUI.setVisible(false);
                mineSweeperHallUI = new MineSweeperClientHallGUI(this);
            }
        } catch (RemoteException e) {
            System.out.println(e.getMessage());
        }
    }

    public void triggeredRegister(String username, String password) {
        this.setUsername(username);
        this.setPassword(password);
        System.out.println("triggeredRegister");
        try {
            int register = mineSweeperServerRI.register(this, username, password);
            
            System.out.println("int register " + register);
            if (register != 0) {
                System.out.println("You are now registed, please login!");
            }

        } catch (RemoteException e) {
            System.out.println(e.getMessage());
        }
    }

    public void triggeredLogout(String username, String password) {
        System.out.println("triggeredLogout");
        try {
            this.setLoggedin(false);
            mineSweeperServerRI.logout(username);
        } catch (RemoteException e) {
        }
    }

    public MineSweeperClientUserGUI getMineSweeperLoginUI() {
        return mineSweeperLoginUI;
    }

    public void setMineSweeperLoginUI(MineSweeperClientUserGUI mineSweeperLoginUI) {
        this.mineSweeperLoginUI = mineSweeperLoginUI;
    }

    public MineSweeperClientHallGUI getMineSweeperHallUI() {
        return mineSweeperHallUI;
    }

    public void setMineSweeperHallUI(MineSweeperClientHallGUI mineSweeperHallUI) {
        this.mineSweeperHallUI = mineSweeperHallUI;
    }
    
    public void startGame(int id) throws RemoteException{
            
             if(mineSweeperServerRI.attachGame(this,id)<4){
                    mineSweeperHallUI.setVisible(false);
                    System.out.println("Driver()...........: roomID= " + id);
                    System.out.println("Entrou bem no jogo ");
                    new Driver(mineSweeperServerRI,id);
                }else{
                    System.out.println("Ja tem jogadores a mais!!");
                }       
    }
    
    
    @Override
    public void simulateNewMoviment(int row, int col) throws RemoteException {
        System.out.println("simulateNewMoviment().......:"+String.valueOf(row) + "  " + String.valueOf(col));
        MainFrame.getInstance().getMineFieldPanel().getBombButtons()[row][col].bombButtonPressed();
        MainFrame.getInstance().getMineFieldPanel().getBombButtons()[row][col].fireBombButtonPressed();
        
    }
}
