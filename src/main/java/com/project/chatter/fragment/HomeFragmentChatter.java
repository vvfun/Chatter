package com.project.chatter.fragment;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.View;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.project.chatter.constant.Constants;
import com.project.chatter.R;
import com.project.chatter.adapter.ChatterListAdapter;
import com.project.chatter.bean.RecordsBean;
import com.project.chatter.bean.UserBean;
import com.project.chatter.data.DataManager;
import com.project.chatter.http.HttpMethods;
import com.project.chatter.http.HttpObserver;
import com.project.chatter.http.OnHttpErrorListener;
import com.project.chatter.http.SubscriberOnNextListener;
import com.project.chatter.utils.Preferences;
import com.project.chatter.utils.SpaceItemDecoration;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadmoreListener;

import java.util.ArrayList;
import java.util.List;

public class HomeFragmentChatter extends BaseFragmentChatter implements OnRefreshLoadmoreListener{

    private static final String TAG = "HomeFragment";
    private ChatterListAdapter mAdapter;
    private int mPageNo;


    private Handler mHandler = new Handler(Looper.getMainLooper()) {
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    getChatStatus();
                    mHandler.sendEmptyMessageDelayed(0, 60 * 1000);
                    break;
            }

        }
    };
    private RecyclerView recyclerView;
    private int mode;
    public static int MODE_NEW = 1;
    public boolean isFirst = true;

    @Override
    protected int getLayoutId() {
        return R.layout.chatter_fragment_home;
    }

    @Override
    protected void onViewCreated(View view) {
        if (null != getArguments()){
            mode = getArguments().getInt(Constants.KEY_MODE,0);
        }
        SmartRefreshLayout smartRefreshLayout = view.findViewById(R.id.smartRefreshLayout);
        smartRefreshLayout.setOnRefreshLoadmoreListener(this);
        recyclerView = view.findViewById(R.id.recyclerView);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(),2);
        recyclerView.addItemDecoration(new SpaceItemDecoration(10,10));
        recyclerView.setLayoutManager(gridLayoutManager);
        getAdapter().bindToRecyclerView(recyclerView);
        smartRefreshLayout.autoRefresh();
        smartRefreshLayout.setEnableLoadmore(false);
        mHandler.sendEmptyMessageDelayed(0, 30 * 1000);

        getAgoId();
    }


    private ChatterListAdapter getAdapter() {
        if (mAdapter == null) {
            mAdapter = new ChatterListAdapter(getActivity());
        }
        return mAdapter;
    }

    @Override
    public void updateUIText() {
    }

    public void onDestroyView() {
        mHandler.removeMessages(0);
        mHandler.removeCallbacksAndMessages(null);
        super.onDestroyView();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
        }
    }


    @Override
    public void onLoadmore(RefreshLayout refreshlayout) {
        getUserList(mPageNo + 1, refreshlayout);
    }

    @Override
    public void onRefresh(final RefreshLayout refreshlayout) {
        getUserList(1, refreshlayout);
        getUserInfo();
    }

    @Override
    public void onResume() {
        super.onResume();
        getUserInfo();
    }

    private void getUserList(final int pageNo, final RefreshLayout refreshlayout) {

        HttpMethods.getInstance().userList(pageNo, Constants.ONE_PAGE_SIZE,mode,
                isFirst?0:1,
                new HttpObserver(new SubscriberOnNextListener<RecordsBean>() {
                    @Override
                    public void onNext(RecordsBean bean, String msg) {
                        if (getView() == null) {
                            return;
                        }
                        isFirst = false;
                        refreshlayout.finishRefresh();
                        refreshlayout.finishLoadmore();
                        ArrayList<UserBean> list = bean.getRecords();

                        mPageNo = pageNo;
                        if (list != null && !list.isEmpty()) {

                            if (pageNo == 1) {
                                getAdapter().setNewData(list);
                            } else {
                                getAdapter().addData(list);
                            }

                        }

                        refreshlayout.setEnableLoadmore(list != null && list.size() > 0);

                    }
                }, getActivity(), false, new OnHttpErrorListener() {
                    @Override
                    public void onConnectError(Throwable e) {
                        if (getView() == null) {
                            return;
                        }
                        refreshlayout.finishRefresh();
                        refreshlayout.finishLoadmore();
                    }

                    @Override
                    public void onServerError(int errorCode, String errorMsg) {
                        if (getView() == null) {
                            return;
                        }
                        refreshlayout.finishRefresh();
                        refreshlayout.finishLoadmore();
                    }
                }));
    }

    private void getChatStatus() {
        HttpMethods.getInstance().chatStatus(new HttpObserver(new SubscriberOnNextListener<ArrayList<UserBean>>() {
            @Override
            public void onNext(ArrayList<UserBean> list, String msg) {
                if (getView() == null || list == null || list.isEmpty()) {
                    return;
                }
                List<UserBean> userList = getAdapter().getData();
                if (userList == null || userList.isEmpty()) {
                    return;
                }
                for (UserBean user : userList) {
                    for (UserBean bean : list) {
                        if (user.getUid() == bean.getUid()) {
                            user.setChatStatus(bean.getChatStatus());
                            user.setChatRealStatus(bean.getChatRealStatus());
                            list.remove(bean);
                            break;
                        }
                    }
                }
                getAdapter().resortUser();
            }
        }, getActivity(), false, new OnHttpErrorListener() {
            @Override
            public void onConnectError(Throwable e) {
            }

            @Override
            public void onServerError(int errorCode, String errorMsg) {
            }
        }));
    }


    private void getUserInfo(){
            HttpMethods.getInstance().userInfo(Preferences.getInstacne().getUid(), new HttpObserver(new SubscriberOnNextListener<UserBean>() {
                @Override
                public void onNext(UserBean user, String msg) {
                    if (null != user){
                        DataManager.getInstance().saveMyUserInfo(user);
                    }


                }
            }, new OnHttpErrorListener() {
                @Override
                public void onConnectError(Throwable e) {

                }

                @Override
                public void onServerError(int errorCode, String errorMsg) {

                }
            }));
    }


    private void getAgoId() {
        HttpMethods.getInstance().getAgoId(new HttpObserver(new SubscriberOnNextListener<String>() {
            @Override
            public void onNext(String agoId, String msg) {
                DataManager.getInstance().setAgoId(agoId);
            }
        }, new OnHttpErrorListener() {
            @Override
            public void onConnectError(Throwable e) {

            }

            @Override
            public void onServerError(int errorCode, String errorMsg) {

            }
        }));
    }
}
