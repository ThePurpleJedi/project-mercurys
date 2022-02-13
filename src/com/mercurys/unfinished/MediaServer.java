package com.mercurys.unfinished;

import com.mercurys.encryption.Encryption;
import com.mercurys.threads.ReadMediaThread;

import java.io.*;
import java.net.*;
import java.util.Scanner;

public class MediaServer {

    private Socket socket;
    private DataOutputStream outputStream;

    private MediaServer(final int port) {
        try (final ServerSocket serverSocket = new ServerSocket()) {
            String hostAddress = "192.168.0.151"; //replace with one's own LAN IP address
            serverSocket.bind(new InetSocketAddress(hostAddress, port));
            System.out.println("""
                    Server initiated.
                    Waiting for client...""");

            this.socket = serverSocket.accept();
            System.out.println("Client accepted!");
            this.outputStream = new DataOutputStream(this.socket.getOutputStream());

        } catch (final IOException e) {
            System.out.println("Woops!");
            e.printStackTrace();
        }
    }

    public static void main(final String[] args) {
        final MediaServer mediaServer = new MediaServer(4444);
        mediaServer.startTalking();

        System.out.println("Thank you for using Project Mercurys!");
    }

    private void startTalking() {
        try {
            final ReadMediaThread incomingMessageReader = new ReadMediaThread(this.socket,
                    "M_Client");

            incomingMessageReader.start();
            this.sendMessages();

            this.closeConnection(incomingMessageReader);
        } catch (final IOException e) {
            e.printStackTrace();
        }
    }

    private void sendMessages() {
        final Encryption encryption = new Encryption();
        final PrintWriter writer = new PrintWriter(this.outputStream, true);
        final Scanner scanner = new Scanner(System.in);
        String outGoingLine = scanner.nextLine();

        while (!outGoingLine.equals("-x-")) {
            if (outGoingLine.startsWith("/image")) {
                this.handleImageUpload(writer, outGoingLine.substring(7));
            } else {
                writer.println(encryption.encrypt(outGoingLine));
            }
            outGoingLine = scanner.nextLine();
        }
        writer.println(encryption.encrypt("Connection closed by server."));
    }

    private void handleImageUpload(final PrintWriter writer, final String outGoingLine) {
        MImageHandler imageHandler = new MImageHandler(outputStream);
        try {
            writer.println(new Encryption().encrypt("/image incoming!"));
            imageHandler.sendImageFileAsByteStream(outGoingLine);
            System.out.println("[M_Console:] Image sent!");
        } catch (final IOException e) {
            System.out.println("Exception Occurred!");
            e.printStackTrace();
        }
    }



    private void closeConnection(final ReadMediaThread readMediaThread) throws IOException {
        readMediaThread.exitThread();
        this.socket.close();
        this.outputStream.close();
        System.out.println("Closing connection... Goodbye!");
    }
}
