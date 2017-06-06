package com.yd.ychat.fragment;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ImageSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.yd.ychat.R;
import com.yd.ychat.act.ChatActivity;
import com.yd.ychat.adapter.FaceRecycleAdapter;
import com.yd.ychat.adapter.ImageRecycleAdapter;
import com.yd.ychat.array.Face_List;
import com.yd.ychat.been.Facebeen;
import com.yd.ychat.port.Face_itemclick;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 荀高杰 on 2017/5/26.
 */

public class faceFragment extends BaseFragment {
    private RecyclerView message_fragment_face_recyclerview;

    private List<Facebeen> facebeens= Face_List.getInstanceList();
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.message_fragment_face,container,false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initview(view);
        FaceRecycleAdapter adapter=new FaceRecycleAdapter(getContext(),facebeens);
        GridLayoutManager glm=new GridLayoutManager(getActivity(),7, LinearLayoutManager.VERTICAL,false);
        message_fragment_face_recyclerview.setLayoutManager(glm);
        message_fragment_face_recyclerview.setAdapter(adapter);
        adapter.setClick(new Face_itemclick() {
            @Override
            public void OnitemClicklistener(int index) {

                SpannableString spannableString = new SpannableString(facebeens.get(index).getName());



                Drawable d = getResources().getDrawable(facebeens.get(index).getRes());
                d.setBounds(0, 0, d.getIntrinsicWidth()/2,  d.getIntrinsicHeight()/2);
                //用这个drawable对象代替字符串easy
                ImageSpan span = new ImageSpan(d, ImageSpan.ALIGN_BASELINE);
                //包括0但是不包括"easy".length()即：4。[0,4)。值得注意的是当我们复制这个图片的时候，实际是复制了"easy"这个字符串。
                spannableString.setSpan(span,0 , facebeens.get(index).getName().length(), Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
                ((ChatActivity)getActivity()).chat_edit_msg_content.append(spannableString);

            }
        });

    }

    private void initview(View v) {
         message_fragment_face_recyclerview= (RecyclerView) v.findViewById(R.id.message_fragment_face_recyclerview);

    }

}
