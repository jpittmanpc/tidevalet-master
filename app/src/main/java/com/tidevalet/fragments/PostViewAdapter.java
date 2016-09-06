package com.tidevalet.fragments;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageSize;
import com.tidevalet.App;
import com.tidevalet.R;
import com.tidevalet.SessionManager;
import com.tidevalet.helpers.Post;
import com.tidevalet.interfaces.MainListener;
import com.tidevalet.thread.adapter;

import java.util.List;
import java.util.Objects;

/**
 * {@link RecyclerView.Adapter} that can display a {@link } and makes a call to the
 * specified {@link MainListener}.
 * TODO: Replace the implementation with code for your data type.
 */
public class PostViewAdapter extends RecyclerView.Adapter<PostViewAdapter.ViewHolder> {
    public static List<Post> posts;
    private final MainListener mListener;



    public PostViewAdapter(List<Post> items, MainListener listener) {
        Log.d("PostViewAdapter", "Size: " + items.size() + "");
        posts = items;
        mListener = listener;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                    View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.view_violation_card, parent, false);
            return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        if (!posts.isEmpty()) {
            holder.post = posts.get(position);
            String[] imagePath;
            try { imagePath = holder.post.getImagePath().split(","); }
            catch(NullPointerException e) { imagePath = holder.post.getLocalImagePath().split(","); }
            ImageLoader imgLoader = ImageLoader.getInstance();
            ImageSize size = new ImageSize(60, 60);
            try {
                if (imagePath[0].charAt(0) == '/') {
                    imagePath[0] = "file://" + imagePath[0];
                }
                imgLoader.displayImage(imagePath[0].replaceAll("\\[", "").replaceAll("\\]","").replaceAll(" ",""), holder.firstImage, size);
            }
            catch(Exception e) { e.printStackTrace(); }
            holder.bldgunit.setText(" Location: " + holder.post.getBldg() + "/" + holder.post.getUnit());
            holder.violtype.setText(holder.post.getViolationType());
            holder.datetext.setText(holder.post.getTimestamp());

    //            holder.pickedup.setText(holder.post.getPU() == 0 ? "No" : "Yes");
            if (holder.post.getIsPosted() == 0) {
                holder.posted.setVisibility(View.VISIBLE);
            } else {
                holder.posted.setVisibility(View.INVISIBLE);
            }
            holder.mView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (null != mListener) {
                        // Notify the active callbacks interface (the activity, if the
                        // fragment is attached to one) that an item has been selected.
                        mListener.onListFragmentInteraction(holder.post, v);
                    }
                }
            });
        }
        else {  }
    }

    @Override
    public int getItemCount() {
        return posts.size();
    }
    @Override
    public void onDetachedFromRecyclerView(RecyclerView recyclerView) {
        super.onDetachedFromRecyclerView(recyclerView);
        Log.d("DETACHED", "");
    }

    public static List<Post> getPosts() {
        try {
            adapter dBadapter = new adapter(App.getAppContext());
            SessionManager sm = new SessionManager(App.getAppContext());
            dBadapter.open();
            posts = dBadapter.getPostsByPropertyId(sm.propertySelected());
            dBadapter.close();
        }
        catch (Exception e) { e.printStackTrace(); }
        return posts;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private final View mView;
        private final TextView bldgunit, posted, violtype, datetext, pickedup;
        private final ImageView firstImage;
        private Post post;
        private ViewHolder(View view) {
            super(view);
            mView = view;
            posted = (TextView) view.findViewById(R.id.posted);
            firstImage = (ImageView) view.findViewById(R.id.firstImage);
            bldgunit = (TextView) view.findViewById(R.id.bldgunit);
            violtype = (TextView) view.findViewById(R.id.violation_type_text);
            datetext = (TextView) view.findViewById(R.id.date_text);
            pickedup = (TextView) view.findViewById(R.id.pickedup);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + violtype.getText() + "'";
        }
    }
}
