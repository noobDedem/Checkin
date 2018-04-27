package sertifikasi.inixindo.checkin;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class CheckinActivity extends AppCompatActivity implements LocationListener {
    private EditText namaLokasi, keteranganLokasi, longitude, latitude, kontributor;
    private Button checkin;
    private LocationManager locationManager;
    private URL url = null;
    private HttpURLConnection conn;
    private String json = "";
    private int responseCode;
    private String status;
    private static final int MY_PERMISSION_REQUEST_LOCATION = 39;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkin);

        // Assigning view and button
        namaLokasi = findViewById(R.id.nama_lokasi);
        keteranganLokasi = findViewById(R.id.keterangan_lokasi);
        longitude = findViewById(R.id.longitude);
        latitude = findViewById(R.id.latitude);
        kontributor = findViewById(R.id.kontributor);
        checkin = findViewById(R.id.check_in_button);

        // Disable edit text
        longitude.setFocusable(false);
        latitude.setFocusable(false);

        // Checking SDK version
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            checkLocationPermission();
        }

        // For some reason GPS_PROVIDED didn't work
        // And using NETWORK_PROVIDED sometimes took a long time to get the location
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        try {
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, this);
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Button to send the data
        checkin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        String APIUrl = "http://wp.garasitekno.com/service/lokasi.php?aksi=simpan&nama=" + namaLokasi.getText().toString() +
                                "&keterangan=" + keteranganLokasi.getText().toString() +
                                "&lat=" + latitude.getText().toString() +
                                "&lon=" + longitude.getText().toString() +
                                "&kontributor=" + kontributor.getText().toString();

                        try {
                            // Creating new URL
                            url = new URL(APIUrl.replaceAll(" ", "%20"));

                            // Connecting to URL
                            conn = (HttpURLConnection) url.openConnection();
                            conn.setReadTimeout(15000);
                            conn.setConnectTimeout(70000);
                            conn.setRequestMethod("GET");
                            conn.setDoOutput(true);

                            // Fetch the response code
                            responseCode = conn.getResponseCode();

                            if (responseCode == HttpURLConnection.HTTP_OK) {
                                // Fetch the input data
                                InputStream input = conn.getInputStream();
                                BufferedReader reader = new BufferedReader(new InputStreamReader(input));
                                StringBuilder result = new StringBuilder();
                                String line;

                                while ((line = reader.readLine()) != null) {
                                    result.append(line);
                                }

                                input.close();
                                reader.close();

                                json = result.toString();
                            } else {
                                Log.e("Connection Error", String.valueOf(responseCode));
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        } finally {
                            conn.disconnect();
                        }

                        try {
                            if (responseCode == HttpURLConnection.HTTP_OK) {
                                // Get the return message from json type
                                JSONObject returnMessage = new JSONObject(json);
                                status = returnMessage.getString("status");
                                Log.v("Status", status);

                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(getApplicationContext(), "status = " + status, Toast.LENGTH_LONG).show();
                                        Intent intent = new Intent(getApplicationContext(), ListCheckinActivity.class);
                                        startActivity(intent);
                                    }
                                });
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }).start();
            }
        });
    }

    /**
     * Checking if location permission is granted or not
     */
    public void checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // Ijin tidak didapatkan
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, MY_PERMISSION_REQUEST_LOCATION);
            } else {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, MY_PERMISSION_REQUEST_LOCATION);
            }
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        // Cek ulang apakah sudah mendapat ijin untuk menggunakan location
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            checkLocationPermission();
        }

        // Assigning the longitude and latitude from the location to the edit text
        String lon = String.valueOf(location.getLongitude());
        String lat = String.valueOf(location.getLatitude());
        longitude.setText(lon);
        latitude.setText(lat);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSION_REQUEST_LOCATION: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                        // Ijin berhasil didapatkan
                    }
                } else {
                    // Ijin ditolak, kembali ke menu sebelumnya
                    onBackPressed();
                    Toast.makeText(this, "permission denied", Toast.LENGTH_LONG).show();
                }
                break;
            }
        }
    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {

    }

    @Override
    public void onProviderDisabled(String s) {

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
