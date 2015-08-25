/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package objects;

import events.InputHandler;
import java.awt.event.KeyEvent;
import util.Transformation;
import util.Vector3f;

/**
 * camera em primeira pessoa
 * @author Victor
 */
public class FPCamera {
    
    public static final float FOV = 50.0f; // field of view
    public static final float NEAR = 0.1f;
    public static final float FAR = 80.0f;
    
    // vetores de posicao, look, up
    public static Vector3f position, look, up;
    public static float xrot = 0, yrot = 0;
    
    public static boolean changed = true;
    
    public static float CAMERA_STEP = 0.1f;
    private SceneObject sceneObject;
    
    public FPCamera(float x, float y, float z, float lx, float ly, float lz,
            float ux, float uy, float uz) {
        position = new Vector3f(x, y, z);
        look = new Vector3f(lx, ly, lz);
        up = new Vector3f(ux, uy, uz);
        
        sceneObject = new SceneObject("camera", new Transformation(x, y, z, 1, 1, 1, 0, 0, 0, 0));
    }
    
    public static void moveForward(float step){
        
        position.x += look.x * step;
        position.y += look.y * step;
        position.z += look.z * step;
        changed = true;
    }
    
    public static void moveBack(float step){
        
        position.x -= look.x * step;
        position.y -= look.y * step;
        position.z -= look.z * step;
        changed = true;
    }
    
    public static void moveRight(float step){
        
        Vector3f v = Vector3f.cross(up, look);
        position.x -= v.x * step;
        position.y -= v.y * step;
        position.z -= v.z * step;
        changed = true;
    }
    
    public static void moveLeft(float step){
        
        Vector3f v = Vector3f.cross(up, look);
        position.x += v.x * step;
        position.y += v.y * step;
        position.z += v.z * step;
        changed = true;
    }
    
    public static void moveUp(float step){
        
        position.x += up.x * step;
        position.y += up.y * step;
        position.z += up.z * step;
        changed = true;
    }
    
    public static void moveDown(float step){
        
        position.x -= up.x * step;
        position.y -= up.y * step;
        position.z -= up.z * step;
        changed = true;
    }
    
    public static void rotateY(float step) {
        yrot += step;
        if(yrot > 360.0f || yrot < -360.0f)
            yrot = 0;
        look.x = (float) Math.sin(yrot * Math.PI/180.0f);
        look.z = (float) Math.cos(yrot * Math.PI/180.0f);
        changed = true;
    }
    
    public static void rotateX(float step) {
        xrot += step;
        if(xrot > 89.0f)
            xrot = 89.0f;
        if(xrot < -89.0f)
            xrot = -89.0f;
        look.y = (float) Math.sin(xrot * Math.PI/180.0f);
        look.z = (float) Math.cos(xrot * Math.PI/180.0f);
        changed = true;
    }
    
    public SceneObject getSceneObject() {
        return sceneObject;
    }
    
    public void update() {
        
        // atualizando camera
        if(InputHandler.getInstance().isKeyPressed(KeyEvent.VK_W)) { // tecla W
            moveForward(CAMERA_STEP);
        }
        if(InputHandler.getInstance().isKeyPressed(KeyEvent.VK_S)) { // tecla S
            moveBack(CAMERA_STEP);
        }
        if(InputHandler.getInstance().isKeyPressed(KeyEvent.VK_A)) { // tecla A
           moveLeft(CAMERA_STEP);
        }
        if(InputHandler.getInstance().isKeyPressed(KeyEvent.VK_D)) { // tecla D
            moveRight(CAMERA_STEP);
        }
        if(InputHandler.getInstance().isKeyPressed(KeyEvent.VK_UP)) { // tecla UP
            moveUp(CAMERA_STEP);
        }
        if(InputHandler.getInstance().isKeyPressed(KeyEvent.VK_DOWN)) { // tecla DOWN
            moveDown(CAMERA_STEP);
        }
        // impede que a camera saia do terreno
        if(position.y < 0.35f) {
            position.y = 0.35f;
        }
        
        sceneObject.setBoundingSphere(position, 0.4f);
    }
}
