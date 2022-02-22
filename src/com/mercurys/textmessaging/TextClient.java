package com.mercurys.textmessaging;

import com.mercurys.encryption.Encryption;
import com.mercurys.readers.TextReaderThread;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class TextClient {

    private Socket socket;
    private DataOutputStream outputStream;
    private PrintWriter writer;
    private Scanner scanner;

    private TextClient(final String serverAddress, final int serverPort) {
        try {
            connectToServer(serverAddress, serverPort);
            initialiseIO();
        } catch (final IOException u) {
            System.out.println("Woops!");
            u.printStackTrace();
        }
    }

    private void initialiseIO() throws IOException {
        this.outputStream = new DataOutputStream(this.socket.getOutputStream());
        this.writer = new PrintWriter(this.outputStream, true);
        scanner = new Scanner(System.in);
    }

    private void connectToServer(String address, int port) throws IOException {
        this.socket = new Socket(address, port);
        System.out.println("Connected to server!" + this.socket.getRemoteSocketAddress());
    }

    public static void main(final String[] args) {
        final TextClient client = new TextClient(getHostAddress(), 4444);
        client.talk();
        System.out.println("Thank you for using Project Mercurys!");
    }

    private static String getHostAddress() {
        final Scanner sc = new Scanner(System.in);
        System.out.println("Enter server host IP address or press 1 for default address:");
        final String hostAddress = sc.next();
        return (hostAddress.equals("1"))? "192.168.0.151" : hostAddress;
    }

    private void talk() {
        try {
            final TextReaderThread readThread = initialiseAndGetReadThread();
            this.writeToServer();
            this.closeConnection(readThread);
        } catch (final IOException e) {
            e.printStackTrace();
        }
    }

    private TextReaderThread initialiseAndGetReadThread() throws IOException {
        final TextReaderThread readThread = new TextReaderThread(this.socket,
                "[MHost" + this.socket.getLocalAddress().toString() + "]");
        readThread.start();
        return readThread;
    }

    private void writeToServer() {
        final Encryption encryption = new Encryption();
        encryptAndPrintMessagesToServer(encryption);
        writer.println(encryption.encrypt("Connection closed by client."));
    }

    private void encryptAndPrintMessagesToServer(Encryption encryption) {
        String outGoingLine;
        while (!(outGoingLine = scanner.nextLine()).equals("-x-")) {
            writer.println(encryption.encrypt(outGoingLine));
        }
    }

    private void closeConnection(final TextReaderThread readThread) throws IOException {
        readThread.exitThread();
        this.socket.close();
        this.outputStream.close();
        System.out.println("Closing connection... Goodbye!");
    }
}