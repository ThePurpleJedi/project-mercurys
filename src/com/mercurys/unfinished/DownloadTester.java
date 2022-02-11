package com.mercurys.unfinished;

import com.mercurys.encryption.Encryption;

import java.io.*;
import java.util.Scanner;

public class DownloadTester {

    public static void main(final String[] args) {
        DownloadTester.doStuff();
    }

    private static void doStuff() {
        final Encryption encryption = new Encryption();
        final PrintWriter writer = new PrintWriter(System.out, true);
        final Scanner scanner = new Scanner(System.in);
        String outGoingLine = scanner.nextLine();

        while (!outGoingLine.equals("-x-")) {
            if (outGoingLine.startsWith("/image")) {
                writer.println("/image " + DownloadTester.getImageAsText(outGoingLine.split(" +")[1]));
                System.out.println("Sending image...");
                continue;
            }
            writer.println(encryption.encrypt(outGoingLine));
            outGoingLine = scanner.nextLine();
        }
        writer.println(encryption.encrypt("Connection closed by server."));
    }

    private static String getImageAsText(final String inputFileName) {
        final Imagician imagician = new Imagician();
        final String textFileName = "/home/raghav/IdeaProjects/project-mercurys/src/com/mercurys/unfinished" +
                "/ImagePixels";
        final StringBuilder imageAsText = new StringBuilder();
        imagician.convertImageToText(inputFileName, textFileName);

        try (final Scanner scanner = new Scanner(new File(textFileName))) {
            imageAsText.append(scanner.nextLine());
            while (scanner.hasNextInt()) {
                imageAsText.append(scanner.nextInt()).append(" ");
            }
            return imageAsText.toString();
        } catch (final FileNotFoundException e) {
            return new Encryption().encrypt("Error!");
        }
    }
}
///home/raghav/IdeaProjects/project-mercurys/src/com/mercurys/unfinished/ImagePixels