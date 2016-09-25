package com.tom.fire3;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private boolean logon = false;
    FirebaseAuth auth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        auth = FirebaseAuth.getInstance();

        ListView list = (ListView) findViewById(R.id.list);
        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1,
                android.R.id.text1);
        list.setAdapter(adapter);
        //
        FirebaseDatabase db = FirebaseDatabase.getInstance();
        DatabaseReference contacts = db.getReference("contacts");
        contacts.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                adapter.add(
                        (String) dataSnapshot.child("name").getValue());
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                adapter.remove(
                        (String) dataSnapshot.child("name").getValue());
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        if ( !logon ){
            startActivity(new Intent(this, LoginActivity.class));
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser user = auth.getCurrentUser();
        if (user!=null){
            Log.d(TAG, "UID:"+user.getUid());
            Log.d(TAG, "email:"+user.getEmail());
            FirebaseDatabase db = FirebaseDatabase.getInstance();
            DatabaseReference users = db.getReference("users");
//            addFriend(user, users);
//            setNickname(user, users);

        }
    }

    private void addFriend(FirebaseUser user, DatabaseReference users) {
        DatabaseReference friends = users.child(user.getUid())
                .child("friends").push();
        Map<String,Object> friend = new HashMap<>();
        friend.put("name", "Jane");
        friend.put("phone", "948488383");
        friend.put("gendar", 0);
        friends.setValue(friend);
        Log.d(TAG, "Key:"+friends.getKey());
    }

    private void setNickname(FirebaseUser user, DatabaseReference users) {
        users.child(user.getUid())
                .child("nickname")
                .setValue("Jack");
    }
}






















