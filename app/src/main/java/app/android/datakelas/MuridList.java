package app.android.datakelas;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class MuridList extends ArrayAdapter<Murid> {
    private Activity context;
    private List<Murid> listMurid;

    public MuridList(Activity context , List<Murid> listMurid){
        super(context,R.layout.list_data_murid,listMurid);
        this.context = context;
        this.listMurid = listMurid;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View listViewMurid = inflater.inflate(R.layout.list_data_murid,null);

        ImageView imageViewMurid = (ImageView) listViewMurid.findViewById(R.id.imageViewMurid);
        TextView textViewNamaMurid = (TextView) listViewMurid.findViewById(R.id.txtNamaMurid);
        TextView textViewTglMurid = (TextView) listViewMurid.findViewById(R.id.txtTglMurid);
        TextView textViewJenkelMurid = (TextView) listViewMurid.findViewById(R.id.txtJenkelMurid);

        Murid murid = listMurid.get(position);
        imageViewMurid.setImageResource(R.drawable.card);
        textViewNamaMurid.setText(murid.getNamaMurid());
        textViewTglMurid.setText(murid.getTanggalMurid());
        textViewJenkelMurid.setText(murid.getJenkelMurid());

        return listViewMurid;
    }
}
