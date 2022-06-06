package com.example.reservet;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class TicketRequest extends StringRequest {
    //서버 URL 설정 (PHP 파일 연동)
    final static private String URL = "http://casin123@casin123.ivyro.net/ticketBook.php";
    private Map<String, String> map;

    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        return map;
    }

    public TicketRequest(String userID, String userPassword, String userName, String userPhone, String depStn, String depStnID,
                         String arrStn, String arrStnID, String trainName, String trainNo, String depTime, String arrTime, int peopleNum,
                         int totalcharge, String imp_uid, String merchant_uid, String bookdate, Response.Listener<String> listener) {
        super(Method.POST, URL, listener, null);

        map =new HashMap<>();
        map.put("userID", userID);
        map.put("userPassword", userPassword);
        map.put("userName", userName);
        map.put("userPhone", userPhone);
        map.put("depStn", depStn);
        map.put("depStnID", depStnID);
        map.put("arrStn", arrStn);
        map.put("arrStnID", arrStnID);
        map.put("trainName", trainName);
        map.put("trainNo", trainNo);
        map.put("depTime", depTime);
        map.put("arrTime", arrTime);
        map.put("peopleNum", peopleNum+"");
        map.put("totalcharge", totalcharge+"");
        map.put("imp_uid", imp_uid);
        map.put("merchant_uid", merchant_uid);
        map.put("bookdate", bookdate);
    }
}
