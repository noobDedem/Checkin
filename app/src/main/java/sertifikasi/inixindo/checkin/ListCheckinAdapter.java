package sertifikasi.inixindo.checkin;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

public class ListCheckinAdapter extends RecyclerView.Adapter<ListCheckinAdapter.MyViewHolder> {
    private ArrayList<Checkin> arrayList;
    private static View view;
    private Context context;

    public ListCheckinAdapter(ArrayList<Checkin> arrayList, Context context) {
        this.arrayList = arrayList;
        this.context = context;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView namaLokasi, keteranganLokasi, longitude, latitude, kontributor;

        public MyViewHolder(View itemView) {
            super(itemView);
            namaLokasi = itemView.findViewById(R.id.nama_lokasi);
            keteranganLokasi = itemView.findViewById(R.id.keterangan_lokasi);
            longitude = itemView.findViewById(R.id.longitude);
            latitude = itemView.findViewById(R.id.latitude);
            kontributor = itemView.findViewById(R.id.kontributor);
        }
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.content_list_checkin, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        final Checkin checkin = arrayList.get(position);
        holder.namaLokasi.setText(checkin.getNamaLokasi());
        holder.keteranganLokasi.setText(checkin.getKeteranganLokasi());
        holder.longitude.setText(checkin.getLongitude());
        holder.latitude.setText(checkin.getLatitude());
        holder.kontributor.setText(checkin.getKontributor());
    }

    @Override
    public int getItemCount() {
        try {
            return arrayList.size();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }
}
