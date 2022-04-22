package com.chemical_son.muslim.activity;


import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Adapter;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.chemical_son.muslim.ApiInterface;
import com.chemical_son.muslim.DayData.OneDay;
import com.chemical_son.muslim.DayData.Timings;
import com.chemical_son.muslim.R;
import com.chemical_son.muslim.adapter.timeAdapter.DataModel;
import com.chemical_son.muslim.adapter.timeAdapter.RecyclerAdapter;

import org.json.JSONException;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class HomeFragment extends Fragment implements LocationListener {

    private Retrofit retrofit;

    private RecyclerView recyclerView;

    private Location location;
    protected LocationManager locationManager;

    SwipeRefreshLayout swipeRefreshLayout;

    public HomeFragment() {
        super(R.layout.fragment_home);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        swipeRefreshLayout = view.findViewById(R.id.home_swipe);

        recyclerView = view.findViewById(R.id.rec_view_home_fragment);

        retrofit = new Retrofit.Builder()
                .baseUrl("https://api.aladhan.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        HashMap<String, Double> map = getLatitudeAndLongitude();
        if(!map.isEmpty())
            getDataFromApi(map.get("latitude"), map.get("longitude"), 1);

        swipeRefreshLayout.setOnRefreshListener(() -> {
            HashMap<String, Double> dataMap = getLatitudeAndLongitude();
            if(!dataMap.isEmpty())
                getDataFromApi(dataMap.get("latitude"), dataMap.get("longitude"), 1);
                swipeRefreshLayout.setRefreshing(false);
        });
    }

    private HashMap<String, Double> getLatitudeAndLongitude(){
        HashMap<String, Double> map = new HashMap<>();

        checkLocationPermission();

        locationManager = (LocationManager)getActivity().getSystemService(Context.LOCATION_SERVICE);
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);

        // getting GPS status
        boolean isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);

        // getting network status
        boolean isNetworkEnabled = locationManager
                .isProviderEnabled(LocationManager.NETWORK_PROVIDER);

        if (isGPSEnabled && isNetworkEnabled) {
            locationManager.requestLocationUpdates(
                    LocationManager.NETWORK_PROVIDER,
                    0,
                    0, this);

            if (locationManager != null) {
                location = locationManager
                        .getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

                if (location != null) {
                    map.put("longitude", location.getLongitude());
                    map.put("latitude", location.getLatitude());
                    locationManager.removeUpdates(this);
                }
            }
        }
        return map;
    }

    private void getDataFromApi(double latitude, double longitude, int method){
        ApiInterface apiInterface = retrofit.create(ApiInterface.class);

        Call<OneDay> call = apiInterface.getDay(latitude, longitude, method);

        call.enqueue(new Callback<OneDay>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onResponse(Call<OneDay> call, Response<OneDay> response) {

                Timings timesData = response.body().getData().getTimings();

                ArrayList<DataModel> data = new ArrayList<>();

                data.add(new DataModel(getString(R.string.fajr), parseHHMMA(timesData.getFajr())));
                data.add(new DataModel(getString(R.string.dhuhr), parseHHMMA(timesData.getDhuhr())));
                data.add(new DataModel(getString(R.string.asr), parseHHMMA(timesData.getAsr())));
                data.add(new DataModel(getString(R.string.maghrib), parseHHMMA(timesData.getMaghrib())));
                data.add(new DataModel(getString(R.string.isha), parseHHMMA(timesData.getIsha())));

                fillRecycleView(data);
                Toast.makeText(getContext(), getString(R.string.updated_successfully), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<OneDay> call, Throwable t) {
                Toast.makeText(getContext(), getString(R.string.weak_network_connection), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void checkLocationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            try {
                if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED ) {
                    ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION,
                            Manifest.permission.ACCESS_COARSE_LOCATION}, 101);
                }
            } catch (Exception e){
                e.printStackTrace();
            }
        }
    }


    @SuppressLint("SimpleDateFormat")
    public String parseHHMMA(String time) {
        String inputPattern = "HH:mm";
        String outputPattern = "h:mm a";
        SimpleDateFormat inputFormat = new SimpleDateFormat(inputPattern);
        SimpleDateFormat outputFormat = new SimpleDateFormat(outputPattern);

        Date date;
        String str = null;

        try {
            date = inputFormat.parse(time);
            str = outputFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return str;
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        locationManager.removeUpdates(this);
    }

    @SuppressLint("CutPasteId")
    public void fillRecycleView(ArrayList<DataModel> data) {

        RecyclerAdapter adapter = new RecyclerAdapter(data);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onLocationChanged(Location location) {
        locationManager.removeUpdates(this);
        this.location = location;

        //Toast.makeText(MainActivity.this, "Latitude: " + location.getLatitude(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onProviderDisabled(String provider) {
        Log.d("Latitude", "disable");
    }

    @Override
    public void onProviderEnabled(String provider) {
        Log.d("Latitude", "enable");
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        Log.d("Latitude", "status");
    }

}
