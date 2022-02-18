package com.mercurys.threads;

import com.mercurys.encryption.*;
import com.mercurys.unfinished.ImageHandler;

import java.awt.image.BufferedImage;
import java.io.*;
import java.net.*;

public class ReadMediaThread extends Thread {

    private final Decryption decryption = new Decryption();
    private final BufferedReader reader;
    private final String sentBy;
    private DataInputStream inputStream;
    private boolean exit;


    public ReadMediaThread(final Socket socket, final String sentBy) throws IOException {
        if (!socket.isClosed()) {
            this.inputStream = new DataInputStream(socket.getInputStream());
        }
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
            this.readAndPrintMessages(reader);
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

    private void readAndPrintMessages(final BufferedReader reader) throws IOException {
        String inBoundLine;
        while (!(inBoundLine = reader.readLine()).equals("-x-")) {
            if (isImage(inBoundLine)) {
                downloadIncomingImage();
            } else if (isValidTextMessage(inBoundLine)) {
                System.out.println(this.sentBy + ": " + this.decryption.decrypt(inBoundLine));
            }
        }
    }

    private boolean isImage(String inBoundLine) {
        return decryption.decrypt(inBoundLine).startsWith("/image");
    }

    private boolean isValidTextMessage(String inBoundLine) {
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