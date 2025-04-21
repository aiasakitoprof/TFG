package src.tg.local.vo.dynamics;

import src.tg.helper.DoubleVector;
import src.tg.helper.Position;
import src.tg.local.vo.VOD;
import src.tg.physics.PhysicalVariables;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.KeyboardFocusManager;
import javax.swing.SwingUtilities;

public class Ship extends VOD {

    private static final int SIZE = 20;
    private static final double ACCELERATION = 0.0005;
    private static final double ROTATION_SPEED = 0.22;
    private static final double MAX_SPEED = 0.5;

    private volatile boolean up, down, left, right;
    private volatile double angle = 270;

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

            DoubleVector acc = new DoubleVector(0, 0);
            if (up) {
                acc.setXY(
                    Math.cos(Math.toRadians(angle)),
                    Math.sin(Math.toRadians(angle))
                );
                acc.scale(ACCELERATION);
            }
            if (down) {
                acc.setXY(
                    Math.cos(Math.toRadians(angle + 180)),
                    Math.sin(Math.toRadians(angle + 180))
                );
                acc.scale(ACCELERATION);
            }

            DoubleVector speed = new DoubleVector(phyVars.speed);
            DoubleVector speedIncrement = new DoubleVector(acc);
            speedIncrement.scale(elapsed);
            speed.add(speedIncrement);

            if (speed.getModule() > MAX_SPEED) {
                double scale = MAX_SPEED / speed.getModule();
                speed.scale(scale);
            }

            phyVars.speed.set(speed);
            phyVars.acceleration.set(acc);

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
}