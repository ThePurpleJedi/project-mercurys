package com.mercurys.unfinished;

import com.mercurys.encryption.Encryption;
import com.mercurys.threads.ReadMediaThread;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.*;
import java.nio.ByteBuffer;
import java.util.Scanner;

public class MediaServer {

    private Socket socket;
    private DataOutputStream outputStream;

    private MediaServer(final int port) {
        try (final ServerSocket serverSocket = new ServerSocket()) {
            serverSocket.bind(new InetSocketAddress("localhost", port));
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
        mediaServer.talk();

        System.out.println("Thank you for using Project Mercurys!");
    }

    private void talk() {
        try {
            final ReadMediaThread readMediaThread = new ReadMediaThread(this.socket,
                    "client1" + this.socket.getLocalAddress().toString());

            readMediaThread.start();
            this.writeToClient();

            this.closeConnection(readMediaThread);
        } catch (final IOException e) {
            e.printStackTrace();
        }
    }

    private void writeToClient() {
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
        try {
            writer.println(new Encryption().encrypt("/image incoming!"));
            this.sendImageFileAsByteStream(outGoingLine);
            System.out.println("[Mercurys:] Image sent!");
        } catch (final IOException e) {
            System.out.println("Exception Occurred!");
            e.printStackTrace();
        }
    }

    private void sendImageFileAsByteStream(final String imageFileName) throws IOException {
        final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        final BufferedImage bufferedImage = ImageIO.read(new File(imageFileName));
        ImageIO.write(bufferedImage, "png", byteArrayOutputStream);

        final byte[] size = ByteBuffer.allocate(4).putInt(byteArrayOutputStream.size()).array();

        this.outputStream.write(size);
        this.outputStream.write(byteArrayOutputStream.toByteArray());
    }

    private void closeConnection(final ReadMediaThread readMediaThread) throws IOException {
        readMediaThread.exitThread();
        this.socket.close();
        this.outputStream.close();
        System.out.println("Closing connection... Goodbye!");
    }
}
