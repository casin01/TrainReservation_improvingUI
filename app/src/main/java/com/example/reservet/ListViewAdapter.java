package com.example.reservet;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class ListViewAdapter extends BaseAdapter {
    ArrayList<TrainTicketInfo> tickets = new ArrayList<>();
    Context mcontext;

    public ListViewAdapter(Context context) {
        this.mcontext=context;
    }

    @Override
    public int getCount() {
        return tickets.size();
    }   //리스트 개수

    public void addItem(TrainTicketInfo item) {
        tickets.add(item);
    }  //리스트에 추가

    //getItem() : 리스트에서 해당하는 인덱스의 데이터(사진, 이름, 전번)를 모두 가져오는 메서드
    // Object를 알아서 캐스팅해서 사용하라는 의미로 반환 타입이 Object
    @Override
    public Object getItem(int index) {
        return tickets.get(index);
    }   //인덱스에 해당하는 항목 가져오기

    @Override
    public long getItemId(int position) {
        return position;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup viewGroup) {
        final Context context = viewGroup.getContext();
        final TrainTicketInfo _ticket = tickets.get(position);  //포지션에 해당하는 항목

        if(convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.listview_list_item, viewGroup, false);

        } else {
            View view = new View(context);
            view = (View) convertView;
        }

        TextView traintype = convertView.findViewById(R.id.traintype);
        TextView deptrain = convertView.findViewById(R.id.deptrain);
        TextView arrtrain = convertView.findViewById(R.id.arrtrain);

        String dephour = _ticket.depPlandTime.substring(8, 10);
        String depmin =_ticket.depPlandTime.substring(10, 12);
        String arrhour = _ticket.arrPlandTime.substring(8, 10);
        String arrmin =_ticket.arrPlandTime.substring(10, 12);

        String depText = _ticket.depPlaceName + "\r\n" + dephour + ":" + depmin;
        String arrText = _ticket.arrPlaceName + "\r\n" + arrhour + ":" + arrmin;

        traintype.setText(_ticket.trainGradeName);
        deptrain.setText(depText);
        arrtrain.setText(arrText);

        return convertView;  //뷰 객체 반환
    }



}

