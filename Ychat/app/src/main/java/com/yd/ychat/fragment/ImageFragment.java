package com.yd.ychat.fragment;

import android.database.Cursor;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;

import com.yd.ychat.R;
import com.yd.ychat.act.ChatActivity;
import com.yd.ychat.act.GroupChatActivity;
import com.yd.ychat.adapter.ImageRecycleAdapter;
import com.yd.ychat.port.Message_imagelist;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import static android.content.Context.WINDOW_SERVICE;

/**
 * Created by 荀高杰 on 2017/5/24.
 */

public class ImageFragment extends BaseFragment implements View.OnClickListener {
    private RecyclerView message_fragment_image_recycleview;
    private Button image_send;
    private List<String> list=new ArrayList<>();
    private ImageRecycleAdapter adapter;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.message_fragment_image,container,false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initview(view);


    }

    private void initview(View view) {
        message_fragment_image_recycleview = (RecyclerView) view.findViewById(R.id.message_fragment_image_recycleview);
        image_send= (Button) view.findViewById(R.id.image_send);
        image_send.setEnabled(false);
        image_send.setOnClickListener(this);
//        获取手机图片的集合
        getimagepathlist();
         adapter=new ImageRecycleAdapter(list,getContext());
        LinearLayoutManager llm=new LinearLayoutManager(getContext());
        llm.setOrientation(LinearLayoutManager.HORIZONTAL);
        message_fragment_image_recycleview.setLayoutManager(llm);
        message_fragment_image_recycleview.setAdapter(adapter);
        adapter.setClick(new Message_imagelist() {
            @Override
            public void size_ok() {
                image_send.setEnabled(true);
            }

            @Override
            public void size_no() {
                image_send.setEnabled(false);
            }
        });

    }

    private void getimagepathlist() {
//        清楚图片数据源
        list.clear();
//        查询图片数据
        Cursor cursor = getActivity().getContentResolver().query(
//                要查询的URI
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
//                想要查询的字段
                null,
//                查询条件
                null,
//                查询条件的值
                null,
//                排序
                null);
        if(cursor!=null){
//            遍历得到的游标
            while (cursor.moveToNext()){
//                获取当前游标中 图片路径 字段的值
                String path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
//                获取 宽 高 的值
                int Width = cursor.getInt(cursor.getColumnIndex(MediaStore.Images.Media.WIDTH));

                int Height = cursor.getInt(cursor.getColumnIndex(MediaStore.Images.Media.HEIGHT))   ;


                Log.e("height",Height+"");
//                添加到集合中
                list.add(path);
            }
//            关闭游标
            cursor.close();
        }

    }

    @Override
    public void onClick(View v) {

//        得到选择的图片集合
        Set<String> paths = adapter.getpaths();
//        用迭代器遍历
        Iterator<String> iterator = paths.iterator();

        while (iterator.hasNext()){
//            创建图片消息（里面会调用发送消息）
            if((getActivity()) instanceof ChatActivity){
                ((ChatActivity)getActivity()).creatimage(iterator.next());
            }else if((getActivity()) instanceof GroupChatActivity){
                ((GroupChatActivity)getActivity()).creatimage(iterator.next());
            }

        }
        adapter.refresh();
        paths.clear();
    }

}
