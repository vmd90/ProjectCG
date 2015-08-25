package objects;

import javax.media.opengl.GL;
import javax.media.opengl.GL2;
import static main.Renderer.*;
import util.Vector3f;

/**
 * AABB
 * @author Victor
 */
public class BoundingBox extends BoundingVolume {
    
    private float dx, dy, dz; // distancia do centro em x, y, z
    /**
     * Constroi uma AABB
     * @param c centro
     * @param dx distancia do centro a borda na direcao x
     * @param dy distancia do centro a borda na direcao y
     * @param dz distancia do centro a borda na direcao z
     * @param dr true para desenhar a AABB ou false caso contrario
     */
    public BoundingBox(Vector3f c, float dx, float dy, float dz) {
        center = c;
        this.dx = dx;
        this.dy = dy;
        this.dz = dz;
        type = 1;
    }
    
    public Vector3f getCenter() {
        return center;
    }
    
    public float getSizeX() {
        return dx;
    }

    public float getSizeY() {
        return dy;
    }
    
    public float getSizeZ() {
        return dz;
    }
    
    public void setSize(float x, float y, float z) {
        dx = x;
        dy = y;
        dz = z;
    }

    @Override
    public void drawBoundingVolume() {
        gl.glDisable(GL2.GL_FOG);
        gl.glEnable(GL2.GL_POLYGON_OFFSET_LINE);
        gl.glPolygonOffset(1, 0);
        gl.glLineWidth(2.0f);
        gl.glBegin(GL2.GL_QUADS);
            // frente
            gl.glVertex3f(center.x + dx, center.y + dy, center.z + dz);
            gl.glVertex3f(center.x + dx, center.y - dy, center.z + dz);
            gl.glVertex3f(center.x - dx, center.y - dy, center.z + dz);
            gl.glVertex3f(center.x - dx, center.y + dy, center.z + dz);
            
            // atras
            gl.glVertex3f(center.x + dx, center.y + dy, center.z - dz);
            gl.glVertex3f(center.x + dx, center.y - dy, center.z - dz);
            gl.glVertex3f(center.x - dx, center.y - dy, center.z + dz);
            gl.glVertex3f(center.x - dx, center.y + dy, center.z + dz);
            
            // esquerda
            gl.glVertex3f(center.x + dx, center.y + dy, center.z - dz);
            gl.glVertex3f(center.x + dx, center.y - dy, center.z - dz);
            gl.glVertex3f(center.x + dx, center.y - dy, center.z + dz);
            gl.glVertex3f(center.x + dx, center.y + dy, center.z + dz);
            
            // direita
            gl.glVertex3f(center.x - dx, center.y + dy, center.z + dz);
            gl.glVertex3f(center.x - dx, center.y - dy, center.z + dz);
            gl.glVertex3f(center.x - dx, center.y - dy, center.z + dz);
            gl.glVertex3f(center.x - dx, center.y + dy, center.z + dz);
        gl.glEnd();
        gl.glDisable(GL.GL_POLYGON_OFFSET_FILL);
        gl.glEnable(GL2.GL_FOG);
    }
}
