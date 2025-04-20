/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tg.local.vo.statics;


import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;
import tg.helper.Position;
import tg.local.vo.VO;


/**
 *
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
