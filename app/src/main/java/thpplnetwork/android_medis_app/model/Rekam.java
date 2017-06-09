package thpplnetwork.android_medis_app.model;

/**
        * Created by ResaQulyubi on 4/2/2016.
        */
public class Rekam {
    private String id_rekam, nik,id_dokter,tgl_periksa,anjuran,indikator;



    public Rekam() {
    }

    public Rekam(String id_rekam, String nik,String id_dokter, String tgl_periksa,String anjuran,String indikator
                ) {
        this.id_rekam = id_rekam;
        this.nik = nik;
        this.id_dokter= id_dokter;
        this.tgl_periksa= tgl_periksa;
        this.anjuran=anjuran;
        this.indikator=indikator;
    }

    public String getId_rekam() {
        return id_rekam;
    }

    public void setId_rekam(String id_rekam) {
        this.id_rekam= id_rekam;
    }

    public String getNik() {
        return nik;
    }

    public void setNik(String nik) {
        this.nik= nik;
    }

    public String getid_dokter() { return id_dokter; }

    public void setId_dokter(String year) {
        this.id_dokter = id_rekam;
    }

    public String getTgl_periksa() {
        return tgl_periksa;
    }

    public void setTgl_periksa(String tgl_periksa) {
        this.tgl_periksa= tgl_periksa;
    }


    public String getAnjuran() {
        return anjuran;
    }

    public void setAnjuran(String anjuran) {
        this.anjuran= anjuran;
    }

    public String getIndikator() {
        return indikator;
    }

    public void setIndikator(String indikator) {
        this.indikator= indikator;
    }

}
