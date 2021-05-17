package com.project.chatter.adapter;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.project.chatter.constant.Constants;
import com.project.chatter.R;
import com.project.chatter.activity.BaseActivityChatter;
import com.project.chatter.bean.UserBean;
import com.project.chatter.call.CallType;
import com.project.chatter.data.DataManager;
import com.project.chatter.fragment.CallFragmentChatter;
import com.project.chatter.utils.ChatterToast;
import com.project.chatter.utils.MyUtils;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class ChatterListAdapter extends BaseQuickAdapter<UserBean, BaseViewHolder> {

    private Context mContext;



    public ChatterListAdapter(Context context) {
        super(R.layout.chatter_item_home);
        mContext = context;
        setOnItemChildClickListener(new OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                final UserBean mCurrentUser = (UserBean) adapter.getItem(position);
                if (view.getId() == R.id.iv_video) {
                    final UserBean userInfo = DataManager.getInstance().getMyUserInfo();
                    if (null != userInfo) {
                        if (!MyUtils.isSelfAnchor() && (userInfo.getDiamond() == 0 ||
                                (null != mCurrentUser && userInfo.getDiamond() < mCurrentUser.getMinConsumeDiamond()))) {
                            ShowChargeDialog();
                        } else {
                            checkAnchorStatusAndCall(mCurrentUser);
                        }
                    }
                }
            }
        });

    }

    @Override
    protected void convert(BaseViewHolder helper, UserBean item) {
        RelativeLayout container = helper.getView(R.id.container);
        int position = helper.getAdapterPosition();
        int height = MyUtils.getMaxHeight(mContext)/3;
            container.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, height+MyUtils.dip2px(mContext,53)));



        if (item.getChatNums() > 0){
            helper.getView(R.id.tv_chat_time).setVisibility(View.VISIBLE);
            helper.setText(R.id.tv_chat_time, String.format(mContext.getResources().getString(R.string.chatter_format_chat_time),item.getChatNums()+""));
        }else {
            helper.getView(R.id.tv_chat_time).setVisibility(View.GONE);
        }

        if (item.getUserStatus() == 2){
            helper.setGone(R.id.iv_hot,true);
        }else {
            helper.setGone(R.id.iv_hot,false);
        }

        helper.addOnClickListener(R.id.iv)
        .addOnClickListener(R.id.iv_video);
        helper.setText(R.id.tvName, item.getNickname())
                .setText(R.id.tvAge, item.getAge() > 0 ? String.valueOf(item.getAge()) : "");
            helper.setGone(R.id.ll1,false);
            helper.setGone(R.id.ll_userType,false);
        switch (item.getChatStatus()) {
            case 1:
                helper.setText(R.id.tvState, R.string.chatter_online)
                        .setImageResource(R.id.ivState, R.drawable.chatter_bg_user_state_online);
                break;
            case 2:
                helper.setText(R.id.tvState, R.string.chatter_busy)
                        .setImageResource(R.id.ivState, R.drawable.chatter_bg_user_state_busy);
                break;
            default:
                helper.setText(R.id.tvState, R.string.chatter_offline)
                        .setImageResource(R.id.ivState, R.drawable.chatter_bg_user_state_offline);
                break;

        }
        MyUtils.loadImage(0, item.getAvatar(), (ImageView) helper.getView(R.id.iv));
    }


    public void resortUser() {
        List<UserBean> list = getData();
        if (list == null || list.isEmpty()) {
            return;
        }
        Collections.sort(list, new Comparator<UserBean>() {
            @Override
            public int compare(UserBean user1, UserBean user2) {
                int status1 = user1.getChatStatus();
                int status2 = user2.getChatStatus();
                if (status1 == status2) {
                    return 0;
                }
                if (status1 == 1) {
                    return -1;
                }
                if (status2 == 1) {
                    return 1;
                }
                if (status1 == 2) {
                    return -1;
                }
                if (status2 == 2) {
                    return 1;
                }
                return 0;
            }
        });
        notifyDataSetChanged();
    }



    private void checkAnchorStatusAndCall(UserBean mCurrentUser) {
        switch (mCurrentUser.getChatStatus()) {
            case 1:
                Bundle bundle = new Bundle();
                bundle.putInt(Constants.KEY_BASE_BEAN, CallType.Call.getType());
                bundle.putSerializable(Constants.KEY_BASE_BEAN_1, mCurrentUser);
                ((BaseActivityChatter)mContext).gotoPager(CallFragmentChatter.class, bundle);//TODO test
                break;
            case 2:
                ChatterToast.Toast(mContext.getResources().getString(R.string.chatter_user_busy));
                break;
            default:
                ChatterToast.Toast(mContext.getResources().getString(R.string.chatter_user_offline));
                break;

        }
    }
    private void ShowChargeDialog() {

        MyUtils.ShowChargeDiamondDialog((BaseActivityChatter)mContext, new MyUtils.DiamondDialogCallBack() {
            @Override
            public void back(String way) {
            }
        });
    }

}
