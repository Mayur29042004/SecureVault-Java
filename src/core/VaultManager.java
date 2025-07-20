package core;

import utils.Encryptor;
import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

public class VaultManager {
    private static Map<String, String> passwordMap = new HashMap<>();
    private static Deque<String> deletedStack = new ArrayDeque<>();
    private static String currentUser;

    // ✅ Set current user (used to define vault file)
    public static void setCurrentUser(String username) {
        currentUser = username;
        loadFromFile();  // ✅ Load their specific file
    }

    // ✅ Get current user vault file
    private static File getVaultFile() {
        return new File("vault_" + currentUser + ".txt");
    }

    // 🔐 Add password
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

    // 🔎 Get password
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

    // ↩ Undo delete
    public static void undoDelete() {
        if (!deletedStack.isEmpty()) {
            String[] split = deletedStack.pop().split(":");
            passwordMap.put(split[0], split[1]);
            saveToFile();
        }
    }

    // 📂 Save all passwords to current user's file
    public static void saveToFile() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(getVaultFile()))) {
            for (Map.Entry<String, String> entry : passwordMap.entrySet()) {
                writer.write(entry.getKey() + ":" + entry.getValue());
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // 📥 Load passwords from current user's file
    public static void loadFromFile() {
        passwordMap.clear();
        File file = getVaultFile();
        if (!file.exists()) {
            System.out.println("⚠ " + file.getName() + " not found. Starting fresh.");
            return;
        }
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(":");
                if (parts.length == 2) {
                    passwordMap.put(parts[0], parts[1]);
                }
            }
            System.out.println("✅ Vault loaded: " + passwordMap.keySet());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // 📋 For TableView
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

    // 🔎 Optional - site list
    public static Set<String> listSites() {
        return passwordMap.keySet();
    }
}