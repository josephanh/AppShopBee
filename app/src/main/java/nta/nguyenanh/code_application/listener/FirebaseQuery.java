package nta.nguyenanh.code_application.listener;


import android.os.Build;
import android.util.Log;


import androidx.annotation.RequiresApi;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

import nta.nguyenanh.code_application.model.Chat;


public class FirebaseQuery<T> {
    public static String TAG = "FB";
    public static final String MESSAGES = "Messages";
    public static final String GROUPS = "Groups";
    public static final String USERS = "Users";
    public static String USERNAME = "";
    public static void checkExitsUsername(String username, ValueEventListener valueEventListener) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        database.getReference(USERS).child(username);
        DatabaseReference users = database.getReference(USERS).child(username);
        users.addListenerForSingleValueEvent(valueEventListener);
    }
    public static void writeTo(String ref) {
        // Write a message to the database
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("messages");
        myRef.setValue("Hello, World!");
    }

    public static void readFrom() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("messages");

        // Read from the database
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                String value = dataSnapshot.getValue(String.class);
                Log.d(TAG, "Value is: " + value);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });

    }


    public static void getListGroups(String username, ValueEventListener valueEventListener) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference(GROUPS);

        myRef.orderByKey().startAt(username).endAt(username.concat("\uf8ff")).addValueEventListener(valueEventListener);

    }
    public static void getListMessages(String path, ChildEventListener valueEventListener) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = database.getReference(MESSAGES).child(path);
        Query query = databaseReference.orderByValue();
        databaseReference.addChildEventListener(valueEventListener);
    }

    public static void getListMessages(String path, ValueEventListener valueEventListener) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = database.getReference(MESSAGES).child(path);
        Query query = databaseReference.orderByValue();
        query.addValueEventListener(valueEventListener);
    }

    public static void sendMessage(String id, String text, String username, long currentTimeMillis, DatabaseReference.CompletionListener
            completionListener) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();

        DatabaseReference myRefGroup = database.getReference(GROUPS).child(id);

        Map<String, Object> hopperUpdates = new HashMap<>();
        hopperUpdates.put("id", id);
        hopperUpdates.put("lastUpdate", text);
        hopperUpdates.put("time", System.currentTimeMillis());
        myRefGroup.updateChildren(hopperUpdates);


        DatabaseReference myRefMessage = database.getReference(MESSAGES).child(id);
        Chat chat = new Chat(username, text, System.currentTimeMillis());

//        myRefMessage.push().setValue(chat, completionListener);

        HashMap<String,Object> chatHashMap = new HashMap<>();
        chatHashMap.put(System.currentTimeMillis()+"",chat);
        myRefMessage.updateChildren(chatHashMap);


    }
    //lấy tin nhắn
    public static void getMessage(){

    }

    public static void getListUser(ValueEventListener valueEventListener) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRefUsers = database.getReference(USERS);
        myRefUsers.addValueEventListener(valueEventListener);
    }


    public static void checkExistGroup(String groupsID, ValueEventListener valueEventListener) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRefUsers = database.getReference(GROUPS).child(groupsID);
        myRefUsers.addListenerForSingleValueEvent(valueEventListener);

    }
}
