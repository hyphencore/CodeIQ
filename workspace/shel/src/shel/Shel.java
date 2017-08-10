package shel;

import java.applet.Applet;
import java.awt.Color;
import java.awt.Graphics;

public class Shel extends Applet{
    private static final long serialVersionUID = 1L;

    private int maxLevel = 5;

    public void init() {
        setSize(640,480);
        setBackground(Color.BLACK);
    }

    public void paint(Graphics g) {
        g.setColor(Color.GREEN);
        drawSierpinskiGasket(g, 319, 40, 30, 430, 609, 430, 0);
    }

    private void drawSierpinskiGasket(Graphics g, int x1, int y1, int x2, int y2, int x3, int y3, int level) {
        if (level == maxLevel) {
            g.drawLine(x1, y1, x2, y2);
            g.drawLine(x2, y2, x3, y3);
            g.drawLine(x3, y3, x1, y1);

        } else {
            int xx1 = (int)((x1 + x2) / 2.0);
            int yy1 = (int)((y1 + y2) / 2.0);
            int xx2 = (int)((x2 + x3) / 2.0);
            int yy2 = (int)((y2 + y3) / 2.0);
            int xx3 = (int)((x3 + x1) / 2.0);
            int yy3 = (int)((y3 + y1) / 2.0);

            level++;

            drawSierpinskiGasket(g, x1, y1, xx1, yy1, xx3, yy3, level);
            drawSierpinskiGasket(g, x2, y2, xx1, yy1, xx2, yy2, level);
            drawSierpinskiGasket(g, x3, y3, xx3, yy3, xx2, yy2, level);
        }
    }
}