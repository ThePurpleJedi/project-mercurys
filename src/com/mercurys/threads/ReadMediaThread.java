package com.mercurys.threads;

import com.mercurys.almostfinished.ImageHandler;
import com.mercurys.encryption.*;

import java.awt.image.BufferedImage;
import java.io.*;
import java.net.SocketException;

public class ReadMediaThread extends Thread {

    private final Decryption decryption = new Decryption();
    private final BufferedReader reader;
    private final String sentBy;
    private final DataInputStream inputStream;
    private boolean exit;


    public ReadMediaThread(final InputStream inputStream, final String sentBy) {
        this.inputStream = new DataInputStream(inputStream);
        reader = new BufferedReader(new InputStreamReader(this.inputStream));
        this.sentBy = sentBy;
        this.exit = false;
    }

    @Override
    public void run() {
        try {
            if (this.exit) {
                exitThread();
            }
            this.readAndPrintMessages();
        } catch (final SocketException s) {
            System.out.println("Socket is closed");
            System.exit(0);
        } catch (final IOException e) {
            System.out.println("Exception Occurred!");
            e.printStackTrace();
        } finally {
            this.exitThread();
        }
    }

    private void readAndPrintMessages() throws IOException {
        String inBoundLine;
        while ((inBoundLine = reader.readLine()) != null) {
            if (inBoundLine.equals("-x-")) return;
            if (isImage(inBoundLine)) {
                downloadIncomingImage();
            } else if (isValidEncryptedTextMessage(inBoundLine)) {
                System.out.println(this.sentBy + ": " + decryption.decrypt(inBoundLine));
            } else {
                System.out.println("Invalid Message Format");
            }
        }
    }

    private boolean isImage(String inBoundLine) {
        return decryption.decrypt(inBoundLine).startsWith("/image");
    }

    private boolean isValidEncryptedTextMessage(String inBoundLine) {
        return new Key(88).getKeyFromMessage(inBoundLine.split(" +")).length() == 88;
    }

    private void downloadIncomingImage() {
        ImageHandler imageHandler = new ImageHandler(inputStream);
        notifyUserOfImageReading();
        final BufferedImage image = imageHandler.getImageAsBufferedImage();
        imageHandler.downloadBufferedImage(image);
        System.out.println("[M. Console]: The image file has been downloaded as "
                + imageHandler.getImageDownloadPath());
    }

    private void notifyUserOfImageReading() {
        System.out.println("[M. Console]: Attention! " +
                "The user at the other end is attempting to send you an image file.");
        System.out.println("[M. Console]: Reading image file...");
    }

    public void exitThread() {
        this.exit = true;
        this.interrupt();
    }
}