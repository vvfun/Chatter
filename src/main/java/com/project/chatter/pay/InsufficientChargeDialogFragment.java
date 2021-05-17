package com.project.chatter.pay;


import android.app.Activity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.project.chatter.R;
import com.project.chatter.activity.BaseActivityChatter;
import com.project.chatter.bean.DiamondProductDto;
import com.project.chatter.bean.PayDiscountInfoDto;
import com.project.chatter.bean.ProductBean;
import com.project.chatter.http.HttpMethods;
import com.project.chatter.http.HttpObserver;
import com.project.chatter.http.SubscriberOnNextListener;
import com.project.chatter.utils.MyUtils;


public class InsufficientChargeDialogFragment extends MyDialogFragment implements View.OnClickListener {


    Activity mContext;
    private View mView;
    CallBack mCallBack;
    private TextView title,tv_desc;
    private TextView tv_diamond;
    private TextView tv_diamond_diff,tv_money;
    private PayDiscountInfoDto mPayDiscountInfoDto;
    private DiamondProductDto item;
    private TextView tv_cancel;

    public InsufficientChargeDialogFragment(Activity context, CallBack callBack) {
        mContext = context;
        mCallBack = callBack;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.chatter_dialog_insufficient_diamonds, container, false);

        mView = view;
        setDialogViewsOnClickListener(view, R.id.bg, R.id.ll_buy);

        title = mView.findViewById(R.id.title);
        tv_desc = mView.findViewById(R.id.tv_desc);
        tv_diamond = mView.findViewById(R.id.tv_diamond);
        tv_diamond_diff = mView.findViewById(R.id.tv_diamond_diff);
        tv_money = mView.findViewById(R.id.tv_money);
        tv_cancel = mView.findViewById(R.id.tv_cancel);
        if (mOnMyDialogListener != null) {
            mOnMyDialogListener.initView(view);
        }


        getFirstDiscount();
             return view;
    }

    private void getFirstDiscount() {
        HttpMethods.getInstance().getDiscountInfo("buyDiamond",new HttpObserver(new SubscriberOnNextListener<PayDiscountInfoDto>() {
            @Override
            public void onNext(PayDiscountInfoDto payDiscountInfoDto, String msg) {

                mPayDiscountInfoDto = payDiscountInfoDto;
                if (getActivity() != null) {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            initView();
                        }
                    });
                }

            }
        },
                (BaseActivityChatter)getActivity(),(BaseActivityChatter)getActivity()));
        initView();
    }




    private void initView() {




        if (null != mPayDiscountInfoDto){

            if (!TextUtils.isEmpty(mPayDiscountInfoDto.getTitle())){
                title.setText(mPayDiscountInfoDto.getTitle());
            }
            if (!TextUtils.isEmpty(mPayDiscountInfoDto.getRemark())){
                tv_desc.setText(mPayDiscountInfoDto.getRemark());
            }
            if (null != mPayDiscountInfoDto.getProductList() &&
                    mPayDiscountInfoDto.getProductList().size() > 0){

                item = mPayDiscountInfoDto.getProductList().get(0);

                tv_diamond.setText(item.getDiamond()+"");
                tv_diamond_diff.setText(item.getDiffDiamond() == 0 ?"":("+"+item.getDiffDiamond()+""));

                String unit = !TextUtils.isEmpty(item.getRegionUnitDesc()) ?item.getRegionUnitDesc():"USD";
                String money = "";
                if (item.getRegionRate() == 0){
                    unit = "USD";
                    money = item.getMoney()+"";
                }else {
                    unit = item.getRegionUnitDesc();
                    money = item.getRegionMoney()+"";
                }
                tv_money.setText(money+" "+unit);
            }




        }
    }
    @Override
    public void onClick(View v) {
        if (isCancelable()){
            dismiss();
        }
        int id = v.getId();
        if (id == R.id.bg) {
        } else if (id == R.id.ll_buy) {
            if (null != item){
                ProductBean mSelectBean = new ProductBean();
                mSelectBean.setProductIosId(item.getProductIosId());
                mSelectBean.setMoney(item.getMoney());
                MyUtils.ShowPayDialog(mContext, mSelectBean, false, new MyUtils.ActionCallBack() {
                    @Override
                    public void back() {
                        if (null != mCallBack) {
                            mCallBack.callBack();
                        }
                    }
                });
            }
        } else if (id == R.id.tv_cancel) {
            if (null != mCallBack) {
                mCallBack.close();
            }
        } else if (id == R.id.tv_more) {
//            if (null != mContext && mContext instanceof BaseActivityChatter) {
//                ((BaseActivityChatter) mContext).gotoPager(WalletFragment.class, null);
//            }
        }
    }

    public void onDismiss() {
        this.dismiss();
    }

    @Override
    public void dismiss() {
        super.dismiss();
    }

    public interface CallBack{
        void callBack();
        void close();
    }
    public void setCancelBtnVisible(){
        tv_cancel.setVisibility(View.VISIBLE);
    }
}
