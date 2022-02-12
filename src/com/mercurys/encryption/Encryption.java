package com.mercurys.encryption;

import java.util.*;

public class Encryption {

    private final String specialChars;
    private String chars = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";

    public Encryption(final String specialChars) {
        this.specialChars = specialChars;
        this.chars = this.chars + specialChars;
    }

    public Encryption() {
        this.specialChars = "!(),–.:;?’'-*/+=<>{}[]#@^&";
        this.chars = this.chars + this.specialChars;
    }

    public String encrypt(final String msg) {

        String[] words = msg.split(" +");
        char[] key = new char[this.chars.length()];

        this.rotateFullMessageBy7(words);
        words = this.reverse(words);
        words = this.perfectSquareSwap(words);
        this.generateRandomKey(key);
        this.translateUsingKey(words, key);

        return this.getFinalEncryptedMessage(words, key);
    }

    private void rotateFullMessageBy7(String[] words) {
        for (int i = 0; i < words.length; i++) {
            final StringBuilder encodedWord = new StringBuilder();

            for (int j = 0; j < words[i].length(); j++) {
                final char c = words[i].charAt(j);
                if (this.chars.indexOf(c) != -1) {
                    encodedWord.append(this.rotateLetterBy7(c));
                } else if (c == '|') {
                    encodedWord.append(c);
                }
            }
            words[i] = encodedWord.toString();
        }
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

    private String[] reverse(String[] words) {
        final StringBuilder msg = new StringBuilder();

        for (int i = words.length - 1; i >= 0; i--) {
            words[i] = new StringBuilder(words[i]).reverse().toString();
            msg.append(words[i]).append(" ");
        }
        return msg.toString().split(" +");
    }

    private String[] perfectSquareSwap(String[] words) {
        int n = 1;
        final List<String> ls = new ArrayList<>(Arrays.asList(words));
        for (int i = 1; i < words.length; i += 2 * (n++) + 1) {
            Collections.swap(ls, i, i - 1);
        }
        return ls.toArray(words);
    }

    private void generateRandomKey(char[] key) {
        for (int i = 0; i < key.length; i++) {
            char c = (char) (92 * Math.random() + 33);
            while (String.valueOf(key).indexOf(c) != -1 || c == '|') {
                c = (char) (92 * Math.random() + 33);
            }
            key[i] = c;
        }
        System.out.println("key length = " + key.length);
    }

    private void translateUsingKey(String[] words, char[] key) {
        for (int i = 0; i < words.length; i++) {
            final char[] letters = words[i].toCharArray();

            for (int j = 0; j < letters.length; j++) {
                letters[j] = (letters[j] != '|')? key[this.chars.indexOf(letters[j])] : '|';
            }

            words[i] = String.valueOf(letters);
        }
    }

    private String getFinalEncryptedMessage(String[] words, char[] key) {
        final StringBuilder encryptedMessage = new StringBuilder();
        int fragmentNumber = 0; //goes from 1 to fragmentNumber - 2

        encryptedMessage.append(this.specialChars);
        encryptedMessage.append(" ");
        this.appendKeyFragment(key, fragmentNumber++, encryptedMessage);

        for (int i = 0; i < words.length; i++) {
            encryptedMessage.append(words[i]).append(" ");
            fragmentNumber = addKeyFragmentsInMessage(encryptedMessage, key, i, words.length, fragmentNumber);
        }
        appendKeyFragment(key, fragmentNumber, encryptedMessage);
        return encryptedMessage.toString();
    }

    private int addKeyFragmentsInMessage(StringBuilder encryptedMessage, char[] key, int i,
                                         int numberOfWords, int fragmentNumber) {

        final int numberOfFragments = key.length / 8;
        if (numberOfWords > numberOfFragments) {
            double nextFragmentPosition = Math.ceil(fragmentNumber * (double) numberOfWords / numberOfFragments);
            if (((int) nextFragmentPosition) == i && fragmentNumber < numberOfFragments - 1) {
                this.appendKeyFragment(key, fragmentNumber, encryptedMessage);
                fragmentNumber++;
            }
        } else if (numberOfWords > 1) {
            if (i == numberOfWords / 2 - 1) {
                for (; fragmentNumber < 10; fragmentNumber++) {
                    this.appendKeyFragment(key, fragmentNumber, encryptedMessage);
                }
            }
        } else {
            for (; fragmentNumber < 10; fragmentNumber++) {
                this.appendKeyFragment(key, fragmentNumber, encryptedMessage);
            }
        }
        return fragmentNumber;
    }

    private void appendKeyFragment(char[] key, final int fragmentNumber, StringBuilder encryptedMessage) {
        encryptedMessage.append("~");
        for (int j = fragmentNumber * 8; j < (fragmentNumber + 1) * 8; j++) {
            encryptedMessage.append(key[j]);
        }
        encryptedMessage.append("~ ");
    }
}