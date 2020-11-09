package de.deggers.test;

import java.awt.*;

public class DefaultZoomModel extends AbstractZoomModel {
  Image image;

  public DefaultZoomModel(Image image) {
    this.image = image;
    this.setValue(50);
  }

  @Override
  public Image getImage() {
    return image;
  }

}

