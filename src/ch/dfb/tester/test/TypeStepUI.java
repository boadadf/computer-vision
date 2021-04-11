package ch.dfb.tester.test;

import ch.dfb.tester.interfaces.UserActionListener;
import ch.dfb.tester.robot.UserActionPlayer;
import ch.dfb.tester.robot.UserActionRecorder;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.image.BufferedImage;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class TypeStepUI extends JPanel implements UserActionListener, StepConfigurationIfc {

  private UserActionRecorder _recorder;
  private JTextField _textField;
  private JTextField _delayTextField;

  public TypeStepUI(UserActionRecorder inRecorder) {
    _recorder = inRecorder;
    createComponents();
  }

  private void createComponents() {
    setLayout(new GridLayout(2, 2));
    JLabel textLabel = new JLabel("Text:");
    add(textLabel);
    _textField = new JTextField();
    add(_textField);
    add(new JLabel("Timeout"));
    _delayTextField = new JTextField("0");
    add(_delayTextField);
    this.setPreferredSize(new Dimension(200, 200));
  }

  @Override
  public void onClickShot(BufferedImage inImageIcon) {

  }

  @Override
  public void onType(char inChar) {

  }

  @Override
  public StepIfc getStep() {
    if (!"".equals(_textField.getText())) {
      return new StepIfc() {
        public boolean executeStep(UserActionPlayer inPlayer) {
          return inPlayer.type(_textField.getText() + "\n");
        }

        public int getRepeat() {
          return 0;
        }

        @Override
        public int getDelay() {
          return Integer.parseInt(_delayTextField.getText());
        }
      };
    }
    return null;
  }
}