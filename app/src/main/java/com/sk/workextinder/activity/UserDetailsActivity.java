package com.sk.workextinder.activity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.sk.workextinder.R;
import com.sk.workextinder.model.User;
import com.sk.workextinder.util.AppAndroidUtils;
import com.sk.workextinder.util.AppConstants;
import com.sk.workextinder.util.CircleImageView;

/*
 * Created by Sambhaji Karad on 12-03-2019
 */

public class UserDetailsActivity extends AppCompatActivity implements OnMapReadyCallback {

    private User mUser;
    private CircleImageView imageViewProfile;
    private ImageView imageViewStatus;
    private TextView textViewName;
    private TextView textViewAge;
    private TextView textViewPhone;
    private TextView textViewEmail;

    private MapView mapView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_details);

        setUpActionBar();
        getViewFromLayout(savedInstanceState);
        getBundleData();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        GoogleMap gmap = googleMap;
        gmap.setMinZoomPreference(12);
        LatLng ny = new LatLng(mUser.getAddress().getGeo().getLatitude(), mUser.getAddress().getGeo().getLongitude());
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(ny);
        gmap.addMarker(markerOptions);
        gmap.moveCamera(CameraUpdateFactory.newLatLng(ny));
    }

    @SuppressLint("SetTextI18n")
    private void setUpDataWithUI() {
        if (mUser.getAvatar() != null && !mUser.getAvatar().isEmpty())
            Glide.with(this).load(mUser.getAvatar())
                    .into(imageViewProfile);

        String fullName = AppAndroidUtils.getFullName(mUser.getFirstName(), mUser.getLastName());
        if (!fullName.isEmpty()) textViewName.setText(fullName);

        if (mUser.getDob() != null && !mUser.getDob().isEmpty())
            textViewAge.setText(String.valueOf(AppAndroidUtils.getAge(mUser.getDob())));

        if (mUser.getPhone() != null && !mUser.getPhone().isEmpty())
            textViewPhone.setText(mUser.getPhone());

        if (mUser.getEmail() != null && !mUser.getEmail().isEmpty())
            textViewEmail.setText(mUser.getEmail());

        imageViewStatus.setBackgroundResource(mUser.isStatus() ? R.drawable.online : R.drawable.offline);
    }

    private void setUpActionBar() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) actionBar.setDisplayHomeAsUpEnabled(true);
    }

    private void getBundleData() {
        mUser = getIntent().getParcelableExtra(AppConstants.MODEL);

        setUpDataWithUI();
    }

    private void getViewFromLayout(Bundle savedInstanceState) {
        imageViewProfile = findViewById(R.id.imageViewProfile);
        imageViewStatus = findViewById(R.id.imageViewStatus);
        textViewName = findViewById(R.id.textViewName);
        textViewAge = findViewById(R.id.textViewAge);
        textViewPhone = findViewById(R.id.textViewPhone);
        textViewEmail = findViewById(R.id.textViewEmail);
        mapView = findViewById(R.id.mapView);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    protected void onStart() {
        super.onStart();
        mapView.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mapView.onStop();
    }

    @Override
    protected void onPause() {
        mapView.onPause();
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        mapView.onDestroy();
        super.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
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
