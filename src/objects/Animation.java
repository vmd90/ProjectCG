/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package objects;

import objectreader.JWavefrontModel;
import util.Transformation;

/**
 *
 * @author victor
 */
public class Animation {
    
    protected int fps;
    protected long prevTime = 0;
    protected int animType; // 1 - para sequencia de inteiros, 2 - para sequencia de objetos
    protected int numObjects;
    protected boolean loop;
    protected boolean animating;
    protected int it = 0; // iterador
    protected JWavefrontModel[] animModels; // modelo(s)
    protected Transformation[] animTransformations; // transformacao(oes)
    
    public Animation(int aType, boolean animating, int fps, boolean loop, int num,
            JWavefrontModel[] models, Transformation[] transformations) {
        this.fps = (fps > 0 ? fps : 1);
        animType = aType;
        this.loop = loop;
        this.animating = animating;
        numObjects = num;
        animModels = models;
        animTransformations = transformations;
    }
    
    public int getType() {
        return animType;
    }
    
    public JWavefrontModel[] getModels() {
        return animModels;
    }
    
    public Object getNext() {
        if(animating) {
            long now = System.currentTimeMillis();
            if(now - prevTime > 1000/fps){
                prevTime = now;
                ++it;
                if(it >= numObjects) {
                    if(loop)
                        it = 0;
                    else
                        it = numObjects - 1;
                }
            }
        }
        if(animType == 1)
            return animTransformations[it];
        return animModels[it];
    }
    
    public void setAnimating(boolean a) {
        animating = a;
    }
    
    public void setLoop(boolean s) {
        loop = s;
    }
}
