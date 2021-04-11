package ch.dfb.tester.test;

import ch.dfb.tester.robot.UserActionRecorder;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

public class TestUI {

  private JPanel _panel;

  private DefaultListModel<StepUI> _testListModel;
  private JList<StepUI> _testList;
  private UserActionRecorder _recorder;
  private int counter = 1;

  public TestUI(UserActionRecorder inRecorder) {
    _testListModel = new DefaultListModel<>();
    _testList = new JList<>(_testListModel);
    _recorder = inRecorder;
    _panel = createComponents();
  }

  public JPanel getPanel() {
    return _panel;
  }

  public List<? extends StepIfc> getSteps() {
    List<StepIfc> steps = new ArrayList<>();
    for (Enumeration<StepUI> stepEnum = _testListModel.elements(); stepEnum.hasMoreElements();) {
      steps.add(stepEnum.nextElement().getStep());
    }
    return steps;
  }

  private JPanel createComponents() {
    JPanel panel = new JPanel();
    JPanel stepsPanel = new JPanel();

    GridBagLayout gridbag = new GridBagLayout();
    GridBagConstraints c = new GridBagConstraints();

    panel.setLayout(gridbag);

    JScrollPane spTestTable = new JScrollPane(_testList);
    c.gridx = 0;
    c.gridy = 0;
    c.gridheight = 2;
    c.fill = GridBagConstraints.BOTH;
    c.weighty = 1;
    c.weightx = 1;
    panel.add(spTestTable, c);

    JButton addTestButton = new JButton("+");
    JButton removeTestButton = new JButton("-");

    addTestButton.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        _testListModel.addElement(new StepUI(_recorder, _testListModel.size() + 1));
        _testList.setSelectedIndex(_testListModel.size() - 1);
      }
    });

    removeTestButton.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        if (_testListModel.getSize() > 0)
          _testListModel.removeElementAt(0);
      }
    });
    _testList.addListSelectionListener(new ListSelectionListener() {

      @Override
      public void valueChanged(ListSelectionEvent e) {
        if (!e.getValueIsAdjusting()) {
          stepsPanel.removeAll();
          StepUI step = (StepUI)_testList.getSelectedValue();
          step.activate();
          stepsPanel.add(step.getPanel());
          panel.validate();
          panel.repaint();
        }
      }

    });

    c.fill = GridBagConstraints.NONE;
    c.gridx = 1;
    c.gridy = 0;
    c.gridwidth = 1;
    c.gridheight = 1;
    c.weighty = 0;
    c.weightx = 0;
    panel.add(addTestButton, c);

    c.gridx = 1;
    c.gridy = 1;
    panel.add(removeTestButton, c);

    c.gridx = 2;
    c.gridy = 0;
    c.gridwidth = 1;
    c.gridheight = 2;
    c.weighty = 1;
    c.weightx = 1;
    c.fill = GridBagConstraints.BOTH;
    panel.add(stepsPanel, c);
    addTestButton.doClick();
    panel.setPreferredSize(new Dimension(200, 200));
    return panel;

  }
}
