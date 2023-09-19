package com.mercurys.handlersandwrappers;

import com.mercurys.readers.MediaReaderThread;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

public class ServerSocketHandler {

    private final ServerSocket serverSocket;
    private final DeviceWrapper serverDeviceWrapper;
    private Socket socket;
    private DataOutputStream outputStream;
    private MediaReaderThread incomingReaderThread;
    private MessageHandler messageHandler;

    public ServerSocketHandler(DeviceWrapper serverDeviceWrapper) throws IOException {
        this.serverSocket = new ServerSocket();
        this.serverDeviceWrapper = serverDeviceWrapper;
        bindServerSocket();
    }

    private void bindServerSocket() throws IOException {
        serverSocket.bind(new InetSocketAddress(serverDeviceWrapper.getDeviceIP(),
                serverDeviceWrapper.getDevicePort()));
        System.out.println("Server initialised, waiting for client...");
    }

    public void talk() throws IOException {
        initialiseSocketParameters();
        talkToClient();
    }

    private void initialiseSocketParameters() throws IOException {
        acceptClientRequest();
        initialiseIO();
    }

    private void acceptClientRequest() throws IOException {
        this.socket = serverSocket.accept();
        System.out.println("Client accepted!");
    }

    private void initialiseIO() throws IOException {
        this.outputStream = new DataOutputStream(this.socket.getOutputStream());
        PrintWriter writer = new PrintWriter(this.outputStream, true);
        messageHandler = new MessageHandler(new Scanner(System.in), writer, outputStream);
        messageHandler.setReceiverName(serverDeviceWrapper.getDeviceName());
    }

    public void talkToClient() throws IOException {
        initialiseReaderThread();
        messageHandler.sendMessagesToOtherUser();
        closeConnection();
    }

    private void initialiseReaderThread() throws IOException {
        incomingReaderThread = new MediaReaderThread(this.socket.getInputStream());
        incomingReaderThread.start();
    }

    private void closeConnection() throws IOException {
        incomingReaderThread.exitThread();
        this.socket.close();
        this.outputStream.close();
        System.out.println("Closing connection... Goodbye!");
    }
}
