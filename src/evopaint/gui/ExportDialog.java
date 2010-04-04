package evopaint.gui;
import evopaint.Configuration;
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
        private Configuration configuration;
	private MainFrame frame;
	private EvoPaint evopaint;
	File file;
	String path;
	
	public ExportDialog(Configuration configuration, EvoPaint evopaint){
            this.configuration = configuration;
            this.frame = evopaint.getFrame();
            this.evopaint = evopaint;
	}
	
	
    public void actionPerformed(java.awt.event.ActionEvent e) {
    	
    	configuration.runLevel = Configuration.RUNLEVEL_STOP;
    	BufferedImage img = evopaint.getPerception().getImage();
    	

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
        			    				
        				checkExtension(".jpg");
        				
        				FileOutputStream fos = new FileOutputStream(path);
        						
        					
        			    ImageIO.write(img, "jpg", fos);
        			    fos.close();
        				
        			}
        			else{
        				
        				checkExtension(".png");
        				
        				ImageIO.write(img, "png", new File(path));
        			
        			}
        		}
    		}else{
    			
    		}
    		
    		
    	
    	
    	} catch (IOException e1) {
    		 	
    	}
    	
    	configuration.runLevel = Configuration.RUNLEVEL_RUNNING;
    }
    
    public void checkExtension(String ending){
		if(file.getPath().endsWith(ending)){
			path=file.getPath();
		}else{
			path=file.getPath().concat(ending);
		}
    }
}
