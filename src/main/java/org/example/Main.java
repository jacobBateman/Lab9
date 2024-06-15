package org.example;

import bufferedImage.ReadWriteImage;

public class Main {
    public static void main(String[] args) {

        ReadWriteImage image = new ReadWriteImage();
        image.readImage("poolrooms.png");
        //image.increaseBrightness(20);

        long startTime = System.currentTimeMillis();
        /*
        image.increaseBrightnessWithThreads(200);

         */
        image.increaseBrightnessOneRowAtTime(200);
        long endTime = System.currentTimeMillis();
        System.out.println("Multi-threaded execution time: " + (endTime - startTime) + " ms");

        image.writeImage("modified_poolrooms.png");

    }
}