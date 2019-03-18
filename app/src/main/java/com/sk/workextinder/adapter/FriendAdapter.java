package com.sk.workextinder.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.sk.workextinder.R;
import com.sk.workextinder.activity.UserDetailsActivity;
import com.sk.workextinder.model.User;
import com.sk.workextinder.util.AppAndroidUtils;
import com.sk.workextinder.util.AppConstants;
import com.sk.workextinder.util.CircleImageView;

import java.util.List;

public class FriendAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<User> userArrayList;
    private Context mContext;

    public FriendAdapter(Context context, List<User> data) {
        this.mContext = context;
        this.userArrayList = data;
    }

    @Override
    public int getItemCount() {
        return userArrayList.size();
    }

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View rootCategoryView = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_friend_item, parent, false);
        return new MessageViewHolder(rootCategoryView);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        final User model = userArrayList.get(position);
        MessageViewHolder messageViewHolder = (MessageViewHolder) holder;
        Glide.with(mContext).load(model.getAvatar())
                .into(messageViewHolder.imageViewIcon);
        messageViewHolder.textViewName.setText(AppAndroidUtils.getFullName(model.getFirstName(), model.getLastName()));
        messageViewHolder.textViewAge.setText(String.valueOf(AppAndroidUtils.getAge(model.getDob())));
        messageViewHolder.textViewLocation.setText(model.getAddress().getStreet() + ", " + model.getAddress().getCity());

        messageViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, UserDetailsActivity.class);
                intent.putExtra(AppConstants.MODEL, model);
                mContext.startActivity(intent);
            }
        });
    }

    private class MessageViewHolder extends RecyclerView.ViewHolder {

        private CircleImageView imageViewIcon;
        private TextView textViewName;
        private TextView textViewAge;
        private TextView textViewLocation;

        private MessageViewHolder(View itemView) {
            super(itemView);
            imageViewIcon = itemView.findViewById(R.id.imageViewIcon);
            textViewName = itemView.findViewById(R.id.textViewName);
            textViewAge = itemView.findViewById(R.id.textViewAge);
            textViewLocation = itemView.findViewById(R.id.textViewLocation);
        }
    }
}
