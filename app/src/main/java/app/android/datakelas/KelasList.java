package app.android.datakelas;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class KelasList extends ArrayAdapter<Kelas> {
    private Activity context;
    private List<Kelas>listKelas;

    public KelasList(Activity context , List<Kelas> listKelas){
        super(context,R.layout.list_data_kelas,listKelas);
        this.context = context;
        this.listKelas = listKelas;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View listViewKelas = inflater.inflate(R.layout.list_data_kelas,null);

        ImageView imageView = (ImageView) listViewKelas.findViewById(R.id.imageView);
        TextView txtNamaKelas = (TextView) listViewKelas.findViewById(R.id.txtNamaKelas);
        TextView txtTingkatanKelas = (TextView) listViewKelas.findViewById(R.id.txtNamaTingkatan);

        Kelas kelas = listKelas.get(position);
        imageView.setImageResource(R.drawable.school);
        txtNamaKelas.setText(kelas.getNamaKelas());
        txtTingkatanKelas.setText(kelas.getTingkatanKelas());

        return  listViewKelas;
    }
}
