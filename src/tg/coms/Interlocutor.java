package src.tg.coms;


public class Interlocutor {

    public String inetAddress;
    public int port;


    protected Interlocutor() {
    }


    protected Interlocutor(String inetAddress, int port) {
        this.inetAddress = inetAddress;
        this.port = port;
    }
}
