package com.app.kishore.pguser.helpers;

import com.android.volley.NetworkResponse;
import com.android.volley.VolleyError;

import org.json.JSONObject;

public interface CallBack {
    public abstract void responseCallback(JSONObject response);
    public abstract void errorCallback(VolleyError error_message);
}
