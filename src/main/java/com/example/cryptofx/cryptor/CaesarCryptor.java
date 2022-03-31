package com.example.cryptofx.cryptor;

import com.example.cryptofx.utils.Alphabet;

public class CaesarCryptor implements Cryptor{

    @Override
    public String encrypt(String data, int key) {
        String result = null;
        if (data != null && !data.equals("")) {
            StringBuilder encryptedStr = new StringBuilder();
            for(char c : data.toCharArray()) {
                encryptedStr.append(Alphabet.getChar(c, key));
            }
            result = encryptedStr.toString();
        }
        return result;
    }

    @Override
    public String decrypt(String data, int key) {
        return encrypt(data, -key);
    }

}