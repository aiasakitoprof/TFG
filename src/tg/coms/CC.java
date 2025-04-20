package src.tg.coms;


import java.net.Socket;
import java.util.ArrayList;
import java.util.Random;


/*

CLIENT MODE CONNECTOR

 */
class CC implements Runnable {

    CCT comsController;
    Thread ccThread;


    /*
    
    CONSTRUCTORS
    
     */
    protected CC(CCT comsController) {
        this.comsController = comsController;

        this.ccThread = new Thread(this);
        this.ccThread.setName("CC Thread · Request down peers to connect");
    }


    /* 
    
    PROTECTED
    
     */
    protected void activate() {
        this.ccThread.start();
    }


    /*
    
    PRIVATE
    
     */
    synchronized private void doSocketRequest(CH channel) {
        Socket clientSocket = null;

        if (channel.isOk()) {
            return;
        }

        try {
            clientSocket = new Socket(channel.getInetAddress(), channel.getPort());

            if (channel.setSocket(clientSocket)) {
                System.out.println("Succesfully connection established! (CC) · " + clientSocket);
            }

        } catch (Exception e) {
            System.err.println("ERROR: Requesting interlocutor to connect failed! (CC) · " + channel + " · " + e.getMessage());
        }
    }


    /*
    
    OVERRIDES
    
     */
    @Override
    public void run() {
        ArrayList<CH> downChannels;
        Random random = new Random();

        while (true) {
            try {
                Thread.sleep(random.nextInt(250));

            } catch (InterruptedException ex) {
                System.err.println(ex.getMessage());
            }

            downChannels = this.comsController.getDownChannels();

            for (CH channel : downChannels) {
                this.doSocketRequest(channel);
            }
        }
    }
}
