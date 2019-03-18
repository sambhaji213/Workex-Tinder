package com.sk.workextinder.fragment;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Point;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;

import com.google.gson.Gson;
import com.mindorks.placeholderview.SwipeDecor;
import com.mindorks.placeholderview.SwipeDirectionalView;
import com.sk.workextinder.R;
import com.sk.workextinder.activity.ChatActivity;
import com.sk.workextinder.model.User;
import com.sk.workextinder.network.ApiClient;
import com.sk.workextinder.network.ApiInterface;
import com.sk.workextinder.util.AppAndroidUtils;
import com.sk.workextinder.util.AppConstants;
import com.sk.workextinder.util.AppPreferenceStorage;
import com.sk.workextinder.util.AppWaitDialog;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Sambhaji Karad on 14-03-2019.
 */

public class CardsFragment extends Fragment {

    private static final String TAG = CardsFragment.class.getSimpleName();
    private AppWaitDialog mWaitDialog;

    private SwipeDirectionalView mSwipeView;
    public static User currentUser;

    private List<User> userParentList = new ArrayList<>();

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_cards, container, false);

        mSwipeView = rootView.findViewById(R.id.swipeView);

        rootView.findViewById(R.id.imageButtonSearch).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showSearchFilterDialog();
            }
        });

        rootView.findViewById(R.id.imageButtonSort).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showFilterDialog();
            }
        });

        rootView.findViewById(R.id.imageButtonReject).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSwipeView.doSwipe(false);
            }
        });

        rootView.findViewById(R.id.imageButtonAccept).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSwipeView.doSwipe(true);
            }
        });

        rootView.findViewById(R.id.imageButtonChat).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openChatActivity();
            }
        });
        return rootView;
    }

    private void showFilterDialog() {
        final Dialog dialog = new Dialog(Objects.requireNonNull(getActivity()), R.style.Theme_Dialog);
        dialog.setTitle(R.string.hint_sort_list);
        dialog.setContentView(R.layout.dialog_sort_filter);

        final RadioGroup radioGroupFilter = dialog.findViewById(R.id.radioGroupFilter);
        Button buttonCancel = dialog.findViewById(R.id.buttonCancel);
        Button buttonApply = dialog.findViewById(R.id.buttonApply);

        buttonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AppAndroidUtils.hideKeyboard(Objects.requireNonNull(getActivity()));
                dialog.cancel();
            }
        });

        buttonApply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AppAndroidUtils.hideKeyboard(Objects.requireNonNull(getActivity()));
                final int selectedId = radioGroupFilter.getCheckedRadioButtonId();
                switch (selectedId) {
                    case R.id.radioButtonFirstName:
                        setListToTinderCard(sortListByFirstName(userParentList));
                        break;
                    case R.id.radioButtonLastName:
                        setListToTinderCard(sortListByLastName(userParentList));
                        break;
                    case R.id.radioButtonEmail:
                        setListToTinderCard(sortListByEmail(userParentList));
                        break;
                    case R.id.radioButtonDOB:
                        setListToTinderCard(sortListByDOB(userParentList));
                        break;
                }

                dialog.cancel();
            }
        });
        dialog.show();
    }

    private void showSearchFilterDialog() {
        final Dialog dialog = new Dialog(Objects.requireNonNull(getActivity()), R.style.Theme_Dialog);
        dialog.setTitle(R.string.hint_search_list);
        dialog.setContentView(R.layout.dialog_search_filter);

        final RadioGroup radioGroupFilter = dialog.findViewById(R.id.radioGroupFilter);
        final EditText editTextSearch = dialog.findViewById(R.id.editTextSearch);
        Button buttonCancel = dialog.findViewById(R.id.buttonCancel);
        Button buttonApply = dialog.findViewById(R.id.buttonApply);

        buttonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AppAndroidUtils.hideKeyboard(Objects.requireNonNull(getActivity()));
                dialog.cancel();
            }
        });

        buttonApply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AppAndroidUtils.hideKeyboard(Objects.requireNonNull(getActivity()));
                String searchQuery = editTextSearch.getText().toString().trim();
                if (searchQuery.isEmpty()) {
                    editTextSearch.setError("Please enter input");
                    editTextSearch.requestFocus();
                    return;
                }

                final int selectedId = radioGroupFilter.getCheckedRadioButtonId();
                if (selectedId < 0) {
                    AppAndroidUtils.showShortToastMessage(getActivity(), getString(R.string.hint_please_select_search_by));
                    return;
                }

                switch (selectedId) {
                    case R.id.radioButtonFirstName:
                        searchByNameEmail(1, searchQuery);
                        break;
                    case R.id.radioButtonLastName:
                        searchByNameEmail(2, searchQuery);
                        break;
                    case R.id.radioButtonEmail:
                        searchByNameEmail(3, searchQuery);
                        break;
                }
                dialog.cancel();
            }
        });
        dialog.show();
    }

    private void searchByNameEmail(int type, String searchQuery) {
        switch (type) {
            case 1:
                searchFirstNameInList(searchQuery);
                break;
            case 2:
                searchLastNameInList(searchQuery);
                break;
            case 3:
                searchEmailInList(searchQuery);
                break;
        }
    }

    private void searchFirstNameInList(String search) {
        List<User> userList = new ArrayList<>();
        for (User user : userParentList) {
            if (user.getFirstName() != null && user.getFirstName().toLowerCase().contains(search.toLowerCase())) {
                userList.add(user);
            }
        }
        if (userList.size() > 0) {
            setListToTinderCard(userList);
        } else {
            AppAndroidUtils.showShortToastMessage(getActivity(), getString(R.string.hint_no_result_found));
        }
    }

    private void searchLastNameInList(String search) {
        List<User> userList = new ArrayList<>();
        for (User user : userParentList) {
            if (user.getLastName() != null && !user.getLastName().toLowerCase().contains(search.toLowerCase())) {
                userList.add(user);
            }
        }
        if (userList.size() > 0) {
            setListToTinderCard(userList);
        } else {
            AppAndroidUtils.showShortToastMessage(getActivity(), getString(R.string.hint_no_result_found));
        }
    }

    private void searchEmailInList(String search) {
        List<User> userList = new ArrayList<>();
        for (User user : userParentList) {
            if (user.getEmail() != null && user.getEmail().toLowerCase().contains(search)) {
                userList.add(user);
            }
        }

        if (userList.size() > 0) {
            setListToTinderCard(userList);
        } else {
            AppAndroidUtils.showShortToastMessage(getActivity(), getString(R.string.hint_no_result_found));
        }
    }

    private List<User> sortListByFirstName(List<User> userParentList) {
        Collections.sort(userParentList, new Comparator<User>() {
            public int compare(User p1, User p2) {
                int res = p1.getFirstName().compareToIgnoreCase(p2.getFirstName());
                if (res != 0)
                    return res;
                return p1.getFirstName().compareToIgnoreCase(p2.getFirstName());
            }
        });
        return userParentList;
    }

    private List<User> sortListByLastName(List<User> userParentList) {
        Collections.sort(userParentList, new Comparator<User>() {
            public int compare(User p1, User p2) {
                int res = p1.getLastName().compareToIgnoreCase(p2.getLastName());
                if (res != 0)
                    return res;
                return p1.getLastName().compareToIgnoreCase(p2.getLastName());
            }
        });
        return userParentList;
    }

    private List<User> sortListByEmail(List<User> userParentList) {
        Collections.sort(userParentList, new Comparator<User>() {
            public int compare(User p1, User p2) {
                int res = p1.getEmail().compareToIgnoreCase(p2.getEmail());
                if (res != 0)
                    return res;
                return p1.getEmail().compareToIgnoreCase(p2.getEmail());
            }
        });
        return userParentList;
    }

    private List<User> sortListByDOB(List<User> userParentList) {
        Collections.sort(userParentList, new Comparator<User>() {
            public int compare(User p1, User p2) {
                int res = p1.getDob().compareToIgnoreCase(p2.getDob());
                if (res != 0)
                    return res;
                return p1.getDob().compareToIgnoreCase(p2.getDob());
            }
        });
        return userParentList;
    }

    private void openChatActivity() {
        Intent intent = new Intent(getActivity(), ChatActivity.class);
        intent.putExtra(AppConstants.MODEL, currentUser);
        startActivity(intent);
    }

    private void setListToTinderCard(List<User> userList) {
        mSwipeView.removeAllViews();
        int bottomMargin = AppAndroidUtils.dpToPx(160);
        Point windowSize = AppAndroidUtils.getDisplaySize(Objects.requireNonNull(getActivity()).getWindowManager());
        int mAnimationDuration = 300;
        mSwipeView.getBuilder()
                .setDisplayViewCount(3)
                .setIsUndoEnabled(true)
                .setSwipeVerticalThreshold(AppAndroidUtils.dpToPx(50))
                .setSwipeHorizontalThreshold(AppAndroidUtils.dpToPx(50))
                .setHeightSwipeDistFactor(10)
                .setWidthSwipeDistFactor(5)
                .setSwipeDecor(new SwipeDecor()
                        .setViewWidth(windowSize.x)
                        .setViewHeight(windowSize.y - bottomMargin)
                        .setViewGravity(Gravity.TOP)
                        .setPaddingTop(20)
                        .setSwipeAnimTime(mAnimationDuration)
                        .setRelativeScale(0.01f));

        Point cardViewHolderSize = new Point(windowSize.x, windowSize.y - bottomMargin);

        if (userList != null && userList.size() > 0) {
            AppPreferenceStorage.saveUserDetails(new Gson().toJson(userList.get(0)));
            for (User user : userList) {
                mSwipeView.addView(new UserTinderCard(getActivity(), user, userList, cardViewHolderSize));
            }
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        mWaitDialog = new AppWaitDialog(getActivity());
        getUserList();
    }

    private void getUserList() {
        mWaitDialog.show();
        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);

        Call<List<User>> call = apiService.getUserList();
        call.enqueue(new Callback<List<User>>() {
            @Override
            public void onResponse(@NonNull Call<List<User>> call, @NonNull Response<List<User>> response) {
                if (mWaitDialog.isShowing()) {
                    mWaitDialog.hide();
                }
                List<User> userList = response.body();
                if (userList != null) {
                    userParentList = userList;
                    setListToTinderCard(userList);
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<User>> call, @NonNull Throwable t) {
                Log.e(TAG, t.toString());
                if (mWaitDialog.isShowing()) {
                    mWaitDialog.hide();
                }
            }
        });
    }
}
