package com.mercurys.encryption;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class Encryption {

    private final String specialChars;
    private String chars = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
    private KeyWrapper encryptionKeyWrapper;
    private int numberOfWords;

    public Encryption() {
        this.specialChars = "!(),–.:;?’'-*/+=<>{}[]#@^&";
        this.chars = this.chars + this.specialChars;
    }

    public String encrypt(final String message) {
        if (message == null || message.equals("")) {
            return "";
        }
        String[] words = initialiseVariables(message);
        words = runEncryptionAlgorithm(words);
        return this.getFinalEncryptedMessage(words);
    }

    private String[] initialiseVariables(String message) {
        encryptionKeyWrapper = new KeyWrapper(chars.length());
        encryptionKeyWrapper.setRandom();
        String[] words = message.split(" +");
        numberOfWords = words.length;
        return words;
    }

    private String[] runEncryptionAlgorithm(String[] words) {
        this.rotateFullMessageBy7(words);
        words = this.reverseFullMessage(words);
        words = this.perfectSquareSwap(words);
        this.translateUsingKey(words);
        return words;
    }

    private void rotateFullMessageBy7(String[] words) {
        for (int i = 0; i < numberOfWords; i++) {
            words[i] = rotateWordBy7(words[i]);
        }
    }

    private String rotateWordBy7(String word) {
        final StringBuilder encodedWord = new StringBuilder();
        for (int j = 0; j < word.length(); j++) {
            final char c = word.charAt(j);
            if (this.chars.indexOf(c) != -1) {
                encodedWord.append(this.rotateLetterBy7(c));
            }
        }
        return encodedWord.toString();
    }

    private char rotateLetterBy7(char c) {
        if (Character.isLowerCase(c)) {
            c += (c < 116)? 7 : -19;
        } else if (Character.isUpperCase(c)) {
            c += (c < 84)? 7 : -19;
        }
        return c;
    }

    private String[] reverseFullMessage(String[] words) {
        final StringBuilder msg = new StringBuilder();
        for (int i = numberOfWords - 1; i >= 0; i--) {
            words[i] = reverseWord(words[i]);
            msg.append(words[i]).append(" ");
        }
        return msg.toString().split(" +");
    }

    private String reverseWord(String words) {
        return new StringBuilder(words).reverse().toString();
    }

    private String[] perfectSquareSwap(String[] words) {
        int n = 1;
        final List<String> ls = new ArrayList<>(Arrays.asList(words));
        for (int i = 1; i < numberOfWords; i += 2 * (n++) + 1) {
            Collections.swap(ls, i, i - 1);
        }
        return ls.toArray(words);
    }

    private void translateUsingKey(String[] words) {
        char[] key = encryptionKeyWrapper.getKeyAsCharArray();
        for (int i = 0; i < numberOfWords; i++) {
            final char[] letters = words[i].toCharArray();
            for (int j = 0; j < letters.length; j++) {
                letters[j] = (letters[j] != '|')? key[this.chars.indexOf(letters[j])] : '|';
            }
            words[i] = String.valueOf(letters);
        }
    }

    private String getFinalEncryptedMessage(String[] words) {
        return getFirstSegmentOfMessage() +
                getMessageWithInterspersedKeyFragments(words) +
                this.getKeyFragment(10);
    }

    private String getMessageWithInterspersedKeyFragments(String[] words) {
        StringBuilder encryptedMessage = new StringBuilder();
        int fragmentNumber = 1;
        for (int i = 0; i < numberOfWords; i++) {
            encryptedMessage.append(words[i]).append(" ");
            encryptedMessage.append(insertFragmentIfInsertable(fragmentNumber, i));
            fragmentNumber += (isFragmentInsertableHere(fragmentNumber, i))? 1 : 0;
        }
        return encryptedMessage.toString();
    }

    private String insertFragmentIfInsertable(int fragmentNumber, int i) {
        StringBuilder encryptedMessage = new StringBuilder();
        if (numberOfWords > encryptionKeyWrapper.getNumberOfFragments()) {
            if (isFragmentInsertableHere(fragmentNumber, i)) {
                encryptedMessage.append(this.getKeyFragment(fragmentNumber));
            }
        } else {
            encryptedMessage.append(getMiddleFragments(i));
        }
        return encryptedMessage.toString();
    }

    private boolean isFragmentInsertableHere(int fragmentNumber, int i) {
        int numberOfFragments = encryptionKeyWrapper.getNumberOfFragments();
        return (Math.rint(fragmentNumber * ((double) numberOfWords / numberOfFragments)) == i &&
                fragmentNumber < numberOfFragments - 1);
    }

    private String getMiddleFragments(int i) {
        StringBuilder encryptedMessage = new StringBuilder();
        if (numberOfWords > 1) {
            if (i == numberOfWords / 2 - 1) {
                encryptedMessage.append(getAllMiddleFragments());
            }
        } else {
            encryptedMessage.append(getAllMiddleFragments());
        }
        return encryptedMessage.toString();
    }

    private String getAllMiddleFragments() {
        StringBuilder encryptedMessage = new StringBuilder();
        for (int i = 1; i < 10; i++) {
            encryptedMessage.append(this.getKeyFragment(i));
        }
        return encryptedMessage.toString();
    }

    private String getFirstSegmentOfMessage() {
        return (this.specialChars + " " + this.getKeyFragment(0));
    }

    private String getKeyFragment(final int fragmentNumber) {
        return "~" +
                encryptionKeyWrapper.getKeyFragment(fragmentNumber) +
                "~ ";
    }
}