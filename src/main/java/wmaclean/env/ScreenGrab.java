package wmaclean.env;

import javax.swing.JFrame;
import java.awt.Component;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;

/**
 * At some point, it would be nice to convert from using handcrafted input vectors to using a CNN.
 * This class is a starting point for taking a JFrame and turning it into a matrix of pixels.
 */
public class ScreenGrab {

    public static BufferedImage getScreenShot(JFrame f) {
        //https://stackoverflow.com/questions/5853879/swing-obtain-image-of-jframe
        Component component = f.getContentPane();
        BufferedImage image = new BufferedImage(
                component.getWidth(),
                component.getHeight(),
                BufferedImage.TYPE_INT_RGB
        );
        // call the Component's paint method, using
        // the Graphics object of the image.
        component.paint( image.getGraphics() ); // alternately use .printAll(..)
        return image;
    }

    public static byte[] getByteArray(JFrame f){
        //https://stackoverflow.com/questions/6524196/java-get-pixel-array-from-image
        BufferedImage bufferedImage = ScreenGrab.getScreenShot(f);
        return ((DataBufferByte) bufferedImage.getRaster().getDataBuffer()).getData();
    }

}
