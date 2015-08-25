package main;

import com.jogamp.opengl.util.Animator;
import events.InputHandler;
import java.awt.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.media.opengl.GLCapabilities;
import javax.media.opengl.awt.GLCanvas;
import javax.swing.JFrame;


public class MainFrame extends JFrame {
    
    private static MainFrame instance = null;
    private GLCanvas canvas;
    private Animator animator;
    
    private MainFrame(String title){
        super(title);
    }
    
    public static MainFrame getInstance(){
        if(instance == null){
            instance = new MainFrame("ProjectCG");
        }
        return instance;
    }
    
    public void start(){
        GLCapabilities caps = new GLCapabilities(null);
        caps.setHardwareAccelerated(true);
        caps.setDoubleBuffered(true);
        caps.setAccumBlueBits(16);
        caps.setAccumGreenBits(16);
        caps.setAccumRedBits(16);
        caps.setStencilBits(16);
        
        canvas = new GLCanvas(caps);
        canvas.addGLEventListener(new Renderer());
        canvas.addKeyListener(InputHandler.getInstance());
        
        try {
            Point p = GraphicsEnvironment.getLocalGraphicsEnvironment().getCenterPoint();
            Robot r = new Robot(GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice());
            r.mouseMove(p.x, p.y);
        } catch (AWTException ex) {
            Logger.getLogger(MainFrame.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        canvas.addMouseMotionListener(InputHandler.getInstance());
        addKeyListener(InputHandler.getInstance());
        addMouseMotionListener(InputHandler.getInstance());
        
        getContentPane().add(canvas);
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setVisible(true);
        
        animator = new Animator(canvas);
        animator.start();
    }
}

