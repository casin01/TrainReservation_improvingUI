package com.example.reservet;

import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class RegisterRequest extends StringRequest {
    //서버 URL 설정 (PHP 파일 연동)
    final static private String URL = "http://casin123@casin123.ivyro.net/Register.php";
    private Map<String, String> map;

    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        return map;
    }

    public RegisterRequest(String userID, String userPassword, String userName, String userPhone, Response.Listener<String> listener) {
        super(Method.POST, URL, listener, error -> {
            Response.ErrorListener errorListener = new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    if (error.networkResponse != null) {
                        Log.e("VolleyError errorListener RegisterRequest.java", "Error Response code: " + error.networkResponse.statusCode);
                    }

                }
            };
        });

        map =new HashMap<>();
        map.put("userID", userID);
        map.put("userPassword", userPassword);
        map.put("userName", userName);
        map.put("userPhone", userPhone);

    }
}
