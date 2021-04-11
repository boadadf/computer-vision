package ch.dfb.tester.test;

import ch.dfb.tester.interfaces.UserActionListener;
import ch.dfb.tester.robot.UserActionRecorder;
import java.awt.Dimension;
import java.awt.image.BufferedImage;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class StepUI implements UserActionListener {

  private JTabbedPane tabbedTestPane;
  private JPanel typePanel = new JPanel();
  private JPanel findPanel = new JPanel();
  private JPanel clickPanel = new JPanel();

  private UserActionRecorder _recorder;
  private final int index;

  public StepUI(UserActionRecorder inRecorder, int inIndex) {
    this._recorder = inRecorder;
    index = inIndex;
    createComponents();
  }

  public JComponent getPanel() {
    return tabbedTestPane;
  }

  public void createComponents() {

    tabbedTestPane = new JTabbedPane();
    typePanel = new TypeStepUI(_recorder);
    findPanel = new FindStepUI(_recorder);
    clickPanel = new ClickStepUI(_recorder);

    tabbedTestPane.addTab("Click", clickPanel);
    tabbedTestPane.addTab("Type", typePanel);
    tabbedTestPane.addTab("Find", findPanel);

    _recorder.setListener((UserActionListener)clickPanel);
    tabbedTestPane.setPreferredSize(new Dimension(450, 170));

    tabbedTestPane.addChangeListener(new ChangeListener() {

      @Override
      public void stateChanged(ChangeEvent e) {
        _recorder.setListener((UserActionListener)tabbedTestPane.getSelectedComponent());
      }

    });
  }

  public StepIfc getStep() {
    return ((StepConfigurationIfc)(tabbedTestPane.getSelectedComponent())).getStep();
  }

  public String toString() {
    return "Step " + index;
  }

  @Override
  public void onClickShot(BufferedImage inImageIcon) {

  }

  @Override
  public void onType(char inChar) {

  }

  public void activate() {
    _recorder.setListener((UserActionListener)tabbedTestPane.getSelectedComponent());
  }

}
