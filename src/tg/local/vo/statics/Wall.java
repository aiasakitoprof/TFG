/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package src.tg.local.vo.statics;


import src.tg.local.vo.VO;

import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;


/**
 * @author juanm
 */
class Wall extends VO {

    WallLocation location;

    private Rectangle2D.Double boundingBox;


    public Wall(WallLocation location) {
        this.location = location;
        this.boundingBox = new Rectangle2D.Double(0, 0, 0, 0);
    }


    public Rectangle2D.Double getBoundingBox() {
        return this.boundingBox;
    }


    public Ellipse2D.Double getBoundingEllipse() {
        return null;
    }

}
