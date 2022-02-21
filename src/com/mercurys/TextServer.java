package com.mercurys;

import com.mercurys.encryption.Encryption;
import com.mercurys.readers.TextReaderThread;

import java.io.*;
import java.net.*;
import java.util.Scanner;

public class TextServer {

    private ServerSocket serverSocket;
    private Socket socket;
    private Scanner scanner;
    private DataOutputStream outputStream;
    private PrintWriter writer;

    private TextServer(final int port) {
        try {
            serverSocket = new ServerSocket();
            initialiseServerSocket(port);
            acceptClient();
            initialiseInputOutputObjects();

        } catch (final IOException e) {
            System.out.println("Woops!");
            e.printStackTrace();
        }
    }

    private void initialiseInputOutputObjects() throws IOException {
        this.outputStream = new DataOutputStream(this.socket.getOutputStream());
        this.writer = new PrintWriter(outputStream, true);
        this.scanner = new Scanner(System.in);
    }

    private void acceptClient() throws IOException {
        this.socket = serverSocket.accept();
        System.out.println("Client accepted!");
    }

    private void initialiseServerSocket(final int serverPort) throws IOException {
        String hostAddress = "192.168.0.151"; //replace with one's own LAN IP address
        serverSocket.bind(new InetSocketAddress(hostAddress, serverPort));
        System.out.println("""
                Server initialised.
                Waiting for client...""");
    }

    public static void main(final String[] args) {
        final TextServer textServer = new TextServer(4444);
        textServer.talk();
        System.out.println("Thank you for using Project Mercurys!");
    }

    private void talk() {
        try {
            final TextReaderThread readThread = initialiseAndGetReadThread();
            this.writeToClient();
            this.closeConnection(readThread);
        } catch (final IOException e) {
            e.printStackTrace();
        }
    }

    private TextReaderThread initialiseAndGetReadThread() throws IOException {
        final TextReaderThread readThread = new TextReaderThread(this.socket,
                "[MClient1" + this.socket.getInetAddress().toString() + "]");

        readThread.start();
        return readThread;
    }

    private void writeToClient() {
        final Encryption encryption = new Encryption();
        encryptAndPrintMessagesToClient(encryption);
        writer.println(encryption.encrypt("Connection closed by server."));
    }

    private void encryptAndPrintMessagesToClient(Encryption encryption) {
        String outGoingLine;
        while (!(outGoingLine = scanner.nextLine()).equals("-x-")) {
            writer.println(encryption.encrypt(outGoingLine));
        }
    }

    private void closeConnection(final TextReaderThread readThread) throws IOException {
        readThread.exitThread();
        this.serverSocket.close();
        this.socket.close();
        this.outputStream.close();
        System.out.println("Closing connection... Goodbye!");
    }
}