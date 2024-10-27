package io;

import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;

public class MousePositionHandler extends MouseMotionAdapter {
    public int x = 0, y = 0;

    @Override
    public void mouseMoved(MouseEvent e) {
        x = e.getX();
        y = e.getY();
    }
}

