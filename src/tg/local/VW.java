package src.tg.local;


import src.tg.helper.Position;
import src.tg.images.Images;
import src.tg.local.vo.VO;

import java.awt.*;
import java.awt.image.BufferStrategy;
import java.util.ArrayList;

import static java.lang.Long.max;
import static java.lang.System.currentTimeMillis;


/**
 * VIEWER
 */
class VW extends Canvas implements Runnable {

    private int framesPerSecond;
    private int maxFramesPerSecond;
    private int delayInMillis;
    private Image background;
    private LV localView;
    private int pixHeigh;
    private int pixWidth;
    private Thread thread;


    /**
     * CONSTRUCTORS
     */
    VW(LV localView, int pixHeigh, int pixWidth) {
        this.maxFramesPerSecond = 24;
        this.framesPerSecond = 0;
        this.delayInMillis = 30;
        this.localView = localView;
        this.pixHeigh = pixHeigh;
        this.pixWidth = pixWidth;

        this.thread = new Thread(this);
        this.thread.setName("VIEWER Thread · Create and display frames");
        //this.thread.setPriority(Thread.MAX_PRIORITY-1);

        Dimension d = new Dimension(pixWidth, pixHeigh);
        this.setPreferredSize(d);

        this.background = Images.getBackground(2);
    }


    /**
     * PUBLICS
     */
    public void activate() {
        this.thread.start();
    }


    /**
     * PRIVATES
     */
    private void paint() {
        BufferStrategy bs;

        bs = this.getBufferStrategy();
        if (bs == null) {
            System.out.println("kgd");
            return; // =======================================================>>
        }

        // Paint background
        Graphics gg = bs.getDrawGraphics();
        gg.drawImage(this.background, 0, 0, this.pixWidth, this.pixHeigh, null);

        // Paint visual objects
        this.paintVO(gg);

        bs.show();
        gg.dispose();
    }


    private void paintVO(Graphics g) {
        Position pos;
        ArrayList<VO> vos;
        //= new ArrayList<>();

        vos = this.localView.getVOList();
        for (VO vo : vos) {
            pos = vo.getPosition();

            if (pos.getX() <= this.pixWidth && pos.getY() <= this.pixHeigh
                    && pos.getX() >= 0 && pos.getY() >= 0) {
                vo.paint(g);
            } else {
                // Provisional
                vo.remove();
            }
        }
    }


    @Override
    public void run() {
        long lastPaintMillisTime;
        long lastPaintMillis;
        long delayMillis;
        long millisPerFrame;
        int framesCounter;

        this.createBufferStrategy(2);

        if ((this.pixHeigh <= 0) || (this.pixWidth <= 0)) {
            System.out.println("Canvas size error: (" + this.pixWidth + "," + this.pixHeigh + ")");
            return; // =======================================================>>
        }

        // Show frames
        framesCounter = 0;
        millisPerFrame = 1000 / this.maxFramesPerSecond;

        while (true) { // TO-DO End condition
            lastPaintMillisTime = currentTimeMillis();
            if (true) { // TO-DO Pause condition
                this.paint();
            }

            lastPaintMillis = currentTimeMillis() - lastPaintMillisTime;
            delayMillis = max(0, millisPerFrame - lastPaintMillis);
            try {
                Thread.sleep(delayMillis);
            } catch (InterruptedException ex) {
            }

        }
    }
}
