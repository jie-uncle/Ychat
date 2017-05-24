package com.yd.ychat.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.yd.ychat.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by 荀高杰 on 2017/5/24.
 */

public class ImageRecycleAdapter extends RecyclerView.Adapter<ImageRecycleAdapter.Myhoudler> {
    private List<String> list;
    private Context context;
    private Set<String> paths=new HashSet<>();

    private boolean[] checked;

    public ImageRecycleAdapter(List<String> list, Context context) {
        this.list = list;
        this.context = context;
        checked=new boolean[list.size()];
    }

    @Override
    public Myhoudler onCreateViewHolder(ViewGroup parent, int viewType) {
        return new Myhoudler(LayoutInflater.from(context).inflate(R.layout.item_message_fragment_image,parent,false));
    }

    @Override
    public void onBindViewHolder(Myhoudler holder, final int position) {
        Glide.with(context)
                .load(list.get(position))
                 .override(500,700)
                .into(holder.item_message_fragment_image);
        holder.item_message_fragment_image_checkbox.setOnCheckedChangeListener(null);
        holder.item_message_fragment_image_checkbox.setChecked(checked[position]);
        holder.item_message_fragment_image_checkbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                checked[position]=isChecked;
                if(isChecked){

                    paths.add(list.get(position));
                }else{

                    paths.remove(list.get(position));
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class  Myhoudler extends RecyclerView.ViewHolder{
        private ImageView item_message_fragment_image;
        private CheckBox item_message_fragment_image_checkbox;
        public Myhoudler(View itemView) {
            super(itemView);
             item_message_fragment_image= (ImageView) itemView.findViewById(R.id.item_message_fragment_image);
            item_message_fragment_image_checkbox= (CheckBox) itemView.findViewById(R.id.item_message_fragment_image_checkbox);
        }
    }
    public Set<String> getpaths(){
        return paths;
    }
}
