package com.example.WorkToGo;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;


public class QRActivity extends AppCompatActivity {
    ImageView kodQR;
    Button przyciskLogout;
    Button przyciskGeneruj;
    String name;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // pobiera dane zalogowanego user (SharedPreferences)
        SharedPreferences prefs = getSharedPreferences("Preferences", MODE_PRIVATE);
        name = prefs.getString("name", "No name defined");
        setContentView(R.layout.activity_q_r);
        kodQR = findViewById(R.id.kod_qr);
        przyciskLogout = findViewById(R.id.przycisk_wyloguj);
        przyciskLogout.setOnClickListener(v -> {

            FirebaseAuth.getInstance().signOut();
            Intent intent = new Intent(QRActivity.this, MainActivity.class);
            startActivity(intent);
            Toast.makeText(QRActivity.this, "Pomyślnie wylogowano!", Toast.LENGTH_SHORT).show();
            finish();
        });

        przyciskGeneruj = findViewById(R.id.przycisk_generuj);
        przyciskGeneruj.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                generateQR();
            }

            public void generateQR() {

                MultiFormatWriter writer = new MultiFormatWriter();
                try {
                    BitMatrix matrix = writer.encode("Witaj" + " " + name + " " + "Nie zapomnij o odzieniu ochronnym przed wejściem na halę produkcyjną!", BarcodeFormat.QR_CODE
                            , 350, 350);
                    BarcodeEncoder encoder = new BarcodeEncoder();
                    Bitmap bitmap = encoder.createBitmap(matrix);
                    kodQR.setImageBitmap(bitmap);
                } catch (WriterException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}

