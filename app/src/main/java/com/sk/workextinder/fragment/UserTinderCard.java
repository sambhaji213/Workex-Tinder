package com.sk.workextinder.fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Point;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.mindorks.placeholderview.annotations.Click;
import com.mindorks.placeholderview.annotations.Layout;
import com.mindorks.placeholderview.annotations.Resolve;
import com.mindorks.placeholderview.annotations.View;
import com.mindorks.placeholderview.annotations.swipe.SwipeCancelState;
import com.mindorks.placeholderview.annotations.swipe.SwipeTouch;
import com.mindorks.placeholderview.annotations.swipe.SwipeView;
import com.sk.workextinder.R;
import com.sk.workextinder.activity.UserDetailsActivity;
import com.sk.workextinder.model.Friends;
import com.sk.workextinder.model.User;
import com.sk.workextinder.util.AppAndroidUtils;
import com.sk.workextinder.util.AppConstants;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Sambhaji Karad on 17-03-2019.
 */

@Layout(R.layout.row_card_item)
public class UserTinderCard {

    @View(R.id.profileImageView)
    ImageView profileImageView;

    @View(R.id.textViewName)
    TextView textViewName;

    @View(R.id.textViewAge)
    TextView textViewAge;

    @View(R.id.textViewLocation)
    TextView textViewLocation;

    @View(R.id.cardView)
    CardView cardView;

    @SwipeView
    android.view.View mSwipeView;

    private User mUser;
    private Context mContext;
    private Point mCardViewHolderSize;
    private List<User> mUserList;

    public UserTinderCard(Context context, User user, List<User> userList, Point cardViewHolderSize) {
        mContext = context;
        mUser = user;
        mUserList = userList;
        mCardViewHolderSize = cardViewHolderSize;
    }

    @SuppressLint("SetTextI18n")
    @Resolve
    void onResolved() {
        Glide.with(mContext).load(mUser.getAvatar()).into(profileImageView);
        textViewName.setText(AppAndroidUtils.getFullName(mUser.getFirstName(), mUser.getLastName()));
        if (mUser.getDob() != null && !mUser.getDob().isEmpty())
            textViewAge.setText(String.valueOf(AppAndroidUtils.getAge(mUser.getDob())));
        textViewLocation.setText(mUser.getAddress().getStreet() + ", " + mUser.getAddress().getCity());
        mSwipeView.setAlpha(1);

        CardsFragment.currentUser = mUser;

        List<User> userFriendList = new ArrayList<>();
        if (mUser.getFriends() != null && mUser.getFriends().size() > 0) {
            for (User userModel : mUserList) {
                for (Friends friends : mUser.getFriends()) {
                    if (userModel.getId() == friends.getId()) {
                        userFriendList.add(userModel);
                    }
                }
            }
        }
        FriendFragment.friendList(userFriendList, mContext);
    }

    @Click(R.id.cardView)
    void onClick() {
        openUserDetailsActivity();
    }

    private void openUserDetailsActivity() {
        Intent intent = new Intent(mContext, UserDetailsActivity.class);
        intent.putExtra(AppConstants.MODEL, mUser);
        mContext.startActivity(intent);
    }

    @SwipeCancelState
    void onSwipeCancelState() {
        Log.d("DEBUG", "onSwipeCancelState");
        mSwipeView.setAlpha(1);
    }

    @SwipeTouch
    void onSwipeTouch(float xStart, float yStart, float xCurrent, float yCurrent) {

        float cardHolderDiagonalLength =
                (float) Math.sqrt(Math.pow(mCardViewHolderSize.x, 2) + (Math.pow(mCardViewHolderSize.y, 2)));
        float distance = (float) Math.sqrt(Math.pow(xCurrent - xStart, 2) + (Math.pow(yCurrent - yStart, 2)));

        float alpha = 1 - distance / cardHolderDiagonalLength;

        Log.d("DEBUG", "onSwipeTouch "
                + " xStart : " + xStart
                + " yStart : " + yStart
                + " xCurrent : " + xCurrent
                + " yCurrent : " + yCurrent
                + " distance : " + distance
                + " TotalLength : " + cardHolderDiagonalLength
                + " alpha : " + alpha
        );

        mSwipeView.setAlpha(alpha);
    }
}
