package com.yd.ychat.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.yd.ychat.R;
import com.yd.ychat.port.RecyclerViewItemClick;

import java.util.List;

/**
 * Created by jie on 2017/6/20.
 */

public class Group_meberAdapter extends RecyclerView.Adapter<Group_meberAdapter.Myviewhouder> {
    private RecyclerViewItemClick click;

    public void setClick(RecyclerViewItemClick click) {
        this.click = click;
    }

    private List<String> list;
    private Context context;

    public Group_meberAdapter(List<String> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @Override
    public Myviewhouder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new Myviewhouder(LayoutInflater.from(context).inflate(R.layout.item_group_member,parent,false));
    }

    @Override
    public void onBindViewHolder(Myviewhouder holder, final int position) {
            holder.group_member.setText(list.get(position));
            holder.group_member.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    click.onitemclicklistener(position);
                }
            });
        holder.group_member.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                click.onitemlongclick(position);
                return false;
            }
        });

    }

    class Myviewhouder extends RecyclerView.ViewHolder{
        private TextView group_member;
        public Myviewhouder(View itemView) {
            super(itemView);
            group_member= (TextView) itemView.findViewById(R.id.item_group_member);
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}
