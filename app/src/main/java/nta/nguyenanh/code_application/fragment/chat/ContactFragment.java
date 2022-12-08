package nta.nguyenanh.code_application.fragment.chat;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


import static nta.nguyenanh.code_application.MainActivity.userModel;
import static nta.nguyenanh.code_application.listener.FirebaseQuery.USERNAME;

import nta.nguyenanh.code_application.ChatActivity;
import nta.nguyenanh.code_application.R;
import nta.nguyenanh.code_application.adapter.UserAdapter;
import nta.nguyenanh.code_application.dialog.DiaLogProgess;
import nta.nguyenanh.code_application.listener.FirebaseQuery;
import nta.nguyenanh.code_application.interfaces.ItemClickSupport;
import nta.nguyenanh.code_application.model.User;


public class ContactFragment extends Fragment {

    private RecyclerView lvList;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_contact, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        DiaLogProgess diaLogProgess = new DiaLogProgess(getContext());
        diaLogProgess.showDialog("Loading");

        lvList = view.findViewById(R.id.lvList);
        FirebaseQuery.getListUser(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                GenericTypeIndicator<HashMap<String, User>> objectsGTypeInd =
                        new GenericTypeIndicator<HashMap<String, User>>() {
                        };
                Map<String, User> objectHashMap = dataSnapshot.getValue(objectsGTypeInd);
                Log.d("TAG user", "onDataChange: "+objectHashMap);

                final List<User> objectArrayList = new ArrayList<>(objectHashMap.values());
                Log.d("TAG user", "onDataChange: "+objectArrayList);

                    LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
                    UserAdapter userAdapter = new UserAdapter(getActivity(), objectArrayList);

                    lvList.setAdapter(userAdapter);
                    lvList.setLayoutManager(linearLayoutManager);
                    lvList.setHasFixedSize(true);
                    diaLogProgess.hideDialog();



                ItemClickSupport.addTo(lvList).setOnItemClickListener(new ItemClickSupport.OnItemClickListener() {
                    @Override
                    public void onItemClicked(RecyclerView recyclerView, int position, View v) {
                        if (userModel.getUserID().equals("7SAStKPFBVhIYxAZydtH")) {
                            String groupID = objectArrayList.get(position).getUsername() + "|" + userModel.getUserID();
                            FirebaseQuery.checkExistGroup(groupID, new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    Intent intent = new Intent(getActivity(), ChatActivity.class);
                                    intent.putExtra("data", dataSnapshot.getKey());
                                    Log.d("KEY", "onDataChange: " + dataSnapshot.getKey());
                                    startActivity(intent);
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });
                        } else {
                            String groupID = userModel.getUserID() + "|" + "7SAStKPFBVhIYxAZydtH";
//                        String groupID = userModel.getUserID() + "|" + objectArrayList.get(position).username;
                            FirebaseQuery.checkExistGroup(groupID, new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    Intent intent = new Intent(getActivity(), ChatActivity.class);
                                    intent.putExtra("data", dataSnapshot.getKey());
                                    Log.d("KEY", "onDataChange: " + dataSnapshot.getKey());
                                    startActivity(intent);
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });
                        }
                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }


}
