package com.atikfaysal.foodapp.backend;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.atikfaysal.foodapp.interfaces.OnResponseTask;

import java.util.Map;

public class BackgroundTask
{
    private CheckInternetConnection internetConnection;
    private RequestQueue requestQueue;
    private OnResponseTask onResponseTask;

    //constructor
    public BackgroundTask(Context context,OnResponseTask task)
    {
        internetConnection = new CheckInternetConnection(context);
        requestQueue = Volley.newRequestQueue(context);
        this.onResponseTask = task;
    }

    //receive server response
    public void serverResponse(final String serverUrl, final Map<String,String> dataMap)
    {
        if(internetConnection.isOnline())
        {
            StringRequest stringRequest = new StringRequest(Request.Method.POST, serverUrl,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String ServerResponse) {
                            onResponseTask.onResultSuccess(ServerResponse.trim());
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError volleyError) {
                            onResponseTask.onResultSuccess(volleyError.toString());
                        }
                    }) {
                @Override
                protected Map<String, String> getParams() {

                    // Creating Map String Params.
                    Map<String, String> params;
                    params = dataMap;

                    return params;
                }
            };
            requestQueue.add(stringRequest);
        }
    }
}
