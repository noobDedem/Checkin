package sertifikasi.inixindo.checkin;

public class Checkin {
    private String namaLokasi;
    private String keteranganLokasi;
    private String longitude;
    private String latitude;
    private String kontributor;

    public Checkin(String namaLokasi, String keteranganLokasi, String longitude, String latitude, String kontributor) {
        this.namaLokasi = namaLokasi;
        this.keteranganLokasi = keteranganLokasi;
        this.longitude = longitude;
        this.latitude = latitude;
        this.kontributor = kontributor;
    }

    public String getNamaLokasi() {
        return namaLokasi;
    }

    public void setNamaLokasi(String namaLokasi) {
        this.namaLokasi = namaLokasi;
    }

    public String getKeteranganLokasi() {
        return keteranganLokasi;
    }

    public void setKeteranganLokasi(String keteranganLokasi) {
        this.keteranganLokasi = keteranganLokasi;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getKontributor() {
        return kontributor;
    }

    public void setKontributor(String kontributor) {
        this.kontributor = kontributor;
    }
}
