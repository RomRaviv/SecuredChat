package com.example.securedchat;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.firestore.FirebaseFirestore;

public class LoginActivity extends AppCompatActivity {

    private TextInputEditText edt_name;
    private TextInputEditText edt_key;
    private MaterialButton btn_login;
    private Intent intent;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        findsViews();
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = edt_name.getText().toString();
                String key = edt_key.getText().toString();
                if(key.length()<16){
                    Toast.makeText(LoginActivity.this, "Must be a 16-character key. Try again.", Toast.LENGTH_SHORT).show();
                    edt_key.setText("");
                } else {
                    Bundle bundle = new Bundle();
                    bundle.putString("name", name);
                    bundle.putString("key", key);
                    intent = new Intent(LoginActivity.this, MainActivity.class);
                    intent.putExtras(bundle);
                    startActivity(intent);
                    finish();
                }
            }
        });
    }


    private void findsViews(){
        btn_login = findViewById(R.id.main_BTN_sign_in);
        edt_name = findViewById(R.id.main_EDT_name);
        edt_key = findViewById(R.id.main_EDT_key);
    }
}