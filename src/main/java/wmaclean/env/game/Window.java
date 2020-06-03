package wmaclean.env.game;

import wmaclean.env.Environment;

import javax.swing.*;
import java.awt.*;

public class Window {

    public static int BOTTOM_BORDER_OFFSET = 20;

    private final JFrame window;

    public Window(final int width, final int height, final String title, final Environment environment){
        window = new JFrame(title);

        window.setPreferredSize(new Dimension(width, height));
        window.setMaximumSize(new Dimension(width, height));
        window.setMinimumSize(new Dimension(width,  height));

        window.setResizable(false);

        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setLocationRelativeTo(null);
        window.add(environment);

        window.setVisible(true);

        environment.start();
    }

    public JFrame get(){
        return this.window;
    }
}
