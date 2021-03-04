package com.codepath.apps.restclienttemplate;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.codepath.apps.restclienttemplate.models.Tweet;
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler;

import org.json.JSONException;
import org.parceler.Parcels;

import okhttp3.Headers;

public class ComposeActivity extends AppCompatActivity {

    public static final String TAG = "ComposeActivity";
    public static final int MAX_TWEET_LENGTH = 280;
    EditText etCompose;
    Button btnTweet;
    TextView tvCharCount;
    TwitterClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compose);
        client = TwitterApplication.getRestClient(this);

        etCompose = findViewById(R.id.etCompose);
        btnTweet = findViewById(R.id.btnTweet);
        tvCharCount = findViewById(R.id.tvCharCount);

        // set on click listener for tweet button
        btnTweet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String newTweetContent = etCompose.getText().toString();
                if(newTweetContent.isEmpty()) {
                    Toast.makeText(ComposeActivity.this, "Text field is empty!", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(newTweetContent.length() > MAX_TWEET_LENGTH) {
                    Toast.makeText(ComposeActivity.this, "Tweet is too long!", Toast.LENGTH_SHORT).show();
                    return;
                }
                Toast.makeText(ComposeActivity.this, "Tweet composed!", Toast.LENGTH_SHORT).show();
                // Make an API call to Twitter to publish tweet
                client.publishTweet(newTweetContent, new JsonHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Headers headers, JSON json) {
                        Log.i(TAG, "OnSuccess to publish tweet");
                        try {
                            Tweet tweet = Tweet.fromJson(json.jsonObject);
                            Log.i(TAG, "Published tweet: " + tweet);
                            Intent intent = new Intent();
                            intent.putExtra("tweet", Parcels.wrap(tweet));
                            // set result code and bundle data for response
                            setResult(RESULT_OK, intent);
                            // closes the activity, pass data to parent
                            finish();

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onFailure(int statusCode, Headers headers, String response, Throwable throwable) {
                        Log.i(TAG, "OnFailure");
                    }
                });
            }
        });

        etCompose.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                int tweetLength = etCompose.getText().toString().length();
                int charsLeft = (280 - tweetLength);
                tvCharCount.setText(Integer.toString(charsLeft));
                if(charsLeft < 0) {
                    tvCharCount.setTextColor(Color.parseColor("#FF0000"));
                }else {
                    tvCharCount.setTextColor(Color.parseColor("#000000"));
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });
    }
}