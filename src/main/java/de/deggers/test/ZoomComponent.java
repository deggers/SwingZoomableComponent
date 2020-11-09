package de.deggers.test;

import javax.swing.*;
import java.awt.*;

public class ZoomComponent extends JComponent {

  private static final String uiClassID = "ZoomComponentUI";
  private ZoomModel model;

  public ZoomComponent(ZoomModel model) {
    setBackground(Color.black);
    setFocusable(true);
    updateUI();
    this.changeModel(model);
  }

  public void changeModel(ZoomModel newModel) {
    if (model != newModel) {
      ZoomModel old = model;
      this.model = newModel;
      firePropertyChange("model", old, newModel);
    }
  }

  public ZoomModel getModel() {
    return model;
  }

  @Override
  public Dimension getPreferredSize() {
    ZoomModel model = getModel();
    Dimension size = new Dimension(100, 100);
    if (model != null) {
      size = model.getScaledSize();
    }
    return size;
  }

  private void setUI(BasicZoomUI ui) {
    super.setUI(ui);
  }

  @Override
  public void updateUI() {
    if (UIManager.get(getUIClassID()) != null) {
      ZoomUI ui = (ZoomUI) UIManager.getUI(this);
      setUI(ui);
    } else {
      setUI(new BasicZoomUI(this));
    }
  }

  @Override
  public BasicZoomUI getUI() {
    return (BasicZoomUI) ui;
  }

  @Override
  public String getUIClassID() {
    return uiClassID;
  }

}

