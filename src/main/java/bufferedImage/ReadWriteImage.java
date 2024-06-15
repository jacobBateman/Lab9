package bufferedImage;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class ReadWriteImage {
    BufferedImage image = null;

    public void readImage(String path){
        try{
            image = ImageIO.read(new File(path));
        }catch(java.io.IOException e){

        }
    }

    public void writeImage(String path){
        try{
            if(image != null){
                File outputFile = new File(path);
                ImageIO.write(image,"png",outputFile);
            }
        }catch(java.io.IOException e){
            System.out.println(e);
        }
    }

    public int isBrightnessCorrect(int constant){
        if (constant < 0){
            constant = 0;
        } else if(constant > 255){
            constant = 255;
        }
        return constant;
    }

    public void increaseBrightness(int value){
        int rgb[] = new int[3]; // Tablica do przechowywania kolorów

        for(int i = 0; i < image.getWidth(); i++){
            for(int j = 0; j < image.getHeight(); j++){
                rgb = image.getRaster().getPixel(i,j,rgb);
                int red = isBrightnessCorrect(rgb[0] + value);
                int green = isBrightnessCorrect(rgb[1] + value);
                int blue = isBrightnessCorrect(rgb[2] + value);
                rgb[0] = red;
                rgb[1] = green;
                rgb[2] = blue;

                image.getRaster().setPixel(i,j,rgb); //i ,j współrzędne pikseli, rgb to tablica trzech kolorów ze zwiększoną jasnością danego piksela
            }
        }
    }

    public void increaseBrightnessWithThreads(int value){

        int numThreads = Runtime.getRuntime().availableProcessors(); // Zwaraca dostępną liczbę wątków
        ExecutorService executor = Executors.newFixedThreadPool(numThreads);
        int width = image.getWidth();
        int height = image.getHeight();
        int chunkHeight = height/numThreads; // Dzielimy części obrazu dla poszczególnych wątków

        for(int i = 0; i < numThreads; i++){
            final int startRow = i * chunkHeight;
            final int endRow = (i == numThreads - 1) ? height : startRow + chunkHeight;
            executor.submit(()->{ // To co ma wykonać każdy z wątków???
               int rgb[] = new int[3];
                for (int x = 0; x < width; x++) {
                    for (int y = startRow; y < endRow; y++) {
                        image.getRaster().getPixel(x, y, rgb);
                        int red = isBrightnessCorrect(rgb[0] + value);
                        int green = isBrightnessCorrect(rgb[1] + value);
                        int blue = isBrightnessCorrect(rgb[2] + value);
                        rgb[0] = red;
                        rgb[1] = green;
                        rgb[2] = blue;
                        image.getRaster().setPixel(x, y, rgb);
                    }
                }
            });
        }
        executor.shutdown();
        try{
            executor.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
        }
        catch(InterruptedException e){
            e.printStackTrace();
        }
    }

    public void increaseBrightnessOneRowAtTime(int value){
        int threadCount = Runtime.getRuntime().availableProcessors();
        ExecutorService executor = Executors.newFixedThreadPool(threadCount);

        for(int i = 0; i < image.getHeight(); i++){
            final int y = i;
            executor.execute(() ->{
                int rgb[] = new int[3];
                for(int x = 0; x < image.getWidth(); x++){
                    image.getRaster().getPixel(x,y,rgb);
                    int red = isBrightnessCorrect(rgb[0] + value);
                    int green = isBrightnessCorrect(rgb[1]+value);
                    int blue = isBrightnessCorrect(rgb[2]+value);
                    rgb[0] = red;
                    rgb[1] = green;
                    rgb[2] = blue;
                    image.getRaster().setPixel(x,y,rgb);
                }
            });

        }
        executor.shutdown();
        try{
            boolean b = executor.awaitTermination(5,TimeUnit.SECONDS);
        }
        catch(InterruptedException e){
            throw new RuntimeException();
        }
    }
}
