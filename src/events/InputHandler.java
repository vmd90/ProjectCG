/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package events;

import java.awt.AWTException;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;
import objects.FPCamera;

/**
 *
 * @author victor
 */
public class InputHandler implements KeyListener, MouseMotionListener {

    private boolean keys[];
    private int mouseCenterX;
    private int mouseCenterY;
    private Robot robot;
    private static InputHandler handler;

    private InputHandler() {
        keys = new boolean[256];
        mouseCenterX = Toolkit.getDefaultToolkit().getScreenSize().width/2;
        mouseCenterY = Toolkit.getDefaultToolkit().getScreenSize().height/2;
        try {
            robot = new Robot();
        }
        catch (AWTException ex) {
            Logger.getLogger(InputHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public static InputHandler getInstance() {
        if(handler == null) {
            handler = new InputHandler();
        }
        return handler;
    }
    
    public boolean isKeyPressed(int keycode) {
        return keys[keycode];
    }

    public void keyTyped(KeyEvent e) {
    }

    public void keyPressed(KeyEvent e) {
        int keycode = e.getKeyCode();
        if (keycode >= 0 && keycode < 256) {
            keys[keycode] = true;
        }
    }

    public void keyReleased(KeyEvent e) {
        int keycode = e.getKeyCode();
        if (keycode >= 0 && keycode < 256) {
            keys[keycode] = false;
        }
    }

    public void mouseDragged(MouseEvent e) {
        
    }

    public void mouseMoved(MouseEvent me) {
        int x = me.getXOnScreen();
        int y = me.getYOnScreen();
        float dx = x - mouseCenterX;
        float dy = y - mouseCenterY;
        robot.mouseMove(mouseCenterX, mouseCenterY);
        
        FPCamera.yrot += FPCamera.CAMERA_STEP * dx;
        FPCamera.xrot += FPCamera.CAMERA_STEP * dy;
        
        if (FPCamera.yrot >= 360)
            FPCamera.yrot -= 360;
        if (FPCamera.yrot < 0)
            FPCamera.yrot += 360;
        if (FPCamera.xrot > 95)
            FPCamera.xrot = 95;
        if (FPCamera.xrot < -95)
            FPCamera.xrot = -95;
        
        FPCamera.look.y = (float) -Math.sin(FPCamera.xrot * Math.PI/180.0f);
        FPCamera.look.x = (float) ((1 - Math.abs(FPCamera.look.y)) * Math.cos(FPCamera.yrot * Math.PI/180.0f));
        FPCamera.look.z = (float) ((1 - Math.abs(FPCamera.look.y)) * Math.sin(FPCamera.yrot * Math.PI/180.0f));
        FPCamera.changed = true;
    }
}
