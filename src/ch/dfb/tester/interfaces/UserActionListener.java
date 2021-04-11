package ch.dfb.tester.interfaces;

import java.awt.image.BufferedImage;

public interface UserActionListener {
  void onClickShot(BufferedImage inImageIcon);

  void onType(char inChar);
}
