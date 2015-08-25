/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package main;

import events.InputHandler;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.media.opengl.GL;
import javax.media.opengl.GL2;
import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.GLEventListener;
import javax.media.opengl.glu.GLU;
import objectreader.JWavefrontModel;
import objectreader.SceneFileReader;
import objects.BoundingVolume;
import objects.FPCamera;
import objects.ParticleSystem;
import objects.Scene;
import objects.SceneObject;
import util.Collision;
import util.Frustum;
import util.Transformation;

/**
 *
 * @author victor
 */
public class Renderer implements GLEventListener {
    public static GL2 gl;
    public static GLU glu = new GLU();
    public static FPCamera camera;
    private Scene scene;
    private MainFrame mainFrame;
    private Frustum frustum = new Frustum();
    private boolean fogEnabled = true;
    
    private ParticleSystem particles;
    
    // calculo de fps
    private long frame = 0, timebase = 0, elapsed = 0;
    
    public Renderer() {
        camera = new FPCamera(0, 0.35f, 0,
                            0, 0, 0,
                            0, 1, 0);
        scene = Scene.getInstance();
        mainFrame = MainFrame.getInstance();
    }

    @Override
    public void init(GLAutoDrawable drawable) {
        try {
            gl = drawable.getGL().getGL2();
            
            gl.glClearColor(0.7f, 0.7f, 0.9f, 1.0f);
            //gl.setSwapInterval(1);
            
            gl.glEnable(GL.GL_DEPTH_TEST);
            gl.glEnable(GL2.GL_LIGHTING);
            
            gl.glEnable(GL.GL_CULL_FACE);
            gl.glCullFace(GL.GL_BACK);
            
            /////////////////////////////////
            // carregando e compilando objetos
            /////////////////////////////////
            createSceneObjects();
            
            Iterator<SceneObject> it = scene.getSceneObjectsIterator();
            while(it.hasNext()){
                SceneObject sceneObject = it.next();
                
                JWavefrontModel[] models = sceneObject.getAnimationModels();
                if(models == null)
                    continue;
                for(JWavefrontModel model : models) {
                    if(sceneObject.useTexture())
                        model.compile(drawable, JWavefrontModel.WF_MATERIAL | JWavefrontModel.WF_TEXTURE | JWavefrontModel.WF_SMOOTH);
                    else
                        model.compile(drawable, JWavefrontModel.WF_COLOR | JWavefrontModel.WF_SMOOTH);
                }
            }
            
            // light
            setSunLight();
            // fog
            setFog();
            
            gl.glMatrixMode(GL2.GL_PROJECTION);
            gl.glLoadIdentity();
            float h = (float)mainFrame.getWidth()/ (float)mainFrame.getHeight();
            glu.gluPerspective(FPCamera.FOV, h, FPCamera.NEAR, FPCamera.FAR);
            
        }
        catch (IOException ex) {
            Logger.getLogger(Renderer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private void createSceneObjects() throws IOException{
        
        SceneFileReader sceneFile = new SceneFileReader("data/test.scene");
        // particulas
        particles = new ParticleSystem("particle", true, 1, false, 0, false, 1, 
                    new Transformation[]{new Transformation(-0.43f, 0.233f, 6.09f, 0.008f, 0.01f, 0.008f, 0, 0, 0, 0)});
        
        scene.addSceneObjectList(sceneFile.getObjects());
        Scene.getInstance().addSceneObject(particles);
    }
    
    private void setFog(){
        
        gl.glEnable(GL2.GL_FOG);
        gl.glFogf(GL2.GL_FOG_START, 5.0f);
        gl.glFogf(GL2.GL_FOG_END, 50.0f);
        gl.glFogfv(GL2.GL_FOG_COLOR, new float[]{0.8f, 0.8f, 0.9f, 1.0f}, 0);
        gl.glFogi(GL2.GL_FOG_MODE, GL.GL_LINEAR);
    }
    
    private void setSunLight(){
        float ambient[] = {0.2f,0.2f,0.2f,1.0f};
        float diffuse[] = {0.7f,0.7f,0.7f, 1.0f};
        float specular[] = {1,1,1, 1.0f};
        float position[] = {0, 16, 10, 1};
        
        gl.glEnable(GL2.GL_LIGHT0);
        gl.glEnable(GL2.GL_COLOR_MATERIAL);
        gl.glColorMaterial(GL.GL_FRONT, GL2.GL_AMBIENT_AND_DIFFUSE);
        
        gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_AMBIENT, ambient, 0);
        gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_DIFFUSE, diffuse, 0);
        gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_SPECULAR, specular, 0);
        gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_POSITION, position, 0);
        
    }

    @Override
    public void display(GLAutoDrawable drawable) {
        
        gl = drawable.getGL().getGL2();
        gl.glClear(GL.GL_COLOR_BUFFER_BIT | GL.GL_DEPTH_BUFFER_BIT);
        gl.glMatrixMode(GL2.GL_MODELVIEW);
        gl.glLoadIdentity();
        
        // desenha ou nao a bounding sphere de cada objeto
        if(InputHandler.getInstance().isKeyPressed(KeyEvent.VK_B)) { // tecla B
            BoundingVolume.isVisible = !BoundingVolume.isVisible;
        }
        // habilita ou nao fog
        if(InputHandler.getInstance().isKeyPressed(KeyEvent.VK_F)) { // tecla F
            fogEnabled = !fogEnabled;
            if(fogEnabled)
                gl.glEnable(GL2.GL_FOG);
            else
                gl.glDisable(GL2.GL_FOG);
        }

        // atualiza camera
        camera.update();
        // atualiza objetos
        {
            Iterator<SceneObject> it = Scene.getInstance().getSceneObjectsIterator();
            while(it.hasNext()) {
                SceneObject so = it.next();
                // animacao da porta
                if(so.getName().contains("door")) {
                    if(Collision.collide(camera.getSceneObject().getBoundingSphere(), so.getBoundingSphere()))
                        so.setAnimating(true);
                    else
                        so.setAnimating(false);
                }
                
                so.update();
            }
        }
        
        glu.gluLookAt(FPCamera.position.x, FPCamera.position.y, FPCamera.position.z,
            FPCamera.position.x + FPCamera.look.x, FPCamera.position.y + FPCamera.look.y, FPCamera.position.z + FPCamera.look.z,
            FPCamera.up.x, FPCamera.up.y, FPCamera.up.z);
        
        if(FPCamera.changed){  // se direcao da camera mudar
            FPCamera.changed = false;
            
            frustum.generate(drawable); // gera o frustum
            
            Scene.getInstance().clearVisibleObjectsList();
            
            Iterator<SceneObject> it = Scene.getInstance().getSceneObjectsIterator();
            while(it.hasNext()) {
                SceneObject so = it.next();
                if(frustum.sphereInFrustum(so.getBoundingSphere()))
                    Scene.getInstance().addVisibleObject(so);
            }
            
//            System.out.println("Renderable objects: "+ Scene.getInstance().getSize());
//            System.out.println(FPCamera.position);
        }
      
        // desenha objetos
        {
            Iterator<SceneObject> it = Scene.getInstance().getVisibleObjectsIterator();
            SceneObject so;
            while(it.hasNext()) {
                so = it.next();
                
                if(so.getName().contains("lake")) {
                    gl.glPushAttrib(GL2.GL_ENABLE_BIT);
                        gl.glEnable(GL.GL_BLEND);
                        gl.glBlendFunc(GL.GL_ONE, GL.GL_ONE);
                        so.draw(drawable);
                    gl.glPopAttrib();
                    continue;
                }
                so.draw(drawable);
            }
        }
        
//        int e = gl.glGetError();
//        if(e != GL.GL_NO_ERROR) {
//            System.err.println("Erro: "+ gl.glGetString(e));
//        }
        // calculo do fps
        ++frame;
        long time = System.currentTimeMillis();
        elapsed = time - timebase;
        if(elapsed > 1000){
            long fps = frame*1000/(time-timebase);
            timebase = time;
            frame = 0;
            
            String str = "FPS: " + fps;
            mainFrame.setTitle(str);
        }

        //gl.glFlush();
    }

    public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height) {
        gl = drawable.getGL().getGL2();
        if (height < 1) {
            height = 1;
        }
        final float h = (float) width / (float)height;
        gl.glViewport(0, 0, width, height);
        
        gl.glMatrixMode(GL2.GL_PROJECTION);
        gl.glLoadIdentity();
        glu.gluPerspective(FPCamera.FOV, h, FPCamera.NEAR, FPCamera.FAR);
        
        gl.glMatrixMode(GL2.GL_MODELVIEW);
        gl.glLoadIdentity();
    }

    public void displayChanged(GLAutoDrawable drawable, boolean modeChanged, boolean deviceChanged) {
    }

    public void dispose(GLAutoDrawable drawable) {
        //To change body of generated methods, choose Tools | Templates.
    }
}
