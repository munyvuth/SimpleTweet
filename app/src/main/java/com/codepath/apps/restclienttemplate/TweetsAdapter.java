package com.codepath.apps.restclienttemplate;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.codepath.apps.restclienttemplate.models.Tweet;

import java.util.List;

public class TweetsAdapter extends  RecyclerView.Adapter<TweetsAdapter.ViewHolder>{
    Context context;
    List<Tweet> tweets;

    //pass in data and context
    public TweetsAdapter(Context context, List<Tweet> tweets) {
        this.context = context;
        this.tweets = tweets;
    }

    @NonNull
    @Override
    //inflate the layout for each row
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_tweet, parent, false);
        return new ViewHolder(view);
    }
    //bind data to view based on position
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        //get data at position
        Tweet tweet = tweets.get(position);
        //bind data to each view
        holder.bind(tweet);
    }

    @Override
    public int getItemCount() {
        return tweets.size();
    }

    // Clean all elements of the recycler
    public void clear() {
        tweets.clear();
        notifyDataSetChanged();
    }

    // Add a list of items -- change to type used
    public void addAll(List<Tweet> tweetList) {
        tweets.addAll(tweetList);
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView ivProfileImage;
        TextView tvScreenName;
        TextView tvTweetContent;
        TextView tvTimestamp;
        TextView tvName;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ivProfileImage = itemView.findViewById(R.id.ivProfileImage);
            tvScreenName = itemView.findViewById(R.id.tvScreenName);
            tvTweetContent = itemView.findViewById(R.id.tvTweetContent);
            tvTimestamp = itemView.findViewById(R.id.tvTimestamp);
            tvName = itemView.findViewById(R.id.tvName);
        }

        public void bind(Tweet tweet) {
            Glide.with(context).load(tweet.user.profileImageUrl).into(ivProfileImage);
            tvScreenName.setText("@" + tweet.user.screenName);
            tvTweetContent.setText(tweet.tweetContent);
            tvTimestamp.setText(tweet.getFormattedTimestamp());
            tvName.setText(tweet.user.name);
        }
    }
}
