package ch.dfb.tester;

import ch.dfb.tester.interfaces.UserActionListener;
import ch.dfb.tester.robot.UserActionPlayer;
import ch.dfb.tester.robot.UserActionRecorder;
import ch.dfb.tester.test.StepIfc;
import ch.dfb.tester.test.TestUI;
import java.awt.AWTException;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.SwingUtilities;

public class MainTeacher implements UserActionListener {

  private UserActionRecorder _recorder;
  private UserActionPlayer _player;
  private JFrame _frame = new JFrame("Demo");

  private JMenuBar _menuBar;
  private JSplitPane _content;
  private JPanel _testBatchPanel;
  private TestUI _testUI;
  static GraphicsDevice device = GraphicsEnvironment.getLocalGraphicsEnvironment().getScreenDevices()[0];
  int counter = 0;

  public MainTeacher() throws AWTException {
    _recorder = new UserActionRecorder();
    _frame = new JFrame("Demo");
    _player = new UserActionPlayer();
    _testUI = new TestUI(_recorder);
  }

  public static void main(String[] args) {
    nu.pattern.OpenCV.loadShared();
    SwingUtilities.invokeLater(new Runnable() {
      @Override
      public void run() {
        try {
          new MainTeacher().start();
        } catch (AWTException e) {
          e.printStackTrace();
        }
      }
    });
  }
  boolean running = false;

  /**
   * Loads all the GUI components.
   */
  public void start() {

    _content = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);

    _testBatchPanel = new JPanel();

    _content.setLeftComponent(_testBatchPanel);
    _content.setRightComponent(_testUI.getPanel());

    GridBagLayout gridbag = new GridBagLayout();
    GridBagConstraints c = new GridBagConstraints();
    _testBatchPanel.setLayout(gridbag);

    JButton executeBatchButton = new JButton(">");
    JButton stopBatchButton = new JButton("||");

    stopBatchButton.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        running = false;
      }
    });

    c.gridx = 2;
    c.gridy = 2;
    _testBatchPanel.add(executeBatchButton, c);

    c.gridx = 2;
    c.gridy = 3;
    c.weighty = 1.0;
    c.anchor = GridBagConstraints.NORTH;
    _testBatchPanel.add(stopBatchButton, c);

    executeBatchButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        new Thread() {
          public void run() {
            _frame.setState(Frame.ICONIFIED);
            _recorder.setListener(null);
            _recorder.stopRecord();
            running = true;
            for (StepIfc step : _testUI.getSteps()) {
              for (int i = 0; i <= step.getRepeat(); i++) {
                running &= step.executeStep(_player);
              }
              try {
                Thread.sleep(step.getDelay() * 1000);
              } catch (InterruptedException e1) {
                e1.printStackTrace();
              }
              if (!running) {
                break;
              }
            }

            JOptionPane.showMessageDialog(null, ("Test finished:" + running), "Result", JOptionPane.INFORMATION_MESSAGE);
            try {
              Thread.sleep(5000);
            } catch (InterruptedException e) {
              e.printStackTrace();
            }
          }
        }.start();
      }
    });

    _menuBar = new JMenuBar();
    JMenu fileMenu = new JMenu("File");
    _menuBar.add(fileMenu);
    JMenuItem newItem = new JMenuItem();
    newItem.addActionListener(new ActionListener() {

      @Override
      public void actionPerformed(ActionEvent e) {

      }

    });
    fileMenu.add(newItem);

    JMenuItem loadItem = new JMenuItem();
    loadItem.addActionListener(new ActionListener() {

      @Override
      public void actionPerformed(ActionEvent e) {

      }

    });
    fileMenu.add(loadItem);

    JMenuItem saveItem = new JMenuItem();
    saveItem.addActionListener(new ActionListener() {

      @Override
      public void actionPerformed(ActionEvent e) {

      }

    });
    fileMenu.add(saveItem);

    fileMenu.addSeparator();

    JMenuItem exitItem = new JMenuItem();
    exitItem.addActionListener(new ActionListener() {

      @Override
      public void actionPerformed(ActionEvent e) {

      }

    });
    fileMenu.add(exitItem);

    _frame.setJMenuBar(_menuBar);
    _frame.getContentPane().add(_content);

    _frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    _frame.pack();
    _frame.setSize(500, 230);
    _frame.setAlwaysOnTop(true);
    _frame.setVisible(true);
    Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
    _frame.setLocation(0, dim.height - _frame.getSize().height);
    _frame.setAutoRequestFocus(true);
  }

  @Override
  public void onClickShot(BufferedImage inImageIcon) {

    _frame.setVisible(true);

    try {
      this.bufferedImageToMat(inImageIcon, "clicked");
    } catch (IOException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }

  }

  @Override
  public void onType(char inChar) {

  }

  public static void bufferedImageToMat(BufferedImage bi, String name) throws IOException {
    File outputfile = new File(name + ".jpg");
    ImageIO.write(bi, "jpg", outputfile);
  }
}
