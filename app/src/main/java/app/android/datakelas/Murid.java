package app.android.datakelas;

public class Murid {
    String idMurid;
    String namaMurid;
    String tanggalMurid;
    String jenkelMurid;

    public Murid(){}

    public Murid(String idMurid, String namaMurid, String tanggalMurid, String jenkelMurid) {
        this.idMurid = idMurid;
        this.namaMurid = namaMurid;
        this.tanggalMurid = tanggalMurid;
        this.jenkelMurid = jenkelMurid;
    }

    public String getIdMurid() {
        return idMurid;
    }

    public String getNamaMurid() {
        return namaMurid;
    }

    public String getTanggalMurid() {
        return tanggalMurid;
    }

    public String getJenkelMurid() {
        return jenkelMurid;
    }
}
