package de.deggers.test;

import javax.swing.*;
import java.awt.*;

public interface ZoomModel extends BoundedRangeModel {

  Image getImage();

  Dimension getScaledSize();

}

