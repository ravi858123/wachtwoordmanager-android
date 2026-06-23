package nl.wachtwoordmanager;

import android.util.Base64;

import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.security.spec.KeySpec;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;

/**
 * AES-256-GCM versleuteling met PBKDF2 sleutelafleiding.
 * Compatibel met de Windows Python-versie (zelfde beveiligingsniveau).
 */
public class Crypto {

    private static final int ITERATIES    = 480_000;
    private static final int SLEUTELLENGTE = 256;
    private static final int SALT_LENGTE  = 16;
    private static final int IV_LENGTE    = 12;
    private static final int TAG_BITS     = 128;

    /** Genereer een willekeurige salt (sla eenmalig op). */
    public static byte[] nieuweSalt() {
        byte[] salt = new byte[SALT_LENGTE];
        new SecureRandom().nextBytes(salt);
        return salt;
    }

    /** Leid een AES-sleutel af van het wachtwoord + salt via PBKDF2. */
    private static SecretKey leidSleutelAf(String wachtwoord, byte[] salt) throws Exception {
        SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
        KeySpec spec = new PBEKeySpec(wachtwoord.toCharArray(), salt, ITERATIES, SLEUTELLENGTE);
        byte[] sleutelBytes = factory.generateSecret(spec).getEncoded();
        return new SecretKeySpec(sleutelBytes, "AES");
    }

    /** Versleutel tekst → Base64-string (IV vooraan). */
    public static String versleutel(String plaintext, String wachtwoord, byte[] salt) throws Exception {
        SecretKey sleutel = leidSleutelAf(wachtwoord, salt);
        byte[] iv = new byte[IV_LENGTE];
        new SecureRandom().nextBytes(iv);

        Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
        cipher.init(Cipher.ENCRYPT_MODE, sleutel, new GCMParameterSpec(TAG_BITS, iv));
        byte[] versleuteld = cipher.doFinal(plaintext.getBytes(StandardCharsets.UTF_8));

        // Combineer IV + versleuteld
        byte[] resultaat = new byte[IV_LENGTE + versleuteld.length];
        System.arraycopy(iv, 0, resultaat, 0, IV_LENGTE);
        System.arraycopy(versleuteld, 0, resultaat, IV_LENGTE, versleuteld.length);
        return Base64.encodeToString(resultaat, Base64.NO_WRAP);
    }

    /** Ontsleutel Base64-string → tekst. Gooit uitzondering bij verkeerd wachtwoord. */
    public static String ontsleutel(String base64Data, String wachtwoord, byte[] salt) throws Exception {
        SecretKey sleutel = leidSleutelAf(wachtwoord, salt);
        byte[] data = Base64.decode(base64Data, Base64.NO_WRAP);

        byte[] iv = new byte[IV_LENGTE];
        byte[] versleuteld = new byte[data.length - IV_LENGTE];
        System.arraycopy(data, 0, iv, 0, IV_LENGTE);
        System.arraycopy(data, IV_LENGTE, versleuteld, 0, versleuteld.length);

        Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
        cipher.init(Cipher.DECRYPT_MODE, sleutel, new GCMParameterSpec(TAG_BITS, iv));
        return new String(cipher.doFinal(versleuteld), StandardCharsets.UTF_8);
    }
}
