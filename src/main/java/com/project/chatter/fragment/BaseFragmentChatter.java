package com.project.chatter.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.InputFilter;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import com.project.chatter.activity.BaseActivityChatter;
import com.project.chatter.http.HttpMethods;
import com.project.chatter.http.HttpObserver;
import com.project.chatter.http.OnHttpErrorListener;
import com.project.chatter.http.SubscriberOnNextListener;
import com.project.chatter.pay.PayManager;


/**
 * Fragment基类提供公共的页面跳转方面，公共弹窗等方法
 *
 */
public abstract class BaseFragmentChatter extends Fragment implements View.OnClickListener {

    // 标示是否第一次执行onStart页面
    private boolean mIsFirstOnStart = true;
    private DisplayMetrics mDisplaymetrics;
    private InputMethodManager mInputMethodManager;


    protected abstract int getLayoutId();

    /**
     * 构造函数，不能使用带有参数的构造函数，因为系统自动回收后，会调用没有参数的构造函数
     */
    public BaseFragmentChatter() {
        super();
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }


    protected void setEditTextHint(int id, String text) {
        EditText et = (EditText) getView().findViewById(id);
        et.setHint(text == null ? "" : text);
    }


    protected void setText(TextView tv, String text) {
        tv.setText(text == null ? "" : text);
    }

    protected void setText(int id, String text) {
        if (null == getView()){
            return;
        }
        TextView tv = (TextView) getView().findViewById(id);
        tv.setText(text == null ? "" : text);
    }

    protected void setText(int id, int textId) {
        if (null == getView()){
            return;
        }
        TextView tv = getView().findViewById(id);
        tv.setText(getString(textId));
    }

    protected void setTextColor(int id, int clorId) {
        TextView tv = getView().findViewById(id);
        tv.setTextColor(getResources().getColor(clorId));
    }

    protected void setImage(int id, int drawableId) {
        ImageView iv = (ImageView) getView().findViewById(id);
        iv.setImageResource(drawableId);
    }

    @RequiresApi(api = Build.VERSION_CODES.CUPCAKE)
    protected InputMethodManager getInputMethodManager() {
        if (mInputMethodManager == null) {
            mInputMethodManager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        }
        return mInputMethodManager;
    }


    protected String getTextById(int resId) {
        if (getView() == null) {
            return "";
        }
        return ((TextView) getView().findViewById(resId)).getText().toString().trim();
    }

    protected void setViewVisible(int... viewIds) {
        if (viewIds == null || getView() == null) {
            return;
        }
        for (int viewId : viewIds) {
            View view = getView().findViewById(viewId);
            if (view != null)
                view.setVisibility(View.VISIBLE);
        }
    }

    protected void setViewGone(int... viewIds) {
        if (viewIds == null || getView() == null) {
            return;
        }
        for (int viewId : viewIds) {
            View view = getView().findViewById(viewId);
            if (view != null)
                view.setVisibility(View.GONE);
        }
    }

    protected void setViewInvisible(int... viewIds) {
        if (viewIds == null || getView() == null) {
            return;
        }
        for (int viewId : viewIds) {
            View view = getView().findViewById(viewId);
            if (view != null)
                view.setVisibility(View.INVISIBLE);
        }
    }

    protected void setEditTextMaxLength(int etId, int maxLength) {
        ((EditText) getView().findViewById(etId)).setFilters(new InputFilter[]{new InputFilter.LengthFilter(maxLength)});
    }

    public void hideKeyBoard() {
//        InputMethodManager in = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
//        if (getActivity() != null && getActivity().getCurrentFocus() != null) {
//            in.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
//        }
    }

    /**
     * 关闭loading的页面
     */
    public void hideLoadingDialog() {
//        if (null != getActivity()){
//            ((BaseActivityPoChat) getActivity()).hideLoadingDialog();
//        }
    }


    public void showLoadingDialog() {
//        if (null != getActivity()){
//            ((BaseActivityPoChat) getActivity()).showLoadingDialog();
//        }
    }

    public void showLoadingDialog(String text) {
//        if (null != getActivity()){
//            ((BaseActivityPoChat) getActivity()).showLoadingDialog(text);
//        }
    }

    protected <VT extends View> VT fv(View parent, int id) {
        return (VT) parent.findViewById(id);
    }

    protected <VT extends View> VT fv(int id) {
        if (null != getView()){

            return (VT) getView().findViewById(id);
        }else {

            return (VT) mView.findViewById(id);
        }


    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
    }


    public void closeKeyBoard() {
//        if (getContext() != null) {
//            InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
//            imm.hideSoftInputFromWindow(getView().getWindowToken(), 0); //强制隐藏键盘
//        }
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mIsFirstOnStart = true;
    }


    public int[] getInScreen(View v) {
        int[] location = new int[2];
        v.getLocationOnScreen(location);
        return location;
    }

    /**
     * 跳转到新的界面
     *
     * @param pagerClass
     * @param bundle
     */
//    public void gotoPager(final Class<?> pagerClass, final Bundle bundle) {
//
//        if (getActivity() instanceof BaseActivityPoChat) {
//
//            ((BaseActivityPoChat) getActivity()).gotoPager(pagerClass, bundle);
//        }
//    }
//
//
//    /**
//     * 返回，如果stack中还有Fragment的话，则返回stack中的fragment，否则 finish当前的Activity
//     */
    public void goBack() {
        closeKeyBoard();
        if (getActivity() != null) {
            ((BaseActivityChatter) getActivity()).goBack();
        }
    }

    View mView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        mView = inflater.inflate(getLayoutId(), null);
        return mView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onViewCreated(view, savedInstanceState);
        onViewCreated(view);
    }

    public void setViewsOnClickListener(int... viewIds) {
        if (viewIds == null || getView() == null) {
            return;
        }
        for (int viewId : viewIds) {
            View view = getView().findViewById(viewId);
            if (view != null)
                view.setOnClickListener(this);
        }
    }

    /**
     * fragment的View创建好后调用
     */
    protected abstract void onViewCreated(View view);

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public void onStop() {
        mIsFirstOnStart = true;
        super.onStop();

    }


    @Override
    public void onStart() {
        super.onStart();
        if (mIsFirstOnStart) {
            updateUIText();
            mIsFirstOnStart = false;
        }
    }
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        // TODO Auto-generated method stub
        super.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    /**
     * 接受到更改语言设置后执行的方法
     */
    public abstract void updateUIText();


    public DisplayMetrics getDisplaymetrics() {
        if (mDisplaymetrics == null) {
            mDisplaymetrics = new DisplayMetrics();
            ((WindowManager) getActivity().getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay().getMetrics(mDisplaymetrics);
        }
        return mDisplaymetrics;
    }

    public void onBackKeyClick() {

    }

    public void onEditKeyListener(EditText et) {
        et.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN) {
                    switch (keyCode) {
                        case KeyEvent.KEYCODE_DPAD_LEFT:
                        case KeyEvent.KEYCODE_DPAD_RIGHT:
                        case KeyEvent.KEYCODE_DPAD_UP:
                        case KeyEvent.KEYCODE_DPAD_DOWN:
                            return true;
                    }
                }
                return false;
            }
        });
    }


    public void onReturnResult(int requestCode, int resultCode, Intent data) {
        PayManager.getInstance().onReturnResult(requestCode, resultCode, data);
    }

    public void stopLoad() {
        if(getView()!=null){
//            SmartRefreshLayout smartRefreshLayout = fv(R.id.smartLayout);
//            if(smartRefreshLayout!=null){
//                smartRefreshLayout.finishRefresh();
//                smartRefreshLayout.finishLoadmore();
//            }
        }
    }

    public void showToast(String text) {
        if (null != getActivity()){
            Toast.makeText(getActivity(), text, Toast.LENGTH_LONG).show();
        }
    }

    public void showToast(int textId) {

        if (null != getActivity()){
            Toast.makeText(getActivity(), getString(textId), Toast.LENGTH_LONG).show();
        }
    }

    public void set2Service(String code, String currentUid) {

        HttpMethods.getInstance().saveCallAndRecevieRecord(code, String.valueOf(currentUid),
                new HttpObserver(new SubscriberOnNextListener() {
                    @Override
                    public void onNext(Object o, String msg) {

                    }
                },getActivity(),false, new OnHttpErrorListener() {
                    @Override
                    public void onConnectError(Throwable e) {

                    }

                    @Override
                    public void onServerError(int errorCode, String errorMsg) {

                    }
                }));
    }


}
