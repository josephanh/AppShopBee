package nta.nguyenanh.code_application;

import static nta.nguyenanh.code_application.MainActivity.userModel;
import static nta.nguyenanh.code_application.listener.FirebaseQuery.USERNAME;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.graphics.Rect;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;


import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import nta.nguyenanh.code_application.adapter.ChatAdapter;
import nta.nguyenanh.code_application.listener.FirebaseQuery;
import nta.nguyenanh.code_application.model.Chat;


public class ChatActivity extends AppCompatActivity {

    private String group;
    private RecyclerView recyclerView;
    private EditText edtInput;
    private ImageView btnSend;
    private List<Chat> objectArrayList;
    private Toolbar toolbar;
    private RelativeLayout activityChat;

    public static int height = 0;
    public static int width = 0;

    @SuppressLint({"MissingInflatedId", "WrongViewCast"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        getSupportActionBar().setDisplayShowHomeEnabled(true);
        activityChat = findViewById(R.id.activityChat);
        recyclerView = findViewById(R.id.lvList);
        edtInput = findViewById(R.id.edtInput);
        btnSend = findViewById(R.id.btnSend);
        btnSend.setEnabled(false);

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        height = displayMetrics.heightPixels;
        width = displayMetrics.widthPixels;

        group = getIntent().getStringExtra("data");

        setTitle(group);

        edtInput.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkBroadKey();
            }
        });
        edtInput.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                checkBroadKey();
            }
        });

        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String text = edtInput.getText().toString().trim();

                if (text.isEmpty()) {
                    edtInput.setError(getString(R.string.notify_empty_text));
                    return;
                }

                edtInput.setText("");
                btnSend.setEnabled(false);
                FirebaseQuery.sendMessage(group, text, userModel.getUserID(), System.currentTimeMillis(), new DatabaseReference.CompletionListener() {
                    @Override
                    public void onComplete(@Nullable DatabaseError databaseError, @NonNull DatabaseReference databaseReference) {
                        edtInput.setText("");
                    }
                });

            }
        });

        edtInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.length() > 0) {
                    btnSend.setEnabled(true);
                } else btnSend.setEnabled(false);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        objectArrayList = new ArrayList<>();
//        final ChatAdapter chatAdapter = new ChatAdapter(ChatActivity.this, objectArrayList);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(ChatActivity.this);
        recyclerView.setLayoutManager(linearLayoutManager);
//        recyclerView.setAdapter(chatAdapter);
        FirebaseQuery.getListMessages(group, new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                Chat chat = dataSnapshot.getValue(Chat.class);
                objectArrayList.add(chat);
                recyclerView.smoothScrollToPosition(objectArrayList.size());
//                recyclerView.smoothScrollToPosition(objectArrayList.size());
//                chatAdapter.notifyDataSetChanged();
                Log.d("ABC", "______________Begin____________");
                for (int i = 0; i < objectArrayList.size(); i++) {
                    Log.d("ABC", "Nội dung tin nhắn: " + objectArrayList.get(i).getText());
                }
                Log.d("ABC", "List size: " + objectArrayList.size());
                Log.d("ABC", "______________End____________");
                ChatAdapter chatAdapter = new ChatAdapter(ChatActivity.this, objectArrayList);
                recyclerView.setAdapter(chatAdapter);
//                Log.e("ABC", "Nội dung tin nhắn: "+chat.getText());
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        FirebaseQuery.getListMessages(group, new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                GenericTypeIndicator<HashMap<String, Chat>> objectsGTypeInd =
                        new GenericTypeIndicator<HashMap<String, Chat>>() {
                        };

                Map<String, Chat> objectHashMap = dataSnapshot.getValue(objectsGTypeInd);

                if (objectHashMap != null) {
//                    final List<Chat> objectArrayList = new ArrayList<>(objectHashMap.values());
//                    ChatAdapter chatAdapter = new ChatAdapter(ChatActivity.this, objectArrayList);
//                    LinearLayoutManager linearLayoutManager = new LinearLayoutManager(ChatActivity.this);
//                    recyclerView.setLayoutManager(linearLayoutManager);
//                    recyclerView.setAdapter(chatAdapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    // youtube https://www.youtube.com/watch?v=LpBEpyDz1dk&ab_channel=TinCoderAndroidJava
    private void checkBroadKey() {
        final View activityView = findViewById(R.id.activityChat);
        activityView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                Rect r = new Rect();
                activityView.getWindowVisibleDisplayFrame(r);
                int height = activityView.getRootView().getHeight() - r.height();
                if(height > 0.25*activityView.getRootView().getHeight()) {
                    // bàn phím xuất hiện nhé
                    if(objectArrayList.size() > 0) {
                        recyclerView.scrollToPosition(objectArrayList.size() - 1);
                        activityView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                    }
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onStop() {
        super.onStop();
        objectArrayList = new ArrayList<>();
    }
}
