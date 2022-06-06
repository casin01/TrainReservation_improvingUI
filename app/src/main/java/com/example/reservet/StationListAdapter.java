package com.example.reservet;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.text.Collator;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class StationListAdapter extends ArrayAdapter<StationInfo>  {
    private static final char HANGUL_BEGIN_UNICODE = 44032; // 가
    private static final char HANGUL_LAST_UNICODE = 55203; // 힣
    private static final char HANGUL_BASE_UNIT = 588;//각자음 마다 가지는 글자수
    //자음
    private static final char[] INITIAL_SOUND = { 'ㄱ', 'ㄲ', 'ㄴ', 'ㄷ', 'ㄸ', 'ㄹ', 'ㅁ', 'ㅂ', 'ㅃ', 'ㅅ', 'ㅆ', 'ㅇ', 'ㅈ', 'ㅉ', 'ㅊ', 'ㅋ', 'ㅌ', 'ㅍ', 'ㅎ' };
    ArrayList<StationInfo> mData;   //받아온 데이터
    ArrayList<StationInfo> mTemp;   //mData와 같음
    Context mContext;
    StationList stationList;

    //arrayAdapter init, 가나다정렬
    public StationListAdapter(Context context, int resource, ArrayList<StationInfo> objects) {
        super(context, resource, objects);
        mData = objects;
        stationList = (StationList)context;
        mContext = context;
        Sort_String();
        mTemp = new ArrayList<>();
        mTemp.addAll(mData);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        parent.invalidate();    //화면갱신
        View v = convertView;

        final int set = position;
        if (v == null) {
            LayoutInflater vi = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = vi.inflate(R.layout.station_list_item, null);
        }
        v.setTag(position);
 //       v.setOnClickListener(this);

        if(mData.size() <= position) return v;

        final StationInfo p = mData.get(position);
        if (p != null) {
            TextView tv = v.findViewById(R.id.tv_stationList);  //그리드뷰 각 항목

            if (tv != null) {
                tv.setText(p.st_station);
            }

        }
        return v;
    }

/*
    //각 역 클릭 시
    @Override
    public void onClick(View v) {
        Message msg = new Message();
        int position = (int)v.getTag();
        msg.obj = mData.get(position);
        msg.what = HandlerMessage.THREAD_HANDLER_SUCCESS_INFO;
        stationList.handler.sendMessage(msg);
    }
*/

    public boolean __matchString(String value, String search){  //value는 각 역
        if( (getInitialSound(value.charAt(0))) == search.charAt(0) ) {   //각각의 초성끼리 같은지 비교한다
            return true;
        }
             //모두 일치한 결과를 찾으면 true를 리턴한다.
        else {
            return false;
        }
             //일치하는 것을 찾지 못했으면 false를 리턴한다.
    }

    public void __Filter(String data)   //data:각 초성
    {
        mData.clear();  // 모두 삭제
        for(StationInfo temp : mTemp)   // mTemp에 복제해둔 데이터 하나씩 불러옴
        {
            if(__matchString(temp.st_station, data))   // 초성에 해당하면, 데이터를 더한다.
                mData.add(temp);
        }

        Sort_String();  // 데이터를 다시 정렬
        notifyDataSetChanged(); // 변화 알림
    }

    /*
    public void Filter(String data)
    {
        if(data.length() == 0) {
            mData.clear();
            mData.addAll(mTemp);
            Sort_String();
            return ;
        }
        mData.clear();  // 모두 삭제
        for(StationInfo temp : mTemp)   // mTemp에 복제해둔 데이터 하나씩 불러옴
        {
            if(matchString(temp.st_station,data))   // 초성에 해당하면, 데이터를 더한다.
                mData.add(temp);
        }

        Sort_String();  // 데이터를 다시 정렬
        notifyDataSetChanged(); // 변화 알림
    }


    //해당 문자가 INITIAL_SOUND인지 검사
    private  boolean isInitialSound(char searchar){
        for(char c:INITIAL_SOUND){
            if(c == searchar){
                return true;
            }
        }
        return false;
    }


    //검색
    public boolean matchString(String value, String search){    //역 이름, 검색한 초성
        int t = 0;
        int seof = value.length() - search.length();
        int slen = search.length();
        if(seof < 0)
            return false; //검색어가 더 길면 false를 리턴한다.
        for(int i = 0;i <= seof;i++){
            t = 0;
            while(t < slen){
                if(isInitialSound(search.charAt(t))==true && isHangul(value.charAt(i+t))){
                    //만약 현재 char이 초성이고 value가 한글이면
                    if(getInitialSound(value.charAt(i+t))==search.charAt(t))
                        //각각의 초성끼리 같은지 비교한다
                        t++;
                    else
                        break;
                } else {
                    //char이 초성이 아니라면
                    if(value.charAt(i+t)==search.charAt(t))
                        //그냥 같은지 비교한다.
                        t++;
                    else
                        break;
                }
            }
            if(t == slen)
                return true; //모두 일치한 결과를 찾으면 true를 리턴한다.
        }
        return false; //일치하는 것을 찾지 못했으면 false를 리턴한다.
    }


    //해당 문자가 한글인지 검사
    private  boolean isHangul(char c) {
        return HANGUL_BEGIN_UNICODE <= c && c <= HANGUL_LAST_UNICODE;
    }
*/

    //해당 문자의 자음을 얻는다.
    private  char getInitialSound(char c) {
        int hanBegin = (c - HANGUL_BEGIN_UNICODE);
        int index = hanBegin / HANGUL_BASE_UNIT;
        return INITIAL_SOUND[index];
    }

    void Sort_String()
    {
        final Comparator<StationInfo> myComparator= new Comparator<StationInfo>() {
            private final Collator collator = Collator.getInstance();
            @Override
            public int compare(StationInfo object1,StationInfo object2) {
                return collator.compare(object1.st_station, object2.st_station);
            }
        };
        // Collections.sort 로 comparator 를 주어서 데이터를 정렬 시킨다.
        Collections.sort(mData, myComparator);
    }
}
