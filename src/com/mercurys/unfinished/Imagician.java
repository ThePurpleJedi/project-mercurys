package com.mercurys.unfinished;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.*;

public class Imagician {

    private BufferedImage bufferedImage;

    public static void main(final String[] args) {
        final Imagician imagician = new Imagician();
        final String textFileName = "/home/raghav/IdeaProjects/project-mercurys/src/com/mercurys/unfinished" +
                "/ImagePixels";
        final String inputFile = "/images/CAT.jpg";
        final String outputFile = "/home/raghav/IdeaProjects/project-mercurys/src/com/mercurys/unfinished/Output.png";
        imagician.convertImageToText(inputFile, textFileName);
        imagician.convertTextToImage(textFileName, outputFile);
    }

    public void convertImageToText(final String inputImageFileName, final String outputTextFileName) {
        this.bufferedImage = new BufferedImage(100, 100, 2);
        try {
            this.bufferedImage = ImageIO.read(Objects.requireNonNull(this.getClass().getResource(inputImageFileName)));
            System.setOut(new PrintStream(outputTextFileName));
            System.out.println(this.bufferedImage.getHeight() + " " + this.bufferedImage.getWidth());

            for (int i = 0; i < this.bufferedImage.getWidth(); i++) {
                for (int j = 0; j < this.bufferedImage.getHeight(); j++) {
                    System.out.print(this.bufferedImage.getRGB(i, j) + " ");
                }
            }

        } catch (final IOException f) {
            f.printStackTrace();
        }
    }

    public void convertTextToImage(final String inputTextFileName, final String outputImageFileName) {
        try (final Scanner sc = new Scanner(new File(inputTextFileName))) {

            final String[] l1 = sc.nextLine().split(" +");
            final int height = Integer.parseInt(l1[0]);
            final int width = Integer.parseInt(l1[1]);

            this.bufferedImage = new BufferedImage(width, height, 2);

            for (int i = 0; i < height * width; i++) {
                this.bufferedImage.setRGB(i / width, i % height, sc.nextInt());
            }

            final File outputImageFile = new File(outputImageFileName);
            ImageIO.write(this.bufferedImage, "png", outputImageFile);
        } catch (final IOException e) {
            e.printStackTrace();
        }
    }
}
