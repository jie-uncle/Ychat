package com.yd.ychat.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.yd.ychat.R;
import com.yd.ychat.adapter.FaceRecycleAdapter;
import com.yd.ychat.adapter.ImageRecycleAdapter;
import com.yd.ychat.port.Face_itemclick;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 荀高杰 on 2017/5/26.
 */

public class faceFragment extends BaseFragment {
    private RecyclerView message_fragment_face_recyclerview;
    private List<Integer> list=new ArrayList<>();
    private int [] face_res={R.drawable.mxq ,R.drawable.mxp ,R.drawable.mxo ,R.drawable.mxn ,R.drawable.mxm ,R.drawable.mxl
            ,R.drawable.mxk ,R.drawable.mxj ,R.drawable.mxi ,R.drawable.mxh ,R.drawable.mxg ,R.drawable.mxd
            ,R.drawable.eft ,R.drawable.eff ,R.drawable.efb ,R.drawable.efa ,R.drawable.eez ,R.drawable.eex
            ,R.drawable.eew ,R.drawable.eet ,R.drawable.ees ,R.drawable.eer ,R.drawable.eeq ,R.drawable.eep
            ,R.drawable.eeo ,R.drawable.eef ,R.drawable.eed ,R.drawable.eec ,R.drawable.edw ,R.drawable.edv
            ,R.drawable.eds ,R.drawable.edr ,R.drawable.edq ,R.drawable.edp ,R.drawable.edo ,R.drawable.edn
            ,R.drawable.edj ,R.drawable.edi ,R.drawable.ede ,R.drawable.edd ,R.drawable.edb ,R.drawable.eda
            ,R.drawable.ecz ,R.drawable.ecy ,R.drawable.ecx ,R.drawable.ecw ,R.drawable.ecv ,R.drawable.ect
            ,R.drawable.ecs ,R.drawable.ecr ,R.drawable.ecq ,R.drawable.ecp ,R.drawable.eco ,R.drawable.ecn
            ,R.drawable.ecm ,R.drawable.ecl ,R.drawable.eck ,R.drawable.ecj ,R.drawable.eci ,R.drawable.ech
            ,R.drawable.ecg ,R.drawable.ecf ,R.drawable.ece ,R.drawable.ecd ,R.drawable.ecc ,R.drawable.ecb
            ,R.drawable.eca ,R.drawable.ebz ,R.drawable.ebx ,R.drawable.ebw ,R.drawable.ebv ,R.drawable.ebu
            ,R.drawable.ebt ,R.drawable.ebs ,R.drawable.ebr ,R.drawable.ebq ,R.drawable.ebp ,R.drawable.ebo
            ,R.drawable.ebl ,R.drawable.ebj ,R.drawable.ebh ,R.drawable.ebg,
            R.drawable.eca,R.drawable.eeg};
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.message_fragment_face,container,false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initview(view);
        FaceRecycleAdapter adapter=new FaceRecycleAdapter(getContext(),list);
        GridLayoutManager glm=new GridLayoutManager(getActivity(),7, LinearLayoutManager.VERTICAL,false);
        message_fragment_face_recyclerview.setLayoutManager(glm);
        message_fragment_face_recyclerview.setAdapter(adapter);
        adapter.setClick(new Face_itemclick() {
            @Override
            public void OnitemClicklistener(int index) {
                Toast.makeText(getContext(),list.get(index).toString(),Toast.LENGTH_LONG).show();
            }
        });

    }

    private void initview(View v) {
         message_fragment_face_recyclerview= (RecyclerView) v.findViewById(R.id.message_fragment_face_recyclerview);
        list.clear();
        for (int i=0;i<face_res.length;i++){
            list.add(face_res[i]);
        }
    }

}
