package visualization;

import util.MouseMode;

import java.awt.*;
import java.awt.font.FontRenderContext;
import java.awt.font.GlyphVector;
import java.awt.geom.Rectangle2D;

public class Button {
    private int x, y, width, height;
    private MouseMode mode = MouseMode.VERTEX;
    private String text = "";

    public Button(MouseMode m, int x, int y, int w, int h) {
        mode = m;
        this.x = x;
        this.y = y;
        width = w;
        height = h;
    }

    public void setText(String s) {
        text = s;
    }

    public boolean isWithin(int mx, int my) {
        boolean valid = mx > x && mx < x + width && my > y && my < y + height;
        if (valid) {
            Drawer.oldMode = Drawer.mode;
            Drawer.mode = mode;
            Drawer.buttonAction();
        }
        return valid;
    }

    public void draw(Graphics g) {
        g.setFont(new Font("Arial", Font.BOLD, 14));
        if (Drawer.mode == mode) {
            g.setColor(Color.DARK_GRAY);
        } else {
            g.setColor(Color.LIGHT_GRAY);
        }
        g.fillRect(x, y, width, height);

        if (Drawer.mode == mode) {
            g.setColor(Color.red);
            g.drawRect(x, y, width, height);
        }

        if (Drawer.mode == mode) {
            g.setColor(Color.LIGHT_GRAY);
        } else {
            g.setColor(Color.DARK_GRAY);
        }
        drawStringCentered(g, text, x - 5, width, y + height / 2, g.getColor());
        //g.drawString(text,x,y);
    }

    /////////////////////////////////////////////////////////////////////////
    //Draws a String centered x-wise, at the y value stated,
    //with Color c
    public static void drawStringCentered(Graphics g, String s, int x, int width, int y, Color c) {
        g.setColor(c);
        Graphics2D g2 = (Graphics2D) g;
        FontRenderContext frc = g2.getFontRenderContext();
        GlyphVector gv1 = g2.getFont().createGlyphVector(frc, s);
        Rectangle2D bds1 = gv1.getVisualBounds();
        int w = (int) bds1.getWidth();
        //
        g.drawString(s, x + (width / 2 - w / 2), y);
    }

    public MouseMode getMode() {
        return mode;
    }
}