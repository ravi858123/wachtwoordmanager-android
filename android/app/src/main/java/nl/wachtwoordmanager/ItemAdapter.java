package nl.wachtwoordmanager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.VH> {

    interface OnItemClick { void onClick(String id); }

    private final Kluis kluis;
    private final OnItemClick listener;
    private List<Map.Entry<String, WachtwoordItem>> gefilterd = new ArrayList<>();
    private String zoekterm = "";

    public ItemAdapter(Kluis kluis, OnItemClick listener) {
        this.kluis = kluis;
        this.listener = listener;
        herlaad();
    }

    public void herlaad() {
        filter(zoekterm);
    }

    public void filter(String term) {
        zoekterm = term.toLowerCase();
        gefilterd = new ArrayList<>();
        for (Map.Entry<String, WachtwoordItem> e : kluis.getItems().entrySet()) {
            if (e.getValue().naam != null &&
                e.getValue().naam.toLowerCase().contains(zoekterm)) {
                gefilterd.add(e);
            }
        }
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_wachtwoord, parent, false);
        return new VH(v);
    }

    @Override
    public void onBindViewHolder(@NonNull VH holder, int position) {
        Map.Entry<String, WachtwoordItem> entry = gefilterd.get(position);
        WachtwoordItem item = entry.getValue();
        holder.tvNaam.setText(item.naam != null ? item.naam : "Naamloos");
        holder.tvGebruiker.setText(item.gebruikersnaam != null ? item.gebruikersnaam : "");
        holder.itemView.setOnClickListener(v -> listener.onClick(entry.getKey()));
    }

    @Override
    public int getItemCount() { return gefilterd.size(); }

    static class VH extends RecyclerView.ViewHolder {
        TextView tvNaam, tvGebruiker;
        VH(View v) {
            super(v);
            tvNaam     = v.findViewById(R.id.tvNaam);
            tvGebruiker = v.findViewById(R.id.tvGebruikersnaam);
        }
    }
}
