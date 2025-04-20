package tg.coms;


import java.io.IOException;
import java.io.Serializable;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;


/*

COMMUNICATION CONTROLLER

 */
public class CCT {

    private HashMap<Interlocutor, CH> channels;
    private SC serverConnector;
    private CC clientConnector;
    private ReceivedObjectsManager objectManager;


    /*
    
    CONSTRUCTORS
    
     */
    public CCT(ReceivedObjectsManager objectManager) {
        this.channels = new HashMap<>();
        this.objectManager = objectManager;
        this.serverConnector = new SC(this);
        this.clientConnector = new CC(this);

        this.serverConnector.activate();
        this.clientConnector.activate();
    }


    /*
    
    PUBLICS
    
     */
    public void addChannel(Interlocutor interlocutor) {
        this.channels.put(interlocutor, new CH(this, interlocutor));
    }


    public int getServerConnectorPort() {
        return this.serverConnector.getPort();
    }


    public boolean getServerConnectorIsOk() {
        return this.serverConnector.isOk();
    }


    public boolean sendObject(Interlocutor interlocutor, Serializable obj) {
        CH channel;
        DataFrame df;

        // Buscamos el canal asociado al interlocutor
        channel = this.channels.get(interlocutor);

        if (channel == null) {
            System.err.println("ERROR Message not sent because channel not found! (CCT)");
            return false; // No se ha encontrado canal asociado a interlocutor
        }

        df = new DataFrame();

        df.type = DataFrameType.APP_OBJECT;
        df.payLoad = obj;

        // Se podria implementar una politica de 3 reintentos
        // para casos en los que no se consigue enviar la trama...
        return channel.sendDataFrame(df);
    }


    /*
    
    PROTECTED METHODS
    
     */
    protected void deliverObjectReceived(Object obj) {
        // Entregar objeto recibido a la aplicación
        this.objectManager.manageReceivedObject(obj);
    }


    protected ArrayList<CH> getDownChannels() {
        ArrayList<CH> downChannels = new ArrayList();

        for (CH channel : this.channels.values()) {
            if (!channel.isOk()) {
                downChannels.add(channel);
            }
        }
        return downChannels;
    }


    protected boolean setSocketToChannel(Socket socket) {
        CH channel = null;

        // Buscar el canal correspondiente a la IP
        // Como la lista de canales es muy estática se podria acelerar
        // usando un segungo hashmap (redundante) por Ip.
        for (CH ch : this.channels.values()) {
            if (ch.getInetAddress().equals(socket.getInetAddress().getHostAddress())) {
                channel = ch;
                break;
            }
        }

        if (channel == null) {
            // Falta cerrar el socket
            return false; // ============== Channel not found =================>
        }

        return channel.setSocket(socket);
    }
}
