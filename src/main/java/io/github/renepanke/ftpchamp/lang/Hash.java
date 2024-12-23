package io.github.renepanke.ftpchamp.lang;

import io.github.renepanke.ftpchamp.exceptions.FTPServerRuntimeException;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Hash {

    public static String getSHA1Length7For(String input) {
        MessageDigest digest;
        try {
            digest = MessageDigest.getInstance("SHA-1");
        } catch (NoSuchAlgorithmException e) {
            throw new FTPServerRuntimeException(e);
        }
        StringBuilder sb = new StringBuilder();
        for (byte b : digest.digest(input.getBytes(StandardCharsets.UTF_8))) {
            sb.append(String.format("%02x", b));
        }
        return sb.substring(0, 7);
    }
}
