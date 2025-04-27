package src.tg.local.vo.dynamics;

import src.tg.helper.DoubleVector;
import src.tg.helper.Position;
import src.tg.local.vo.VOD;
import src.tg.local.vo.VOState;
import src.tg.physics.PhysicalVariables;
import src.tg.images.Images;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

public class Ship extends VOD {

    private static final int SIZE = 20;
    private static final double ACCELERATION = 0.0005;
    private static final double ROTATION_SPEED = 0.22;
    private static final double MAX_SPEED = 0.5;

    private boolean up, down, left, right;
    private double angle = 270;
    private boolean braking = false;

    private final int windowWidth = 1300;
    private final int windowHeight = 700;

    private final BufferedImage shipImage;

    public Ship(DoubleVector startPos) {
        this.getPosition().setXY(startPos.getX(), startPos.getY());
        setupKeyBindings();

        this.shipImage = Images.loadImage("src/tg/images/assets/spaceship-5.png");
        if (shipImage != null) {
            this.getMainImage().setImageAndDimension(shipImage);
        }
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
                double G = 6 * 10e-11;
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
        Graphics2D g2 = (Graphics2D) gr.create();
        int x = (int) (getPosition().getX());
        int y = (int) (getPosition().getY());

        if (shipImage != null) {
            int imageWidth = shipImage.getWidth();
            int imageHeight = shipImage.getHeight();

            AffineTransform transform = AffineTransform.getRotateInstance(
                    Math.toRadians(angle), x, y
            );
            g2.setTransform(transform);
            g2.drawImage(shipImage, x - imageWidth / 2, y - imageHeight / 2, null);
        } else {
            super.paint(gr);
        }
        g2.dispose();
    }
}