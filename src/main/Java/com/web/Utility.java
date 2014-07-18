package com.web;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by R500 on 18.7.2014 г..
 */
public class Utility {
    public static String toSHA1(byte[] convertme) {
        MessageDigest md = null;
        try {
            md = MessageDigest.getInstance("SHA-1");
        }
        catch(NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return new String(md.digest(convertme));
    }
}


//    toSHA1("abap".getBytes())
//  abap is the password string