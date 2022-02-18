package com.mercurys.unfinished;

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

    public ImageHandler() {
    }

    public void sendImageFileAsByteStream(final String imageFileName) throws IOException {
        final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        final BufferedImage bufferedImage = ImageIO.read(new File(imageFileName));
        ImageIO.write(bufferedImage, "png", byteArrayOutputStream);

        final byte[] size = ByteBuffer.allocate(4).putInt(byteArrayOutputStream.size()).array();

        this.outputStream.write(size);
        this.outputStream.write(byteArrayOutputStream.toByteArray());
    }

    public BufferedImage getImageAsBufferedImage() {
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

    public void downloadBufferedImage(final BufferedImage image) {
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
            this.imageDownloadPath = downloadPath;
        } catch (final IOException e) {
            e.printStackTrace();
        }
    }

    public String getImageDownloadPath() {
        return imageDownloadPath;
    }
}
