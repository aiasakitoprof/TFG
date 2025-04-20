package src.tg.local;


import src.tg.local.vo.VO;
import src.tg.local.vo.VOD;
import src.tg.local.vo.dynamics.Planet;
import src.tg.peer.PCT;

import java.util.ArrayList;


/**
 * LOCAL CONTROLLER
 */
public class LCT {

    private LV localView;
    private LM localModel;
    private PCT peerController;


    /**
     * CONSTRUCTORS
     */
    public LCT(PCT peerController) {
        this.peerController = peerController;
        this.localModel = new LM(this);
        this.localView = new LV(this);
    }

    /**
     * STATICS
     */
    public static long getVOCreated() {
        return LM.getVOCreated();
    }

    public static long getVODeads() {
        return LM.getVODeads();
    }

    /**
     * ONLY PACKAGE
     */
    ArrayList<VO> getVOList() {
        return this.localModel.getVOList();
    }

    /**
     * PUBLICS
     */
    public void addVisualObject(VO vo) {
        this.localModel.addVisualObject(vo);
    }

    public void collisionManagement(VOD vod, ArrayList<VO> collided) {
        //
        // FX to implement
        // ---------------
        // Damage, disintegration, die, explosion, fire, impact
        // Fragmentation, nuclear accident, disappear, fade
        // Security vault, implosion, consolidation. lightning
        //

        for (VO vo : collided) {
            if (vo instanceof Planet) {
                ((Planet) vo).impact(vod);
            } else {
                new VOKiller(vo).start();
            }
        }

        new VOKiller(vod).start();
    }
}
