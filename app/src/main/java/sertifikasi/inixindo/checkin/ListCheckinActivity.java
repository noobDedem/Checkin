package sertifikasi.inixindo.checkin;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class ListCheckinActivity extends AppCompatActivity {
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private ArrayList<Checkin> arrayList;
    private int response_code;
    private URL url = null;
    private HttpURLConnection conn;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_checkin);

        // Initialize new arraylist
        arrayList = new ArrayList<>();

        // Fetch the data
        fetchData();

        //  Setup the recycler view for displaying the data
        mRecyclerView = findViewById(R.id.recycler_view);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        // Setup the adapter
        mAdapter = new ListCheckinAdapter(arrayList, getApplicationContext());
        mRecyclerView.setAdapter(mAdapter);
    }

    /**
     * Fetch data from the URL and add it to the array list
     */
    public void fetchData() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                String APIUrl = "http://wp.garasitekno.com/service/lokasi.php";
                String json = "";

                try {
                    // Creating new URL
                    url = new URL(APIUrl);

                    // Connecting to URL
                    conn = (HttpURLConnection) url.openConnection();
                    conn.setReadTimeout(15000);
                    conn.setConnectTimeout(70000);
                    conn.setRequestMethod("GET");
                    conn.setDoOutput(true);

                    // Fetch the response code
                    response_code = conn.getResponseCode();

                    if (response_code == HttpURLConnection.HTTP_OK) {
                        // Fetch the inpur data
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
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(getApplicationContext(), "Gagal mengambil data", Toast.LENGTH_LONG).show();
                            }
                        });
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    conn.disconnect();
                }

                // Emptying the arraylist for initialization
                if (!arrayList.isEmpty()) {
                    arrayList.clear();
                }

                try {
                    if (response_code == HttpURLConnection.HTTP_OK) {
                        JSONObject level0 = new JSONObject(json);
                        String status = level0.getString("status");
                        if (status.equals("success")) {
                            JSONArray level1 = level0.getJSONArray("data");
                            for (int i = 0; i < level1.length(); i++) {
                                JSONObject level2 = level1.getJSONObject(i);
                                int id = level2.getInt("id");
                                String nama = level2.getString("nama");
                                String keterangan = level2.getString("keterangan");
                                String lat = level2.getString("lat");
                                String lon = level2.getString("lon");
                                String kontributor = level2.getString("kontributor");
                                arrayList.add(new Checkin(nama, keterangan, lon, lat, kontributor));
                            }
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mAdapter.notifyDataSetChanged();
                        System.out.println(arrayList);
                    }
                });
            }
        }).start();
    }
}
