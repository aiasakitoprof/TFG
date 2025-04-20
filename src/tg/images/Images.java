/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package src.tg.images;


import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;


/**
 * @author juanm
 */
public class Images {

    private static BufferedImage spaceship_1;
    private static BufferedImage spaceship_2;
    private static BufferedImage spaceship_3;

    private static BufferedImage galaxy_1;
    private static BufferedImage galaxy_2;
    private static BufferedImage galaxy_3;

    private static BufferedImage planet_1;
    private static BufferedImage planet_2;
    private static BufferedImage planet_3;
    private static BufferedImage planet_4;
    private static BufferedImage planet_5;
    private static BufferedImage planet_6;

    private static BufferedImage asteroid_1;
    private static BufferedImage asteroid_2;
    private static BufferedImage asteroid_3;
    private static BufferedImage asteroid_4;
    private static BufferedImage asteroid_5;
    private static BufferedImage asteroid_6;

    private static BufferedImage background_1;
    private static BufferedImage background_2;
    private static BufferedImage background_3;

    private static BufferedImage stars_1;
    private static BufferedImage stars_2;
    private static BufferedImage stars_3;

    private static BufferedImage crack_1;
    private static BufferedImage crack_2;
    private static BufferedImage crack_3;

    private static boolean imagesLoaded = false;


    /**
     * STATICS
     */
    public static BufferedImage getAsteroid(int number) {
        if (!Images.imagesLoaded) {
            Images.loadAllImages();
        }

        switch (number) {
            case 1:
                return Images.asteroid_1;
            case 2:
                return Images.asteroid_2;
            case 3:
                return Images.asteroid_3;
            case 4:
                return Images.asteroid_4;
            case 5:
                return Images.asteroid_5;
            case 6:
                return Images.asteroid_6;
        }

        return Images.asteroid_1;
    }


    public static BufferedImage getCrack(int number) {
        if (!Images.imagesLoaded) {
            Images.loadAllImages();
        }

        switch (number) {
            case 1:
                return Images.crack_1;
            case 2:
                return Images.crack_2;
            case 3:
                return Images.crack_3;
        }

        return Images.crack_1;
    }


    public static BufferedImage getStarship(int number) {
        if (!Images.imagesLoaded) {
            Images.loadAllImages();
        }

        switch (number) {
            case 1:
                return Images.spaceship_1;
            case 2:
                return Images.spaceship_2;
            case 3:
                return Images.spaceship_3;
        }

        return Images.spaceship_1;
    }


    public static BufferedImage getPlanet(int number) {
        if (!Images.imagesLoaded) {
            Images.loadAllImages();
        }

        switch (number) {
            case 1:
                return Images.planet_1;
            case 2:
                return Images.planet_2;
            case 3:
                return Images.planet_3;
            case 4:
                return Images.planet_4;
            case 5:
                return Images.planet_5;
            case 6:
                return Images.planet_6;
        }

        return Images.planet_3;
    }


    public static BufferedImage getBackground(int number) {
        if (!Images.imagesLoaded) {
            Images.loadAllImages();
        }

        switch (number) {
            case 1:
                return Images.background_1;
            case 2:
                return Images.background_2;
            case 3:
                return Images.background_3;
        }

        return Images.background_1;
    }


    public static void loadAllImages() {
        if (Images.imagesLoaded) {
            return;
        }

        String assetsPath = "src/tg/images/assets/";

        Images.asteroid_1 = Images.loadImage(assetsPath + "asteroid-1-mini.png");
        Images.asteroid_2 = Images.loadImage(assetsPath + "asteroid-2-mini.png");
        Images.asteroid_3 = Images.loadImage(assetsPath + "asteroid-3-mini.png");
        Images.asteroid_4 = Images.loadImage(assetsPath + "asteroid-4-mini.png");
        Images.asteroid_5 = Images.loadImage(assetsPath + "asteroid-5-mini.png");
        Images.asteroid_6 = Images.loadImage(assetsPath + "asteroid-6-mini.png");

        Images.galaxy_1 = Images.loadImage(assetsPath + "galaxy-1.png");
        Images.galaxy_2 = Images.loadImage(assetsPath + "galaxy-2.png");
        Images.galaxy_3 = Images.loadImage(assetsPath + "galaxy-3.png");

        Images.spaceship_1 = Images.loadImage(assetsPath + "spaceship-1.png");
        Images.spaceship_2 = Images.loadImage(assetsPath + "spaceship-2.png");
        Images.spaceship_3 = Images.loadImage(assetsPath + "spaceship-3.png");

        Images.planet_1 = Images.loadImage(assetsPath + "planet-1.png");
        Images.planet_2 = Images.loadImage(assetsPath + "planet-2.png");
        Images.planet_3 = Images.loadImage(assetsPath + "planet-3.png");
        Images.planet_4 = Images.loadImage(assetsPath + "planet-4.png");
        Images.planet_5 = Images.loadImage(assetsPath + "planet-5.png");
        Images.planet_6 = Images.loadImage(assetsPath + "planet-6.png");

        Images.background_1 = Images.loadImage(assetsPath + "background-1.png");
        Images.background_2 = Images.loadImage(assetsPath + "background-2.jpeg");

        Images.imagesLoaded = true;

        System.out.println("All images loaded!");
    }


    private static BufferedImage loadImage(String fileName) {
        BufferedImage img;

        img = null;
        try {
            img = ImageIO.read(new File(fileName));
        } catch (IOException e) {
            System.err.println("Load image error · <Images> · [" + fileName + "] · " + e.getMessage());
            img = null;
        }

        return img;
    }
}
