package io;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class KeyHandler implements KeyListener {

    public boolean upPressed, downPressed, leftPressed, rightPressed;
    public boolean shiftPressed,spacePressed;

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        updatePressedKeys(e, true);
    }

    @Override
    public void keyReleased(KeyEvent e) {
        updatePressedKeys(e, false);
    }

    private void updatePressedKeys(KeyEvent e, boolean pressed) {
        int code = e.getKeyCode();
        if(code == KeyEvent.VK_W) upPressed = pressed;
        if(code == KeyEvent.VK_S) downPressed = pressed;
        if(code == KeyEvent.VK_A) leftPressed = pressed;
        if(code == KeyEvent.VK_D) rightPressed = pressed;
        if(code == KeyEvent.VK_SPACE) spacePressed = pressed;
        if(code == KeyEvent.VK_SHIFT) shiftPressed = pressed;
    }
}
