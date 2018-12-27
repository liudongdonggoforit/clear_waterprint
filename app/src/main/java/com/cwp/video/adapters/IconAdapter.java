package com.cwp.video.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.cwp.video.R;

import java.util.List;
import java.util.Map;

import androidx.recyclerview.widget.RecyclerView;

public class IconAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Integer imageViewIds[] = {R.drawable.douyin, R.drawable.huoshan, R.drawable.instagram,
            R.drawable.kuaishou, R.drawable.meipai, R.drawable.pipixia, R.drawable.tiktok,
            R.drawable.toutiao, R.drawable.weibo, R.drawable.weishi};
    private String titles[] = {"抖音", "火山", "Instagram", "快手", "美拍", "皮皮虾", "TikTok", "头条", "微博", "微视"};
    private Context mContext;

    public IconAdapter(Context context) {
        mContext = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(mContext).inflate(R.layout.item_icon_holder, parent, false);
        return new ContentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {

        ContentViewHolder viewHolder = (ContentViewHolder) holder;
        viewHolder.mTextView.setText(titles[position]);
        viewHolder.imageView.setImageResource(imageViewIds[position]);
    }

    @Override
    public int getItemCount() {

        return titles.length;
    }

    class ContentViewHolder extends RecyclerView.ViewHolder {
        TextView mTextView;
        ImageView imageView;

        public ContentViewHolder(View itemView) {

            super(itemView);
            mTextView = itemView.findViewById(R.id.url_title);
            imageView = itemView.findViewById(R.id.url_image);
        }
    }
}
