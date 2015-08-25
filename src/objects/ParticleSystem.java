/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package objects;

import javax.media.opengl.GL;
import javax.media.opengl.GL2;
import javax.media.opengl.GLAutoDrawable;
import util.Transformation;

/**
 *
 * @author Victor
 */
public class ParticleSystem extends SceneObject {
    
    // Number of particles to display.
    private static final int MAX_PARTICLES = 1000;
    private static final float DELAY = 2F;
    private static final float XGRAVITY_CHANGE = 0;
    private static final float YGRAVITY_CHANGE = 0;
    private static boolean resetParticles = false;
    // The array of particles.
    private Particle[] m_aParticles = new Particle[MAX_PARTICLES];
    
    public ParticleSystem(String name, boolean useTexture, int aType, boolean animating, int fps, boolean loop, int num,
           Transformation[] transfs) {
        super(name, useTexture, aType, animating, fps, loop, num, transfs);
        // Create new particles for the array and ask each particle 
        // to initialize itself.
        for (int i = 0; i < MAX_PARTICLES; i++) {
           m_aParticles[i] = new Particle();
        }
    }
    
    @Override
    public void update() {
        // Test if we should reset the particles.
      if (resetParticles) {
         // We should reset the particles.
         for (int i = 0; i < MAX_PARTICLES; i++) {
            ((Particle)m_aParticles[i]).reset(false);
         }
         resetParticles = false;
      }
      super.update();
    }
    
    @Override
    public void draw(GLAutoDrawable drawable) {
        Particle particle;
        
        // Loop through the Particles array and draw each particle.
        for (int i = 0; i < MAX_PARTICLES; i++) {
           // Each particle is handled differently depending on whether it's
           // alive or not.
           particle = m_aParticles[i];
           if (particle.isAlive()) {
              // This particular particle is alive. 
              handleLiveParticle(drawable, particle);
           } else {
              // This particular particle is dead.
              handleDeadParticle(particle);
           }
        }
    }
    

   /**
    * Draw the live particle using triangle strips.
    * 
    * @param drawable
    * @param particle The Particle object to display.
    */
   private void handleLiveParticle(GLAutoDrawable drawable, Particle particle) {
      // The current location of the particle; Need to account for the zoom
      // distance so user can zoom in and out the particles.
      
        GL2 gl = drawable.getGL().getGL2();

        gl.glPushAttrib(GL2.GL_ENABLE_BIT);
            gl.glDisable(GL2.GL_LIGHTING);
            gl.glEnable(GL.GL_TEXTURE_2D);
            gl.glEnable(GL.GL_BLEND);
            gl.glBlendFunc(GL.GL_SRC_ALPHA, GL.GL_ONE_MINUS_SRC_ALPHA);

            gl.glPushMatrix();
                gl.glTranslatef(particle.getXLocation(), particle.getYLocation(), particle.getZLocation());
                // desenha o sistema
                super.draw(drawable);

            gl.glPopMatrix();
        gl.glPopAttrib();

        // Update the particles' properties.
        updateParticle(particle);
   }

   /**
    * Updates the properties of the given particle. For example, its life,
    * location, etc. must be updated.
    * 
    * @param particle The Particle to be updated.
    */
   private void updateParticle(Particle particle) {
      // Update the particles' life. Each particle's life decreases.
      particle.setLife(particle.getLife() - particle.getFade());
      // Update the particles's location. The particle's new location is
      // equal to it's current location + it's directional speed/slowdown.
      particle.setXLocation(
         particle.getXLocation() + particle.getXSpeed() / (DELAY * 1000));
      particle.setYLocation(
         particle.getYLocation() + particle.getYSpeed() / (DELAY * 1000));
      particle.setZLocation(
         particle.getZLocation() + particle.getZSpeed() / (DELAY * 1000));
      // Update the particle's directional speed. It's directional speed
      // is affected by the direction's gravitional pull.
      particle.setXSpeed(particle.getXSpeed() + particle.getXGravity());
      particle.setYSpeed(particle.getYSpeed() + particle.getYGravity());
      particle.setZSpeed(particle.getZSpeed() + particle.getZGravity());
      // Update the particle's X and Y gravitional pulls. The gravitational
      // pulls can be modified by the user.
      particle.setXGravity(particle.getXGravity() + XGRAVITY_CHANGE);
      particle.setYGravity(particle.getYGravity() + YGRAVITY_CHANGE);
   }

   /**
    * Handles the dead particle given. A dead particle is restarted by
    * given it full life, new color, new speeds, etc.
    * 
    * @param particle The dead particle to be processed.
    */
   private void handleDeadParticle(Particle particle) {
      // Ask the particle to restart itself.
      particle.restart();
   }
}
