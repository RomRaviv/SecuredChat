package com.example.securedchat;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.Toast;

import com.example.securedchat.Models.DecryptEncrypt;
import com.example.securedchat.Models.Message;
import com.example.securedchat.Models.MessageAdapter;
import com.example.securedchat.Models.orderComparator;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.Collections;

public class MainActivity extends AppCompatActivity {

    private ArrayList<Message> encryptedMassages;
    private PopupWindow popup;
    //private final DataManager dataManager = DataManager.getInstance();
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private RecyclerView recycler_my_messages;
    private Button btn_send;
    private EditText text;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViews();
        encryptedMassages = new ArrayList<>();
        loadMessages();

        Bundle bundle = getIntent().getExtras();
        String name = bundle.getString("name");
        String key = bundle.getString("key");


        btn_send.setOnClickListener(view -> {
            try {
                String encryptMessage = DecryptEncrypt.encrypt(text.getText().toString(), key);
                Message temp = new Message(encryptMessage, name , encryptedMassages.size());
                encryptedMassages.add(encryptedMassages.size(),temp);
                saveMassages(temp);
                initAdapter();
                text.setText("");
                InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });



    }

    private void findViews() {
        recycler_my_messages = findViewById(R.id.recycler_my_messages);
        btn_send = findViewById(R.id.btn_send);
        text = findViewById(R.id.inputText);
    }

    private void initAdapter() {
        Collections.sort(encryptedMassages,new orderComparator());
        ArrayList<Message> messages = encryptedMassages;
        MessageAdapter messageAdapter = new MessageAdapter(this, messages);
        recycler_my_messages.setLayoutManager(new LinearLayoutManager(this));
        recycler_my_messages.setHasFixedSize(true);
        recycler_my_messages.setAdapter(messageAdapter);

        messageAdapter.setListener(new MessageAdapter.MessageListener() {
            @Override
            public void decryptMessage(Message message, int position) throws Exception {
                showPopupWindow(findViewById(R.id.main_activity),message);
            }

        });
    }
    public void loadMessages() {
        CollectionReference myRef = db.collection("Messages");
        myRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                for (QueryDocumentSnapshot document : task.getResult()) {
                    encryptedMassages.add(document.toObject(Message.class));
                }
                initAdapter();
            } else {
                Log.d("pttt", "Error getting documents: ", task.getException());
            }
        });
    }

    public void saveMassages(Message message) {
        db.collection("Messages").document(message.getMessageId()).set(message);
    }

    public void showPopupWindow(View view , Message message) throws Exception {

        LayoutInflater inflater = (LayoutInflater)
                getSystemService(LAYOUT_INFLATER_SERVICE);
        View popupView = inflater.inflate(R.layout.popup, null);

        popup = new PopupWindow(popupView, 800, 600, true);
        popup.showAtLocation(view, Gravity.CENTER, 0, 0);


        MaterialButton btn_popup_save =(MaterialButton) popupView.findViewById(R.id.btn_popup_save);
        MaterialButton btn_popup_exit =(MaterialButton) popupView.findViewById(R.id.btn_popup_exit);
        TextInputEditText lbl_popup_key = (TextInputEditText) popupView.findViewById(R.id.lbl_popup_key);
        TextInputLayout edt_popup_key = (TextInputLayout) popupView.findViewById(R.id.edt_popup_key);

        btn_popup_save.setOnClickListener(v -> {
            try {
                lbl_popup_key.setText(DecryptEncrypt.decrypt(message.getDescription(), lbl_popup_key.getText().toString()));
                edt_popup_key.setHint("message:");
            } catch (Exception e) {
                Toast.makeText(MainActivity.this, "This key is not correct", Toast.LENGTH_SHORT).show();
            }

        });

        btn_popup_exit.setOnClickListener(v -> popup.dismiss());
    }

}