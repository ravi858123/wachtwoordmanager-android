package nl.wachtwoordmanager;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Base64;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Beheert opslag en ophalen van versleutelde wachtwoordinformatie.
 */
public class Kluis {

    private static final String PREFS_NAAM  = "kluis_prefs";
    private static final String KEY_SALT    = "salt";
    private static final String KEY_DATA    = "data";
    private static final String KEY_BESTAAT = "bestaat";

    private final SharedPreferences prefs;
    private final Gson gson = new Gson();

    private String hoofd;
    private byte[] salt;
    private Map<String, WachtwoordItem> items = new LinkedHashMap<>();

    public Kluis(Context context) {
        prefs = context.getSharedPreferences(PREFS_NAAM, Context.MODE_PRIVATE);
    }

    /** Geeft aan of er al een kluis aangemaakt is. */
    public boolean bestaatKluis() {
        return prefs.getBoolean(KEY_BESTAAT, false);
    }

    /** Maak een nieuwe kluis met het opgegeven wachtwoord. */
    public void maakNieuw(String wachtwoord) throws Exception {
        salt = Crypto.nieuweSalt();
        prefs.edit()
            .putString(KEY_SALT, Base64.encodeToString(salt, Base64.NO_WRAP))
            .putBoolean(KEY_BESTAAT, true)
            .apply();
        hoofd = wachtwoord;
        items = new LinkedHashMap<>();
        slaOp();
    }

    /**
     * Open bestaande kluis. Gooit uitzondering als wachtwoord fout is.
     * Geeft false terug als data niet ontsleuteld kan worden.
     */
    public boolean open(String wachtwoord) {
        try {
            String saltStr = prefs.getString(KEY_SALT, null);
            if (saltStr == null) return false;
            salt = Base64.decode(saltStr, Base64.NO_WRAP);

            String versleuteld = prefs.getString(KEY_DATA, null);
            if (versleuteld == null) {
                // Lege kluis
                hoofd = wachtwoord;
                items = new LinkedHashMap<>();
                return true;
            }

            String json = Crypto.ontsleutel(versleuteld, wachtwoord, salt);
            Type type = new TypeToken<LinkedHashMap<String, WachtwoordItem>>() {}.getType();
            items = gson.fromJson(json, type);
            hoofd = wachtwoord;
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /** Sla de huidige items versleuteld op. */
    public void slaOp() throws Exception {
        String json = gson.toJson(items);
        String versleuteld = Crypto.versleutel(json, hoofd, salt);
        prefs.edit().putString(KEY_DATA, versleuteld).apply();
    }

    public Map<String, WachtwoordItem> getItems() { return items; }

    public WachtwoordItem getItem(String id) { return items.get(id); }

    public String voegToe(WachtwoordItem item) throws Exception {
        String id = UUID.randomUUID().toString();
        items.put(id, item);
        slaOp();
        return id;
    }

    public void slaItemOp(String id, WachtwoordItem item) throws Exception {
        items.put(id, item);
        slaOp();
    }

    public void verwijderItem(String id) throws Exception {
        items.remove(id);
        slaOp();
    }

    public void vergrendel() {
        hoofd = null;
        salt = null;
        items = new LinkedHashMap<>();
    }
}
