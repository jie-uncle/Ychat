package com.yd.ychat.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.yd.ychat.R;
import com.yd.ychat.been.Facebeen;
import com.yd.ychat.port.Face_itemclick;

import java.util.List;

/**
 * Created by 荀高杰 on 2017/5/26.
 */

public class FaceRecycleAdapter extends RecyclerView.Adapter<FaceRecycleAdapter.MyViewholder>{
    private Context context;
    private List<Facebeen> list;
    private Face_itemclick click;

    public void setClick(Face_itemclick click) {
        this.click = click;
    }

    public FaceRecycleAdapter(Context context, List<Facebeen> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public MyViewholder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MyViewholder(LayoutInflater.from(context).inflate(R.layout.item_message_fragment_face,parent,false));
    }
    class MyViewholder extends RecyclerView.ViewHolder{
        private ImageView item_message_fragment_face_iv;

        public MyViewholder(View itemView) {
            super(itemView);
            item_message_fragment_face_iv= (ImageView) itemView.findViewById(R.id.item_message_fragment_face_iv);
        }
    }
    @Override
    public void onBindViewHolder(MyViewholder holder, final int position) {
            holder.item_message_fragment_face_iv.setImageResource(list.get(position).getRes());
        holder.item_message_fragment_face_iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                click.OnitemClicklistener(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}
