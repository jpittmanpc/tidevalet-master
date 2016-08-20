package com.tidevalet.fragments;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tidevalet.App;
import com.tidevalet.R;
import com.tidevalet.SessionManager;
import com.tidevalet.helpers.Post;
import com.tidevalet.interfaces.MainListener;
import com.tidevalet.thread.adapter;

import java.util.ArrayList;
import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a {@link } and makes a call to the
 * specified {@link MainListener}.
 * TODO: Replace the implementation with code for your data type.
 */
public class MyPostRecyclerViewAdapter extends RecyclerView.Adapter<MyPostRecyclerViewAdapter.ViewHolder> {
    public static List<Post> posts = getPosts();
    private final MainListener mListener;


    public MyPostRecyclerViewAdapter(List<Post> items, MainListener listener) {
        Log.d("RECYCLER", items.size() + "");
        posts = items;
        mListener = listener;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_violation_posts, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = posts.get(position);
        holder.mIdView.setText("Type " + posts.get(position).getViolationType());
        holder.mContentView.setText("Posted? " + posts.get(position).getIsPosted() + " Bldg: " + posts.get(position).getBldg() + " Unit: " + posts.get(position).getUnit());
        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.
                    mListener.onListFragmentInteraction(holder.mItem);
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
        public final TextView mIdView;
        public final TextView mContentView;
        public Post mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mIdView = (TextView) view.findViewById(R.id.id);
            mContentView = (TextView) view.findViewById(R.id.content);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mContentView.getText() + "'";
        }
    }
}
