package com.tidevalet.fragments;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.tidevalet.App;
import com.tidevalet.R;
import com.tidevalet.SessionManager;
import com.tidevalet.helpers.Post;
import com.tidevalet.interfaces.MainListener;
import com.tidevalet.thread.adapter;

import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a {@link } and makes a call to the
 * specified {@link MainListener}.
 * TODO: Replace the implementation with code for your data type.
 */
public class PostViewAdapter extends RecyclerView.Adapter<PostViewAdapter.ViewHolder> {
    public static List<Post> posts = getPosts();
    private final MainListener mListener;


    public PostViewAdapter(List<Post> items, MainListener listener) {
        Log.d("RECYCLER", items.size() + "");
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
        holder.post = posts.get(position);
        String[] imagePath = holder.post.getLocalImagePath().split(",");

        Bitmap image = ThumbnailUtils.extractThumbnail(BitmapFactory.decodeFile(imagePath[0]),
                (holder.firstImage.getDrawable().getIntrinsicWidth()) /2, (holder.firstImage.getDrawable().getIntrinsicHeight()) /2);
        holder.bldgunit.setText(" Location: " + holder.post.getBldg() + "/" + holder.post.getUnit());
        holder.violtype.setText(holder.post.getViolationType());
        if (holder.post.getIsPosted() == 0) { holder.posted.setVisibility(View.VISIBLE); }
        else { holder.posted.setVisibility(View.VISIBLE); }
        holder.firstImage.setImageBitmap(image);
        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.
                    mListener.onListFragmentInteraction(holder.post);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return posts.size();
    }

    public static List<Post> getPosts() {
        try {
            adapter dBadapter = new adapter(App.getAppContext());
            SessionManager sm = new SessionManager(App.getAppContext());
            dBadapter.open();
            posts = dBadapter.getPostsByPropertyId(sm.propertySelected());
            dBadapter.close();
            Log.d("getPosts()", "Posts: " + posts.size());
        }
        catch (Exception e) { e.printStackTrace(); }
        return posts;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView bldgunit, posted;
        public final TextView violtype;
        public final ImageView firstImage;
        public Post post;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            posted = (TextView) view.findViewById(R.id.posted);
            firstImage = (ImageView) view.findViewById(R.id.firstImage);
            bldgunit = (TextView) view.findViewById(R.id.bldgunit);
            violtype = (TextView) view.findViewById(R.id.violation_type_text);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + violtype.getText() + "'";
        }
    }
}
