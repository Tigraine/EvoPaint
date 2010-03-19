package evopaint.gui;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;

import evopaint.EvoPaint;

public class ExportDialog implements ActionListener {
	private MainFrame frame;
	private EvoPaint evopaint;
	
	public ExportDialog(MainFrame frame, EvoPaint evopaint){
		this.frame = frame;
		this.evopaint = evopaint;
	}
	
	
    public void actionPerformed(java.awt.event.ActionEvent e) {
    	File file= null;
    	evopaint.setRunning(false);
    	BufferedImage img = frame.getShowcase().getImage();
    	

    	try{
    	    FileNameExtensionFilter jpgFilter = new FileNameExtensionFilter("*.jpg","jpg", "jpeg");
    	    FileNameExtensionFilter pngFilter = new FileNameExtensionFilter("*.png","png");

    		
    		JFileChooser chooser = new JFileChooser();
    	
    		chooser.setFileFilter(jpgFilter);
    		chooser.setFileFilter(pngFilter);

    		int option = chooser.showSaveDialog(frame);
    		
    
    		if(option == JFileChooser.APPROVE_OPTION){  
        		if(chooser.getSelectedFile()!=null){  
        			file = chooser.getSelectedFile();
        				            			
        			if((chooser.getFileFilter().getDescription()).compareTo("*.jpg")==0){
        			    				
        				FileOutputStream fos = new FileOutputStream(file.getPath().concat(file.getName().concat(".jpg")));
        				
        			    ImageIO.write(img, "jpg", fos);
        			    fos.close();
        				
        			}
        			else{
        				ImageIO.write(img, "png", new File(file.getPath().concat(file.getName().concat(".png"))));
        			
        			}
        		}
    		}else{
    			
    		}
    		
    		
    	
    	
    	} catch (IOException e1) {
    		 	
    	}
    	
    	evopaint.setRunning(true);
    }
}
