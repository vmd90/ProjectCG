/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package objectreader;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.StringTokenizer;
import java.util.logging.Level;
import java.util.logging.Logger;
import objects.SceneObject;
import util.Transformation;

/**
 *
 * @author victor
 */
public class SceneFileReader {
    private File file;
    private ArrayList<SceneObject> sceneObjects;
    
    public SceneFileReader(String filename) {
        file = new File(filename);
        sceneObjects = new ArrayList<SceneObject>();
        loadObjects();
    }
    
    public ArrayList<SceneObject> getObjects() {
        return sceneObjects;
    }
    
    private void loadObjects() {
        try {
            BufferedReader br = new BufferedReader(new FileReader(file));
            String line;
            while((line = br.readLine()) != null) {
                if(line.length() > 0) {
                    if(line.charAt(0) == 'o') {
                        StringTokenizer tokenizer = new StringTokenizer(line, " [];");
                        while(tokenizer.hasMoreTokens()) {
                            tokenizer.nextToken();
                            String foldername = tokenizer.nextToken();
                            boolean useTex = Boolean.parseBoolean(tokenizer.nextToken());
                            int animType = Integer.parseInt(tokenizer.nextToken());
                            boolean animating = Boolean.parseBoolean(tokenizer.nextToken());
                            int fps = Integer.parseInt(tokenizer.nextToken());
                            boolean loop = Boolean.parseBoolean(tokenizer.nextToken());
                            int numAnim = Integer.parseInt(tokenizer.nextToken());
                            Transformation transf[] = null;
                            if(animType == 1) { // seq de transformacoes
                                transf = new Transformation[numAnim];
                                int i = 0;
                                while(i < numAnim) {
                                    StringTokenizer tok = new StringTokenizer(tokenizer.nextToken(), ",");
                                    while(tok.hasMoreTokens()) {
                                        transf[i] = new Transformation();
                                        transf[i].x = Float.parseFloat(tok.nextToken());
                                        transf[i].y = Float.parseFloat(tok.nextToken());
                                        transf[i].z = Float.parseFloat(tok.nextToken());
                                        transf[i].sx = Float.parseFloat(tok.nextToken());
                                        transf[i].sy = Float.parseFloat(tok.nextToken());
                                        transf[i].sz = Float.parseFloat(tok.nextToken());
                                        transf[i].angle = Float.parseFloat(tok.nextToken());
                                        transf[i].rx = Float.parseFloat(tok.nextToken());
                                        transf[i].ry = Float.parseFloat(tok.nextToken());
                                        transf[i].rz = Float.parseFloat(tok.nextToken());
                                    }
                                    ++i;
                                }
                            }
                            else if(animType == 2) {
                                transf = new Transformation[1];
                                StringTokenizer tok = new StringTokenizer(tokenizer.nextToken(), ",");
                                    while(tok.hasMoreTokens()) {
                                        transf[0] = new Transformation();
                                        transf[0].x = Float.parseFloat(tok.nextToken());
                                        transf[0].y = Float.parseFloat(tok.nextToken());
                                        transf[0].z = Float.parseFloat(tok.nextToken());
                                        transf[0].sx = Float.parseFloat(tok.nextToken());
                                        transf[0].sy = Float.parseFloat(tok.nextToken());
                                        transf[0].sz = Float.parseFloat(tok.nextToken());
                                        transf[0].angle = Float.parseFloat(tok.nextToken());
                                        transf[0].rx = Float.parseFloat(tok.nextToken());
                                        transf[0].ry = Float.parseFloat(tok.nextToken());
                                        transf[0].rz = Float.parseFloat(tok.nextToken());
                                    }
                            }
                            sceneObjects.add(new SceneObject(foldername, useTex, animType, animating, fps, loop, numAnim, transf));
                        }
                    }
                }
            }
            if(br != null)
                br.close();
        }
        catch (Exception ex) {
            Logger.getLogger(SceneFileReader.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
