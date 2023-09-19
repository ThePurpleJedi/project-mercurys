package com.mercurys.handlersandwrappers;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class PDFHandler {

    private DataInputStream inputStream;
    private DataOutputStream outputStream;
    private String pdfDownloadPath;

    public PDFHandler(DataOutputStream outputStream) {
        this.outputStream = outputStream;
    }

    public PDFHandler(DataInputStream inputStream) {
        this.inputStream = inputStream;
    }

    public static void main(String[] args) {
        try {
            PDFHandler printerOfPDFs = new PDFHandler(new DataOutputStream(new FileOutputStream(
                    "/home/raghav/Documents/Java/Testing.txt")));
            PDFHandler downloaderOfPDFS = new PDFHandler(new DataInputStream(new FileInputStream(
                    "/home/raghav/Documents/Java/Testing.txt")));

            printerOfPDFs.sendPDFFile(
                    "/home/raghav/Documents/Books/Non Fiction/Plane-Trigonometry-Part-I.pdf");
            System.out.println("PDF read and printed to output");
            downloaderOfPDFS.downloadPDF();
            System.out.println("PDF downloaded");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            System.exit(0);
        }
    }

    public void sendPDFFile(final String pdfFileName) {
        try {
            this.outputStream.writeInt(getPDFAsByteArray(pdfFileName).length);
            this.outputStream.write(getPDFAsByteArray(pdfFileName));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private byte[] getPDFAsByteArray(String fileName) throws IOException {
        Path pathToPDF = Paths.get(fileName);
        return Files.readAllBytes(pathToPDF);
    }

    public void downloadPDF() {
        try {
            byte[] fileToDownload = readPDFByteArray();
            pdfDownloadPath = setDownloadLocation();
            writePDFToDownloadLocation(fileToDownload);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private byte[] readPDFByteArray() throws IOException {
        byte[] pdfAsBytes = new byte[inputStream.readInt()];
        inputStream.readFully(pdfAsBytes);
        return pdfAsBytes;
    }

    private void writePDFToDownloadLocation(byte[] fileAsByteArray) throws IOException {
        createDirectoriesToDownloadLocation();
        Path pathToFile = Paths.get(pdfDownloadPath);
        Files.write(pathToFile, fileAsByteArray);
    }

    private void createDirectoriesToDownloadLocation() throws IOException {
        String s = System.getProperty("file.separator");
        Files.createDirectories(Paths.get(
                pdfDownloadPath.substring(0, pdfDownloadPath.lastIndexOf(s)) + s));
    }

    private String setDownloadLocation() {
        final String s = System.getProperty("file.separator");
        return MessageFormat.format("{0}{1}Documents{1}Mercurys{1}ReceivedFile{2}.pdf",
                System.getProperty("user.home"), s, getFormattedSimpleDate());
    }

    private String getFormattedSimpleDate() {
        return new SimpleDateFormat("ddMMyyyy-HHmmss").format(Calendar.getInstance().getTime());
    }

    public String getPdfDownloadPath() {
        return pdfDownloadPath;
    }
}