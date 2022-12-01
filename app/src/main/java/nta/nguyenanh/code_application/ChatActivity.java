package nta.nguyenanh.code_application;

import static nta.nguyenanh.code_application.MainActivity.userModel;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
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

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import nta.nguyenanh.code_application.adapter.ChatAdapter;
import nta.nguyenanh.code_application.listener.FirebaseQuery;
import nta.nguyenanh.code_application.model.Chat;


public class ChatActivity extends AppCompatActivity {

    public static int height = 0;
    public static int width = 0;
    //chon hinh tu gallery
    Button BSelectImage;
    Uri selectedImageUri;
    int SELECT_PICTURE = 200;
    boolean checkimage = false;
    private String group;
    private RecyclerView recyclerView;
    private EditText edtInput;
    private ImageView btnSend, btnimage;
    private List<Chat> objectArrayList;
    private Toolbar toolbar;
    private RelativeLayout activityChat;

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
        btnimage = findViewById(R.id.btnimage);
        btnSend.setEnabled(false);

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        height = displayMetrics.heightPixels;
        width = displayMetrics.widthPixels;

        group = getIntent().getStringExtra("data");

        setTitle(group);
        btnimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageChooser();


            }
        });

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
                btnSend.setEnabled(charSequence.length() > 0);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        objectArrayList = new ArrayList<>();
        ChatAdapter chatAdapter = new ChatAdapter(ChatActivity.this, objectArrayList);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(ChatActivity.this);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(chatAdapter);
        FirebaseQuery.getListMessages(group, new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                Chat chat = dataSnapshot.getValue(Chat.class);

                objectArrayList.add(chat);
//                recyclerView.smoothScrollToPosition(objectArrayList.size());
//                chatAdapter.notifyDataSetChanged();
                Log.d("ABC", "______________Begin____________");
                for (int i = 0; i < objectArrayList.size(); i++) {
                    Log.d("ABC", "Nội dung tin nhắn: " + objectArrayList.get(i).getText());
                }
                Log.d("ABC", "List size: " + objectArrayList.size());
                Log.d("ABC", "______________End____________");
                if (checkimage) {
                    objectArrayList.remove(objectArrayList.size() - 1);
                    checkimage = !checkimage;
                }
                chatAdapter.notifyDataSetChanged();
                recyclerView.smoothScrollToPosition(objectArrayList.size());
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
                if (height > 0.25 * activityView.getRootView().getHeight()) {
                    // bàn phím xuất hiện nhé
                    if (objectArrayList.size() > 0) {
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
    }

    void imageChooser() {

        // create an instance of the
        // intent of the type image
        Intent i = new Intent();
        i.setType("image/*");
        i.setAction(Intent.ACTION_GET_CONTENT);

        // pass the constant to compare it
        // with the returned requestCode
        startActivityForResult(Intent.createChooser(i, "Select Picture"), SELECT_PICTURE);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {

            // compare the resultCode with the
            // SELECT_PICTURE constant
            if (requestCode == SELECT_PICTURE) {
                // Get the url of the image from data
                selectedImageUri = data.getData();
                checkimage = true;
                objectArrayList.add(new Chat(userModel.getUserID(), String.valueOf(selectedImageUri), System.currentTimeMillis()));
                ChatAdapter chatAdapter = new ChatAdapter(ChatActivity.this, objectArrayList);
                recyclerView.setAdapter(chatAdapter);
                recyclerView.scrollToPosition(objectArrayList.size() - 1);
                if (null != selectedImageUri) {
                    Bitmap bitmap = null;
                    try {
                        bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImageUri);
                        uploadtofirebase(bitmap);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }


                    // update the preview image in the layout
//                    IVPreviewImage.setImageURI(selectedImageUri);
                }
            }
        }

    }

    private void uploadtofirebase(Bitmap bitmap) {
        Calendar c = Calendar.getInstance();
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageReference = storage.getReference();
        StorageReference imageReference = storageReference.child(c.getTimeInMillis() + ".jpg");
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
        byte[] bytes = outputStream.toByteArray();
        UploadTask uploadTask = imageReference.putBytes(bytes);
        uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
            @Override
            public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                if (task.isSuccessful()) {
                    return imageReference.getDownloadUrl();

                }
                return null;
            }
        }).addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task<Uri> task) {
                if (task.isSuccessful()) {
                    Uri dowloadUri = task.getResult();
                    edtInput.setText("");
                    FirebaseQuery.sendMessage(group, String.valueOf(dowloadUri), userModel.getUserID(), System.currentTimeMillis(), new DatabaseReference.CompletionListener() {
                        @Override
                        public void onComplete(@Nullable DatabaseError databaseError, @NonNull DatabaseReference databaseReference) {
                            edtInput.setText("");
                        }
                    });

                }
            }
        });
    }
}
