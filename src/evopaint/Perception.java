/*
 *  Copyright (C) 2010 Markus Echterhoff <tam@edu.uni-klu.ac.at>
 *
 *  This file is part of EvoPaint.
 *
 *  EvoPaint is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with EvoPaint.  If not, see <http://www.gnu.org/licenses/>.
 */

package evopaint;

import evopaint.pixel.Pixel;
import evopaint.util.ExceptionHandler;
import evopaint.util.avi.AVIOutputStream;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;

/**
 *
 * @author Markus Echterhoff <tam@edu.uni-klu.ac.at>
 */
public class Perception {
    private Configuration configuration;
    private BufferedImage image;
    private int[] internalImage;
    File videoFile;
    AVIOutputStream videoOut;

    public BufferedImage getImage() {
        return image;
    }

    public void createImage() {
        for (int i = 0; i < internalImage.length; i++) {
            Pixel pixie = configuration.world.getUnclamped(i);
            internalImage[i] =
                        pixie == null ?
                        configuration.backgroundColor :
                        pixie.getPixelColor().getInteger();
        }
        if (videoOut != null) {
            try {
                videoOut.writeFrame(configuration.mainFrame.getShowcase().translate(image));
            } catch (IOException ex) {
                ExceptionHandler.handle(ex, false, "Cannot write to video file anymore... delete some porn or go buy a harddisk!?");
            }
        }
    }

    public synchronized boolean startRecording() {
        if (videoOut != null) {
            return false;
        }
        try {
            videoFile = new File(configuration.fileHandler.getHomeDir(),
                    "EvoPaint-recording.avi");
            for (int i = 1; videoFile.exists(); i++) {
                videoFile = new File(configuration.fileHandler.getHomeDir(),
                    "EvoPaint-recording_" + i + ".avi");
            }
            videoOut = new AVIOutputStream(
                    videoFile, AVIOutputStream.VideoFormat.PNG);
            videoOut.setTimeScale(1);
            videoOut.setFrameRate(configuration.fpsVideo);
        } catch (IOException ex) {
            ExceptionHandler.handle(ex, false, "Cannot open your video file. Ya well, plenty of reasons possible... you fix it!?");
            videoOut = null;
            return false;
        }
        return true;
    }

    public synchronized void stopRecording() {
        boolean finishedOK = false; // if we call showSaveDialog() after videoOut.finish(), the video consists of one long same frame only
        try {
            videoOut.finish();
            finishedOK = true;
        } catch (IOException ex) {
            Logger.getLogger(Perception.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            videoOut = null;
        }
        if (finishedOK) {
            showSaveDialog();
        }
    }

    public void showSaveDialog() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileFilter(new FileNameExtensionFilter("*.avi", "avi"));
        int option = fileChooser.showSaveDialog(configuration.mainFrame);

        boolean copySuccessful = true;
        if (option == JFileChooser.APPROVE_OPTION) {
            
            File saveLocation = fileChooser.getSelectedFile();
            FileInputStream in = null;
            FileOutputStream out = null;
            try {
                in = new FileInputStream(videoFile);
                out = new FileOutputStream(saveLocation);
                byte [] buffer = new byte[1024];
                int readBytes;
                while ((readBytes = in.read(buffer)) > 0) {
                    out.write(buffer, 0, readBytes);
                }

            } catch (IOException ex) {
                copySuccessful = false;
                ExceptionHandler.handle(ex, false, "An error occured copying your video file to the designated location. But don't worry, it is save. Checkout your .evopaint folder and move it yourself. The file is called EvoPaint-recording.avi");
            } finally {
                if (in != null) {
                    try {
                        in.close();
                    } catch (IOException ex) {
                        ExceptionHandler.handle(ex, false, "Unable to close file.");
                    }
                }
                if (out != null) {
                    try {
                        out.close();
                    } catch (IOException ex) {
                        ExceptionHandler.handle(ex, false, "Unable to close file.");
                    }
                }
            }
        }

        if (copySuccessful) {
            videoFile.delete();
        }
        videoFile = null;
    }

    public Perception(Configuration configuration) {
        this.configuration = configuration;
        this.image = new BufferedImage(configuration.dimension.width, configuration.dimension.height,
                BufferedImage.TYPE_INT_RGB);
        this.internalImage = ((DataBufferInt)this.image.getRaster().getDataBuffer()).getData();

    }
}
