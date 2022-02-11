package com.mercurys.unfinished;

import com.mercurys.encryption.Encryption;
import com.mercurys.threads.ReadMediaThread;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.util.Scanner;

public class MediaClient {

    private Socket socket;
    private DataOutputStream outputStream;

    private MediaClient(final String address, final int port) {
        try {
            this.socket = new Socket(address, port);
            System.out.println("Connected to server! [" + this.socket.getRemoteSocketAddress() + "]");
            this.outputStream = new DataOutputStream(this.socket.getOutputStream());

        } catch (final IOException u) {
            System.out.println("Woops!");
            u.printStackTrace();
        }
    }

    public static void main(final String[] args) {
        final MediaClient client = new MediaClient(getHostAddress(), 4444);
        client.talk();
        System.out.println("Thank you for using Project Mercurys!");
    }

    private static String getHostAddress() {
        final Scanner sc = new Scanner(System.in);
        System.out.println("Enter server host address [IP] or press 1 for localhost:");
        final String hostAddress = sc.next();
        return (hostAddress.equals("1"))? "localhost" : hostAddress;
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

