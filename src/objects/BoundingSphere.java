
package objects;

import com.jogamp.opengl.util.gl2.GLUT;
import util.Vector3f;

/**
 *
 * @author Victor
 */
public class BoundingSphere extends BoundingVolume {
    
    private float radius;
    private static GLUT glut = new GLUT();
    
    public BoundingSphere(Vector3f c, float r) {
        center = c;
        radius = r;
        type = 0;
    }
    
    public Vector3f getCenter() {
        return center;
    }
    
    public float getSize() {
        return radius;
    }
    
    public void setSize(float r) {
        radius = r;
    }

    @Override
    public void drawBoundingVolume() {
        glut.glutWireSphere(radius, 10, 10);
    }

}
