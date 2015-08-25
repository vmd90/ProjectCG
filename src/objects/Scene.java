/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package objects;

import java.util.ArrayList;
import java.util.Iterator;
import javax.media.opengl.GLAutoDrawable;

/**
 *
 * @author victor
 */
public class Scene {
    
    // todos os objetos carregados aqui
    private ArrayList<SceneObject> sceneObjects;
    // todos os objetos que serao renderizados
    private ArrayList<SceneObject> visibleObjects;
    
    private static Scene instance = null;
    
    private Scene(){
        sceneObjects = new ArrayList<SceneObject>();
        visibleObjects = new ArrayList<SceneObject>();
    }
    
    public static Scene getInstance() {
        if(instance == null)
            instance = new Scene();
        return instance;
    }
    
    public void addSceneObject(SceneObject so){
        sceneObjects.add(so);
    }
    
    public void addSceneObjectList(ArrayList<SceneObject> list) {
        sceneObjects = list;
    }
    
    public void addVisibleObject(SceneObject so) {
        visibleObjects.add(so);
    }
    
    public void addVisibleObjectList(ArrayList<SceneObject> list) {
        visibleObjects = list;
    }
    
    public void clearVisibleObjectsList() {
        visibleObjects.clear();
    }
    
    public Iterator<SceneObject> getSceneObjectsIterator(){
        return sceneObjects.iterator();
    }
    
    public Iterator<SceneObject> getVisibleObjectsIterator() {
        return visibleObjects.iterator();
    }
    
    public void drawAll(GLAutoDrawable glad){
        for(SceneObject sceneObject : visibleObjects){
            sceneObject.draw(glad);
        }
    }
    
    public int getSize(){
        return visibleObjects.size();
    }
}
