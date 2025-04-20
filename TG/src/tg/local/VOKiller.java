package tg.local;


import tg.local.vo.VO;


/**
 *
 */
public class VOKiller extends Thread {

    VO visualObject;


    public VOKiller(VO vo) {
        super();

        this.setPriority(MIN_PRIORITY);
        this.visualObject = vo;
    }


    @Override
    public void run() {
        this.visualObject.die();
    }
}
