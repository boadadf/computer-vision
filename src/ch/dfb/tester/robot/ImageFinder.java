package ch.dfb.tester.robot;

import java.awt.AWTException;
import java.awt.Robot;
import java.awt.event.InputEvent;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import org.opencv.core.Core;
import org.opencv.core.Core.MinMaxLocResult;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

public class ImageFinder {

  private static byte[] readStream(InputStream stream) throws IOException {
    // Copy content of the image to byte-array
    ByteArrayOutputStream buffer = new ByteArrayOutputStream();
    int nRead;
    byte[] data = new byte[524288]; // 16384

    while ((nRead = stream.read(data, 0, data.length)) != -1) {
      buffer.write(data, 0, nRead);
    }

    buffer.flush();
    byte[] temporaryImageInMemory = buffer.toByteArray();
    buffer.close();
    stream.close();
    return temporaryImageInMemory;
  }

  public static void move(String inFile, String templateFile) throws IOException, AWTException {

    Mat img = Imgcodecs.imread(inFile);
    Mat templ = Imgcodecs.imread(templateFile);
    // / Create the result matrix
    int result_cols = img.cols() - templ.cols() + 1;
    int result_rows = img.rows() - templ.rows() + 1;
    Mat result = new Mat(result_rows, result_cols, CvType.CV_32FC1);

    // / Do the Matching and Normalize
    Imgproc.matchTemplate(img, templ, result, Imgproc.TM_CCOEFF);
    Core.normalize(result, result, 0, 1, Core.NORM_MINMAX, -1, new Mat());

    // / Localizing the best match with minMaxLoc
    MinMaxLocResult mmr = Core.minMaxLoc(result);

    Point matchLoc = mmr.maxLoc;
    Robot r = new Robot();

    r.mouseMove((int)matchLoc.x + 75, (int)matchLoc.y + 15);
    r.mousePress(InputEvent.BUTTON1_MASK);
    r.mouseRelease(InputEvent.BUTTON1_MASK);
    System.out.println("Moving to image finder:" + matchLoc);
    Point p = new Point(matchLoc.x + templ.cols(), matchLoc.y + templ.rows());
    // / Show me what you got
    Imgproc.rectangle(img, matchLoc, new Point(matchLoc.x + templ.cols(), matchLoc.y + templ.rows()), new Scalar(0, 255, 0));

    // Save the visualized detection.
    System.out.println("Writing " + "output");
    Imgcodecs.imwrite("output.jpg", img);

  }

}