package com.mercurys.almostfinished;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.ByteBuffer;
import java.nio.file.*;
import java.text.*;
import java.util.Calendar;

public class ImageHandler {

    DataOutputStream outputStream;
    DataInputStream inputStream;
    String imageDownloadPath;

    public ImageHandler(DataOutputStream outputStream) {
        this.outputStream = outputStream;
    }

    public ImageHandler(DataInputStream inputStream) {
        this.inputStream = inputStream;
    }

    public void sendImageFileAsByteStream(final String imageFileName) throws IOException {
        final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        final BufferedImage bufferedImage = ImageIO.read(new File(imageFileName));

        writeImageToOutputStream(byteArrayOutputStream, bufferedImage);
    }

    private void writeImageToOutputStream(ByteArrayOutputStream byteArrayOutputStream, BufferedImage bufferedImage) throws IOException {
        ImageIO.write(bufferedImage, "png", byteArrayOutputStream);
        final byte[] size = ByteBuffer.allocate(4).putInt(byteArrayOutputStream.size()).array();
        this.outputStream.write(size);
        this.outputStream.write(byteArrayOutputStream.toByteArray());
    }

    public BufferedImage getImageAsBufferedImage() {
        try {
            return ImageIO.read(new ByteArrayInputStream(getImageAsBytes()));
        } catch (final IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    private byte[] getImageAsBytes() throws IOException {
        return readImageSize(ByteBuffer.wrap(readImageSize(4)).asIntBuffer().get());
    }

    private byte[] readImageSize(int x) throws IOException {
        final byte[] imgSize = new byte[x];
        this.inputStream.readFully(imgSize);
        return imgSize;
    }

    public void downloadBufferedImage(final BufferedImage image) {
        if (image == null) {
            return;
        }
        setDownloadPath();
        writeImageToDownloadLocation(image);
    }

    private void writeImageToDownloadLocation(BufferedImage image) {
        try {
            createDirectoriesToDownloadLocation();
            ImageIO.write(image, "png", new File(imageDownloadPath));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void createDirectoriesToDownloadLocation() throws IOException {
        String s = System.getProperty("file.separator");
        Files.createDirectories(Paths.get(
                imageDownloadPath.substring(0, imageDownloadPath.lastIndexOf(s)) + s));
    }

    private void setDownloadPath() {
        final String s = System.getProperty("file.separator");
        imageDownloadPath =
                MessageFormat.format("{0}{1}Pictures{1}Mercurys{1}{3}{1}ReceivedImage{2}.png",
                        System.getProperty("user.home"), s, getFormattedSimpleDate("HHmmss"),
                        getFormattedSimpleDate("dd.MM.yyyy"));
    }

    private String getFormattedSimpleDate(String format) {
        return new SimpleDateFormat(format)
                .format(Calendar.getInstance().getTime());
    }

    public String getImageDownloadPath() {
        return imageDownloadPath;
    }
}
