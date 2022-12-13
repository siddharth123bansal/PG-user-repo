package com.app.kishore.pguser.helpers;

import android.content.Context;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class VolleyRequest {

    Context c;
    JsonObjectRequest request;
    CallBack mCallback;
    RequestQueue q;
    public VolleyRequest(Context c, CallBack mCallback) {
        this.c = c;
        this.mCallback = mCallback;
        q = Volley.newRequestQueue(c);
    }

    public void getRequest(String url, String token){

        if( Utils.isNetworkAvailable(c)) {
            request = new JsonObjectRequest(
                    Request.Method.GET,
                    url,null,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            mCallback.responseCallback(response);

                        }
                    },

                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            mCallback.errorCallback(error);

                        }
                    }

            ) {

                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    if(token.length() == 0)
                        return super.getHeaders();
                    Map<String, String> headers = new HashMap<String, String>();
                    headers.put("Authorization", "Bearer " + token);
                    return headers;
                }
            };


            request.setRetryPolicy(
                    new DefaultRetryPolicy(
                            0,
                            DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                            DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
                    ));


            q.add(request);
        }else{
            Toast.makeText(c,"No Internet Connection",Toast.LENGTH_LONG).show();
        }

    }

    public void putWithBody(String url, JSONObject body, String token) {
        if(Utils.isNetworkAvailable(c)) {
            request = new JsonObjectRequest(
                    Request.Method.PUT,
                    url,
                    body
                    ,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            mCallback.responseCallback(response);

                        }
                    },

                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            mCallback.errorCallback(error);

                        }
                    }

            ) {


                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    if(token.length() == 0)
                        return super.getHeaders();
                    Map<String, String> headers = new HashMap<String, String>();
                    headers.put("Authorization", "Bearer " + token);
                    return headers;
                }
            };


            request.setRetryPolicy(
                    new DefaultRetryPolicy(
                            0,
                            DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                            DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
                    ));


            q.add(request);
        }else{
            Toast.makeText(c,"No Internet Connection",Toast.LENGTH_LONG).show();
        }

    }





    public void postWithBody(String url, JSONObject body, String token) {
   if( Utils.isNetworkAvailable(c)) {
       request = new JsonObjectRequest(
               Request.Method.POST,
               url,
               body
               ,
               new Response.Listener<JSONObject>() {
                   @Override
                   public void onResponse(JSONObject response) {
                       mCallback.responseCallback(response);

                   }
               },

               new Response.ErrorListener() {
                   @Override
                   public void onErrorResponse(VolleyError error) {
                       mCallback.errorCallback(error);

                   }
               }

       ) {


           @Override
           public Map<String, String> getHeaders() throws AuthFailureError {
               if(token.length() == 0)
               return super.getHeaders();
               Map<String, String> headers = new HashMap<String, String>();
               headers.put("Authorization", "Bearer " + token);
               return headers;
           }

           @Override
           public String getBodyContentType() {
               return super.getBodyContentType();
           }
       };


       request.setRetryPolicy(
               new DefaultRetryPolicy(
                       0,
                       DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                       DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
               ));


       q.add(request);
   }else{
       Toast.makeText(c,"No Internet Connection",Toast.LENGTH_LONG).show();
   }

    }

}
