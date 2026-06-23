package nl.wachtwoordmanager;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.slider.Slider;
import com.google.android.material.textfield.TextInputEditText;

import java.security.SecureRandom;

public class ItemActivity extends AppCompatActivity {

    private static final String LETTERS   = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static final String CIJFERS   = "0123456789";
    private static final String SYMBOLEN  = "!@#$%^&*()-_=+";

    private TextInputEditText etNaam, etGebruiker, etWachtwoord, etUrl, etNotities;
    private TextView tvStatus, tvLengte;
    private Slider sliderLengte;
    private CheckBox cbSymbolen;
    private Kluis kluis;
    private String itemId;
    private Handler handler = new Handler(Looper.getMainLooper());
    private Runnable verbergStatus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item);

        kluis  = App.getKluis();
        itemId = getIntent().getStringExtra("id");

        etNaam      = findViewById(R.id.etNaam);
        etGebruiker = findViewById(R.id.etGebruikersnaam);
        etWachtwoord = findViewById(R.id.etWachtwoord);
        etUrl       = findViewById(R.id.etUrl);
        etNotities  = findViewById(R.id.etNotities);
        tvStatus    = findViewById(R.id.tvStatus);
        tvLengte    = findViewById(R.id.tvLengte);
        sliderLengte = findViewById(R.id.sliderLengte);
        cbSymbolen  = findViewById(R.id.cbSymbolen);

        // Laad bestaand item
        if (itemId != null) {
            WachtwoordItem item = kluis.getItem(itemId);
            if (item != null) {
                etNaam.setText(item.naam);
                etGebruiker.setText(item.gebruikersnaam);
                etWachtwoord.setText(item.wachtwoord);
                etUrl.setText(item.url);
                etNotities.setText(item.notities);
                TextView titelBalk = findViewById(R.id.tvTitelBalk);
                titelBalk.setText(item.naam != null ? item.naam : "Item bewerken");
            }
        } else {
            TextView titelBalk = findViewById(R.id.tvTitelBalk);
            titelBalk.setText("Nieuw item");
        }

        // Slider
        sliderLengte.addOnChangeListener((slider, value, fromUser) ->
            tvLengte.setText(String.valueOf((int) value)));

        // Generator
        Button btnGenereer = findViewById(R.id.btnGenereer);
        btnGenereer.setOnClickListener(v -> {
            int lengte = (int) sliderLengte.getValue();
            boolean metSymbolen = cbSymbolen.isChecked();
            etWachtwoord.setText(genereerWachtwoord(lengte, metSymbolen));
        });

        // Kopieer
        Button btnKopieer = findViewById(R.id.btnKopieer);
        btnKopieer.setOnClickListener(v -> {
            String pw = etWachtwoord.getText() != null ? etWachtwoord.getText().toString() : "";
            ClipboardManager cm = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
            cm.setPrimaryClip(ClipData.newPlainText("wachtwoord", pw));
            toonStatus(getString(R.string.gekopieerd));
        });

        // Opslaan
        Button btnOpslaan = findViewById(R.id.btnOpslaan);
        btnOpslaan.setOnClickListener(v -> slaOp());

        // Verwijderen
        Button btnVerwijder = findViewById(R.id.btnVerwijder);
        btnVerwijder.setOnClickListener(v -> {
            if (itemId == null) { finish(); return; }
            new AlertDialog.Builder(this)
                .setMessage(R.string.verwijder_bevestig)
                .setPositiveButton(R.string.ja, (d, w) -> {
                    try {
                        kluis.verwijderItem(itemId);
                        finish();
                    } catch (Exception e) {
                        Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton(R.string.nee, null)
                .show();
        });

        // Terug
        findViewById(R.id.btnTerug).setOnClickListener(v -> finish());
    }

    private void slaOp() {
        WachtwoordItem item = new WachtwoordItem(
            tekst(etNaam, "Naamloos"),
            tekst(etGebruiker, ""),
            tekst(etWachtwoord, ""),
            tekst(etUrl, ""),
            tekst(etNotities, "")
        );
        try {
            if (itemId == null) {
                itemId = kluis.voegToe(item);
                TextView titelBalk = findViewById(R.id.tvTitelBalk);
                titelBalk.setText(item.naam);
            } else {
                kluis.slaItemOp(itemId, item);
            }
            toonStatus(getString(R.string.opgeslagen));
        } catch (Exception e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    private String tekst(TextInputEditText et, String standaard) {
        String s = et.getText() != null ? et.getText().toString().trim() : "";
        return s.isEmpty() ? standaard : s;
    }

    private void toonStatus(String bericht) {
        tvStatus.setText(bericht);
        tvStatus.setVisibility(android.view.View.VISIBLE);
        if (verbergStatus != null) handler.removeCallbacks(verbergStatus);
        verbergStatus = () -> tvStatus.setVisibility(android.view.View.INVISIBLE);
        handler.postDelayed(verbergStatus, 2500);
    }

    private String genereerWachtwoord(int lengte, boolean metSymbolen) {
        String tekens = LETTERS + CIJFERS + (metSymbolen ? SYMBOLEN : "");
        SecureRandom rng = new SecureRandom();
        StringBuilder sb = new StringBuilder(lengte);
        for (int i = 0; i < lengte; i++) {
            sb.append(tekens.charAt(rng.nextInt(tekens.length())));
        }
        return sb.toString();
    }
}
