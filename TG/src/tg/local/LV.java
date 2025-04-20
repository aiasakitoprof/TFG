package tg.local;


import java.awt.Container;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.util.ArrayList;
import javax.swing.JFrame;
import tg.local.vo.VO;


/**
 * LOCAL VIEW
 */
class LV extends JFrame implements MouseWheelListener, ActionListener, ComponentListener {

    private LCT localController;
    private CP controlPanel;
    private VW viewer;

    private final int GAME_PIX_HEIGHT = 700;
    private final int GAME_PIX_WIDTH = 1300;


    /**
     * CONSTRUCTOR
     */
    LV(LCT localController) {
        this.localController = localController;

        this.controlPanel = new CP(this);
        this.viewer = new VW(this, GAME_PIX_HEIGHT, GAME_PIX_WIDTH);

        this.createFrame();
        this.viewer.activate();
    }


    /**
     * PRIVATE
     */
    private void addVW(Container pane) {
        GridBagConstraints c = new GridBagConstraints();

        c.anchor = GridBagConstraints.NORTHWEST;
        c.fill = GridBagConstraints.BOTH;
        c.gridx = 1;
        c.gridy = 0;
        c.weightx = 1F;
        c.weighty = 0;
        c.gridheight = 10;
        c.gridwidth = 8;
        pane.add(this.viewer, c);
    }


    private void createFrame() {
        Container panel;

        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLayout(new GridBagLayout());

        panel = this.getContentPane();

        // Add components to panel
        this.addVW(panel);

        panel.addMouseWheelListener(this);

        this.pack();
        this.setVisible(true);

        this.addComponentListener(this);
    }


    /**
     * ONLY PACKAGE
     */
    ArrayList<VO> getVOList() {
        return this.localController.getVOList();
    }


    /**
     * OVERRIDES
     */
    @Override
    public void mouseWheelMoved(MouseWheelEvent mwe) {
    }


    @Override
    public void actionPerformed(ActionEvent ae) {
    }


    @Override
    public void componentResized(ComponentEvent ce) {
    }


    @Override
    public void componentMoved(ComponentEvent ce) {
    }


    @Override
    public void componentShown(ComponentEvent ce) {
    }


    @Override
    public void componentHidden(ComponentEvent ce) {
    }

}
