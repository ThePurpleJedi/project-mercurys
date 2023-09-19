package com.mercurys.handlersandwrappers;

import com.mercurys.encryption.Encryption;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.MessageFormat;
import java.util.Scanner;

public class MessageHandler {
    private final Scanner scanner;
    private final Encryption encryption = new Encryption();
    private final PrintWriter writer;
    private final DataOutputStream outputStream;

    private String receiverName = "";

    public MessageHandler(Scanner scanner, PrintWriter writer, DataOutputStream outputStream) {
        this.scanner = scanner;
        this.writer = writer;
        this.outputStream = outputStream;
    }

    public void sendMessagesToOtherUser() throws IOException {
        String outGoingLine;
        while (!(outGoingLine = scanner.nextLine()).equals("-x-")) {
            sendMessage(outGoingLine);
        }
        writer.println(new Encryption().encrypt("Connection closed by server."));
    }

    public void sendMessage(String outGoingLine) throws IOException {
        if (outGoingLine.startsWith("/")) {
            switch (outGoingLine.split(" +")[0]) {
                case "/image" -> this.handleImageUpload(outGoingLine.substring(7));
                case "/pdf" -> this.handlePDFUpload(outGoingLine.substring(5));
                default -> writeMessage("Error! -x-");
            }
        } else
            writeMessage(outGoingLine);
    }

    private void writeMessage(String outGoingLine) {
        writer.println(encryption.encrypt(
                MessageFormat.format("{0}: {1}", receiverName, outGoingLine)));
    }

    private void handlePDFUpload(String pdfFileName) {
        PDFHandler pdfHandler = new PDFHandler(outputStream);
        writer.println("/pdf");
        pdfHandler.sendPDFFile(pdfFileName);
        System.out.println("[M. Console]: PDF sent!");
    }

    private void handleImageUpload(final String imageFileName) throws IOException {
        ImageHandler imageHandler = new ImageHandler(outputStream);
        writer.println("/image");
        imageHandler.sendImageFile(imageFileName);
        System.out.println("[M. Console]: Image sent!");
    }

    public void setReceiverName(String name) {
        this.receiverName = name;
    }
}
