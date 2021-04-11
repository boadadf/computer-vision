package ch.dfb.tester.test;

import ch.dfb.tester.interfaces.UserActionListener;
import ch.dfb.tester.robot.UserActionPlayer;
import ch.dfb.tester.robot.UserActionRecorder;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class ClickStepUI extends JPanel implements UserActionListener, StepConfigurationIfc {
  private UserActionRecorder _recorder;
  private JLabel _imageLabel;
  private BufferedImage _image;
  private JTextField _delayTextField;
  private JTextField _repeatTextField;

  public ClickStepUI(UserActionRecorder inRecorder) {
    _recorder = inRecorder;
    createComponents();
  }

  private void createComponents() {
    setLayout(new GridLayout(3, 2));
    JButton recordClick = new JButton(">");
    add(recordClick);
    _imageLabel = new JLabel();
    _delayTextField = new JTextField("0");
    _repeatTextField = new JTextField("0");
    add(_imageLabel);
    add(new JLabel("Wait"));
    add(_delayTextField);
    add(new JLabel("Repeat"));
    add(_repeatTextField);
    recordClick.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        _recorder.record();
      }
    });
    this.setPreferredSize(new Dimension(200, 200));
  }

  @Override
  public void onClickShot(BufferedImage inImageIcon) {
    _image = inImageIcon;
    _imageLabel.setIcon(new ImageIcon(inImageIcon));
  }

  @Override
  public void onType(char inChar) {

  }

  @Override
  public StepIfc getStep() {
    if (_image != null) {
      return new StepIfc() {
        public boolean executeStep(UserActionPlayer inPlayer) {
          return inPlayer.clickOn(_image);
        }

        public int getDelay() {
          return Integer.parseInt(_delayTextField.getText());
        }

        public int getRepeat() {
          return Integer.parseInt(_repeatTextField.getText());
        }
      };
    }
    return null;
  }

}
