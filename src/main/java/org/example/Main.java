package org.example;

import bufferedImage.ReadWriteImage;

public class Main {
    public static void main(String[] args) {

        ReadWriteImage image = new ReadWriteImage();
        image.readImage("poolrooms.png");
        image.increaseBrightness(20);
        image.writeImage("modified_poolrooms.png");

    }
}