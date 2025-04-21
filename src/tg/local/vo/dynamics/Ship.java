package src.tg.local.vo.dynamics;

import src.tg.helper.DoubleVector;
import src.tg.local.vo.VOD;

import java.awt.*;
import java.awt.event.KeyEvent;
import javax.swing.SwingUtilities;

public class Ship extends VOD {

    private static final int SIZE = 40;
    private static final double SPEED = 7.0;

    private volatile boolean up, down, left, right;

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
        while (this.getState() != src.tg.local.vo.VOState.DEAD) {
            if (this.getState() == src.tg.local.vo.VOState.ALIVE) {
                double dx = 0, dy = 0;
                if (up) dy -= SPEED;
                if (down) dy += SPEED;
                if (left) dx -= SPEED;
                if (right) dx += SPEED;
                if (dx != 0 || dy != 0) {
                    this.getPosition().add(new DoubleVector(dx, dy));
                }
            }
            try {
                Thread.sleep(16); // ~60 FPS
            } catch (InterruptedException ignore) {}
        }
    }

    @Override
    public void paint(Graphics gr) {
        super.paint(gr);

        Graphics2D g2 = (Graphics2D) gr.create();
        int x = (int) (getPosition().getX() - SIZE / 2);
        int y = (int) (getPosition().getY() - SIZE / 2);
        g2.setColor(Color.WHITE);
        g2.fillRect(x, y, SIZE, SIZE);
        g2.setColor(Color.BLACK);
        g2.drawRect(x, y, SIZE, SIZE);
        g2.dispose();
    }
}