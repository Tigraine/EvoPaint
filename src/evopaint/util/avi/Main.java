/**
 * @(#)Main.java  1.2  2009-08-29
 *
 * Copyright (c) 2008-2009 Werner Randelshofer
 * Hausmatt 10, CH-6405 Immensee, Switzerland
 * All rights reserved.
 *
 * The copyright of this software is owned by Werner Randelshofer.
 * You may not use, copy or modify this software, except in
 * accordance with the license agreement you entered into with
 * Werner Randelshofer. For details see accompanying license terms.
 */
package evopaint.util.avi;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.Random;

/**
 * Main.
 *
 * @author Werner Randelshofer
 * @version 1.1 2009-08-29 Added raw output.
 * <br>1.0 2008-00-15 Created.
 */
public class Main {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        try {
            test(new File("avidemo-jpg.avi"), AVIOutputStream.VideoFormat.JPG, 1f);
            test(new File("avidemo-png.avi"), AVIOutputStream.VideoFormat.PNG, 1f);
            test(new File("avidemo-raw.avi"), AVIOutputStream.VideoFormat.RAW, 1f);
            
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    private static void test(File file, AVIOutputStream.VideoFormat format, float quality) throws IOException {
        AVIOutputStream out = null;
        Graphics2D g = null;
        try {
            out = new AVIOutputStream(file, format);
            out.setVideoCompressionQuality(quality);
            
            out.setTimeScale(1);
            out.setFrameRate(30);

            Random r = new Random(0); // use seed 0 to get reproducable output
            BufferedImage img = new BufferedImage(320, 160, BufferedImage.TYPE_INT_RGB);
            g = img.createGraphics();
            g.setBackground(Color.WHITE);
            g.clearRect(0, 0, img.getWidth(), img.getHeight());

            for (int i = 0; i < 100; i++) {
                g.setColor(new Color(r.nextInt()));
                g.fillRect(r.nextInt(img.getWidth() - 30), r.nextInt(img.getHeight() - 30), 30, 30);
                out.writeFrame(img);
            }

        } finally {
            if (g != null) {
                g.dispose();
            }
            if (out != null) {
                out.close();
            }
        }
    }
}
