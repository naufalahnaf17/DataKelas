package app.android.datakelas;

public class Kelas {
    String idKelas;
    String namaKelas;
    String tingkatanKelas;

    public Kelas(){}

    public Kelas(String idKelas, String namaKelas, String tingkatanKelas ) {
        this.idKelas = idKelas;
        this.namaKelas = namaKelas;
        this.tingkatanKelas = tingkatanKelas;
    }

    public String getIdKelas() {
        return idKelas;
    }

    public String getNamaKelas() {
        return namaKelas;
    }

    public String getTingkatanKelas() {
        return tingkatanKelas;
    }

}
