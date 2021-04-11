package ch.dfb.tester.test;

import ch.dfb.tester.robot.UserActionPlayer;

public interface StepIfc {

  public boolean executeStep(UserActionPlayer inPlayer);

  public int getDelay();

  public int getRepeat();
}
