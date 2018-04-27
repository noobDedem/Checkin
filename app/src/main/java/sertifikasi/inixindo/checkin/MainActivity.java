package sertifikasi.inixindo.checkin;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    private Button checkin, listCheckin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Assigning checkin and listcheckin button
        checkin = findViewById(R.id.checkin);
        listCheckin = findViewById(R.id.list_check_in);

        // Intent to checkin activity
        checkin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent checkinIntent = new Intent(getApplicationContext(), CheckinActivity.class);
                startActivity(checkinIntent);
            }
        });

        // Intent to list checkin activity
        listCheckin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent listCheckinIntent = new Intent(getApplicationContext(), ListCheckinActivity.class);
                startActivity(listCheckinIntent);
            }
        });
    }
}
