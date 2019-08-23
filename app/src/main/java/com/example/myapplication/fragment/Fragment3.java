package com.example.myapplication.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.myapplication.LbsActivity;
import com.example.myapplication.OtherActivity;
import com.example.myapplication.R;
import com.example.myapplication.TheOtherActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class Fragment3 extends Fragment {
        private ListView lv;
        private SimpleAdapter adapter;
        private List<Map<String, Object>> list;
        private Map<String, Object> map;

        @Nullable
        @Override
        public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            View view =  inflater.inflate(R.layout.fragment_3,container,false);
            lv = (ListView) view.findViewById(R.id.tab_listview);

            lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                Intent intent = null;
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, final int i, final long l) {
                    Toast.makeText(getActivity(), "你点击了第"+(i+1)+"个", Toast.LENGTH_LONG).show();
                    switch (i){
                        case 0:  intent =new Intent(getActivity(),OtherActivity.class);
                                 startActivity(intent);
                                 break;
                        case 1:  intent =new Intent(getActivity(), TheOtherActivity.class);
                                 startActivity(intent);
                                 break;
                        case 2:  intent =new Intent(getActivity(), LbsActivity.class);
                                 startActivity(intent);
                                 break;
                    }

                }
            });



            return  view;
        }

         @Override
        public void onActivityCreated(@Nullable Bundle savedInstanceState) {
            super.onActivityCreated(savedInstanceState);
             adapter = new SimpleAdapter(getActivity(), getData(), R.layout.tab_listview_item,
                     new String[]{"img", "title", "body"},
                     //配置适配器，并获取对应Item中的ID
                     new int[]{R.id.itemimg, R.id.itemtitle, R.id.itembody});
             lv.setAdapter(adapter);
        }
         //数据的获取@！
        private List<? extends Map<String, ?>> getData() {
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        //将需要的值传入map中
        map = new HashMap<String, Object>();
        map.put("title", "最新公告事项");
        map.put("body", "不知道未来几天有什么最新消息？那就点我查看查看呗");
        map.put("img", R.drawable.msg1);
        list.add(map);


        map = new HashMap<String, Object>();
        map.put("title", "校内最新消息通知");
        map.put("body", "校级活动，电影本周放啥？点我查看");
        map.put("img", R.drawable.msg2);
        list.add(map);

        map = new HashMap<String, Object>();
        map.put("title", "圈内交流园地");
        map.put("body", "来都来了，何不进来说几句？");
        map.put("img", R.drawable.msg3);
        list.add(map);

        return list;
    }
        @Override
        public void onCreate(@Nullable Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
        }


}