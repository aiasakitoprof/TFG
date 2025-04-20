package tg.coms;


/*

CHANNEL READER

 */
class CR extends Thread {

    private CH channel;


    /*
    
    CONSTRUCTORS
    
     */
    protected CR(CH channel) {
        this.channel = channel;
        this.setName("CR Thread · Channel reader · "
                + this.channel.getInetAddress() + ":" + this.channel.getPort());
    }


    /*
    
    OVERRIDES
    
     */
    @Override
    public void run() {
        DataFrame df = null;

        while (true) {
            df = this.channel.readChannel();

            if (df != null) {
                switch (df.type) {
                    case STAY_ALIVE:
                        System.out.println("CH: Stay alive received!");
                        break;
                    case APP_OBJECT:
                        this.channel.deliverObjectReceived(df.payLoad);
                        break;
                }
            }
        }
    }
}
