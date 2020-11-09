package de.deggers.test;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.plaf.ComponentUI;
import java.awt.*;
import java.awt.event.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

public class BasicZoomUI extends ZoomUI {

  private MouseAdapter mouseHandler;
  private ZoomComponent zoomComponent;
  private final ChangeListener changeHandler = (ChangeEvent e) -> {
    zoomComponent.revalidate();
    zoomComponent.repaint();
  };

  private final PropertyChangeListener propertyChangeHandler = (PropertyChangeEvent evt) -> {
    ZoomModel old = (ZoomModel) evt.getOldValue();
    if (old != null) {
      old.removeChangeListener(changeHandler);
    }
    ZoomModel newValue = (ZoomModel) evt.getNewValue();
    if (newValue != null) {
      newValue.addChangeListener(changeHandler);
    }
  };

  private BasicZoomUI() {
    // not allowed from the outside
  }

  public BasicZoomUI(ZoomComponent zoomComponent) {
    this.zoomComponent = zoomComponent;
  }

  protected void installMouseListener() {
    mouseHandler = new MouseAdapter() {

      @Override
      public void mouseClicked(MouseEvent e) {
        zoomComponent.requestFocusInWindow();
      }

      @Override
      public void mouseWheelMoved(MouseWheelEvent e) {
        int amount = e.getWheelRotation();
        ZoomModel model = zoomComponent.getModel();
        if (model != null) {

          int value = model.getValue();
          model.setValue(value + amount);

        }
      }

    };
    zoomComponent.addMouseListener(mouseHandler);
    zoomComponent.addMouseWheelListener(mouseHandler);

  }

  protected void installModelPropertyChangeListener() {
    zoomComponent.addPropertyChangeListener("model", propertyChangeHandler);

  }

  protected void installKeyBindings() {

    Action zoomIn = new ZoomInAction();
    Action zoomOut = new ZoomOutAction();

    InputMap inputMap = zoomComponent.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
    inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_ADD, 0), "zoomIn");
    inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_SUBTRACT, 0), "zoomOut");
    inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_PLUS, 0), "zoomIn");
    inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_MINUS, 0), "zoomOut");

    ActionMap actionMap = zoomComponent.getActionMap();
    actionMap.put("zoomIn", zoomIn);
    actionMap.put("zoomOut", zoomOut);
  }

  private void installModelChangeListener() {

    ZoomModel model = zoomComponent.getModel();
    if (model != null) {
      model.addChangeListener(changeHandler);
    }

  }

  @Override
  public void installUI(JComponent c) {
    zoomComponent = (ZoomComponent) c;

    installMouseListener();
    installModelPropertyChangeListener();
    installKeyBindings();
    installModelChangeListener();
  }

  private void uninstallModelChangeListener() {
    zoomComponent.getModel().removeChangeListener(changeHandler);
  }

  private void uninstallKeyBindings() {
    InputMap inputMap = zoomComponent.getInputMap(JComponent.WHEN_FOCUSED);
    inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_ADD, 0), "donothing");
    inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_SUBTRACT, 0), "donothing");
    inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_PLUS, 0), "donothing");
    inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_MINUS, 0), "donothing");

    AbstractAction blank = new AbstractAction() {
      @Override
      public void actionPerformed(ActionEvent e) {
      }
    };

    ActionMap actionMap = zoomComponent.getActionMap();
    actionMap.put("zoomIn", blank);
    actionMap.put("zoomOut", blank);
  }

  private void uninstallModelPropertyChangeListener() {
    zoomComponent.removePropertyChangeListener(propertyChangeHandler);
  }

  private void uninstallMouseListener() {

    zoomComponent.removeMouseWheelListener(mouseHandler);
    mouseHandler = null;

  }

  @Override
  public void uninstallUI(JComponent c) {

    uninstallModelChangeListener();
    uninstallModelPropertyChangeListener();
    uninstallKeyBindings();
    uninstallMouseListener();

    mouseHandler = null;
    zoomComponent = null;

  }

  @Override
  public void paint(Graphics g, JComponent c) {
    super.paint(g, c);
    paintImage(g);
  }

  protected void paintImage(Graphics g) {
    if (zoomComponent != null) {
      ZoomModel model = zoomComponent.getModel();
      Image image = model.getImage();
      Dimension size = model.getScaledSize();
      int x = (zoomComponent.getWidth() - size.width) / 2;
      int y = (zoomComponent.getHeight() - size.height) / 2;
      g.drawImage(image, x, y, size.width, size.height, zoomComponent);
    }
  }

  // IntelliJ says not used - actually used. -- DO NOT DELETE !
  // Probably used on first instantiation
  public static ComponentUI createUI(JComponent c) {
    return new BasicZoomUI();
  }

  protected class ZoomAction extends AbstractAction {
    private final int delta;

    public ZoomAction(int delta) {
      this.delta = delta;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
      ZoomModel model = zoomComponent.getModel();
      if (model != null) {
        model.setValue(model.getValue() + delta);
      }
    }

  }

  final class ZoomOutAction extends ZoomAction {
    public ZoomOutAction() {
      super(-5);
    }
  }

  final class ZoomInAction extends ZoomAction {
    public ZoomInAction() {
      super(5);
    }
  }

}
