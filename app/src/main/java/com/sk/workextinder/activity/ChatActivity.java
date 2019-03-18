package com.sk.workextinder.activity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.google.gson.Gson;
import com.sk.workextinder.R;
import com.sk.workextinder.model.User;
import com.sk.workextinder.util.AppConstants;
import com.sk.workextinder.util.AppPreferenceStorage;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class ChatActivity extends AppCompatActivity {

    LinearLayout linearLayout;
    RelativeLayout relativeLayout;
    ImageView imageViewSent;
    EditText editTextMessage;
    ScrollView scrollView;
    Firebase reference1, reference2;

    private User mChatWithUser;
    private User userLoggedIn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        getBundleData();
        setUpActionBar();
        getViewFromLayout();


        userLoggedIn = new Gson().fromJson(AppPreferenceStorage.getUserDetails(), User.class);

        Firebase.setAndroidContext(this);
        reference1 = new Firebase("https://workex-tinder.firebaseio.com/messages/" + userLoggedIn.getFirstName() + "_" + mChatWithUser.getFirstName());
        reference2 = new Firebase("https://workex-tinder.firebaseio.com/messages/" + mChatWithUser.getFirstName() + "_" + userLoggedIn.getFirstName());

        imageViewSent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String messageText = editTextMessage.getText().toString();

                if (!messageText.equals("")) {
                    Map<String, String> map = new HashMap<>();
                    map.put("message", messageText);
                    map.put("user", userLoggedIn.getFirstName());
                    reference1.push().setValue(map);
                    reference2.push().setValue(map);
                    editTextMessage.setText("");
                }
            }
        });

        reference1.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Map map = dataSnapshot.getValue(Map.class);
                String message = Objects.requireNonNull(map.get("message")).toString();
                String userName = Objects.requireNonNull(map.get("user")).toString();

                if (userName.equals(userLoggedIn.getFirstName())) {
                    addMessageBox("You:-\n" + message, 1);
                } else {
                    addMessageBox(mChatWithUser.getFirstName() + ":-\n" + message, 2);
                }
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
    }

    private void getBundleData() {
        mChatWithUser = getIntent().getParcelableExtra(AppConstants.MODEL);
    }

    @SuppressLint("RtlHardcoded")
    public void addMessageBox(String message, int type) {
        TextView textView = new TextView(ChatActivity.this);
        textView.setText(message);

        LinearLayout.LayoutParams lp2 = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        lp2.weight = 1.0f;

        if (type == 1) {
            lp2.gravity = Gravity.LEFT;
            textView.setBackgroundResource(R.drawable.bubble_in);
        } else {
            lp2.gravity = Gravity.RIGHT;
            textView.setBackgroundResource(R.drawable.bubble_out);
        }
        textView.setLayoutParams(lp2);
        linearLayout.addView(textView);
        scrollView.fullScroll(View.FOCUS_DOWN);
    }

    private void getViewFromLayout() {
        linearLayout = findViewById(R.id.linearLayout);
        relativeLayout = findViewById(R.id.relativeLayout);
        imageViewSent = findViewById(R.id.imageViewSent);
        editTextMessage = findViewById(R.id.editTextMessage);
        scrollView = findViewById(R.id.scrollView);
    }

    private void setUpActionBar() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) actionBar.setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        this.finish();
    }
}