package src.tg.local.vo.dynamics;

import src.tg.helper.DoubleVector;
import src.tg.helper.Position;
import src.tg.local.vo.VOD;
import src.tg.local.vo.VOState;
import src.tg.physics.PhysicalVariables;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;

public class Ship extends VOD {

    private static final int SIZE = 20;
    private static final double ACCELERATION = 0.0005;
    private static final double ROTATION_SPEED = 0.22;
    private static final double MAX_SPEED = 0.5;

    private volatile boolean up, down, left, right;
    private volatile double angle = 270;
    private volatile boolean braking = false;

    private final int windowWidth = 1300;
    private final int windowHeight = 700;

    public Ship(DoubleVector startPos) {
        this.getPosition().setXY(startPos.getX(), startPos.getY());
        setupKeyBindings();
    }

    private void setupKeyBindings() {
        SwingUtilities.invokeLater(() -> {
            KeyboardFocusManager.getCurrentKeyboardFocusManager().addKeyEventDispatcher(e -> {
                if (e.getID() == KeyEvent.KEY_PRESSED || e.getID() == KeyEvent.KEY_RELEASED) {
                    boolean pressed = e.getID() == KeyEvent.KEY_PRESSED;
                    switch (e.getKeyCode()) {
                        case KeyEvent.VK_W -> up = pressed;
                        case KeyEvent.VK_S -> down = pressed;
                        case KeyEvent.VK_A -> left = pressed;
                        case KeyEvent.VK_D -> right = pressed;
                        case KeyEvent.VK_Q -> braking = pressed;
                    }
                }
                return false;
            });
        });
    }

    @Override
    public void run() {
        PhysicalVariables phyVars = this.getPhysicalModel().phyVariables;
        Position pos = this.getPosition();

        long prevMillis = System.currentTimeMillis();

        while (this.getState() != src.tg.local.vo.VOState.DEAD) {
            if (this.getState() == src.tg.local.vo.VOState.ALIVE) {
                long nowMillis = System.currentTimeMillis();
                long elapsed = nowMillis - prevMillis;
                prevMillis = nowMillis;

                if (left) angle -= ROTATION_SPEED * elapsed;
                if (right) angle += ROTATION_SPEED * elapsed;

                DoubleVector userAcc = new DoubleVector(0, 0);
                if (up) {
                    userAcc.setXY(
                            Math.cos(Math.toRadians(angle)),
                            Math.sin(Math.toRadians(angle))
                    );
                    userAcc.scale(ACCELERATION);
                }
                if (down) {
                    userAcc.setXY(
                            Math.cos(Math.toRadians(angle + 180)),
                            Math.sin(Math.toRadians(angle + 180))
                    );
                    userAcc.scale(ACCELERATION);
                }

                DoubleVector gravAccel = new DoubleVector(0, 0);
                double G = 3e-11;
                DoubleVector shipPos = new DoubleVector(pos);
                for (Planet planet : Planet.getAllPlanets()) {
                    DoubleVector planetPos = planet.getPosition();
                    double dx = planetPos.getX() - shipPos.getX();
                    double dy = planetPos.getY() - shipPos.getY();
                    double r2 = dx * dx + dy * dy;
                    double distance = Math.sqrt(r2);

                    if (distance < 1) distance = 1;

                    double forceMagnitude = G * planet.getMass() / (distance * distance);
                    double ax = forceMagnitude * dx / distance;
                    double ay = forceMagnitude * dy / distance;
                    gravAccel.setXY(gravAccel.getX() + ax, gravAccel.getY() + ay);
                }

                DoubleVector totalAccel = new DoubleVector(userAcc);
                totalAccel.add(gravAccel);

                if (braking) {
                    final double BRAKE_STRENGTH = 0.0005;

                    DoubleVector brakeAccel = new DoubleVector(phyVars.speed);
                    if (brakeAccel.getModule() > 0) {
                        brakeAccel.scale(-1);
                        double speedMag = phyVars.speed.getModule();
                        double brakeMag = Math.min(BRAKE_STRENGTH, speedMag / (elapsed > 0 ? elapsed : 1));
                        if (speedMag > 0) {
                            brakeAccel.scale(brakeMag / speedMag);
                            totalAccel.add(brakeAccel);
                        }
                    }
                }

                DoubleVector speed = new DoubleVector(phyVars.speed);
                DoubleVector speedIncrement = new DoubleVector(totalAccel);
                speedIncrement.scale(elapsed);
                speed.add(speedIncrement);

                if (speed.getModule() > MAX_SPEED) {
                    double scale = MAX_SPEED / speed.getModule();
                    speed.scale(scale);
                }

                phyVars.speed.set(speed);
                phyVars.acceleration.set(totalAccel);

                DoubleVector offset = new DoubleVector(speed);
                offset.scale(elapsed);
                pos.add(offset);
                pos.setPositionMillis(nowMillis);

                double x = pos.getX();
                double y = pos.getY();
                double halfSize = SIZE / 2.0;

                boolean bounced = false;
                if (x - halfSize < 0) {
                    pos.setX(halfSize);
                    phyVars.speed.setX(-phyVars.speed.getX());
                    bounced = true;
                }
                if (x + halfSize > windowWidth) {
                    pos.setX(windowWidth - halfSize);
                    phyVars.speed.setX(-phyVars.speed.getX());
                    bounced = true;
                }
                if (y - halfSize < 0) {
                    pos.setY(halfSize);
                    phyVars.speed.setY(-phyVars.speed.getY());
                    bounced = true;
                }
                if (y + halfSize > windowHeight) {
                    pos.setY(windowHeight - halfSize);
                    phyVars.speed.setY(-phyVars.speed.getY());
                    bounced = true;
                }
                if (bounced) {
                    phyVars.speed.scale(0.85);
                }

                for (Planet planet : Planet.getAllPlanets()) {
                    double dx = planet.getPosition().getX() - pos.getX();
                    double dy = planet.getPosition().getY() - pos.getY();
                    double distance = Math.sqrt(dx * dx + dy * dy);

                    double planetRadius = planet.getMainImage().getScaledImageDimension().getModule() / 2.0;
                    double shipRadius = SIZE / 2.0;

                    if (distance < planetRadius + shipRadius) {
                        this.setState(VOState.DEAD);
                        break;
                    }
                }
                this.getLocalModel().collisionDetection(this);
            }

            try {
                Thread.sleep(16);
            } catch (InterruptedException ignore) {}
        }
    }

    @Override
    public void paint(Graphics gr) {
        if (getState() == VOState.DEAD) {
            return;
        }
        super.paint(gr);

        Graphics2D g2 = (Graphics2D) gr.create();
        int x = (int) (getPosition().getX());
        int y = (int) (getPosition().getY());

        Polygon shipShape = new Polygon();
        double r = SIZE / 2.0;

        for (int i = 0; i < 3; ++i) {
            double theta = Math.toRadians(angle + (i == 0 ? 0 : (i == 1 ? 135 : -135)));
            int px = x + (int) (r * Math.cos(theta));
            int py = y + (int) (r * Math.sin(theta));
            shipShape.addPoint(px, py);
        }

        g2.setColor(Color.WHITE);
        g2.fillPolygon(shipShape);
        g2.setColor(Color.BLACK);
        g2.drawPolygon(shipShape);

        g2.dispose();
    }

    private void stopShip() {
        PhysicalVariables phyVars = this.getPhysicalModel().phyVariables;
        phyVars.speed.setXY(0, 0);
        phyVars.acceleration.setXY(0, 0);
    }
}