package src.tg.coms;


import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;


/*

SERVER MODE CONNECTOR

 */
class SC implements Runnable {

    CCT comsController;
    private int activePort;
    private int auxPort;
    private int mainPort;
    private Thread threadSC = null;
    private ServerSocket serverSocket = null;


    /*
    
    CONSTRUCTORS
    
     */
    protected SC(CCT comsController, int port) {
        this.activePort = port;
        this.mainPort = -1; // Queda desactivado
        this.auxPort = -1; // Queda desactivado

        this.comsController = comsController;
        this.threadSC = new Thread(this);
        this.threadSC.setName("SC Thread · Accept peers connecting requests");
    }


    // Selecciona de forma automatica el puerto. 
    // Si puede selecciona el main y si este no funciona seleccionar
    // el auxiliar. Esto va bien para varios peers en la misma IP.
    protected SC(CCT comsController) {
        this.activePort = -1;
        this.mainPort = 10000; // Queda desactivado
        this.auxPort = this.mainPort + 1; // Queda desactivado

        this.comsController = comsController;
        this.threadSC = new Thread(this);
        this.threadSC.setName("SC Thread · Accept peers connecting requests");
    }


    /*
    
    PROTECTEDS
    
     */
    void activate() {
        this.threadSC.start();
    }


    protected int getPort() {
        return this.activePort;
    }


    protected boolean isOk() {
        return this.serverSocket != null;
    }


    /*
    
    PRIVATES
    
     */
    private boolean openServerPort() {
        if (this.isOk()) {
            return false; // Puerto no ha abierto porque ya los estaba ...
        }

        if (this.activePort == -1) {
            return this.openServerSocketInAutoMode();
        }

        return this.openServerSocketInNormalMode();
    }


    private boolean openServerSocketInAutoMode() {
        // Mode MAIN
        try {

            this.serverSocket = new ServerSocket(this.mainPort);
            this.activePort = this.mainPort;
            System.out.println("Server Socket main port open! (SC) · " + this.serverSocket);
            return true; // ======================= OK ========================>

        } catch (IOException e) {
            System.out.println("Main port establishment failed! (SC) · " + e.getMessage());
            this.activePort = -1;
            this.serverSocket = null;
        }

        // Mode AUX
        try {
            this.serverSocket = new ServerSocket(this.auxPort);
            this.activePort = this.auxPort;
            System.out.println("Server Socket auxport open! (SC) · " + this.serverSocket);
            return true; // ======================= OK ========================>

        } catch (IOException e) {
            System.out.println("Auxiliary port establishment also failed! (SC) · " + e.getMessage());
            System.err.println("ERROR: Server socket could not be open! (SC)");
            this.serverSocket = null;
        }

        this.activePort = -1;
        return false;
    }


    private boolean openServerSocketInNormalMode() {
        try {

            this.serverSocket = new ServerSocket(this.activePort);
            this.activePort = this.mainPort;
            System.out.println("Server Socket port open! (SC) · " + this.serverSocket);
            return true; // ======================= OK ========================>

        } catch (IOException e) {
            System.out.println("Port establishment failed! (SC) · " + e.getMessage());
            this.serverSocket = null;
        }

        return false; // ====================== Not OK ========================>
    }


    private Socket waitForConnectionRequest() {
        Socket clientSocket = null;

        try {
            clientSocket = this.serverSocket.accept();
            return clientSocket;  // ==========================================>

        } catch (Exception e1) {
            System.err.println("ERROR: Accepting new client reequest! (SC) · " + e1.getMessage());
        }

        try {
            serverSocket.close();
        } catch (Exception e2) {
            System.err.println("ERROR: Closing server Socket! (SC) · " + e2.getMessage());
        }

        clientSocket = null;
        this.serverSocket = null;
        return null;
    }


    private void waitPortToBeOpen() {
        while (!this.isOk()) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException ex) {
                System.err.println("ERROR: Trying to sleep! (SC) · " + ex.getMessage());
            }

            System.out.println("Trying to reactivate server socket! (SC)");
            this.openServerPort();
        }
    }


    @Override
    public void run() {
        Socket newSocket = null;

        while (true) {
            this.waitPortToBeOpen();
            newSocket = this.waitForConnectionRequest();

            if (newSocket != null) {
                if (this.comsController.setSocketToChannel(newSocket)) {
                    System.out.println("Succesfully connection established! (SC) · " + newSocket);
                } else {
                    System.out.println("Connection request received rejected! (SC) · " + newSocket);
                }
            }
        }
    }
}
