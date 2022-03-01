package com.example.muslim;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity implements LocationListener {
    Location location;

    protected LocationManager locationManager;
    TextView txtLat;
    SharedPreferences savedTimes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TextView tx = findViewById(R.id.textView);

        savedTimes = getSharedPreferences("savedTimes", MODE_PRIVATE);

        String defaultData = getResources().getString(R.string.default_data);
        String oldData = savedTimes.getString("oldTimes", defaultData);
        tx.setText(oldData);

        JSONObject jsonObject;
        try {
            jsonObject = new JSONObject(oldData);
//            Toast.makeText(MainActivity.this, jsonObject.toString(), Toast.LENGTH_SHORT).show();
            fillRecycleView(jsonObject.getJSONArray("datetime").getJSONObject(0));
        } catch (JSONException e) {
            e.printStackTrace();
        }


        try {
            //get current location
            locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 0, this);
            location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
            if (location == null) {
                //This is what you need:
                locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 1000, 0, this);
                location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
            }

            //update old data if phone is connected to the internet
            HandleRequests handleRequests = new HandleRequests(MainActivity.this);
            handleRequests.getAzaanTimes(location.getLatitude(), location.getLongitude(), 333.0, 1, new HandleRequests.VolleyResponseListener() {
                @Override
                public void onResponse(boolean status, JSONObject jsonObject) {
                    if (status) {
                        try {
                            Toast.makeText(MainActivity.this, getResources().getString(R.string.updated_successfully), Toast.LENGTH_SHORT).show();
                            //store new data in sharedPreferences
                            savedTimes = getSharedPreferences("oldTimes", MODE_PRIVATE);
                            SharedPreferences.Editor editor = savedTimes.edit();
                            editor.putString("oldTimes", jsonObject.getJSONObject("results").toString());
                            editor.commit();
                            fillRecycleView(jsonObject.getJSONObject("results").getJSONArray("datetime").getJSONObject(0));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    } else {
                        Toast.makeText(MainActivity.this, getResources().getString(R.string.not_updated), Toast.LENGTH_SHORT).show();
                    }
                }
            });

            txtLat = findViewById(R.id.textview1);
            txtLat.setText("Latitude:" + location.getLatitude() + ", Longitude:" + location.getLongitude());

            savedTimes = getSharedPreferences("savedTimes", MODE_PRIVATE);

            defaultData = getResources().getString(R.string.default_data);
            oldData = savedTimes.getString("oldTimes", defaultData);
            tx.setText(oldData);

            try {
                jsonObject = new JSONObject(oldData);
//            Toast.makeText(MainActivity.this, jsonObject.toString(), Toast.LENGTH_SHORT).show();
                fillRecycleView(jsonObject.getJSONArray("datetime").getJSONObject(0));
            } catch (JSONException e) {
                e.printStackTrace();
            }

            Button bt = findViewById(R.id.refreshButton);

            bt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    HandleRequests handleRequests = new HandleRequests(MainActivity.this);
                    handleRequests.getAzaanTimes(location.getLatitude(), location.getLongitude(), 333.0, 1, new HandleRequests.VolleyResponseListener() {
                        @Override
                        public void onResponse(boolean status, JSONObject jsonObject) {
                            if (status) {
                                try {
                                    tx.setText(jsonObject.getJSONObject("results").toString());

                                    Toast.makeText(MainActivity.this, getResources().getString(R.string.updated_successfully), Toast.LENGTH_SHORT).show();

                                    //store api token in sharedPreferences
                                    SharedPreferences.Editor editor = savedTimes.edit();
                                    editor.putString("oldTimes", jsonObject.getJSONObject("results").toString());
                                    editor.commit();

                                    fillRecycleView(jsonObject.getJSONObject("results").getJSONArray("datetime").getJSONObject(0));

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            } else {
                                Toast.makeText(MainActivity.this, getResources().getString(R.string.not_updated), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            });
        }
        catch (Exception e){
            System.out.println(e.getMessage());
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        locationManager.removeUpdates(this);

    }

    @SuppressLint("CutPasteId")
    public void fillRecycleView(JSONObject jsonObject) {
        //JSONArray jsonArray = jsonObject.names();
        try {
            TextView name, time;
            name = findViewById(R.id.gregorian).findViewById(R.id.name);
            time = findViewById(R.id.gregorian).findViewById(R.id.date);
            name.setText(getResources().getString(R.string.gregorian));
            time.setText(jsonObject.getJSONObject("date").getString("gregorian"));

            name = findViewById(R.id.hijri).findViewById(R.id.name);
            time = findViewById(R.id.hijri).findViewById(R.id.date);
            name.setText(getResources().getString(R.string.hijri));
            time.setText(jsonObject.getJSONObject("date").getString("hijri"));

            name = findViewById(R.id.fajr).findViewById(R.id.name);
            time = findViewById(R.id.fajr).findViewById(R.id.time);
            name.setText(getResources().getString(R.string.fajr));
            time.setText(jsonObject.getJSONObject("times").getString("Fajr"));

            name = findViewById(R.id.sunrise).findViewById(R.id.name);
            time = findViewById(R.id.sunrise).findViewById(R.id.time);
            name.setText(getResources().getString(R.string.sunrise));
            time.setText(jsonObject.getJSONObject("times").getString("Sunrise"));

            name = findViewById(R.id.dhuhr).findViewById(R.id.name);
            time = findViewById(R.id.dhuhr).findViewById(R.id.time);
            name.setText(getResources().getString(R.string.dhuhr));
            time.setText(jsonObject.getJSONObject("times").getString("Dhuhr"));

            name = findViewById(R.id.asr).findViewById(R.id.name);
            time = findViewById(R.id.asr).findViewById(R.id.time);
            name.setText(getResources().getString(R.string.asr));
            time.setText(jsonObject.getJSONObject("times").getString("Asr"));

            name = findViewById(R.id.maghrib).findViewById(R.id.name);
            time = findViewById(R.id.maghrib).findViewById(R.id.time);
            name.setText(getResources().getString(R.string.maghrib));
            time.setText(jsonObject.getJSONObject("times").getString("Maghrib"));

            name = findViewById(R.id.isha).findViewById(R.id.name);
            time = findViewById(R.id.isha).findViewById(R.id.time);
            name.setText(getResources().getString(R.string.isha));
            time.setText(jsonObject.getJSONObject("times").getString("Isha"));

        } catch (JSONException e) {
            System.out.println(e.getMessage());
        }
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