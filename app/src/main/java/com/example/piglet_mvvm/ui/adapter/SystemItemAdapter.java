package com.example.piglet_mvvm.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.piglet_mvvm.R;
import com.example.piglet_mvvm.bean.SystemBean;
import com.google.android.flexbox.FlexboxLayout;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

/**
 * Created By 刘纯贵
 * Created Time 2020/2/22
 */
public class SystemItemAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private LayoutInflater mInflater;
    private Context context;
    private List<SystemBean> data;
    private Queue<TextView> mFlexItemTextViewCaches = new LinkedList<>();
    private OnItemClickListener mOnItemClickListener = null;

    public SystemItemAdapter(Context context, List<SystemBean> data) {
        this.context = context;
        this.data = data;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mInflater.from(context).inflate(R.layout.system_item,null);
        return new FblHolder(view);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        mOnItemClickListener = onItemClickListener;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof FblHolder){
            ((FblHolder) holder).tv_system_title.setText(data.get(position).getName());
            SystemBean systemBeans = data.get(position);
            FlexboxLayout flexboxLayout = ((FblHolder) holder).fl_child;
            for (int i = 0; i < systemBeans.getChildren().size(); i++) {
                TextView child = createOrGetCacheFlexItemTextView(flexboxLayout);
                child.setText(systemBeans.getChildren().get(i).getName());
                int finalI = i;
                child.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (mOnItemClickListener != null) {
                            mOnItemClickListener.onClick(systemBeans, finalI);
                        }
                    }
                });
                flexboxLayout.addView(child);
            }

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mOnItemClickListener != null) {
                        mOnItemClickListener.onClick(data.get(position), 0);
                    }
                }
            });
        }
    }



    @Override
    public int getItemCount() {
        return data.size();
    }

    @Override
    public void onViewRecycled(@NonNull RecyclerView.ViewHolder holder) {
        super.onViewRecycled(holder);
        FlexboxLayout fbl = holder.itemView.findViewById(R.id.fbl);
        for (int i = 0; i < fbl.getChildCount(); i++) {
            mFlexItemTextViewCaches.offer((TextView) fbl.getChildAt(i));
        }
        fbl.removeAllViews();
    }

    private TextView createOrGetCacheFlexItemTextView(FlexboxLayout fbl) {
        TextView tv = mFlexItemTextViewCaches.poll();
        if (tv != null) {
            return tv;
        }
        return createFlexItemTextView(fbl);
    }

    private TextView createFlexItemTextView(FlexboxLayout fbl) {
        if (mInflater == null) {
            mInflater = LayoutInflater.from(fbl.getContext());
        }
        return (TextView) mInflater.inflate(R.layout.rv_item_knowledge_child, fbl, false);
    }

    class FblHolder extends RecyclerView.ViewHolder{

        private TextView tv_system_title;
        private FlexboxLayout fl_child;

        public FblHolder(@NonNull View itemView) {
            super(itemView);
            tv_system_title = itemView.findViewById(R.id.tv_name);
            fl_child = itemView.findViewById(R.id.fbl);
        }
    }

    public interface OnItemClickListener {
        void onClick(SystemBean bean, int pos);
    }

}
