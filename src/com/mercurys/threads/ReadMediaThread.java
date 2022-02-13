package com.mercurys.threads;

import com.mercurys.encryption.Decryption;
import com.mercurys.unfinished.MImageHandler;

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
                this.interrupt();
            }
            this.readAndPrintMessages(reader);
            this.exitThread();

        } catch (final SocketException s) {
            System.out.println("Socket is closed");
            System.exit(0);
        } catch (final IOException e) {
            System.out.println("Interrupted sleep");
        }
    }

    private void readAndPrintMessages(final BufferedReader reader) throws IOException {
        String inBoundLine = "";
        while (!inBoundLine.equals("-x-")) {
            inBoundLine = reader.readLine();
            if (inBoundLine == null) {
                break;
            } else if (decryption.decrypt(inBoundLine).startsWith("/image")) {
                downloadIncomingImage();
            } else if (decryption.getKeyFromMessage(inBoundLine.split(" +")).length() == 88) {
                System.out.println(this.sentBy + ": " + this.decryption.decrypt(inBoundLine));
            }
        }
    }

    private void downloadIncomingImage() {
        MImageHandler imageHandler = new MImageHandler(inputStream);
        System.out.println("[M. Console]: Attention! " +
                "The user at the other end is attempting to send you an image file.");
        System.out.println("[M. Console]: Reading image file...");
        final BufferedImage image = imageHandler.getImageAsBufferedImage();
        imageHandler.downloadBufferedImage(image);
        System.out.println("[M. Console]: The image file has been downloaded as "
                + imageHandler.getImageDownloadPath());
    }

    public void exitThread() {
        this.exit = true;
        this.interrupt();
    }
}