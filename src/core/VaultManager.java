package core;

import utils.Encryptor;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

public class VaultManager {

    private static final String FILE_NAME = "vault.txt";

    // ✅ This must be initialized to avoid NullPointerException
    private static Map<String, String> passwordMap = new HashMap<>();

    // Used only if you're tracking undo-delete and VaultEntry model
    private static Map<String, String> vault = new HashMap<>();
    private static Deque<String> deletedStack = new ArrayDeque<>();

    // ✅ Load vault.txt on startup
    static {
        loadFromFile();
    }

    // 🔐 Add password to map and save to file
    public static void addPassword(String site, String password) {
        try {
            passwordMap.put(site.toLowerCase(), Encryptor.encrypt(password));
            saveToFile();
            System.out.println("✅ Added: " + site);
        } catch (Exception e) {
            System.err.println("❌ Error adding password: " + site);
            e.printStackTrace();
        }
    }

    // 🔎 Retrieve decrypted password
    public static String getPassword(String site) {
        try {
            if (passwordMap.containsKey(site.toLowerCase())) {
                return Encryptor.decrypt(passwordMap.get(site.toLowerCase()));
            }
        } catch (Exception e) {
            System.err.println("❌ Error decrypting password for site: " + site);
            e.printStackTrace();
            return "Decryption Error";
        }
        return "Not Found";
    }

    // 🗑 Delete password
    public static void deletePassword(String site) {
        if (passwordMap.containsKey(site.toLowerCase())) {
            deletedStack.push(site + ":" + passwordMap.get(site.toLowerCase()));
            passwordMap.remove(site.toLowerCase());
            saveToFile();
        }
    }

    // ↩ Undo last delete
    public static void undoDelete() {
        if (!deletedStack.isEmpty()) {
            String[] split = deletedStack.pop().split(":");
            passwordMap.put(split[0], split[1]);
            saveToFile();
        }
    }

    // 📂 Save password map to file
    public static void saveToFile() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_NAME))) {
            for (Map.Entry<String, String> entry : passwordMap.entrySet()) {
                writer.write(entry.getKey() + ":" + entry.getValue());
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // 📥 Load from vault.txt into passwordMap
    public static void loadFromFile() {
        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_NAME))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(":");
                if (parts.length == 2) {
                    passwordMap.put(parts[0], parts[1]);
                }
            }
            System.out.println("✅ Vault loaded: " + passwordMap.keySet());
        } catch (IOException e) {
            System.out.println("⚠ vault.txt not found. Starting fresh.");
        }
    }

    // 📋 For displaying in TableView
    public static List<VaultEntry> getAllPasswords() {
        return passwordMap.entrySet().stream()
                .map(e -> {
                    try {
                        return new VaultEntry(e.getKey(), Encryptor.decrypt(e.getValue()));
                    } catch (Exception ex) {
                        throw new RuntimeException("Decryption failed for key: " + e.getKey(), ex);
                    }
                })
                .collect(Collectors.toList());
    }

    // Optional: list only site names
    public static Set<String> listSites() {
        return passwordMap.keySet();
    }
}