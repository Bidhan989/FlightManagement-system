package bcu.cmp5332.bookingsystem.util;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Utility class for password hashing and verification.
 * Uses SHA-256 algorithm for secure password storage.
 * 
 * @version 8.0 (Authentication System)
 */
public class PasswordUtil {
    
    /**
     * Hashes a password using SHA-256 algorithm.
     * 
     * @param password the plain text password
     * @return the hashed password as a hexadecimal string
     */
    public static String hashPassword(String password) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(password.getBytes(StandardCharsets.UTF_8));
            return bytesToHex(hash);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("SHA-256 algorithm not found", e);
        }
    }
    
    /**
     * Verifies if a password matches a hash.
     * 
     * @param password the plain text password to verify
     * @param passwordHash the stored password hash
     * @return true if password matches hash, false otherwise
     */
    public static boolean verifyPassword(String password, String passwordHash) {
        String hashedInput = hashPassword(password);
        return hashedInput.equals(passwordHash);
    }
    
    /**
     * Converts byte array to hexadecimal string.
     * 
     * @param bytes the byte array
     * @return hexadecimal string representation
     */
    private static String bytesToHex(byte[] bytes) {
        StringBuilder hexString = new StringBuilder();
        for (byte b : bytes) {
            String hex = Integer.toHexString(0xff & b);
            if (hex.length() == 1) {
                hexString.append('0');
            }
            hexString.append(hex);
        }
        return hexString.toString();
    }
    
    /**
     * Validates password strength.
     * Password must be at least 6 characters.
     * 
     * @param password the password to validate
     * @return true if password meets requirements, false otherwise
     */
    public static boolean isValidPassword(String password) {
        if (password == null || password.length() < 6) {
            return false;
        }
        return true;
    }
    
    /**
     * Gets password strength requirements message.
     * 
     * @return the requirements message
     */
    public static String getPasswordRequirements() {
        return "Password must be at least 6 characters long.";
    }
}
