package nl.wachtwoordmanager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

public class LoginActivity extends AppCompatActivity {

    private TextInputEditText etWachtwoord, etHerhaal;
    private TextInputLayout tilHerhaal;
    private Button btnLogin;
    private TextView tvOndertitel;
    private Kluis kluis;
    private boolean isNieuw;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        kluis = App.getKluis();
        isNieuw = !kluis.bestaatKluis();

        etWachtwoord = findViewById(R.id.etWachtwoord);
        etHerhaal    = findViewById(R.id.etHerhaal);
        tilHerhaal   = findViewById(R.id.tilHerhaal);
        btnLogin     = findViewById(R.id.btnLogin);
        tvOndertitel = findViewById(R.id.tvOndertitel);

        if (isNieuw) {
            tvOndertitel.setText(R.string.nieuwe_kluis);
            tilHerhaal.setVisibility(View.VISIBLE);
            btnLogin.setText(R.string.aanmaken);
        }

        btnLogin.setOnClickListener(v -> doeLogin());

        etHerhaal.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_DONE) { doeLogin(); return true; }
            return false;
        });
        etWachtwoord.setOnEditorActionListener((v, actionId, event) -> {
            if (!isNieuw && actionId == EditorInfo.IME_ACTION_DONE) { doeLogin(); return true; }
            return false;
        });
    }

    private void doeLogin() {
        String pw = etWachtwoord.getText() != null ? etWachtwoord.getText().toString() : "";
        if (pw.isEmpty()) {
            Toast.makeText(this, "Vul een wachtwoord in", Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            if (isNieuw) {
                String pw2 = etHerhaal.getText() != null ? etHerhaal.getText().toString() : "";
                if (!pw.equals(pw2)) {
                    Toast.makeText(this, getString(R.string.fout_herhaal), Toast.LENGTH_SHORT).show();
                    return;
                }
                if (pw.length() < 6) {
                    Toast.makeText(this, getString(R.string.fout_kort), Toast.LENGTH_SHORT).show();
                    return;
                }
                kluis.maakNieuw(pw);
            } else {
                if (!kluis.open(pw)) {
                    Toast.makeText(this, getString(R.string.fout_wachtwoord), Toast.LENGTH_SHORT).show();
                    return;
                }
            }
            startActivity(new Intent(this, MainActivity.class));
            finish();
        } catch (Exception e) {
            Toast.makeText(this, "Fout: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }
}
