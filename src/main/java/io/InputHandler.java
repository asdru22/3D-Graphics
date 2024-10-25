package io;


import math.Double2D;

public class InputHandler {

    public KeyHandler keyHandler = new KeyHandler();
    public MouseListenerHandler mouseListenerHandler = new MouseListenerHandler();
    public MousePositionHandler mousePosHandler = new MousePositionHandler();

    public InputHandler() {
    }

    public Double2D getMousePos() {
        return mousePosHandler.mousePos;
    }
    public boolean isLeftPressed(){return mouseListenerHandler.leftPressed;}
}
