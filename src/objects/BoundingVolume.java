/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package objects;

import util.Vector3f;

/**
 *
 * @author victor
 */
public abstract class BoundingVolume {
    
    protected Vector3f center;
    protected int type;  // tipo de volume: 0 - BSphere, 1 - BBox
    public static boolean isVisible = false;
    
    public abstract Vector3f getCenter();
    
    public void setCenter(Vector3f p) {
        center = p;
    }
    
    public abstract void drawBoundingVolume();
    
    public int getType() {
        return type;
    }
}
