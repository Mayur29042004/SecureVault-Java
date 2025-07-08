package core;

import utils.Encryptor;
import java.util.*;
import java.io.*;

public class VaultManager {
    private static final String FILE = "vault.txt";
    private static HashMap<String, String> vault = new HashMap<>();
    private static Stack<String> deletedStack = new Stack<>();

    public static void loadVault() throws Exception {
        vault.clear();
        try (BufferedReader br = new BufferedReader(new FileReader(FILE))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] split = line.split(":");
                if (split.length == 2) {
                    vault.put(split[0], Encryptor.decrypt(split[1]));
                }
            }
        }
    }

    public static void saveVault() throws Exception {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(FILE))) {
            for (Map.Entry<String, String> entry : vault.entrySet()) {
                bw.write(entry.getKey() + ":" + Encryptor.encrypt(entry.getValue()));
                bw.newLine();
            }
        }
    }

    public static void addPassword(String site, String password) {
        vault.put(site, password);
    }

    public static String getPassword(String site) {
        return vault.getOrDefault(site, "Not Found");
    }

    public static void deletePassword(String site) {
        if (vault.containsKey(site)) {
            deletedStack.push(site + ":" + vault.get(site));
            vault.remove(site);
        }
    }

    public static void undoDelete() {
        if (!deletedStack.isEmpty()) {
            String[] split = deletedStack.pop().split(":");
            vault.put(split[0], split[1]);
        }
    }

    public static Set<String> listSites() {
        return vault.keySet();
    }
}