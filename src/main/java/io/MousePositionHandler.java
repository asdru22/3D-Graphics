package io;

import math.Double2D;

import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;

public class MousePositionHandler extends MouseMotionAdapter {
    public Double2D mousePos = new Double2D(0,0);

    @Override
    public void mouseMoved(MouseEvent e) {
        mousePos.x = e.getX();
        mousePos.y = e.getY();
    }
}

