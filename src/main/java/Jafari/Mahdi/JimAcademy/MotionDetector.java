package Jafari.Mahdi.JimAcademy;

import org.opencv.core.*;
import org.opencv.imgproc.Imgproc;
import org.opencv.videoio.VideoCapture;

import java.util.ArrayList;
import java.util.List;

public class MotionDetector {

    private boolean continueDetection = true;

    public MotionDetector(int cameraNumber) {
        while (continueDetection)
            startDetecting(cameraNumber);
    }

    public boolean isContinueDetection() {
        return continueDetection;
    }

    public void setContinueDetection(boolean continueDetection) {
        this.continueDetection = continueDetection;
    }


    public void startDetecting(int cameraNumber) {
        Mat frame = new Mat();
        Mat firstFrame = new Mat();
        Mat gray = new Mat();
        Mat frameDelta = new Mat();
        Mat thresh = new Mat();
        List<MatOfPoint> cnts = new ArrayList<MatOfPoint>();


        VideoCapture camera = new VideoCapture();
        camera.open(cameraNumber); //open camera

        //set the video size to 512x288
        camera.set(3, 512);
        camera.set(4, 288);

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        camera.read(frame);
        //convert to grayscale and set the first frame
        Imgproc.cvtColor(frame, firstFrame, Imgproc.COLOR_BGR2GRAY);
        Imgproc.GaussianBlur(firstFrame, firstFrame, new Size(21, 21), 0);

        boolean noMotionDetected = true;
        while (noMotionDetected) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            camera.read(frame);

            //convert to grayscale
            Imgproc.cvtColor(frame, gray, Imgproc.COLOR_BGR2GRAY);
            Imgproc.GaussianBlur(gray, gray, new Size(21, 21), 0);

            //compute difference between first frame and current frame
            Core.absdiff(firstFrame, gray, frameDelta);
            Imgproc.threshold(frameDelta, thresh, 25, 255, Imgproc.THRESH_BINARY);

            Imgproc.dilate(thresh, thresh, new Mat(), new Point(-1, -1), 2);
            Imgproc.findContours(thresh, cnts, new Mat(), Imgproc.RETR_EXTERNAL, Imgproc.CHAIN_APPROX_SIMPLE);

            for (MatOfPoint cnt : cnts) {
                if (Imgproc.contourArea(cnt) < 500) {
                    continue;
                }

                System.out.println("Motion detected!!!");
                noMotionDetected = false;
                break;
            }
        }

    }

}