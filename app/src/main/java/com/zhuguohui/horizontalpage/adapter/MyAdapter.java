package com.zhuguohui.horizontalpage.adapter;

import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.zhuguohui.horizontalpage.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by zhuguohui on 2016/11/8.
 */

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {

    private static List<String> data = new ArrayList<>();
    private static int data_name = 0;
    private Random random=new Random();
    public MyAdapter(){
        setData();
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.layout_item, parent, false);
        return new MyViewHolder(view);
    }


    private void setData(){
        int size=random.nextInt(70);
        for (int i = 1; i <= size; i++) {
            data.add(data_name + "-" + i + "  我曾经写过一个使用RecycleView打造水平分页GridView。当时用到的是对数据的重排序，但是这样处理还是有些问题，比如用户数据更新以后还需要继续重排序，包括对滑动事件的处理也不是很好。当时主要因为时间比较匆忙，写的不是很好，这一次我将采用自定义LayoutManger的方式实现水平分页的排版，使用一个工具类实现一行代码就让RecycleView具有分页滑动的特性。\n" +
                    "\n" +
                    "作者：zhuguohui\n" +
                    "链接：https://www.jianshu.com/p/3fe949083029\n" +
                    "来源：简书\n" +
                    "著作权归作者所有。商业转载请联系作者获得授权，非商业转载请注明出处。");
//            data.add(data_name + "-" + i);
        }
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        final String title = data.get(position);
        holder.tv_title.setText(title);
        holder.tv_title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(v.getContext(), "item" + /*title*/position + " 被点击了", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }


    static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tv_title;

        public MyViewHolder(View itemView) {
            super(itemView);
            tv_title = (TextView) itemView.findViewById(R.id.tv_title);
        }
    }

    public void updateData() {
        data_name++;
        data.clear();
        setData();

    }
}
