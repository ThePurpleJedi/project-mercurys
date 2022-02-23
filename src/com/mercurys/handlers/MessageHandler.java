package com.mercurys.handlers;

import com.mercurys.encryption.Encryption;

import java.io.*;
import java.util.Scanner;

public class MessageHandler {
    private final Scanner scanner;
    private final Encryption encryption = new Encryption();
    private final PrintWriter writer;
    private final DataOutputStream outputStream;

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
        if (outGoingLine.startsWith("/image")) {
            this.handleImageUpload(outGoingLine.substring(7));
        } else {
            writer.println(encryption.encrypt(outGoingLine));
        }
    }

    private void handleImageUpload(final String imageFileName) throws IOException {
        ImageHandler imageHandler = new ImageHandler(outputStream);
        writer.println(new Encryption().encrypt("/image incoming!"));
        imageHandler.sendImageFileAsByteStream(imageFileName);
        System.out.println("[M. Console]: Image sent!");
    }
}
