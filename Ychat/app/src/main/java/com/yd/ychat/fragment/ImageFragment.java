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
import com.yd.ychat.adapter.ImageRecycleAdapter;

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
        image_send.setOnClickListener(this);
        getimagepathlist();
         adapter=new ImageRecycleAdapter(list,getContext());
        LinearLayoutManager llm=new LinearLayoutManager(getContext());
        llm.setOrientation(LinearLayoutManager.HORIZONTAL);
        message_fragment_image_recycleview.setLayoutManager(llm);
        message_fragment_image_recycleview.setAdapter(adapter);

    }

    private void getimagepathlist() {
        list.clear();
        Cursor cursor = getActivity().getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, null, null, null, null);
        if(cursor!=null){
            while (cursor.moveToNext()){
                String path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
                int Width = cursor.getInt(cursor.getColumnIndex(MediaStore.Images.Media.WIDTH));
                int Height = cursor.getInt(cursor.getColumnIndex(MediaStore.Images.Media.HEIGHT))   ;

                Log.e("height",Height+"");
                list.add(path);
            }
            cursor.close();
        }

    }

    @Override
    public void onClick(View v) {
        ChatActivity chatActivity = (ChatActivity) getActivity();
        Set<String> paths = adapter.getpaths();
        Iterator<String> iterator = paths.iterator();
        while (iterator.hasNext()){
            chatActivity.creatImagemsg(iterator.next());
        }
    }
    private  void tupainchali(){
        WindowManager w= (WindowManager) getActivity().getSystemService(WINDOW_SERVICE);
        int height = w.getDefaultDisplay().getHeight();

        Log.e("Heig",height+"");
    }
}
