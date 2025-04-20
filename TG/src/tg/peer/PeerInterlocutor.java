package tg.peer;


import tg.coms.Interlocutor;


public class PeerInterlocutor extends Interlocutor {

    public PeerLocation location;
    public int peerId;


    PeerInterlocutor() {
        super();
    }


    PeerInterlocutor(String inetAddress, int port) {
        super(inetAddress, port);

        this.inetAddress = inetAddress;
        this.port = port;
    }


    PeerInterlocutor(String inetAddress, int port, PeerLocation location) {
        super(inetAddress, port);

        this.location = location;
    }


    PeerInterlocutor(String inetAddress, int port, PeerLocation location, int peerId) {
        super(inetAddress, port);

        this.location = location;
        this.peerId = peerId;
    }
}
