/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package util;

import objects.BoundingSphere;

/**
 * Trata da colisao de objetos
 * @author victor
 */
public class Collision {
    /**
     * Testa se duas esferas se intersectam
     * @param a
     * @param b
     * @return 
     */
    public static boolean collide(BoundingSphere a, BoundingSphere b) {
        Vector3f d = Vector3f.sub(a.getCenter(), b.getCenter());
        float dist2 = Vector3f.dot(d, d);
        float radiusSum = a.getSize() + b.getSize();
        return (dist2 <= radiusSum * radiusSum);
    }
    
    
}
