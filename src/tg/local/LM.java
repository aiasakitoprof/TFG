package src.tg.local;


import src.tg.local.vo.VO;
import src.tg.local.vo.VOD;
import src.tg.local.vo.VOState;
import src.tg.local.vo.dynamics.Asteroid;

import java.util.ArrayList;


/**
 * LOCAL MODEL
 */
public class LM {

    private static long voCreated = 0;
    private static long voDeads = 0;

    private final ArrayList<VO> visualObjects;
    private final LCT localController;


    /**
     * CONSTRUCTORS
     */
    public LM(LCT localControler) {
        this.localController = localControler;
        this.visualObjects = new ArrayList<>();
    }

    /**
     * STATICS
     */
    static long getVOCreated() {
        return LM.voCreated;
    }

    static long getVODeads() {
        return LM.voDeads;
    }

    /**
     * ONLY PACKAGE
     */
    synchronized public void addVisualObject(VO visualObject) {
        this.visualObjects.add(visualObject);
        visualObject.setLocalModel(this);

        LM.voCreated++;

        visualObject.setId(LM.voCreated);

        if (visualObject instanceof VOD) {
            ((VOD) visualObject).activate();
        }
    }

    public void collisionDetection(VOD vod) {
        ArrayList<VO> vos = this.getVOList();
        ArrayList<VO> voCollided = new ArrayList<VO>();

        if (vod.getState() != VOState.ALIVE) {
            return;
        }

        for (VO vo : vos) {
            if (vo != vod) {

                if (vo.getState() == VOState.ALIVE) {
                    if (vo.getBoundingEllipse().intersects(vod.getBoundingBox())) {
                        if (vod.getBoundingEllipse().intersects(vo.getBoundingBox())) {
                            vo.setState(VOState.COLLIDED);
                            vod.setState(VOState.COLLIDED);
                            voCollided.add(vo);
                        }
                    }
                }
            }
        }

        if (voCollided.size() > 0) {
            this.localController.collisionManagement(vod, voCollided);
        }
    }

    synchronized public void removeVO(VO visualObject) {
        this.visualObjects.remove(visualObject);
        LM.voDeads++;
    }

    synchronized ArrayList<VO> getVOList() {
        return (ArrayList) this.visualObjects.clone();
    }

    synchronized public ArrayList<Asteroid> getAllAsteroids() {
        ArrayList<Asteroid> asteroids = new ArrayList<>();
        for (VO vo : this.visualObjects) {
            if (vo instanceof Asteroid) {
                asteroids.add((Asteroid) vo);
            }
        }
        return asteroids;
    }
}
