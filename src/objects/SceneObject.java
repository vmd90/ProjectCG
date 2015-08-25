/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package objects;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.media.opengl.GL;
import javax.media.opengl.GL2;
import javax.media.opengl.GLAutoDrawable;
import objectreader.JWavefrontModel;
import util.Transformation;
import util.Vector3f;

/**
 *
 * @author victor
 */
public class SceneObject {
    
    protected JWavefrontModel model;
    protected Transformation transformation;
    
    protected String name;
    protected boolean useTexture;
    protected BoundingVolume boundingVolume;
    
    protected Animation animation;
    
    private static HashMap<String, JWavefrontModel> allModels = new HashMap<String, JWavefrontModel>();
    
    // construtor especifico para objetos sem animacao e sem modelo
    public SceneObject(String name, Transformation transf) {
        this.name = name;
        transformation = transf;
        model = null;
        animation = null;
        boundingVolume = new BoundingSphere(new Vector3f(transformation.x, transformation.y, transformation.z),
                Math.max(Math.max(transformation.sx, transformation.sy), transformation.sz));
    }
    
    public SceneObject(String foldername, boolean useTex, int aType, boolean animating, int fps, boolean loop,
            int num, Transformation transfs[]) {
        name = foldername;
        File[] files = new File("data/" + foldername).listFiles();
        ArrayList<JWavefrontModel> list = new ArrayList<JWavefrontModel>();
        for(File file : files) { // carregar todos os modelos
            if(file.getName().toLowerCase().endsWith(".obj") && !file.isDirectory()) { // so carregar .obj
                if(allModels.containsKey(file.getName())) {
                    model = allModels.get(file.getName());
                }
                else {
                    try {
                        model = new JWavefrontModel(file);
                        model.unitize();
                        model.facetNormals();
                        model.vertexNormals(90);
                    } catch (IOException ex) {
                        Logger.getLogger(SceneObject.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    allModels.put(file.getName(), model);
                }
                list.add(model);
            }
        }
        
        useTexture = useTex;
        transformation = transfs[0]; // transformacao inicial
        boundingVolume = new BoundingSphere(new Vector3f(transformation.x, transformation.y, transformation.z),
                Math.max(Math.max(transformation.sx, transformation.sy), transformation.sz));
        
        JWavefrontModel[] m = new JWavefrontModel[list.size()];
        for(int i = 0; i < list.size(); ++i) {
            m[i] = list.get(i);
        }
        animation = new Animation(aType, animating, fps, loop, num, m, transfs);
    }
    
    public JWavefrontModel getCurrentModel(){
        return model;
    }
    
    public JWavefrontModel[] getAnimationModels() {
        if(animation != null)
            return animation.getModels();
        return null;
    }
    
    public String getName() {
        return name;
    }
    
    public boolean useTexture() {
        return useTexture;
    }
    
    public void setBoundingSphere(Vector3f v, float r) {
        boundingVolume.setCenter(v);
        ((BoundingSphere)boundingVolume).setSize(r);
    }
    
    public BoundingSphere getBoundingSphere() {
        return (BoundingSphere) boundingVolume;
    }
    
    public void update() {
        if(animation != null) {
            if(animation.getType() == 1) { // sequencia de transformacoes, substituindo a transformacao corrente
                transformation = (Transformation) animation.getNext();
                setBoundingSphere(new Vector3f(transformation.x, transformation.y, transformation.z),
                        Math.max(Math.max(transformation.sx, transformation.sy), transformation.sz));
            }
            else { // sequencia de objetos, substituindo o objeto corrente
                model = (JWavefrontModel) animation.getNext();
            }
        }
    }
    
    public void setAnimating(boolean a) {
        animation.setAnimating(a);
    }
    
    public void draw(GLAutoDrawable glad) {
        GL2 gl = glad.getGL().getGL2();

        if(model != null) {
            gl.glPushMatrix();
                gl.glTranslatef(transformation.x, transformation.y, transformation.z);

                if(BoundingVolume.isVisible) { // desenha o bounding volume se for ativado
                    boundingVolume.drawBoundingVolume();
                }

                gl.glScalef(transformation.sx, transformation.sy, transformation.sz);
                gl.glRotatef(transformation.angle, transformation.rx, transformation.ry, transformation.rz);
                model.draw(glad);
            gl.glPopMatrix();
        }
    }
}
