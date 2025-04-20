package tg.coms;


import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.Socket;


/*

CHANNEL

 */
class CH {

    private Interlocutor interlocutor = null;

    private long lastTimeRXFrame;
    private Socket socket = null;
    private CCT commsController = null;
    private ObjectOutputStream out = null;                                            // 2nd statement
    private ObjectInputStream in = null;
    private CR channelReader = null;
    private Thread channelThread = null;


    /*
    
    CONTRUCTORS
    
     */
    CH(CCT commsController, Interlocutor interlocutor) {
        this.commsController = commsController;
        this.interlocutor = interlocutor;
        this.channelReader = new CR(this);

        this.channelReader.start();

        this.socket = null;
        this.out = null;                                            // 2nd statement
        this.in = null;
    }


    /*
    
    ONLY PACKAGE
     
     */
    void deliverObjectReceived(Serializable obj) {
        this.commsController.deliverObjectReceived(obj);
    }


    synchronized String getInetAddress() {
        return this.interlocutor.inetAddress;
    }


    synchronized int getPort() {
        return this.interlocutor.port;
    }


    synchronized boolean isOk() {
        return this.socket != null;
    }


    synchronized boolean sendDataFrame(DataFrame df) {
        if (!this.isOk()) {
            // Ojo => La trama no se enviará 
            return false; // =================================================>
        }

        try {
            this.out.writeObject(df);
            this.out.flush();
            return true; // ==================================================>

        } catch (Exception ex) {
            System.err.println("ERROR Sending dataframe! (CH) · " + ex.getMessage());
            this.killSocket();
        }

        return false;
    }


    synchronized boolean setSocket(Socket socket) {
        if (this.isOk()) {
            System.out.println("Socket already setted! (CH)");
            try {
                socket.close();
            } catch (IOException ex) {
                System.err.println("ERROR Closing refused socket! (CH) · " + ex.getMessage());
            }
            return false; // ==================================================>
        }

        try {
            this.out = new ObjectOutputStream(socket.getOutputStream());
        } catch (IOException ex) {
            System.err.println("ERROR Creating output stream! (CH) · " + ex.getMessage());
            this.killSocket();
            this.notifyAll();
            return false; // ==================================================>
        }

        try {
            this.in = new ObjectInputStream(socket.getInputStream());
        } catch (IOException ex) {
            System.err.println("ERROR Creating input stream! (CH) · " + ex.getMessage());
            this.killSocket();
            this.notifyAll();
            return false; // ==================================================>
        }

        this.socket = socket;
        this.notifyAll();
        return true;
    }


    DataFrame readChannel() {
        DataFrame df;

        // Esperar a que el canal sea funcional!
        if (!waitingIsOk()) {
            return null;
        }

        // Esperar a que llegue una trama.
        try {
            df = (DataFrame) this.in.readObject();
            return df;

        } catch (ClassCastException cex) {
            System.err.println("ERROR Casting received object to dataframe! (CH) · " + cex.getMessage());

        } catch (Exception ex) {
            System.err.println("ERROR Receiving object! (CH) · " + ex.getMessage());

            this.killSocket();
        }

        return null;
    }


    /*
    
    PRIVATES
    
     */
    synchronized private Boolean waitingIsOk() {
        // Esperar a que el canal sea funcional!
        while (!this.isOk()) {
            try {
                this.wait();
            } catch (InterruptedException ex) {
                System.err.println("ERROR Wait interrupted!!! (CH) · " + ex.getMessage());
                return false;
            }
        }

        return true;
    }


    synchronized private void killSocket() {
        System.out.println("Killing socket now!!!! (CH)");
        try {
            this.out.close();
            this.in.close();
            this.socket.close();
        } catch (Exception ex) {
            System.err.println("ERROR closing channel! (CH) · " + ex.getMessage());
        }
        this.in = null;
        this.out = null;
        this.socket = null;
    }


    /*
    
    OVERRIDES
    
     */
    @Override
    public String toString() {
        return "<" + this.interlocutor.inetAddress + ":" + Integer.toString(this.interlocutor.port) + ">";
    }
}
