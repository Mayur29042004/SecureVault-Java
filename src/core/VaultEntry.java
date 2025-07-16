package core;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class VaultEntry {
    private final StringProperty site;
    private final StringProperty password;

    public VaultEntry(String site, String password) {
        this.site = new SimpleStringProperty(site);
        this.password = new SimpleStringProperty(password);
    }

    public String getSite() {
        return site.get();
    }

    public void setSite(String site) {
        this.site.set(site);
    }

    public StringProperty siteProperty() {
        return site;
    }

    public String getPassword() {
        return password.get();
    }

    public void setPassword(String password) {
        this.password.set(password);
    }

    public StringProperty passwordProperty() {
        return password;
    }
}