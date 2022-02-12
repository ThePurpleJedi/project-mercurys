package com.mercurys.threads;

import com.mercurys.encryption.Decryption;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.*;
import java.nio.ByteBuffer;
import java.nio.file.*;
import java.text.*;
import java.util.Calendar;

public class ReadMediaThread extends Thread {

    private final Decryption decryption = new Decryption();
    private final BufferedReader reader;
    private final String sentBy;
    private DataInputStream inputStream;
    private boolean exit;


    public ReadMediaThread(final Socket socket, final String sentBy) throws IOException {
        if (!socket.isClosed()) {
            this.inputStream = new DataInputStream(socket.getInputStream());
        }
        reader = new BufferedReader(new InputStreamReader(this.inputStream));
        this.sentBy = sentBy;
        this.exit = false;
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
            } else if (decryption.decrypt(inBoundLine).startsWith("/image")) {
                final BufferedImage image = this.getImageAsBufferedImage();
                this.downloadBufferedImage(image);
            } else if (decryption.getKeyFromMessage(inBoundLine.split(" +")).length() == 88) {
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
                MessageFormat.format("{0}{1}Pictures{1}Mercurys{1}{3}{1}ReceivedImage{2}.png",
                        System.getProperty("user.home"), s, new SimpleDateFormat("HHmmss")
                                .format(Calendar.getInstance().getTime()), new SimpleDateFormat("dd.MM.yyyy")
                                .format(Calendar.getInstance().getTime()));
        try {
            Files.createDirectories(Paths.get(downloadPath.substring(0, downloadPath.lastIndexOf(s)) + s));
            ImageIO.write(image, "png", new File(downloadPath));
        } catch (final IOException e) {
            e.printStackTrace();
        }
        System.out.println("[M. Console]: The image file has been downloaded as " + downloadPath);
    }

    private BufferedImage getImageAsBufferedImage() {
        System.out.println("[M. Console]: Attention! " +
                "The user at the other end is attempting to send you an image file.");
        System.out.println("[M. Console]: Reading image file...");
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
    }

    public void exitThread() {
        this.exit = true;
        this.interrupt();
    }
}