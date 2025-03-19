package org.example.functions;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.stream.Collectors;

public class HashService {
    // SHA1 Hash (for HIBP )
    public static String generateSHA1(String input) {
        return hash(input, "SHA-1");
    }

    private static String hash(String input, String algorithm) {
        try {
            MessageDigest digest = MessageDigest.getInstance(algorithm);
            byte[] encodedHash = digest.digest(input.getBytes(StandardCharsets.UTF_8));
            return bytesToHex(encodedHash);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Hashing error: " + e.getMessage());
        }
    }

    private static String bytesToHex(byte[] hash) {
        StringBuilder hexString = new StringBuilder();
        for (byte b : hash) {
            String hex = Integer.toHexString(0xff & b);
            if (hex.length() == 1) hexString.append('0');
            hexString.append(hex);
        }
        return hexString.toString();
    }

    // HIBP query
    public static String queryHIBP(String passwordSha1) {
        String sha1HashPrefix = passwordSha1.substring(0, 5);
        String result = null;
        try {
            URL url = new URL("https://api.pwnedpasswords.com/range/" + sha1HashPrefix);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");

            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
            String line = null;
            while ((line = in.readLine()) != null) {
                String[] parts = line.split(":");
                if ((sha1HashPrefix+parts[0]).equalsIgnoreCase(passwordSha1)) {
                    result = "your password may have been leaked, and the appear times is: " + parts[1];
                    return result;
                }
            }
            in.close();
            return "your password is safe";

        } catch (Exception e) {
            throw new RuntimeException("HIBP query failed: " + e.getMessage());
        }
    }
}
