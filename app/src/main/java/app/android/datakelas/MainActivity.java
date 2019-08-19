package app.android.datakelas;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.github.clans.fab.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    //digunakan untuk memnindahkan data dari 1 activity ke activity lain
    public static final String ID_KELAS = "idKelas";
    public static final String NAMA_KELAS = "namaKelas";
    public static final String TINGKATAN_KELAS = "tingkatanKelas";

    //inisialisasi awal untuk item yang ada di xml
    DatabaseReference databaseReference;
    private FloatingActionButton fab;
    List<Kelas> listKelas;
    ListView listViewKelas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //inisialisasi ulang untuk item yang ada di xml
        fab = (FloatingActionButton) findViewById(R.id.addBtn);
        databaseReference = FirebaseDatabase.getInstance().getReference("Kelas");
        listKelas = new ArrayList<>();
        listViewKelas = (ListView)findViewById(R.id.listDataKelas);

        //membuat floating action button bekerja saat di pencet
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialogAdd();
            }
        });

        //membuat item yang di pencet bisa di tambah data , ubah data , bahkan menghapus data
        listViewKelas.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                //memulai activity ke Murid Activity
                Kelas kelas = listKelas.get(i);
                Intent intent = new Intent(getApplicationContext(),MuridActivity.class);
                intent.putExtra(ID_KELAS,kelas.getIdKelas());
                intent.putExtra(NAMA_KELAS,kelas.getNamaKelas());
                intent.putExtra(TINGKATAN_KELAS,kelas.getTingkatanKelas());

                startActivity(intent);

            }
        });

        listViewKelas.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {

                Kelas kelas = listKelas.get(i);

                showDialogWhatToDo(kelas.getIdKelas());

                return false;
            }
        });

    }

    private void showDialogWhatToDo(final String idKelas) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.layout_update_or_delete,null);
        dialogBuilder.setView(dialogView);

        Button btnDoUpdate = (Button) dialogView.findViewById(R.id.doUpdate);
        Button btnDoDelete = (Button) dialogView.findViewById(R.id.doDelete);

        final AlertDialog alertDialog = dialogBuilder.create();
        alertDialog.show();

        btnDoUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialogUpdateKelas(idKelas);
                alertDialog.dismiss();
            }
        });

        btnDoDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteKelas(idKelas);
                alertDialog.dismiss();
            }
        });

    }

    private void deleteKelas(String idKelas) {
        DatabaseReference dKelas = FirebaseDatabase.getInstance().getReference("Kelas").child(idKelas);
        DatabaseReference dMurid = FirebaseDatabase.getInstance().getReference("Murid").child(idKelas);
        dKelas.removeValue();
        dMurid.removeValue();

        Toast.makeText(this, "Berhasil Hapus Data", Toast.LENGTH_SHORT).show();

    }

    private void showDialogUpdateKelas(final String idKelas) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.layout_update_kelas,null);
        dialogBuilder.setView(dialogView);

       final Spinner spinnerNamaKelasUpdate = (Spinner) dialogView.findViewById(R.id.spinnerNamaKelasUpdate);
       final Spinner spinnerTingkatanKelasUpdate = (Spinner) dialogView.findViewById(R.id.spinnerTingkatanKelasUpdate);
       final Button buttonUpdate = (Button) dialogView.findViewById(R.id.updateKelas);

        dialogBuilder.setTitle("Update Kelas ");
        final AlertDialog alertDialog = dialogBuilder.create();
        alertDialog.show();

        buttonUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String namaKelas = spinnerNamaKelasUpdate.getSelectedItem().toString();
                String tingkatanKelas = spinnerTingkatanKelasUpdate.getSelectedItem().toString();

                updateKelas(idKelas , namaKelas , tingkatanKelas);
                alertDialog.dismiss();
            }
        });
    }

    private boolean updateKelas(String idKelas , String namaKelas , String tingkatanKelas){
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Kelas").child(idKelas);
        Kelas kelas = new Kelas(idKelas , namaKelas , tingkatanKelas);
        databaseReference.setValue(kelas);

        Toast.makeText(this, "Behasil Update Data", Toast.LENGTH_SHORT).show();

        return true;
    }


    //overide method onStart sangat penting agar saat data ada perubahan langsung listview bertambah
    @Override
    protected void onStart() {
        super.onStart();

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                listKelas.clear();

                for (DataSnapshot kelasSnapShot : dataSnapshot.getChildren()){
                    Kelas kelas = kelasSnapShot.getValue(Kelas.class);
                    listKelas.add(kelas);
                }

                KelasList Adapter = new KelasList(MainActivity.this,listKelas);
                listViewKelas.setAdapter(Adapter);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    //method untuk menampilkan alert dialog untuk menambahkan data
    private void showDialogAdd(){
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.layout_tambah_kelas, null);
        dialogBuilder.setView(dialogView);

        //bisa pakai final atau tidak
        final Spinner spinnerNamaKelas = (Spinner) dialogView.findViewById(R.id.spinnerNamaKelas);
        final Spinner spinnerTingkatanKelas = (Spinner) dialogView.findViewById(R.id.spinnerTingkatanKelas);
        Button buttonAddKelas = (Button) dialogView.findViewById(R.id.tambahKelas);

        //set judul alert dialog agar tidak bingung
        dialogBuilder.setTitle("Daftar Kelas");
        final AlertDialog alertDialog = dialogBuilder.create();
        alertDialog.show();

        //membuat tombol addMurid bekerja dengan semestinya
        buttonAddKelas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String namaKelas = spinnerNamaKelas.getSelectedItem().toString();
                String tingkatanKelas = spinnerTingkatanKelas.getSelectedItem().toString();

                String idKelas = databaseReference.push().getKey();
                Kelas kelas = new Kelas(idKelas , namaKelas , tingkatanKelas );
                databaseReference.child(idKelas).setValue(kelas);

                Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content), "Data Berhasil Di Tambahkan", Snackbar.LENGTH_LONG);
                snackbar.show();

                alertDialog.dismiss();
            }
        });


    }

}
