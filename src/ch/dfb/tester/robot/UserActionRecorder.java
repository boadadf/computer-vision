package ch.dfb.tester.robot;

import ch.dfb.tester.interfaces.UserActionListener;
import java.awt.AWTEvent;
import java.awt.AWTException;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.event.AWTEventListener;
import java.awt.event.InputEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;

public class UserActionRecorder implements AWTEventListener {
  private JFrame _glassFrame;
  private UserActionListener _listener;
  private final static Robot _robot;
  static {
    try {
      _robot = new Robot();
    } catch (AWTException e) {
      throw new ExceptionInInitializerError(e);
    }
  }
  static GraphicsDevice device = GraphicsEnvironment.getLocalGraphicsEnvironment().getScreenDevices()[0];

  public UserActionRecorder() {

    _glassFrame = new JFrame();

    _glassFrame.setUndecorated(true);
    _glassFrame.setExtendedState(JFrame.MAXIMIZED_BOTH);

    _glassFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    _glassFrame.setOpacity(0.01f);
    _glassFrame.pack();
    _glassFrame.addMouseListener(new MouseAdapter() {

      public void mouseReleased(final MouseEvent e) {
        System.out.println("release! " + e);
        onClick(e.getPoint());
        int b = e.getButton();
        final int mod = b == 1 ? InputEvent.BUTTON1_DOWN_MASK : b == 2 ? InputEvent.BUTTON2_DOWN_MASK : InputEvent.BUTTON3_DOWN_MASK;
        SwingUtilities.invokeLater(new Runnable() {
          @Override
          public void run() {
            System.out.println("clicking " + e.getPoint());
            _robot.mousePress(mod);
            _robot.mouseRelease(mod);
          }
        });
      }
    });

  }

  public void setListener(UserActionListener inListener) {
    stopRecord();
    _listener = inListener;
  }

  @Override
  public void eventDispatched(AWTEvent event) {
    System.out.println(event);
    // if (event instanceof FocusEvent) {
    // FocusEvent fe = (FocusEvent)event;
    // fe.get
    // Point p = MouseInfo.getPointerInfo().getLocation();
    // System.out.println("Mouse Clicked at " + p.x + ", " + p.y);
    // onClick(p);
    // }
  }

  private void onClick(Point inP) {
    if (_listener != null) {
      _glassFrame.setVisible(false);
      BufferedImage image = getCropImage(inP);
      _listener.onClickShot(image);
    }
  }

  private BufferedImage getCropImage(Point inP) {
    System.out.println("Point:" + inP);
    BufferedImage image = _robot.createScreenCapture(new Rectangle(Toolkit.getDefaultToolkit().getScreenSize()));
    return image.getSubimage(inP.x - 75, _glassFrame.getY() + inP.y - 15, 150, 30);
  }

  public BufferedImage getCropImageAtMousePosition() {
    record();
    Point p = MouseInfo.getPointerInfo().getLocation();
    p.y -= _glassFrame.getY();
    stopRecord();
    return getCropImage(p);
  }

  public void record() {
    _glassFrame.setVisible(true);
  }

  public void stopRecord() {
    _glassFrame.setVisible(false);
  }

}
