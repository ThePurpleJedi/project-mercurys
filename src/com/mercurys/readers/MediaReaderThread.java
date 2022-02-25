package com.mercurys.readers;

import com.mercurys.encryption.*;
import com.mercurys.handlers.*;

import java.awt.image.BufferedImage;
import java.io.*;
import java.net.SocketException;

public class MediaReaderThread extends Thread {

    private final Decryption decryption = new Decryption();
    private final BufferedReader reader;
    private final String sentBy;
    private final DataInputStream inputStream;
    private boolean exit;


    public MediaReaderThread(final InputStream inputStream, final String sentBy) {
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
            handleIncomingMessage(inBoundLine);
        }
    }

    private void handleIncomingMessage(String inBoundLine) {
        if (isValidEncryptedTextMessage(inBoundLine)) {
            printMessage(inBoundLine);
        } else if (inBoundLine.startsWith("/")) {
            handleFileTransferCommand(inBoundLine);
        }
    }

    private void printMessage(String inBoundLine) {
        System.out.println(this.sentBy + ": " + decryption.decrypt(inBoundLine));
    }

    private boolean isValidEncryptedTextMessage(String inBoundLine) {
        return new Key(88).getKeyFromMessage(inBoundLine.split(" +")).length() == 88;
    }

    private void handleFileTransferCommand(String inBoundLine) {
        switch (getFileType(inBoundLine)) {
            case "/image" -> this.downloadIncomingImage();
            case "/pdf" -> this.downloadIncomingPDF();
            case "Woops" -> System.out.println("woops");
            default -> System.out.println("Unknown command!");
        }
    }

    private String getFileType(String inBoundLine) {
        return (!inBoundLine.isBlank())? inBoundLine.split(" +")[0] : "Woops";
    }

    private void downloadIncomingImage() {
        notifyUserOfFileReading("image");
        ImageHandler imageHandler = new ImageHandler(inputStream);
        BufferedImage bufferedImage = imageHandler.getImageAsBufferedImage();
        imageHandler.downloadBufferedImage(bufferedImage);
        notifyUserOfDownloadLocation(imageHandler.getImageDownloadPath());
    }

    private void notifyUserOfDownloadLocation(String downloadPath) {
        System.out.println("[M. Console]: The image file has been downloaded as "
                + downloadPath);
    }

    private void notifyUserOfFileReading(String fileType) {
        System.out.println("[M. Console]: Attention! " +
                "The other user is uploading a file of type " + fileType);
        System.out.println("[M. Console]: Reading file...");
    }

    private void downloadIncomingPDF() {
        PDFHandler pdfHandler = new PDFHandler(inputStream);
        notifyUserOfFileReading("PDF");
        pdfHandler.downloadPDF();
        notifyUserOfDownloadLocation(pdfHandler.getPdfDownloadPath());
    }

    public void exitThread() {
        this.exit = true;
        this.interrupt();
    }
}