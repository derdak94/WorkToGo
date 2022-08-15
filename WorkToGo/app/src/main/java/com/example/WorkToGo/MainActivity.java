package com.example.WorkToGo;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {
    // W tym miejscu jest definicja obiektow do ktorych wprowadzamy dane

    EditText etUsername, etPassword;
    Button btSubmit;
    CheckBox showpassword;
    private FirebaseAuth mAuth; // prywatna klasa FireBase Auth. Jest prywatna poniewaz chcemy zeby tylko kilka funkcji mialo do niej dostep

    @Override // Tworzenie interfejsu aplikacji
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Przypisujemy wartosci pol graficznych z makiety aplikacji do zmiennych, ktore koduja dzialanie przyciskow
        setContentView(R.layout.activity_main);
        mAuth = FirebaseAuth.getInstance();
        etUsername = findViewById(R.id.et_username);
        etPassword = findViewById(R.id.et_password);
        showpassword = findViewById(R.id.showpassword);
        showpassword.setOnCheckedChangeListener((buttonView, isChecked) -> {

            // funkcja logiczna, ktora odpowiada za odslanianie i ukrywanie hasla.
            if (isChecked) {
                etPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
            } else {
                etPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
            }
        });


            btSubmit = findViewById(R.id.bt_submit);
            btSubmit.setOnClickListener(v -> {

                // Mechanizm obslugi bledow. Jeśli pole hasla jest puste to wyswietla komunikat.
                try {
                    signIn(etUsername.getText().toString(), etPassword.getText().toString());
                }
                catch(Exception e) {
                    Toast.makeText(MainActivity.this, "Brak loginu lub hasła", Toast.LENGTH_SHORT).show();
                }

            });
    }



    @Override
    public void onStart() {
        super.onStart();
        // Logowanie do bazy Firebase.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            currentUser.reload();
        }
    }
        // Logowanie nastepuje tutaj.
    private void signIn(String email, String password) {

        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(this, task -> {
            if (task.isSuccessful()) {
                Toast.makeText(MainActivity.this, "Pomyślnie zalogowano!", Toast.LENGTH_SHORT).show();
                SharedPreferences.Editor editor = getSharedPreferences("Preferences", MODE_PRIVATE).edit();
                editor.putString("name", etUsername.getText().toString());
                editor.apply(); // koniec uwierzytelnienia
                Intent intent = new Intent(MainActivity.this, QRActivity.class);
                startActivity(intent);
                finish();

            } else {
                Toast.makeText(MainActivity.this, "Nieprawidłowy login lub hasło!", Toast.LENGTH_SHORT).show();
            }
        });
    }
}





