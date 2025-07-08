package auth;

import java.io.*;
import java.security.MessageDigest;
import java.util.HashMap;

public class LoginManager {
    private static final String FILE = "user_data.txt";
    private static HashMap<String, String> userDB = new HashMap<>();

    public static void loadUsers() {
        try (BufferedReader br = new BufferedReader(new FileReader(FILE))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] split = line.split(":");
                if (split.length == 2) {
                    userDB.put(split[0], split[1]);
                }
            }
        } catch (IOException e) {
            System.out.println("No user file found. Creating new.");
        }
    }

    public static boolean register(String username, String password) {
        if (userDB.containsKey(username)) return false;
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(FILE, true))) {
            String hashed = hash(password);
            bw.write(username + ":" + hashed);
            bw.newLine();
            userDB.put(username, hashed);
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    public static boolean login(String username, String password) {
        String hashed = hash(password);
        return userDB.containsKey(username) && userDB.get(username).equals(hashed);
    }

    private static String hash(String input) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hash = md.digest(input.getBytes());
            StringBuilder sb = new StringBuilder();
            for (byte b : hash) sb.append(String.format("%02x", b));
            return sb.toString();
        } catch (Exception e) {
            return null;
        }
    }
}