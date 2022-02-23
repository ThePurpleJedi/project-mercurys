package com.mercurys.handlers;

import com.mercurys.readers.MediaReaderThread;

import java.io.*;
import java.net.*;
import java.util.Scanner;

public class ServerSocketHandler {

    private final ServerSocket serverSocket;
    private Socket socket;
    private DataOutputStream outputStream;
    private MediaReaderThread incomingReaderThread;
    private MessageHandler messageHandler;

    public ServerSocketHandler(String hostIPAddress, int serverPort) throws IOException {
        serverSocket = new ServerSocket();
        initialiseServerSocket(hostIPAddress, serverPort);
        acceptClientRequest();
        initialiseIO();
    }

    private void initialiseServerSocket(String hostAddress, int serverPort) throws IOException {
        serverSocket.bind(new InetSocketAddress(hostAddress, serverPort));
        System.out.println("""
                Server initialised.
                Waiting for client...""");
    }

    private void acceptClientRequest() throws IOException {
        this.socket = serverSocket.accept();
        System.out.println("Client accepted!");
    }

    private void initialiseIO() throws IOException {
        this.outputStream = new DataOutputStream(this.socket.getOutputStream());
        PrintWriter writer = new PrintWriter(this.outputStream, true);
        messageHandler = new MessageHandler(new Scanner(System.in), writer, outputStream);
    }

    public void talkToClient() throws IOException {
        initialiseReaderThread();
        messageHandler.sendMessagesToOtherUser();
        closeConnection();
    }

    private void initialiseReaderThread() throws IOException {
        incomingReaderThread = new MediaReaderThread(this.socket.getInputStream(),
                "[User01]");
        incomingReaderThread.start();
    }

    private void closeConnection() throws IOException {
        incomingReaderThread.exitThread();
        this.socket.close();
        this.outputStream.close();
        System.out.println("Closing connection... Goodbye!");
    }
}
