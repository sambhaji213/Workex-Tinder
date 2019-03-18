package com.sk.workextinder.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.sk.workextinder.R;
import com.sk.workextinder.adapter.FriendAdapter;
import com.sk.workextinder.model.User;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Sambhaji Karad on 14-03-2019.
 */

public class FriendFragment extends Fragment {

    private static RecyclerView recyclerView;
    private static TextView textViewNoResult;
    private static FriendAdapter mAdapter;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_friend, container, false);

        recyclerView = rootView.findViewById(R.id.recyclerView);
        textViewNoResult = rootView.findViewById(R.id.textViewNoResult);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 1);
        recyclerView.setLayoutManager(gridLayoutManager);
        return rootView;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    public static void friendList(List<User> userList, Context context) {
        if (userList != null && userList.size() > 0) {
            mAdapter = new FriendAdapter(context, userList);
            textViewNoResult.setVisibility(View.GONE);
        } else {
            mAdapter = new FriendAdapter(context, new ArrayList<User>());
            textViewNoResult.setVisibility(View.VISIBLE);
        }
        recyclerView.setAdapter(mAdapter);

    }
}
