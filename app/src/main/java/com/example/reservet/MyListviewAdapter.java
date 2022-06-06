package com.example.reservet;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class MyListviewAdapter extends BaseAdapter {
    ArrayList<AllInfo> myticket = new ArrayList<>();
    Context mcontext;

    public MyListviewAdapter(Context context) {
        this.mcontext=context;
    }

    @Override
    public int getCount() {
        return myticket.size();
    }   //리스트 개수

    //getItem() : 리스트에서 해당하는 인덱스의 데이터(사진, 이름, 전번)를 모두 가져오는 메서드
    // Object를 알아서 캐스팅해서 사용하라는 의미로 반환 타입이 Object
    @Override
    public Object getItem(int index) {
        return myticket.get(index);
    }   //인덱스에 해당하는 항목 가져오기

    @Override
    public long getItemId(int position) {
        return position;
    }

    // 데이터 세팅을 위한 메소드
    public void setItems(ArrayList<AllInfo> items) {
        this.myticket = items;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup viewGroup) {
        final Context context = viewGroup.getContext();
        final AllInfo ticket__ = myticket.get(position);  //포지션에 해당하는 항목

        if(convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.my_ticket_item, viewGroup, false);

        } else {
            View view = new View(context);
            view = (View) convertView;
        }

        //xml
        TextView mydate =convertView.findViewById(R.id.mydate);
        TextView ticketnum =convertView.findViewById(R.id.ticketnum);
        TextView trainTypeNo = convertView.findViewById(R.id.trainTypeNo);
        TextView dep = convertView.findViewById(R.id.dep);
        TextView arr = convertView.findViewById(R.id.arr);

        //날짜 20220101금 + 매수
        String year = ticket__.bookdate.substring(0,4);
        String month = ticket__.bookdate.substring(4,6);
        String day = ticket__.bookdate.substring(6,8);
        String weekday = ticket__.bookdate.substring(8);
        String dateText = year + "년 " + month + "월 " + day + "일 ("+weekday +")";
        mydate.setText(dateText);
        ticketnum.setText(ticket__.peopleNum+"매");

        //기차 종류, 번호
        String traintext = "[ " + ticket__.trainName + " " + ticket__.trainNo + " ]";
        trainTypeNo.setText(traintext);

        //출발 도착 날짜
        String dephour = ticket__.depTime.substring(8, 10);
        String depmin =ticket__.depTime.substring(10, 12);
        String arrhour = ticket__.arrTime.substring(8, 10);
        String arrmin =ticket__.arrTime.substring(10, 12);

        String depText = ticket__.depStn + " (" + dephour + ":" + depmin + ")";
        String arrText = ticket__.arrStn + " (" + arrhour + ":" + arrmin + ")";
        dep.setText(depText);
        arr.setText(arrText);

//        Log.e("mylistviewadpater", "어댑터 연결");

        return convertView;  //뷰 객체 반환
    }
}

