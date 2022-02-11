package com.mercurys.threads;

import com.mercurys.encryption.Decryption;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.*;
import java.nio.ByteBuffer;
import java.nio.file.*;
import java.text.MessageFormat;
import java.util.Scanner;

public class ReadMediaThread extends Thread {

    private final Decryption decryption = new Decryption();
    private final Scanner sc = new Scanner(System.in);
    private final BufferedReader reader;
    private final String sentBy;
    private DataInputStream inputStream;
    private boolean exit;
    private int i;

    public ReadMediaThread(final Socket socket, final String sentBy) throws IOException {
        if (!socket.isClosed()) {
            this.inputStream = new DataInputStream(socket.getInputStream());
        }
        reader = new BufferedReader(new InputStreamReader(this.inputStream));
        this.sentBy = sentBy;
        this.exit = false;
        this.i = 0;
    }

    @Override
    public void run() {
        try {
            if (this.exit) {
                this.interrupt();
            }

            this.readAndPrintMessages(reader);
            this.exitThread();

        } catch (final SocketException s) {
            System.out.println("Socket is closed");
            System.exit(0);
        } catch (final IOException e) {
            System.out.println("Interrupted sleep");
        }
    }

    private void readAndPrintMessages(final BufferedReader reader) throws IOException {
        String inBoundLine = "";
        while (!inBoundLine.equals("-x-")) {
            inBoundLine = reader.readLine();
            if (inBoundLine == null) {
                break;
            } else if (inBoundLine.startsWith("/image")) {
                final BufferedImage image = this.getImageAsBufferedImage();
                this.downloadBufferedImage(image);
                //TODO: Why is it printing the byte array
            } else {
                System.out.println(inBoundLine);
                System.out.println(this.sentBy + ": " + this.decryption.decrypt(inBoundLine));
            }
        }
    }

    private void downloadBufferedImage(final BufferedImage image) {
        if (image == null) {
            return;
        }
        final String s = System.getProperty("file.separator");
        final String downloadPath =
                MessageFormat.format("{0}{1}Pictures{1}Mercurys{1}ReceivedImage{2}.png",
                        System.getProperty("user.home"), s, this.i++);
        try {
            Files.createDirectories(Paths.get(downloadPath.substring(0, downloadPath.lastIndexOf(s)) + s));
            ImageIO.write(image, "png", new File(downloadPath));
        } catch (final IOException e) {
            e.printStackTrace();
        }
        System.out.println("The image file has been downloaded as " + downloadPath);
    }

    private BufferedImage getImageAsBufferedImage() {
        System.out.println("[Mercurys]: Attention! " +
                "The user at the other end is attempting to send you an image file. " +
                "\nDo you wish to download it? [Y/n]");

        final String perm = this.sc.next();
        if (perm.equalsIgnoreCase("Y")) {
            try {
                final byte[] imgSize = new byte[4];
                this.inputStream.readFully(imgSize);
                final byte[] imgArr = new byte[ByteBuffer.wrap(imgSize).asIntBuffer().get()];
                this.inputStream.readFully(imgArr);
                return ImageIO.read(new ByteArrayInputStream(imgArr));

            } catch (final IOException e) {
                System.out.println("Exception Occurred!");
                e.printStackTrace();
                return null;
            }
        } else {
            return null;
        }
    }

    public void exitThread() {
        this.exit = true;
        this.interrupt();
    }
}