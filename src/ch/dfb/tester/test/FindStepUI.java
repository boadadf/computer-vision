package ch.dfb.tester.test;

import ch.dfb.tester.interfaces.UserActionListener;
import ch.dfb.tester.robot.UserActionPlayer;
import ch.dfb.tester.robot.UserActionRecorder;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.awt.image.DataBuffer;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class FindStepUI extends JPanel implements UserActionListener, StepConfigurationIfc {
  private UserActionRecorder _recorder;
  private JLabel _imageLabel;
  private BufferedImage _image;
  private JTextField _delayTextField;

  public FindStepUI(UserActionRecorder inRecorder) {
    _recorder = inRecorder;
    createComponents();
  }

  private void createComponents() {
    setLayout(new GridLayout(2, 2));
    JButton recordClick = new JButton(">");
    add(recordClick);
    _imageLabel = new JLabel();
    _delayTextField = new JTextField("0");
    add(_imageLabel);
    add(new JLabel("Timeout"));
    add(_delayTextField);
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
        public int getRepeat() {
          return 0;
        }

        public boolean executeStep(UserActionPlayer inPlayer) {
          try {
            inPlayer.moveTo(_image);
          } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
          }
          BufferedImage actualImage = _recorder.getCropImageAtMousePosition();
          try {
            ImageIO.write(actualImage, "jpg", new File("out.jpg"));
          } catch (IOException e) {
            // TODO Auto-generated catch blockasasas
            e.printStackTrace();
          }
          float rate = compareImage(_image, actualImage);
          System.out.println("rate:" + rate);
          return rate > 90;
        }

        public int getDelay() {
          return Integer.parseInt(_delayTextField.getText());
        }

        public float compareImage(BufferedImage biA, BufferedImage biB) {

          float percentage = 0;
          try {
            // take buffer data from both image files //
            DataBuffer dbA = biA.getData().getDataBuffer();
            int sizeA = dbA.getSize();
            DataBuffer dbB = biB.getData().getDataBuffer();
            int sizeB = dbB.getSize();
            int count = 0;
            // compare data-buffer objects //
            if (sizeA == sizeB) {

              for (int i = 0; i < sizeA; i++) {

                if (dbA.getElem(i) == dbB.getElem(i)) {
                  count = count + 1;
                }

              }
              percentage = (count * 100) / sizeA;
            } else {
              System.out.println("Both the images are not of same size");
            }

          } catch (Exception e) {
            System.out.println("Failed to compare image files ...");
          }
          return percentage;
        }

        boolean bufferedImagesEqual(BufferedImage img1, BufferedImage img2) {
          if (img1.getWidth() == img2.getWidth() && img1.getHeight() == img2.getHeight()) {
            for (int x = 0; x < img1.getWidth(); x++) {
              for (int y = 0; y < img1.getHeight(); y++) {
                if (img1.getRGB(x, y) != img2.getRGB(x, y))
                  return false;
              }
            }
          } else {
            return false;
          }
          return true;
        }
      };
    }
    return null;
  }

}
