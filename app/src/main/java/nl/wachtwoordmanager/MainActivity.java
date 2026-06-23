package nl.wachtwoordmanager;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;

public class MainActivity extends AppCompatActivity {

    private ItemAdapter adapter;
    private Kluis kluis;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        kluis = App.getKluis();

        RecyclerView rv = findViewById(R.id.rvItems);
        rv.setLayoutManager(new LinearLayoutManager(this));
        adapter = new ItemAdapter(kluis, id -> {
            Intent i = new Intent(this, ItemActivity.class);
            i.putExtra("id", id);
            startActivity(i);
        });
        rv.setAdapter(adapter);

        // Zoeken
        TextInputEditText etZoeken = findViewById(R.id.etZoeken);
        etZoeken.addTextChangedListener(new TextWatcher() {
            public void beforeTextChanged(CharSequence s, int st, int c, int a) {}
            public void onTextChanged(CharSequence s, int st, int b, int c) {
                adapter.filter(s.toString());
            }
            public void afterTextChanged(Editable s) {}
        });

        // Vergrendelen
        findViewById(R.id.btnVergrendel).setOnClickListener(v -> {
            kluis.vergrendel();
            startActivity(new Intent(this, LoginActivity.class));
            finish();
        });

        // Nieuw item
        ExtendedFloatingActionButton fab = findViewById(R.id.fabNieuw);
        fab.setOnClickListener(v -> {
            Intent i = new Intent(this, ItemActivity.class);
            i.putExtra("id", (String) null);
            startActivity(i);
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        adapter.herlaad();
    }
}
