package com.finalproject.treebackendroom1.Service;

import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
@Service
public class PasswordSecurity {
    public String passwordSecurity(String password){
        MessageDigest md = null;

        try {
            md = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        byte[] digest = md.digest(password.getBytes());
        BigInteger bi = new BigInteger(1, digest);
        String hash = bi.toString(16);
        return hash;

    }

}
