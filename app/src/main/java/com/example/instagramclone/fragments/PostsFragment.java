package com.example.instagramclone.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.instagramclone.EndlessRecyclerViewScrollListener;
import com.example.instagramclone.Post;
import com.example.instagramclone.PostsAdapter;
import com.example.instagramclone.R;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class PostsFragment extends Fragment {

    public static final String TAG = "PostsFragment";
    private RecyclerView rvPosts;
    protected PostsAdapter adapter;
    protected List<Post> allPosts;
    protected SwipeRefreshLayout swipeContainer;
    protected EndlessRecyclerViewScrollListener scrollListener;
    private LinearLayoutManager linearLayoutManager;


    public PostsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_posts, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        rvPosts = view.findViewById(R.id.rvPosts);
        allPosts = new ArrayList<>();
        adapter = new PostsAdapter(getContext(),allPosts);

        //Steps to use recycler view
        //0 create layout for one row in the list
        //1 create the adapter
        //2 create the data source
        //3 set the adapter on the recycler view
        rvPosts.setAdapter(adapter);

        //Swipe Refresh
        swipeContainer = (SwipeRefreshLayout)view.findViewById(R.id.swipeContainer);
        // Setup refresh listener which triggers new data loading
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // Your code to refresh the list here.
                // Make sure you call swipeContainer.setRefreshing(false)
                // once the network request has completed successfully.
                clear();
                queryPosts();
            }
        });
        // Configure the refreshing colors
        swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);

        //4 set the layout manager on the recycler view
        linearLayoutManager = new LinearLayoutManager(getContext());
        rvPosts.setLayoutManager(linearLayoutManager);

        scrollListener = new EndlessRecyclerViewScrollListener(linearLayoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                queryPostsTwo();
            }
        };

        queryPosts();
        rvPosts.addOnScrollListener(scrollListener);
    }

    protected void queryPostsTwo() {
        ParseQuery<Post> query = ParseQuery.getQuery(Post.class);
        //get all the posts
        query.include(Post.KEY_USER);
        query.setLimit(30);
        query.addAscendingOrder(Post.KEY_CREATED_KEY);
        query.findInBackground(new FindCallback<Post>() {
            @Override
            public void done(List<Post> posts, ParseException e) {
                if (e != null) {
                    Log.e(TAG,"Issue with getting posts",e);
                }
                for (Post post : posts) {
                    SimpleDateFormat DateFor = new SimpleDateFormat("dd/MM/yyyy");
                    SimpleDateFormat DateTimeFor = new SimpleDateFormat("h:mm a");
                    String stringDate = DateFor.format(post.getDateMe());
                    String stringTime = DateTimeFor.format(post.getDateMe());
                    Log.i(TAG,"PostTWOOOO: " + post.getDescription() + ", username: " + post.getUser().getUsername() + " date: " + stringDate + " time " + stringTime);
                }
                addAll(posts);
                swipeContainer.setRefreshing(false);
                //adapter.notifyDataSetChanged();
            }
        });
    }

    protected void queryPosts() {
        ParseQuery<Post> query = ParseQuery.getQuery(Post.class);
        //get all the posts
        query.include(Post.KEY_USER);
        query.setLimit(20);
        query.addDescendingOrder(Post.KEY_CREATED_KEY);
        query.findInBackground(new FindCallback<Post>() {
            @Override
            public void done(List<Post> posts, ParseException e) {
                if (e != null) {
                    Log.e(TAG,"Issue with getting posts",e);
                }
                for (Post post : posts) {
                    SimpleDateFormat DateFor = new SimpleDateFormat("dd/MM/yyyy");
                    SimpleDateFormat DateTimeFor = new SimpleDateFormat("h:mm a");
                    String stringDate = DateFor.format(post.getDateMe());
                    String stringTime = DateTimeFor.format(post.getDateMe());
                    Log.i(TAG,"Post: " + post.getDescription() + ", username: " + post.getUser().getUsername() + " date: " + stringDate + " time " + stringTime);
                }
                addAll(posts);
                swipeContainer.setRefreshing(false);
                //adapter.notifyDataSetChanged();
            }
        });
    }

    public void clear() {
        allPosts.clear();
        adapter.notifyDataSetChanged();
    }

    // Add a list of items -- change to type used
    public void addAll(List<Post> posts) {
        allPosts.addAll(posts);
        adapter.notifyDataSetChanged();
    }
}