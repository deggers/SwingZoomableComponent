package de.deggers.test;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;

public class TestZoom100 {

  public static void main(String[] args) {
    new TestZoom100();
  }

  public TestZoom100() {
    SwingUtilities.invokeLater(() -> {
      try {
        UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
      } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException ex) {
        ex.printStackTrace();
      }

      UIManager.getDefaults().put("ZoomComponentUI", "de.deggers.test.BasicZoomUI");

      try {
        String home = System.getProperty("user.home");
        File f = new File(home + "/image.jpg"); // @TODO maybe replace with String.format
        DefaultZoomModel model = new DefaultZoomModel(ImageIO.read(f));
        ZoomComponent zoomComp = new ZoomComponent(model);

        JSlider slider = new JSlider(model);

        JFrame frame = new JFrame("Testing");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(new JScrollPane(zoomComp));
        frame.add(slider, BorderLayout.SOUTH);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
      } catch (IOException exp) {
        exp.printStackTrace();
      }
    });
  }

}

