package services;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Md5 {

    public String encrypt(final String input) throws NoSuchAlgorithmException {
        final MessageDigest md = MessageDigest.getInstance("MD5");
        final byte[] messageDigest = md.digest(input.getBytes());
        final BigInteger number = new BigInteger(1, messageDigest);
        return String.format("%032x", number);
    }
}
