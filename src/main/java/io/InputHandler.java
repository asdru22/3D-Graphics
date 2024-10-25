package io;


public class InputHandler {

    public KeyHandler keyHandler = new KeyHandler();
    public MouseListenerHandler mouseListenerHandler = new MouseListenerHandler();
    public MousePositionHandler mousePosHandler = new MousePositionHandler();

    public InputHandler() {
    }

    public boolean isLeftPressed(){return mouseListenerHandler.leftPressed;}
}
