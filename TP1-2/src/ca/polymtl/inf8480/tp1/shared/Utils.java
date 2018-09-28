package ca.polymtl.inf8480.tp1.shared;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Utils {
    public static byte[] getMD5(byte[] content) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            return md.digest(content);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
    }
}
