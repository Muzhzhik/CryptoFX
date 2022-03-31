package com.example.cryptofx.cryptor;

public interface Cryptor {
    String encrypt(String data, int key);
    String decrypt(String data, int key);
}
