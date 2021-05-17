package com.project.chatter.pay;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.project.chatter.R;


public class MyDialogFragment extends DialogFragment implements View.OnClickListener {

    private int mLayoutId;

    public OnMyDialogListener mOnMyDialogListener;

    private IDismissListener mListener;

    private boolean mIsClickDismiss = true;

    private boolean mIsNeedDismiss = false;

    public void setLayout(int layoutId) {
        mLayoutId = layoutId;
    }

    public void setOnMyDialogListener(OnMyDialogListener onMyDialogListener) {
        mOnMyDialogListener = onMyDialogListener;
    }


    /**
     * 必须在OnMyDialogListener的initView中调
     *
     * @param dialogView
     * @param viewIds
     */
    public void setDialogViewsOnClickListener(View dialogView, int... viewIds) {
        if (viewIds == null || dialogView == null) {
            return;
        }
        for (int viewId : viewIds) {
            dialogView.findViewById(viewId).setOnClickListener(this);
        }
    }

    public void setClickDismiss(boolean isClickDismiss) {
        mIsClickDismiss = isClickDismiss;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NO_TITLE, R.style.dialog_fragment_style);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(mLayoutId, container, false);
        if (mOnMyDialogListener != null) {
            mOnMyDialogListener.initView(view);
        }
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        Dialog dialog = getDialog();
        if (dialog != null) {
            int width = ViewGroup.LayoutParams.MATCH_PARENT;
            int height = ViewGroup.LayoutParams.WRAP_CONTENT;
            dialog.getWindow().setLayout(width, height);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mIsNeedDismiss) {
            dismiss();
        }
    }

    public void setNeedDismiss(boolean isNeedDismiss) {
        mIsNeedDismiss = isNeedDismiss;
    }

    @Override
    public void onClick(View view) {
        if (mOnMyDialogListener != null) {
            mOnMyDialogListener.onViewClick(view.getId());
        }
        if (mIsClickDismiss) {
            dismiss();
        }
    }

    @Override
    public void onDismiss(final DialogInterface dialog) {
        super.onDismiss(dialog);
        if (mListener != null) {
            mListener.onDismiss();
        }
        mOnMyDialogListener = null;
        mListener = null;
    }

    public void setOnDismiss(IDismissListener listener) {
        mListener = listener;
    }


    public interface OnMyDialogListener {
        public void initView(View view);

        public void onViewClick(int viewId);
    }

    public interface IDismissListener {
        public void onDismiss();
    }

    @Override
    public void show(@NonNull FragmentManager manager, @Nullable String tag) {
        // super.show(manager, tag)
        if (!this.isAdded()){
            FragmentTransaction ft = manager.beginTransaction();
            ft.add(this, tag);
            ft.commitAllowingStateLoss();
        }
    }


}
