package com.project.chatter.activity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.KeyEvent;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.project.chatter.R;
import com.project.chatter.fragment.BaseFragmentChatter;


/**
 * 空的Activty，会替换你传进来的Fragment,当Activity存在时使用存在的Activity，整个程序中只有一个该Activity
 */
public class EmptyActivityChatter extends BaseActivityChatter {

    private boolean mCanBackFinish=true;
    String fragmentName;
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chatter_activity_empty);
        mCanBackFinish=true;
        onNewIntent(getIntent());
    }

    @Override
    public void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        fragmentName = intent.getStringExtra("FRAGMENT_NAME");
        BaseFragmentChatter fragment = (BaseFragmentChatter) Fragment.instantiate(this,
                fragmentName);
        Bundle b = intent.getExtras();
        fragment.setArguments(b);
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.add(R.id.container, fragment, fragmentName);
        ft.addToBackStack(null);
        ft.commitAllowingStateLoss();
    }

    public String getFragmentName() {
        return fragmentName;
    }

//    @Override
//    protected void onFromBackground() {
//        super.onFromBackground();
//
//    }

    public void setCanBackFinish(boolean canBackFinish){
        mCanBackFinish=canBackFinish;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if(!mCanBackFinish){
                return false;
            }
            if (getVisibleFragment() == null
                    || getVisibleFragment().getActivity() == null) {
                finish();
            } else {
                getVisibleFragment().goBack();
            }
            return false;
        }
        return super.onKeyDown(keyCode, event);
    }

}

