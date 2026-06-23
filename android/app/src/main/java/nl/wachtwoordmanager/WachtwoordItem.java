package nl.wachtwoordmanager;

public class WachtwoordItem {
    public String naam;
    public String gebruikersnaam;
    public String wachtwoord;
    public String url;
    public String notities;

    public WachtwoordItem() {}

    public WachtwoordItem(String naam, String gebruikersnaam,
                          String wachtwoord, String url, String notities) {
        this.naam = naam;
        this.gebruikersnaam = gebruikersnaam;
        this.wachtwoord = wachtwoord;
        this.url = url;
        this.notities = notities;
    }
}
