package util;

/**
 *
 * @author victor
 */
public class Vector3f {
    
    public static Vector3f ZERO = new Vector3f(0, 0, 0);
    
    public float x,y,z;
    
    public Vector3f(float x, float y, float z){
        this.x = x;
        this.y = y;
        this.z = z;
    }
    
    public Vector3f(Vector3f p) {
        x = p.x;
        y = p.y;
        z = p.z;
    }
    
    public void set(Vector3f v) {
        x = v.x;
        y = v.y;
        z = v.z;
    }
    
    public void normalize(){
        float length = this.length();
        if(length > 0.001f){
            x /= length;
            y /= length;
            z /= length;
        }
    }
    
    public float length(){
        return (float) Math.sqrt((x*x) + (y*y) + (z*z));
    }
    
    public float dot(Vector3f u) {
        return x*u.x + y*u.y + z*u.z;
    }
    
    public float dot(float p[]) {
        return x*p[0] + y*p[1] + z*p[2] + p[3];
    }
    
    public Vector3f cross(Vector3f u) {
        return new Vector3f(y * u.z - u.y * z, z * u.x - u.z * x, x * u.y - u.x * y);
    }
    
    public static Vector3f mult(Vector3f u, float c) {
        return new Vector3f(c*u.x, c*u.y, c*u.z);
    }
    
    /**
     * Produto vetorial de u para v
     * @param u 
     * @param v
     * @return vetor resultante
     */
    public static Vector3f cross(Vector3f u, Vector3f v){
        return new Vector3f(u.y*v.z - v.y*u.z, v.x*u.z - u.x*v.z, u.x*v.y - v.x*u.y);
//        return new Vector3f(v.y * u.z - u.y * v.z, v.z * u.x - u.z * v.x, v.x * u.y - u.x * v.y);
    }
    /**
     * Produto escalar entre u e v
     * @param u
     * @param v
     * @return 
     */
    public static float dot(Vector3f u, Vector3f v) {
        return (u.x*v.x + u.y*v.y + u.z*v.z);
    }
    
    public static Vector3f add(Vector3f u, Vector3f v) {
        return new Vector3f(u.x+v.x, u.y+v.y, u.z+v.z);
    }
    
    public static Vector3f sub(Vector3f u, Vector3f v) {
        return new Vector3f(u.x-v.x, u.y-v.y, u.z-v.z);
    }
    
    public static Vector3f negative(Vector3f u) {
        return new Vector3f(-u.x, -u.y, -u.z);
    }
    
    public float getDistance(Vector3f u) {
        return Vector3f.sub(this, u).length();
    }
    
    
    @Override
    public String toString(){
        return "["+x+","+y+","+z+"]";
    }
}
