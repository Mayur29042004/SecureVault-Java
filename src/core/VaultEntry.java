package core;

public class VaultEntry {
    private String site;
    private String password;

    public VaultEntry(String site, String password) {
        this.site = site;
        this.password = password;
    }

    public String getSite() {
        return site;
    }

    public String getPassword() {
        return password;
    }
}