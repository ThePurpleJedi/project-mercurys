package com.mercurys.unfinished;

import com.mercurys.encryption.Encryption;
import com.mercurys.threads.ReadMediaThread;

import java.io.*;
import java.net.*;
import java.util.Scanner;

public class MediaServer {

    private ServerSocket serverSocket;
    private Socket socket;
    private DataOutputStream outputStream;
    private PrintWriter writer;
    private Scanner scanner;


    private MediaServer(final int port) {
        try {
            serverSocket = new ServerSocket();
            initialiseServerSocket(port);
            acceptClientRequest();
            initialiseNecessaryClasses();

        } catch (final IOException e) {
            System.out.println("Exception occurred!");
            e.printStackTrace();
        }
    }

    private void initialiseNecessaryClasses() throws IOException {
        this.outputStream = new DataOutputStream(this.socket.getOutputStream());
        this.writer = new PrintWriter(this.outputStream, true);
        this.scanner = new Scanner(System.in);
    }

    private void acceptClientRequest() throws IOException {
        this.socket = serverSocket.accept();
        System.out.println("Client accepted!");
    }

    private void initialiseServerSocket(int serverPort) throws IOException {
        String hostAddress = "192.168.0.151"; //replace with one's own LAN IP address
        serverSocket.bind(new InetSocketAddress(hostAddress, serverPort));
        System.out.println("""
                Server initiated.
                Waiting for client...""");
    }

    public static void main(final String[] args) {
        final MediaServer mediaServer = new MediaServer(4444);
        mediaServer.startTalking();
        System.out.println("Thank you for using Project Mercurys!");
    }

    private void startTalking() {
        try {
            final ReadMediaThread incomingReaderThread = initialiseReaderThread();
            sendMessages();
            closeConnection(incomingReaderThread);
        } catch (final IOException e) {
            e.printStackTrace();
        }
    }

    private ReadMediaThread initialiseReaderThread() throws IOException {
        final ReadMediaThread incomingReaderThread = new ReadMediaThread(this.socket,
                "M_Client");
        incomingReaderThread.start();
        return incomingReaderThread;
    }

    private void sendMessages() {
        final Encryption encryption = new Encryption();
        String outGoingLine;
        while (!(outGoingLine = scanner.nextLine()).equals("-x-")) {
            sendMessageUsingEncryption(encryption, outGoingLine);
        }

        writer.println(encryption.encrypt("Connection closed by server."));
    }

    private void sendMessageUsingEncryption(Encryption encryption, String outGoingLine) {
        if (outGoingLine.startsWith("/image")) {
            this.handleImageUpload(writer, outGoingLine.substring(7));
        } else {
            writer.println(encryption.encrypt(outGoingLine));
        }
    }

    private void handleImageUpload(final PrintWriter writer, final String outGoingLine) {
        try {
            sendImageToClient(writer, outGoingLine);
        } catch (final IOException e) {
            System.out.println("Exception Occurred!");
            e.printStackTrace();
        }
    }

    private void sendImageToClient(PrintWriter writer, String outGoingLine) throws IOException {

        writer.println(new Encryption().encrypt("/image incoming!"));
        new ImageHandler((DataOutputStream) socket.getOutputStream()).sendImageFileAsByteStream(outGoingLine);
        System.out.println("[M_Console:] Image sent!");
    }

    private void closeConnection(final ReadMediaThread readMediaThread) throws IOException {
        readMediaThread.exitThread();
        this.socket.close();
        this.outputStream.close();
        System.out.println("Closing connection... Goodbye!");
    }
}
