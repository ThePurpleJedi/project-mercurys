package com.mercurys.unfinished;

import java.util.Scanner;

public class DownloadTester {

    public static void main(final String[] args) {
        final Scanner scanner = new Scanner(System.in);
        final String inBoundLine = scanner.nextLine();
        final Imagician imagician = new Imagician();
        final int i = 1;
        if (inBoundLine.startsWith("/image ")) {
            System.out.println("Note: The user is attempting to send you an image file. " +
                    "Do you wish to download it? [Y/n]");
            final String perm = scanner.next();
            if (perm.equalsIgnoreCase("Y")) {
                final String s = System.getProperty("file.separator");
                final String downloadPath = System.getProperty("user.home") + s + "ReceivedImage" + i + ".jpg";
                imagician.convertTextToImage(inBoundLine.split(" +")[1], downloadPath);
                System.out.println("The file has been downloaded in the " + downloadPath + " folder");
            }
        }
    }
}
