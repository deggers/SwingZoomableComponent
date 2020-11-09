package de.deggers.test;

import javax.swing.*;
import java.awt.*;

public abstract class AbstractZoomModel extends DefaultBoundedRangeModel implements ZoomModel {

  public AbstractZoomModel() {
    super(100, 0, 0, 200);
  }

  @Override
  public Dimension getScaledSize() {
    Dimension size = new Dimension(0, 0);
    Image image = getImage();
    if (image != null) {
      double scale = getValue() / 100d;
      size.width = (int) Math.round(image.getWidth(null) * scale);
      size.height = (int) Math.round(image.getHeight(null) * scale);

    }
    return size;
  }

}

