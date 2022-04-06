package com.example.muslim.activites;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.muslim.R;
import com.example.muslim.adapter.TimesData;
import com.example.muslim.adapter.TimesAdapter;
import com.example.muslim.request_handelers.HandleRequests;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class TimesActivity extends AppCompatActivity implements LocationListener {
    Location location;

    protected LocationManager locationManager;
    SharedPreferences savedTimes;
    ListView main_list_v;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_times);

        main_list_v = findViewById(R.id.main_list_v);


        savedTimes = getSharedPreferences("savedTimes", MODE_PRIVATE);

        String defaultData = getResources().getString(R.string.default_data);
        String oldData = savedTimes.getString("oldTimes", defaultData);

        JSONObject jsonObject;
        try {
            jsonObject = new JSONObject(oldData);
//            Toast.makeText(MainActivity.this, jsonObject.toString(), Toast.LENGTH_SHORT).show();
            fillRecycleView(jsonObject);
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
            HandleRequests handleRequests = new HandleRequests(TimesActivity.this);
            handleRequests.getAzaanTimes(location.getLatitude(), location.getLongitude(), 333.0, 1, new HandleRequests.VolleyResponseListener() {
                @Override
                public void onResponse(boolean status, JSONObject jsonObject) {
                    if (status) {
                        try {
                            Toast.makeText(TimesActivity.this, getResources().getString(R.string.updated_successfully), Toast.LENGTH_SHORT).show();
                            //store new data in sharedPreferences
                            SharedPreferences.Editor editor = savedTimes.edit();
                            editor.putString("oldTimes", jsonObject.getJSONObject("data").toString());
                            editor.commit();
                            fillRecycleView(jsonObject.getJSONObject("data"));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    } else {
                        Toast.makeText(TimesActivity.this, getResources().getString(R.string.not_updated), Toast.LENGTH_SHORT).show();
                    }
                }
            });

            savedTimes = getSharedPreferences("savedTimes", MODE_PRIVATE);

            defaultData = getResources().getString(R.string.default_data);
            oldData = savedTimes.getString("oldTimes", defaultData);

            try {
                jsonObject = new JSONObject(oldData);
//            Toast.makeText(MainActivity.this, jsonObject.toString(), Toast.LENGTH_SHORT).show();
                fillRecycleView(jsonObject);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            Button bt = findViewById(R.id.refreshButton);

            bt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    HandleRequests handleRequests = new HandleRequests(TimesActivity.this);
                    handleRequests.getAzaanTimes(location.getLatitude(), location.getLongitude(), 333.0, 1, new HandleRequests.VolleyResponseListener() {
                        @Override
                        public void onResponse(boolean status, JSONObject jsonObject) {
                            if (status) {
                                try {

                                    Toast.makeText(TimesActivity.this, getResources().getString(R.string.updated_successfully), Toast.LENGTH_SHORT).show();
                                    sendNotification(getResources().getString(R.string.updated_successfully));
                                    //store api token in sharedPreferences
                                    SharedPreferences.Editor editor = savedTimes.edit();
                                    editor.putString("oldTimes", jsonObject.getJSONObject("data").toString());
                                    editor.commit();

                                    fillRecycleView(jsonObject.getJSONObject("data"));

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            } else {
                                Toast.makeText(TimesActivity.this, getResources().getString(R.string.not_updated), Toast.LENGTH_SHORT).show();
                                sendNotification(getResources().getString(R.string.not_updated));
                            }
                        }
                    });
                }
            });

        }
        catch (Exception e){
            System.out.println(e.getMessage());
        }

        Button btGoToQuranPage = findViewById(R.id.goToFehresPage);
        btGoToQuranPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TimesActivity.this, FehrisActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        locationManager.removeUpdates(this);

    }

    @SuppressLint("CutPasteId")
    public void fillRecycleView(JSONObject jsonObject) {
        //JSONArray jsonArray = jsonObject.names();

        //call adapter to set all data
        ArrayList<TimesData> data = new ArrayList<>();
        TimesData temp;

        try {
            temp = new TimesData(getResources().getString(R.string.fajr),
                    parseHHMMA(jsonObject.getJSONObject("timings").getString("Fajr")));
            data.add(temp);

            temp = new TimesData(getResources().getString(R.string.sunrise),
                    parseHHMMA(jsonObject.getJSONObject("timings").getString("Sunrise")));
            data.add(temp);

            temp = new TimesData(getResources().getString(R.string.dhuhr),
                    parseHHMMA(jsonObject.getJSONObject("timings").getString("Dhuhr")));
            data.add(temp);

            temp = new TimesData(getResources().getString(R.string.asr),
                    parseHHMMA(jsonObject.getJSONObject("timings").getString("Asr")));
            data.add(temp);

            temp = new TimesData(getResources().getString(R.string.sunset),
                    parseHHMMA(jsonObject.getJSONObject("timings").getString("Sunset")));
            data.add(temp);

            temp = new TimesData(getResources().getString(R.string.maghrib),
                    parseHHMMA(jsonObject.getJSONObject("timings").getString("Maghrib")));
            data.add(temp);

            temp = new TimesData(getResources().getString(R.string.isha),
                    parseHHMMA(jsonObject.getJSONObject("timings").getString("Isha")));
            data.add(temp);

            temp = new TimesData(getResources().getString(R.string.imsak),
                    parseHHMMA(jsonObject.getJSONObject("timings").getString("Imsak")));
            data.add(temp);

            temp = new TimesData(getResources().getString(R.string.midnight),
                    parseHHMMA(jsonObject.getJSONObject("timings").getString("Midnight")));
            data.add(temp);

        } catch (JSONException e) {
            System.out.println("error: " + e.getMessage());
        }

        //Toast.makeText(this, "data: "+ display(data), Toast.LENGTH_LONG).show();
        TimesAdapter adapter = new TimesAdapter(TimesActivity.this, data);
        main_list_v.setAdapter(adapter);

//        try {
//            TextView name, time;
//            name = findViewById(R.id.gregorian).findViewById(R.id.name);
//            time = findViewById(R.id.gregorian).findViewById(R.id.date);
//            name.setText(getResources().getString(R.string.gregorian));
//            time.setText(jsonObject.getJSONObject("date").getJSONObject("gregorian").getString("date"));
//
//            name = findViewById(R.id.hijri).findViewById(R.id.name);
//            time = findViewById(R.id.hijri).findViewById(R.id.date);
//            name.setText(getResources().getString(R.string.hijri));
//            time.setText(jsonObject.getJSONObject("date").getJSONObject("hijri").getString("date"));
//
//            name = findViewById(R.id.fajr).findViewById(R.id.name);
//            time = findViewById(R.id.fajr).findViewById(R.id.time);
//            name.setText(getResources().getString(R.string.fajr));
//            time.setText(parseHHMMA(jsonObject.getJSONObject("timings").getString("Fajr")));
//
//            name = findViewById(R.id.sunrise).findViewById(R.id.name);
//            time = findViewById(R.id.sunrise).findViewById(R.id.time);
//            name.setText(getResources().getString(R.string.sunrise));
//            time.setText(parseHHMMA(jsonObject.getJSONObject("timings").getString("Sunrise")));
//
//            name = findViewById(R.id.dhuhr).findViewById(R.id.name);
//            time = findViewById(R.id.dhuhr).findViewById(R.id.time);
//            name.setText(getResources().getString(R.string.dhuhr));
//            time.setText(parseHHMMA(jsonObject.getJSONObject("timings").getString("Dhuhr")));
//
//            name = findViewById(R.id.asr).findViewById(R.id.name);
//            time = findViewById(R.id.asr).findViewById(R.id.time);
//            name.setText(getResources().getString(R.string.asr));
//            time.setText(parseHHMMA(jsonObject.getJSONObject("timings").getString("Asr")));
//
//            name = findViewById(R.id.sunset).findViewById(R.id.name);
//            time = findViewById(R.id.sunset).findViewById(R.id.time);
//            name.setText(getResources().getString(R.string.sunset));
//            time.setText(parseHHMMA(jsonObject.getJSONObject("timings").getString("Sunset")));
//
//            name = findViewById(R.id.maghrib).findViewById(R.id.name);
//            time = findViewById(R.id.maghrib).findViewById(R.id.time);
//            name.setText(getResources().getString(R.string.maghrib));
//            time.setText(parseHHMMA(jsonObject.getJSONObject("timings").getString("Maghrib")));
//
//            name = findViewById(R.id.isha).findViewById(R.id.name);
//            time = findViewById(R.id.isha).findViewById(R.id.time);
//            name.setText(getResources().getString(R.string.isha));
//            time.setText(parseHHMMA(jsonObject.getJSONObject("timings").getString("Isha")));
//
//            name = findViewById(R.id.imsak).findViewById(R.id.name);
//            time = findViewById(R.id.imsak).findViewById(R.id.time);
//            name.setText(getResources().getString(R.string.imsak));
//            time.setText(parseHHMMA(jsonObject.getJSONObject("timings").getString("Imsak")));
//
//            name = findViewById(R.id.midnight).findViewById(R.id.name);
//            time = findViewById(R.id.midnight).findViewById(R.id.time);
//            name.setText(getResources().getString(R.string.midnight));
//            time.setText(parseHHMMA(jsonObject.getJSONObject("timings").getString("Midnight")));
//
//        } catch (JSONException e) {
//            System.out.println(e.getMessage());
//        }
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

    public String parseHHMMA(String time) {
        String inputPattern = "HH:mm";
        String outputPattern = "h:mm a";
        SimpleDateFormat inputFormat = new SimpleDateFormat(inputPattern);
        SimpleDateFormat outputFormat = new SimpleDateFormat(outputPattern);

        Date date = null;
        String str = null;

        try {
            date = inputFormat.parse(time);
            str = outputFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return str;
    }


    void sendNotification(String message){
        //next is to make schedule for this notifications (*ok*)
        Intent intent = new Intent(TimesActivity.this, TimesActivity.class);
        PendingIntent contentIntent = PendingIntent.getActivity(TimesActivity.this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder b = new NotificationCompat.Builder(TimesActivity.this);

        b.setAutoCancel(true)
                .setDefaults(Notification.DEFAULT_ALL)
                .setWhen(System.currentTimeMillis())
                .setSmallIcon(R.drawable.mosque_icon)
                .setTicker("Hearty365")
                .setContentTitle("Refresh status")
                .setContentText(message)
                .setDefaults(Notification.DEFAULT_LIGHTS| Notification.DEFAULT_SOUND| Notification.DEFAULT_VIBRATE)
                .setContentIntent(contentIntent)
                .setContentInfo("Info");


        NotificationManager notificationManager = (NotificationManager) (TimesActivity.this).getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(1, b.build());
    }


    //for test only
    private String display(ArrayList<TimesData> arrOfData){
        String result = "";
        for (TimesData data: arrOfData) {
            result += data.getName() + ", ";
        }
        return result;
    }

}