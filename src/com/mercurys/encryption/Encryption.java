package com.mercurys.encryption;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class Encryption {

    private final String specialChars;
    private String chars = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
    private final char[] key = new char[this.chars.length()];
    private String[] words;

    public Encryption(final String specialChars) {
        this.specialChars = specialChars;
        this.chars += specialChars;
    }

    public Encryption() {
        this.specialChars = "!(),–.:;?’'-*/+=<>{}[]#@^&";
        this.chars += this.specialChars;
    }

    public String encrypt(final String msg) {

        //get input
        this.words = msg.split(" +");

        //shift letters by 7 places
        //we do not rotate the special chars or digits
        this.rotateFullMessageBy7();

        //reverse letter sequence in individual words, and reverse word sequence in sentences
        this.reverse();

        //swap words at perfect square position
        this.perfectSquareSwap();

        //generate a random "key" for the message
        this.generateRandomKey();

        //translate the message using the encryption key
        this.translateUsingKey();

        //display final encrypted message, with attached encryption key in 8 letter fragments mixed into the message
        return this.getFinalEncryptedMessage();
    }

    private void rotateFullMessageBy7() {
        for (int i = 0; i < this.words.length; i++) {
            final StringBuilder encodedWord = new StringBuilder();

            for (int j = 0; j < this.words[i].length(); j++) {
                final char c = this.words[i].charAt(j);
                if (this.chars.indexOf(c) != -1) {
                    encodedWord.append(this.rotateLetterBy7(c));
                } else if (c == '|') {
                    encodedWord.append(c);
                }
            }
            this.words[i] = encodedWord.toString();
        }
    }

    private void reverse() {
        final StringBuilder msg = new StringBuilder();

        for (int i = this.words.length - 1; i >= 0; i--) {
            this.words[i] = new StringBuilder(this.words[i]).reverse().toString();
            msg.append(this.words[i]).append(" ");
        }
        this.words = msg.toString().split(" +");
    }

    private void perfectSquareSwap() {
        int n = 1;
        final List<String> ls = new ArrayList<>(Arrays.asList(this.words));
        for (int i = 1; i < this.words.length; i += 2 * (n++) + 1) {
            Collections.swap(ls, i, i - 1);
        }
        this.words = ls.toArray(this.words);
    }

    private void generateRandomKey() {
        for (int i = 0; i < this.key.length; i++) {
            char c = (char) (92 * Math.random() + 33);
            while (String.valueOf(this.key).indexOf(c) != -1 || c == '|') {
                c = (char) (92 * Math.random() + 33);
            }
            this.key[i] = c;
        }
    }

    private void translateUsingKey() {
        for (int i = 0; i < this.words.length; i++) {
            final char[] letters = this.words[i].toCharArray();

            for (int j = 0; j < letters.length; j++) {
                letters[j] = (letters[j] != '|') ? this.key[this.chars.indexOf(letters[j])] : '|';
            }

            this.words[i] = String.valueOf(letters);
        }
    }

    private String getFinalEncryptedMessage() {
        final StringBuilder encryptedMessage = new StringBuilder();
        final int fragmentLength = 8;
        final int fragmentNumber = this.key.length / 8;
        int n = 1;
        double k = Math.ceil((double) this.words.length / fragmentNumber);
        encryptedMessage.append(this.specialChars);
        encryptedMessage.append(" ~"); //print first key fragment
        for (int i = 0; i < fragmentLength; i++) {
            encryptedMessage.append(this.key[i]);
        }
        encryptedMessage.append("~ ");

        for (int i = 0; i < this.words.length; i++) {
            encryptedMessage.append(this.words[i]).append(" ");

            if (this.words.length > fragmentNumber) {
                if (((int) k) == i && n < fragmentNumber - 1) {
                    encryptedMessage.append(this.getKeyFragment(n, n + 1));
                    k += Math.round((double) this.words.length / fragmentNumber);
                    n++;
                }
            } else if (this.words.length > 1) {
                if (i == this.words.length / 2 - 1) {
                    encryptedMessage.append(this.getKeyFragment(1, 10));
                }
            } else {
                encryptedMessage.append(this.getKeyFragment(1, 10));
            }
        } //mix the key in the message

        encryptedMessage.append("~"); //print last key fragment
        for (int i = 10 * fragmentLength; i < this.key.length; i++) {
            encryptedMessage.append(this.key[i]);
        }
        encryptedMessage.append("~");

        return encryptedMessage.toString();
    }

    private char rotateLetterBy7(char c) {
        if (this.chars.indexOf(c) < 26) {

            if (c < 116) {
                c += 7;
            } else {
                c -= 19;
            }

        } else if (this.chars.indexOf(c) < 52) {

            if (c < 84) {
                c += 7;
            } else {
                c -= 19;
            }

        }

        return c;
    }

    private String getKeyFragment(final int startIndex, final int endIndex) {
        final StringBuilder fragment = new StringBuilder();
        fragment.append("~");
        for (int j = startIndex * 8; j < endIndex * 8; j++) {
            fragment.append(this.key[j]);
        }
        fragment.append("~ ");

        return fragment.toString();
    }
}