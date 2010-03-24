/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package evopaint;

import evopaint.pixel.Pixel;
import evopaint.gui.MainFrame;

import java.awt.Dimension;
import java.awt.image.BufferedImage;

/**
 *
 * @author tam
 */
public class EvoPaint {
    private World world;
    public void setWorld(World world) {
		this.world = world;
	}


	private boolean running = true;
    MainFrame frame;
    public void setFrame(MainFrame frame) {
		this.frame = frame;
	}


	private Configuration configuration;
    private Perception perception;



	public EvoPaint() {
        this.configuration = new Configuration(new Dimension(200,200));

         // create empty world
        long time =0;
        this.world = new World(
                new Pixel[configuration.getDimension().width * configuration.getDimension().height],
                time, configuration);

        this.perception = new Perception(new BufferedImage
                (configuration.getDimension().width, configuration.getDimension().height,
                BufferedImage.TYPE_INT_RGB));

        this.perception.createImage(world);

        this.frame = new MainFrame(configuration, this);
        
        
 
    }


	public void work() {
        while (true) {
     
            if (configuration.isRunning() == false) {
                try { Thread.sleep(500); } catch (InterruptedException e) {}
                continue;
            }

            this.perception.createImage(world);

            this.world.step();
            
            frame.getShowcase().repaint();

           //this.frame.getShowcase().paintImmediately(0, 0, this.frame.getShowcase().getWidth(), this.frame.getShowcase().getHeight());
        }
    }

    public Perception getPerception() {
        return perception;
    }

    public World getWorld() {
        return world;
    }

    public MainFrame getFrame() {
		return frame;
	}

	public void setRunning(boolean running) {
        this.running = running;
    }

    public boolean isRunning() {
        return running;
    }
    

    public Configuration getConfiguration() {
		return configuration;
	}
    
    
    public void setPerception(Perception perception) {
		this.perception = perception;
	}
    


	public void setConfiguration(Configuration newConf) {
		// TODO Auto-generated method stub
		this.configuration=newConf;
	}

    public static void main(String args[]) {
        EvoPaint evopaint = new EvoPaint();
  
        evopaint.work();
//        evopaint.work();
        

    }



}
