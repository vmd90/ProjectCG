package main;

import javax.swing.SwingUtilities;

/**
 *
 * @author victor
 */
public class Main {
    
    public static void main(String[] args){
        
        SwingUtilities.invokeLater(new Runnable() {

            @Override
            public void run() {
                MainFrame frame = MainFrame.getInstance();
                frame.start();
            }
        });
    }
}
