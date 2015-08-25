/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package util;

/**
 *
 * @author victor
 */
public class Transformation {
    
    public float x, y, z, sx, sy, sz, angle, rx, ry, rz;
    
    public Transformation() {
        
    }
    
    public Transformation(float x, float y, float z, float sx, float sy, float sz, float angle,
            float rx, float ry, float rz) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.sx = sx;
        this.sy = sy;
        this.sz = sz;
        this.angle = angle;
        this.rx = rx;
        this.ry = ry;
        this.rz = rz;
    }
}
