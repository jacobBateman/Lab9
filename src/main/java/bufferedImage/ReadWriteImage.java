package bufferedImage;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;

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

    public int isBrignthessCorrect(int constant){
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
                int red = isBrignthessCorrect(rgb[0] + value);
                int green = isBrignthessCorrect(rgb[1] + value);
                int blue = isBrignthessCorrect(rgb[2] + value);
                rgb[0] = red;
                rgb[1] = green;
                rgb[2] = blue;

                image.getRaster().setPixel(i,j,rgb); //i ,j współrzędne pikseli, rgb to tablica trzech kolorów ze zwiększoną jasnością danego piksela
            }
        }
    }
}
