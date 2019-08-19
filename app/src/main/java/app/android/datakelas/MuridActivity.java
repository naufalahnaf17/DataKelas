package app.android.datakelas;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.github.clans.fab.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class MuridActivity extends AppCompatActivity {

    private DatePickerDialog.OnDateSetListener mDateSetListener;
    DatabaseReference databaseReference;
    FloatingActionButton fabMurid;
    TextView txtNamaKelasMurid , txtNamaTingkatanMurid , txtJumlahSiswaMurid;
    ListView listViewMurid;
    List<Murid> listMurid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_murid);

        txtNamaKelasMurid = (TextView)findViewById(R.id.txtNamaKelasMurid);
        txtNamaTingkatanMurid = (TextView)findViewById(R.id.txtNamaTingkatanMurid);
        txtJumlahSiswaMurid = (TextView)findViewById(R.id.txtJumlahSiswaMurid);
        fabMurid = (FloatingActionButton) findViewById(R.id.addBtnMurid);
        listViewMurid = (ListView) findViewById(R.id.listDataMurid);
        listMurid = new ArrayList<>();


        Intent intent = getIntent();
        String idKelas = intent.getStringExtra(MainActivity.ID_KELAS);
        String namaKelas = intent.getStringExtra(MainActivity.NAMA_KELAS);
        String tingkatanKelas = intent.getStringExtra(MainActivity.TINGKATAN_KELAS);

        txtNamaKelasMurid.setText(namaKelas);
        txtNamaTingkatanMurid.setText("Kelas "+tingkatanKelas);

        databaseReference = FirebaseDatabase.getInstance().getReference("Murid").child(idKelas);
        
        fabMurid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showAddDialogMurid();
            }
        });

        listViewMurid.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                Toast.makeText(MuridActivity.this, "Belum Ada Fitur Menghapus Murid , Maaf", Toast.LENGTH_SHORT).show();
                return false;
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();

        databaseReference.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                listMurid.clear();

                    for (DataSnapshot muridSnapShot : dataSnapshot.getChildren()){
                        Murid murid = muridSnapShot.getValue(Murid.class);
                        listMurid.add(murid);
                    }

                    MuridList adapter = new MuridList(MuridActivity.this,listMurid);
                    listViewMurid.setAdapter(adapter);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void showAddDialogMurid(){
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.layout_tambah_murid, null);
        dialogBuilder.setView(dialogView);

        //bisa pakai final atau tidak
        final EditText eNamaMurid = (EditText) dialogView.findViewById(R.id.eNamaMurid);
        final TextView txtTanggalLahir = (TextView) dialogView.findViewById(R.id.txtTanggalLahir);
        final Button buttonTanggal = (Button) dialogView.findViewById(R.id.btnTanggal);
        final Spinner spinnerKelamin = (Spinner) dialogView.findViewById(R.id.spinnerKelamin);
        Button buttonAddMurid = (Button) dialogView.findViewById(R.id.tambahMurid);

        //set judul alert dialog agar tidak bingung
        dialogBuilder.setTitle("Daftar Murid");
        final AlertDialog alertDialog = dialogBuilder.create();
        alertDialog.show();

        //membuat set date listener
        buttonTanggal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar cal = Calendar.getInstance();
                int tahun = cal.get(Calendar.YEAR);
                int bulan = cal.get(Calendar.MONTH);
                int hari = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(MuridActivity.this ,
                        android.R.style.Theme_Holo_Light_Dialog_MinWidth ,
                        mDateSetListener ,
                        tahun , bulan , hari);

                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }
        });

        mDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int tahun, int bulan, int hari) {
                bulan = bulan+1;
                String tanggal = bulan + " - " +hari+" - "+tahun;
                txtTanggalLahir.setText(tanggal);
            }
        };

        //membuat tombol addMurid bekerja dengan semestinya
        buttonAddMurid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String namaMurid = eNamaMurid.getText().toString().trim();
                String jenkelMurid = spinnerKelamin.getSelectedItem().toString();
                String tanggal = txtTanggalLahir.getText().toString();

                String idMurid = databaseReference.push().getKey();
                Murid murid = new Murid(idMurid , namaMurid , tanggal  , jenkelMurid  );
                databaseReference.child(idMurid).setValue(murid);

                Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content), "Data Berhasil Di Tambahkan", Snackbar.LENGTH_LONG);
                snackbar.show();

                alertDialog.dismiss();
            }
        });


    }
}
