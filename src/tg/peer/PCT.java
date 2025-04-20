package src.tg.peer;


import src.tg.coms.CCT;
import src.tg.coms.Interlocutor;
import src.tg.coms.ReceivedObjectsManager;
import src.tg.local.LCT;
import src.tg.local.vo.VO;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Random;


public class PCT implements Runnable, ReceivedObjectsManager {

    private long receivedObjects;
    private long sentObjects;
    private CCT commsController;
    private LCT localController;
    private Thread threadPCT;
    private ArrayList<Interlocutor> interlocutors;


    /**
     * CONSTRUCTOR
     */
    public PCT() {

        this.receivedObjects = 0;
        this.sentObjects = 0;
        this.commsController = new CCT(this);
        this.localController = new LCT(this);
        this.interlocutors = new ArrayList<Interlocutor>();

        // Solo para el caso de dos instancias en la misma IP
        this.createInterlocutorWithSameIP();
        this.createChannels();

        this.threadPCT = new Thread(this); // Envia paquetes de prueba
        this.threadPCT.setName("PCT-Thread · AppFrames Sender");
    }

    /**
     * STATICS
     */
    public static long getVOCreated() {
        return LCT.getVOCreated();
    }

    public static long getVODeads() {
        return LCT.getVODeads();
    }

    /**
     * PUBLICS
     */
    public void activate() {
        this.threadPCT.start();
    }

    public void addVisualObject(VO vo) {
        this.localController.addVisualObject(vo);
    }

    /**
     * PRIVATES
     */
    private void addPeer(PeerLocation location, String ip, int port) {
        PeerInterlocutor newPeer;
        Random random = new Random();

        // TO-DO:   Ver como generar identificadores de peers....
        //          De momento le meto un random().
        newPeer = new PeerInterlocutor(ip, port, location, random.nextInt(100));

        this.interlocutors.add(newPeer);

    }

    private void createChannels() {
        for (Interlocutor interlocutor : this.interlocutors) {
            this.commsController.addChannel(interlocutor);
        }
    }

    // Solo para usar en caso de dos instancias en la misma IP
    // Solo usar para este caso!!!!
    private void createInterlocutorWithSameIP() {
        int port;
        String inetAddress;
        PeerLocation location;

        inetAddress = this.getLocalIp();
        port = this.getRemotePort();
        location = PeerLocation.EAST;

        if (inetAddress != null) {
            this.addPeer(location, inetAddress, port);
        }
    }

    private String getLocalIp() {
        String ip = null;

        try {
            ip = InetAddress.getLocalHost().getHostAddress();

        } catch (UnknownHostException ex) {
            System.err.println("ERROR Recovering local address! (PCT) · " + ex.getMessage());
        }

        return ip;
    }

    private int getRemotePort() {
        int remotePort;
        int serverConnectorPort;

        // Waiting for server connector &&**
        while (!this.commsController.getServerConnectorIsOk()) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException ex) {

            }
        }

        serverConnectorPort = this.commsController.getServerConnectorPort();

        // Selección del puerto del otro interlocutor
        remotePort = 10001;
        if (serverConnectorPort == 10001) {
            remotePort = 10000;
        }

        return remotePort;
    }

    private void manageMessage(AppMessage msg) {
        if (msg == null) {
            System.err.println("ERROR Null message object received! (PCT)");
            return; // =======================================================>
        }

        System.out.println(
                "Message received (PCT) · "
                        + msg.message
                        + " ·  [RECEIVED SEQ:"
                        + Long.toString(this.receivedObjects)
                        + "]");
    }

    private boolean sendMessage(Interlocutor interlocutor, String msg) {
        AppFrame af = new AppFrame();
        AppMessage appMsg = new AppMessage();

        appMsg.message = msg + " [SEND SEQ: "
                + Long.toString(this.sentObjects)
                + "]";

        af.type = AppFrameType.APP_MESSAGE;
        af.payLoad = appMsg;

        if (!this.commsController.sendObject(interlocutor, af)) {
            return false;
        }

        this.sentObjects++;
        return true;
    }

    /**
     * OVERRIDES
     */
    @Override
    public void manageReceivedObject(Object obj) {
        AppFrame ap = (AppFrame) obj;

        switch (ap.type) {
            case APP_MESSAGE:
                this.manageMessage((AppMessage) ap.payLoad);
                break;
            case ASTEROID:
                break;
            case BALL:
                break;
            case PLAYER_SHIP:
                break;
            case MISIL:
                break;
            case BULLET:
                break;
            case PLAYER_SHIP_ACTION:
                break;
        }

        this.receivedObjects++;
    }

    // Hilo de prueba que envia mensajes cada cierto tiempo.
    // Solo a efectos de testing
    @Override
    public void run() {
        Random random = new Random();

        while (true) {
            // Hay interlocutores
            if (this.interlocutors.size() > 0) {
                if (!this.sendMessage(this.interlocutors.get(0), "This is a test message!")) {
                    System.err.println("ERROR Unsent message! (PCT)");
                }
            }

            try {
                Thread.sleep(random.nextInt(4000));
            } catch (InterruptedException ex) {
            }
        }
    }

}
