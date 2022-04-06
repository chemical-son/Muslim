package com.example.muslim.request_handelers;

import android.content.Context;
import android.content.Intent;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


public class HandleRequests {


    Context context;
    private String url;
    private static final String API_SERVER_URL = "";

    //constructor
    public HandleRequests(Context context) {
        this.context = context;
    }

    //move to next activity
    protected void moveToNextActivity(Context thisContext, Class nextContext){
        Intent intent = new Intent(thisContext, nextContext);
        thisContext.startActivity(intent);
    }

    public interface VolleyResponseListener{
        void onResponse(boolean status, JSONObject data);
    }

    public void getAzaanTimes(Double latitude,Double longitude, double elevation, int method, final VolleyResponseListener volleyResponseListener){

        final int[] statusCode = { 0 };
        boolean status[] = { false };
        final JSONObject[] data = { null };

/*
        String URL = "https://api.pray.zone/v2/times/today.json?longitude="
                + longitude + "&latitude=" + latitude + "&elevation=" + elevation
                + "&timeformat=" + time_format;*/

        String URL = "https://api.aladhan.com/v1/timings/now?latitude="
                + latitude + "&longitude=" + longitude + "&method=" + method;

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, URL, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        status[0] = true;
                        data[0] = response;
                        volleyResponseListener.onResponse(status[0], data[0]);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //Toast.makeText(context, error.getMessage(), Toast.LENGTH_SHORT).show();
                volleyResponseListener.onResponse(status[0], null);
            }
        }) {

            @Override
            public String getBodyContentType() {
                return "application/json; charset=utf-8";
            }

            @Override
            protected Response<JSONObject> parseNetworkResponse(NetworkResponse response) {
                statusCode[0] =response.statusCode;
                return super.parseNetworkResponse(response);
            }

            //to send api_token
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headerMap = new HashMap<String, String>();
                headerMap.put("Content-Type", "application/json");
                return headerMap;
            }
        };

        MySingleton.getInstance(context).addToRequestQueue(jsonObjectRequest);
    }


}

