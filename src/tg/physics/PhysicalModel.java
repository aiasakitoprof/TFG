package src.tg.physics;


import src.tg.helper.DoubleVector;
import src.tg.helper.Position;
import src.tg.local.vo.dynamics.Planet;

import java.awt.*;
import java.io.Serializable;

import static java.lang.System.currentTimeMillis;


/**
 * @author juanm
 * <p>
 * A SIMPLE PHYSICAL MODEL APPLIED TO DYNAMIC OBJECTS BY DEFAULT
 */
public class PhysicalModel implements Serializable {

    public static boolean debugMode = false;

    public PhysicalCharacteristics phyCharacteristics;
    public PhysicalVariables phyVariables;


    /**
     * CONSTRUCTORS
     */
    public PhysicalModel() {
        this.phyCharacteristics = new PhysicalCharacteristics();
        this.phyVariables = new PhysicalVariables();

        this.phyVariables.acceleration = new DoubleVector(0, 0);
        this.phyVariables.speed = new DoubleVector(0, 0);
    }


    public void calcNewLocation(Position pos, PhysicalVariables phyVar) {
        long nowMillis, elapsedMillis;
        DoubleVector offset;
        DoubleVector speed;
        Position newPos;

        phyVar.cloneSpeed(this.phyVariables.speed);
        phyVar.cloneAcceleration(this.phyVariables.acceleration);

        // Tiempo transcurrido desde el último cambio de posición
        nowMillis = currentTimeMillis();
        elapsedMillis = nowMillis - pos.getPositionMillis();

        // Desplazamiento durante el lapso de tiempo -> e = v*t
        offset = this.calcOffset(phyVar.speed, elapsedMillis);

        // Nueva posición
        newPos = new Position(pos);
        newPos.add(offset);
        newPos.setPositionMillis(nowMillis);

        pos.clone(newPos);

        // Incremento de velocidad debido a la aceleración -> v = v + a*t
        speed = this.calcSpeed(phyVar.acceleration, phyVar.speed, elapsedMillis);
        phyVar.cloneSpeed(speed);

    }

    public void calcNewLocationGravity(Position pos, PhysicalVariables phyVar) {
        DoubleVector objectPos = new DoubleVector(pos);
        DoubleVector gravAccel = new DoubleVector(0, 0);
        double G = 6e-11;

        phyVar.cloneSpeed(this.phyVariables.speed);
        phyVar.cloneAcceleration(this.phyVariables.acceleration);

        for (Planet planet : Planet.getAllPlanets()) {
            DoubleVector planetPos = planet.getPosition();

            double dx = planetPos.getX() - objectPos.getX();
            double dy = planetPos.getY() - objectPos.getY();
            double r2 = dx * dx + dy * dy;
            double distance = Math.sqrt(r2);

            if (distance < 1) distance = 1;

            double forceMagnitude = G * planet.getMass() / (distance * distance);

            double ax = forceMagnitude * dx / distance;
            double ay = forceMagnitude * dy / distance;

            gravAccel.setXY(gravAccel.getX() + ax, gravAccel.getY() + ay);
        }

        phyVar.acceleration.set(gravAccel);

        long nowMillis = currentTimeMillis();
        long elapsedMillis = nowMillis - pos.getPositionMillis();

        DoubleVector offset = this.calcOffset(phyVar.speed, elapsedMillis);
        Position newPos = new Position(pos);
        newPos.add(offset);
        newPos.setPositionMillis(nowMillis);

        pos.clone(newPos);
        DoubleVector speed = this.calcSpeed(phyVar.acceleration, phyVar.speed, elapsedMillis);
        phyVar.cloneSpeed(speed);
    }

    public DoubleVector calcOffset(DoubleVector speed, float elapsedMillis) {
        DoubleVector offset = new DoubleVector();

        offset.set(speed);
        offset.scale(elapsedMillis);  // e = v.t

        return offset;
    }


    public DoubleVector calcSpeed(DoubleVector acceleration, DoubleVector speed, float elapsedMillis) {
        DoubleVector newSpeed;
        DoubleVector speedIncrement;

        speedIncrement = new DoubleVector(acceleration); // a
        speedIncrement.scale(elapsedMillis);    // a*t

        newSpeed = new DoubleVector(speed);
        newSpeed.add(speedIncrement);         // v = v + a*t

        return newSpeed;
    }


    public void cloneAcceleration(DoubleVector fVector) {
        this.phyVariables.acceleration.set(fVector);
    }


    public void cloneSpeed(DoubleVector fVector) {
        this.phyVariables.speed.set(fVector);
    }


    public void paint(Graphics gr, Position pos) {
        if (!PhysicalModel.debugMode) {
            return;
        }

        int x = (int) pos.getX();
        int y = (int) pos.getY();

        // Aceleración
        gr.setColor(Color.RED);
        gr.drawLine(
                x, y,
                x + (int) (this.phyVariables.acceleration.getX() * 200000),
                y + (int) (this.phyVariables.acceleration.getY() * 200000));

        // Velocidad
        gr.setColor(Color.YELLOW);
        gr.drawLine(
                x, y,
                x + (int) (this.phyVariables.speed.getX() * 50),
                y + (int) (this.phyVariables.speed.getY() * 50));
    }


    public void resetAcceleration() {
        this.phyVariables.acceleration = new DoubleVector(0, 0);
    }


    public void resetSpeed() {
        this.phyVariables.speed = new DoubleVector(0, 0);
    }

}
