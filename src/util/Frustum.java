/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package util;

import javax.media.opengl.GL;
import javax.media.opengl.GL2;
import javax.media.opengl.GLAutoDrawable;
import objects.BoundingBox;
import objects.BoundingSphere;

/**
 *
 * @author Victor
 */
public class Frustum {
    
    private float[][] planes; // planos do frustum da camera
    
    public Frustum() {
        planes = new float[6][4];
    }
    
    /**
     * Gera os planos do frustum
     */
    public void generate(GLAutoDrawable glad) {
        GL2 gl = glad.getGL().getGL2();
        float proj[] = new float[16];
        float modl[] = new float[16];
        float clip[] = new float[16];
        float t;

        gl.glGetFloatv( GL2.GL_PROJECTION_MATRIX, proj, 0 );
        gl.glGetFloatv( GL2.GL_MODELVIEW_MATRIX, modl, 0 );

        // multiplicacao de modelview com projection
        clip[ 0] = modl[ 0] * proj[ 0] + modl[ 1] * proj[ 4] + modl[ 2] * proj[ 8] + modl[ 3] * proj[12];
        clip[ 1] = modl[ 0] * proj[ 1] + modl[ 1] * proj[ 5] + modl[ 2] * proj[ 9] + modl[ 3] * proj[13];
        clip[ 2] = modl[ 0] * proj[ 2] + modl[ 1] * proj[ 6] + modl[ 2] * proj[10] + modl[ 3] * proj[14];
        clip[ 3] = modl[ 0] * proj[ 3] + modl[ 1] * proj[ 7] + modl[ 2] * proj[11] + modl[ 3] * proj[15];

        clip[ 4] = modl[ 4] * proj[ 0] + modl[ 5] * proj[ 4] + modl[ 6] * proj[ 8] + modl[ 7] * proj[12];
        clip[ 5] = modl[ 4] * proj[ 1] + modl[ 5] * proj[ 5] + modl[ 6] * proj[ 9] + modl[ 7] * proj[13];
        clip[ 6] = modl[ 4] * proj[ 2] + modl[ 5] * proj[ 6] + modl[ 6] * proj[10] + modl[ 7] * proj[14];
        clip[ 7] = modl[ 4] * proj[ 3] + modl[ 5] * proj[ 7] + modl[ 6] * proj[11] + modl[ 7] * proj[15];

        clip[ 8] = modl[ 8] * proj[ 0] + modl[ 9] * proj[ 4] + modl[10] * proj[ 8] + modl[11] * proj[12];
        clip[ 9] = modl[ 8] * proj[ 1] + modl[ 9] * proj[ 5] + modl[10] * proj[ 9] + modl[11] * proj[13];
        clip[10] = modl[ 8] * proj[ 2] + modl[ 9] * proj[ 6] + modl[10] * proj[10] + modl[11] * proj[14];
        clip[11] = modl[ 8] * proj[ 3] + modl[ 9] * proj[ 7] + modl[10] * proj[11] + modl[11] * proj[15];

        clip[12] = modl[12] * proj[ 0] + modl[13] * proj[ 4] + modl[14] * proj[ 8] + modl[15] * proj[12];
        clip[13] = modl[12] * proj[ 1] + modl[13] * proj[ 5] + modl[14] * proj[ 9] + modl[15] * proj[13];
        clip[14] = modl[12] * proj[ 2] + modl[13] * proj[ 6] + modl[14] * proj[10] + modl[15] * proj[14];
        clip[15] = modl[12] * proj[ 3] + modl[13] * proj[ 7] + modl[14] * proj[11] + modl[15] * proj[15];

        // extraindo o plano da direita
        planes[0][0] = clip[ 3] - clip[ 0];
        planes[0][1] = clip[ 7] - clip[ 4];
        planes[0][2] = clip[11] - clip[ 8];
        planes[0][3] = clip[15] - clip[12];
        
        t = (float) Math.sqrt( planes[0][0] * planes[0][0] + planes[0][1] * planes[0][1] + planes[0][2] * planes[0][2] );
        planes[0][0] /= t;
        planes[0][1] /= t;
        planes[0][2] /= t;
        planes[0][3] /= t;

        // plano da esquerda
        planes[1][0] = clip[ 3] + clip[ 0];
        planes[1][1] = clip[ 7] + clip[ 4];
        planes[1][2] = clip[11] + clip[ 8];
        planes[1][3] = clip[15] + clip[12];

        t = (float) Math.sqrt( planes[1][0] * planes[1][0] + planes[1][1] * planes[1][1] + planes[1][2] * planes[1][2] );
        planes[1][0] /= t;
        planes[1][1] /= t;
        planes[1][2] /= t;
        planes[1][3] /= t;

        // plano de baixo
        planes[2][0] = clip[ 3] + clip[ 1];
        planes[2][1] = clip[ 7] + clip[ 5];
        planes[2][2] = clip[11] + clip[ 9];
        planes[2][3] = clip[15] + clip[13];

        t = (float) Math.sqrt( planes[2][0] * planes[2][0] + planes[2][1] * planes[2][1] + planes[2][2] * planes[2][2] );
        planes[2][0] /= t;
        planes[2][1] /= t;
        planes[2][2] /= t;
        planes[2][3] /= t;

        // plano de cima
        planes[3][0] = clip[ 3] - clip[ 1];
        planes[3][1] = clip[ 7] - clip[ 5];
        planes[3][2] = clip[11] - clip[ 9];
        planes[3][3] = clip[15] - clip[13];

        t = (float) Math.sqrt( planes[3][0] * planes[3][0] + planes[3][1] * planes[3][1] + planes[3][2] * planes[3][2] );
        planes[3][0] /= t;
        planes[3][1] /= t;
        planes[3][2] /= t;
        planes[3][3] /= t;

        // plano mais longe
        planes[4][0] = clip[ 3] - clip[ 2];
        planes[4][1] = clip[ 7] - clip[ 6];
        planes[4][2] = clip[11] - clip[10];
        planes[4][3] = clip[15] - clip[14];

        t = (float) Math.sqrt( planes[4][0] * planes[4][0] + planes[4][1] * planes[4][1] + planes[4][2] * planes[4][2] );
        planes[4][0] /= t;
        planes[4][1] /= t;
        planes[4][2] /= t;
        planes[4][3] /= t;

        // plano mais perto
        planes[5][0] = clip[ 3] + clip[ 2];
        planes[5][1] = clip[ 7] + clip[ 6];
        planes[5][2] = clip[11] + clip[10];
        planes[5][3] = clip[15] + clip[14];

        t = (float) Math.sqrt( planes[5][0] * planes[5][0] + planes[5][1] * planes[5][1] + planes[5][2] * planes[5][2] );
        planes[5][0] /= t;
        planes[5][1] /= t;
        planes[5][2] /= t;
        planes[5][3] /= t;
    }
    
    /**
     * Verifica se a esfera esta no frustum e retorna tambem o quao longe da camera esta
     * @param sphere
     * @return 0 se nao esta no frustum, ou a distancia em relacao a camera
     */
    public boolean sphereInFrustum(BoundingSphere sphere) {
        float radius = sphere.getSize();
        Vector3f s = new Vector3f(sphere.getCenter().x, sphere.getCenter().y, sphere.getCenter().z);
        for(int i = 0; i < 6; ++i) {
            if((planes[i][0] * s.x + planes[i][1] * s.y + planes[i][2] * s.z + planes[i][3]) <= -radius)
                return false;  // nao esta dentro do frustum
        }
        return true;
    }
    /**
     * Verifica se (bounding box) esta dentro do frustum
     * @param box 
     * @return true se esta dentro e false caso contrario
     */
    public boolean boxInFrustum(BoundingBox box) {
        float sizeX = box.getSizeX();
        float sizeY = box.getSizeY();
        float sizeZ = box.getSizeZ();
        float x = box.getCenter().x;
        float y = box.getCenter().y;
        float z = box.getCenter().z;
        for( int p = 0; p < 6; p++ ) {
           if( planes[p][0] * (x - sizeX) + planes[p][1] * (y - sizeY) + planes[p][2] * (z - sizeZ) + planes[p][3] > 0 )
              continue;
           if( planes[p][0] * (x + sizeX) + planes[p][1] * (y - sizeY) + planes[p][2] * (z - sizeZ) + planes[p][3] > 0 )
              continue;
           if( planes[p][0] * (x - sizeX) + planes[p][1] * (y + sizeY) + planes[p][2] * (z - sizeZ) + planes[p][3] > 0 )
              continue;
           if( planes[p][0] * (x + sizeX) + planes[p][1] * (y + sizeY) + planes[p][2] * (z - sizeZ) + planes[p][3] > 0 )
              continue;
           if( planes[p][0] * (x - sizeX) + planes[p][1] * (y - sizeY) + planes[p][2] * (z + sizeZ) + planes[p][3] > 0 )
              continue;
           if( planes[p][0] * (x + sizeX) + planes[p][1] * (y - sizeY) + planes[p][2] * (z + sizeZ) + planes[p][3] > 0 )
              continue;
           if( planes[p][0] * (x - sizeX) + planes[p][1] * (y + sizeY) + planes[p][2] * (z + sizeZ) + planes[p][3] > 0 )
              continue;
           if( planes[p][0] * (x + sizeX) + planes[p][1] * (y + sizeY) + planes[p][2] * (z + sizeZ) + planes[p][3] > 0 )
              continue;
           return false;
        }
        return true;
    }
}
