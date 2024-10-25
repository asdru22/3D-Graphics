package io;

import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;

public class MousePositionHandler extends MouseMotionAdapter {
    public int x = 0, y = 0;
    public int deltaX = 0, deltaY = 0;
    private int lastX = 0, lastY = 0;

    @Override
    public void mouseMoved(MouseEvent e) {
        int newX = e.getX();
        int newY = e.getY();

        deltaX = newX - lastX;
        deltaY = newY - lastY;

        lastX = newX;
        lastY = newY;

        x = newX;
        y = newY;
    }
}

