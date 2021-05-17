package com.project.chatter.fragment;

import android.Manifest;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.SurfaceView;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

import com.project.chatter.BaseApplication;
import com.project.chatter.constant.ConstantsChatter;
import com.project.chatter.constant.Constants;
import com.project.chatter.R;
import com.project.chatter.activity.BaseActivityChatter;
import com.project.chatter.activity.EmptyActivityChatter;
import com.project.chatter.bean.CallHttpBean;
import com.project.chatter.bean.SCBean;
import com.project.chatter.bean.UserBean;
import com.project.chatter.call.CallType;
import com.project.chatter.call.CallUtils;
import com.project.chatter.data.DataManager;
import com.project.chatter.http.HttpMethods;
import com.project.chatter.http.HttpObserver;
import com.project.chatter.http.OnHttpErrorListener;
import com.project.chatter.http.SubscriberOnNextListener;
import com.project.chatter.utils.ChatterToast;
import com.project.chatter.utils.MyUtils;
import com.project.chatter.utils.PermissionChecker;
import com.project.chatter.utils.Preferences;

import java.util.Random;

import io.agora.rtc.IRtcEngineEventHandler;
import io.agora.rtc.RtcEngine;
import io.agora.rtc.video.VideoCanvas;
import io.agora.rtc.video.VideoEncoderConfiguration;

import static io.agora.rtc.Constants.CONNECTION_STATE_CONNECTED;
import static io.agora.rtc.Constants.CONNECTION_STATE_DISCONNECTED;
import static io.agora.rtc.Constants.REMOTE_AUDIO_STATE_DECODING;
import static io.agora.rtc.Constants.REMOTE_AUDIO_STATE_STOPPED;

public class CallFragmentChatter extends BaseFragmentChatter {

    private static final String TAG = CallFragmentChatter.class.getSimpleName();

    private RtcEngine mRtcEngine;


    private static final String[] RequiredPermissions = new String[]{Manifest.permission.CAMERA,
            Manifest.permission.RECORD_AUDIO,
            Manifest.permission.WRITE_EXTERNAL_STORAGE};
    private PermissionChecker permissionChecker = new PermissionChecker();

    private CallType mCallType;  //0表示主动打电话，1表示接电话
    private UserBean mPeerUser;
    private boolean mMuted;
    private boolean mVideoMuted;

    private RelativeLayout mSmallContainer;
    private RelativeLayout mBigContainer;
    private SurfaceView mLocalView;
    private SurfaceView mRemoteView;

    private ImageView mCallBtn;
    private ImageView btn_refuse;
    private LinearLayout ll_call_btn;
    private ImageView mMuteBtn;
    private ImageView mVideoBtn;
    private ImageView mSwitchCameraBtn;

    private MediaPlayer mMediaPlayer;
    private TextView mTvTime;
    private LinearLayout control_panel;
    private ImageView ivAvater_small;
    private SCBean mSCBean;


    @Override
    protected int getLayoutId() {
        return R.layout.chatter_fragment_call;
    }

    @Override
    protected void onViewCreated(View view) {
        Bundle bundle = getArguments();
        if (bundle == null) {
            getActivity().finish();
            return;
        }
        mCallType = CallType.getEnum(bundle.getInt(Constants.KEY_BASE_BEAN, 0));
        mPeerUser = (UserBean) bundle.getSerializable(Constants.KEY_BASE_BEAN_1);
        mSCBean = (SCBean) bundle.getSerializable(Constants.KEY_BASE_BEAN_2);
        resetUserView();

        if (mPeerUser == null) {
            getActivity().finish();
            return;
        }
        ((EmptyActivityChatter) getActivity()).setCanBackFinish(false);
        setViewsOnClickListener(R.id.btn_call, R.id.btn_switch_camera, R.id.btn_mute
                ,R.id.btn_video,R.id.iv_close,R.id.btn_refuse);
        initUI(view);
        checkPermissions();
        if (null != getActivity()){
            getActivity().getWindow().addFlags(WindowManager.LayoutParams.FLAG_SECURE);
        }

        BaseApplication.canCall = false;
    }






    @Override
    public void updateUIText() {
    }




    @RequiresApi(api = Build.VERSION_CODES.ECLAIR)
    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.btn_call) {
            if (null != DataManager.getInstance().getMyUserInfo() &&
                    DataManager.getInstance().getMyUserInfo().getDiamond() > 0) {

                acceptCall();

            } else {//余额小于钻石数，跳出付款窗

                ShowChargeDiamondDialog("", new CallBack() {
                    @Override
                    public void callBack() {
                        acceptCall();
                    }
                });

            }
        } else if (id == R.id.btn_refuse) {
            endCall();
        } else if (id == R.id.iv_close) {
            endCall();
        } else if (id == R.id.btn_switch_camera) {
            if (null != mRtcEngine) {
                mRtcEngine.switchCamera();
            }
        } else if (id == R.id.btn_mute) {
            mMuted = !mMuted;
            if (null != mRtcEngine) {
                mRtcEngine.muteLocalAudioStream(mMuted);
            }
            int res = mMuted ? R.drawable.chatter_btn_mute : R.drawable.chatter_btn_unmute;
            mMuteBtn.setImageResource(res);
        } else if (id == R.id.btn_video) {
            mVideoMuted = !mVideoMuted;
            if (null != mRtcEngine) {
                mRtcEngine.muteLocalVideoStream(mVideoMuted);
            }
            muteLocalVideoUI();

            int resVideo = mVideoMuted ? R.drawable.chatter_btn_video_unable : R.drawable.chatter_btn_video_enable;

            mVideoBtn.setImageResource(resVideo);
        } else if (id == R.id.local_video_view_container) {
            exchangeVideoView();
        }
    }

    private void acceptCall() {
        CallUtils.answerUser(mPeerUser, mSCBean.getRoomId(), true,
                new CallUtils.CallBack() {
                    @Override
                    public void callBack(final CallHttpBean bean) {
                        if (getActivity() == null) {
                            return;
                        }
                    }
                });
        showButtons(true);

        String roomId =  Preferences.getInstacne().getValues("roomId","");
        if (!TextUtils.isEmpty(roomId)){
            joinAgoraChannel(roomId);
        }
    }

    boolean isLocalSmall = true;
    @RequiresApi(api = Build.VERSION_CODES.ECLAIR)
    private void exchangeVideoView() {
        if (mSmallContainer != null) {
            mSmallContainer.removeAllViews();
        }
        if (mBigContainer != null) {
            mBigContainer.removeAllViews();
        }


        if (isLocalSmall){
            isLocalSmall = false;
            if (mLocalView != null) {
                mBigContainer.addView(mLocalView);
            }
            if (mRemoteView != null) {
                mSmallContainer.addView(mRemoteView);
                mRemoteView.setZOrderMediaOverlay(true);
            }
        }else {
            isLocalSmall = true;
            if (mRemoteView != null) {
                mBigContainer.addView(mRemoteView);
            }
            if (mLocalView != null) {
                mSmallContainer.addView(mLocalView);
                mLocalView.setZOrderMediaOverlay(true);
            }
        }


    }

    @RequiresApi(api = Build.VERSION_CODES.ECLAIR)
    private void muteLocalVideoUI() {
        UserBean userBean = DataManager.getInstance().getMyUserInfo();
        if (mVideoMuted){

            mSmallContainer.setEnabled(false);
            if (isLocalSmall){
                mSmallContainer.removeAllViews();
                mSmallContainer.setVisibility(View.GONE);
                MyUtils.loadImage(0, mPeerUser.getAvatar(), (ImageView) fv(R.id.ivAvater1));
            }else {
                mBigContainer.removeAllViews();
                mBigContainer.setVisibility(View.GONE);

                setViewVisible(R.id.ivAvater1);
                MyUtils.loadImage(0, userBean.getAvatar(), (ImageView) fv(R.id.ivAvater1));
            }

        }else {

            mSmallContainer.setEnabled(true);
            if (isLocalSmall){
                if (mLocalView != null) {
                    mSmallContainer.addView(mLocalView);
                }
                mSmallContainer.setVisibility(View.VISIBLE);
                mLocalView.setZOrderMediaOverlay(true);
            }else {
                if (mLocalView != null) {
                    mBigContainer.addView(mLocalView);
                }

                setViewGone(R.id.ivAvater1);
                mBigContainer.setVisibility(View.VISIBLE);
                mLocalView.setZOrderMediaOverlay(false);

            }

        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        permissionChecker.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }




    public void chatMessage(final String error) {
        MyUtils.log(0,TAG,"chatMessage "+error);
        if (getActivity() == null || getView() == null
                || TextUtils.isEmpty(error)) {
            return;
        }
        if ("EM_NOT_ENOUGH".equals(error)){
            MyUtils.ShowChargeDiamondDialog((BaseActivityChatter)getActivity(), new MyUtils.DiamondDialogCallBack() {
                @Override
                public void back(String way) {
                }
            });
        }
    }


    public void commit(final String key, final String requestId) {
        if (null != getActivity()){

            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    HttpMethods.getInstance().commitSocket(key,requestId,
                            new HttpObserver(new SubscriberOnNextListener() {
                                @Override
                                public void onNext(Object o, String msg) {
                                    MyUtils.log(0,TAG,"commit socket");
                                }
                            },getActivity(),false,(BaseActivityChatter)getActivity()));
                }
            });
        }

    }


    private void ShowChargeDiamondDialog() {
        ShowChargeDiamondDialog(getResources().getString(R.string.chatter_charge_gift_title),null);
    }
    private void ShowChargeDiamondDialog(String title, final CallBack callBack) {
        MyUtils.ShowChargeDiamondDialog(getActivity(), title,new MyUtils.DiamondDialogCallBack() {
            @Override
            public void back(String way) {

                if (null != callBack){
                    callBack.callBack();
                }
//                            set2Service("Recharge_Random_"+way);
            }
        });

    }

    interface CallBack{
        void callBack();
    }


    @Override
    public void onPause() {
        super.onPause();
        if (null != mMediaPlayer){
            mMediaPlayer.pause();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (null != mMediaPlayer){
            mMediaPlayer.start();
        }
    }

    @Override
    public void onDestroyView() {
        stopRing();
        MyUtils.log(0,TAG,"onDestroyView");
        mHandler.removeCallbacksAndMessages(null);
        mHandler.removeMessages(0);
        mHandler.removeMessages(1);
        mHandler.removeMessages(3);


        endCall();
        RtcEngine.destroy();
        super.onDestroyView();
    }




    public void sendDiamondMsg() {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                    MyUtils.log(0,TAG,"sendDiamondMsg");
                    UserBean myInfo = DataManager.getInstance().getMyUserInfo();
                    if (null != myInfo && null != mPeerUser){
                        MyUtils.log(0,TAG,"getDiamond == "+myInfo.getDiamond()+
                                "\ngetPreMinuteDiamond == "+mPeerUser.getMinConsumeDiamond());

                        if (myInfo.getDiamond() < mPeerUser.getMinConsumeDiamond()){
                            chatMessage("EM_NOT_ENOUGH");
                        }
                    }
                    mHandler.sendEmptyMessageDelayed(0, 60 * 1000);
                    HttpMethods.getInstance().payChat(mPeerUser.getUid(),
                            mPeerUser.getPreMinuteDiamond() + "," + 60,
                            new HttpObserver(new SubscriberOnNextListener<Integer>() {
                                @Override
                                public void onNext(Integer o, String msg) {
                                    MyUtils.log(1,TAG,o+"");
                                    UserBean myInfo = DataManager.getInstance().getMyUserInfo();
                                    myInfo.setDiamond(o);

//                                    chatMessage(DEDUCT_TIP.getName());
                                }
                            }, getActivity(), false,new OnHttpErrorListener() {
                                @Override
                                public void onConnectError(Throwable e) {

                                }

                                @Override
                                public void onServerError(int errorCode, String errorMsg) {
                                    if (errorCode == 1006){
                                        showToast(errorMsg);
                                        chatMessage("EM_NOT_ENOUGH");
                                    }
                                }
                            }));
            }
        });

    }


    int count = 0;
    int EndCallCount = 0;
    public Handler mHandler = new Handler(Looper.getMainLooper()) {
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 0){
                sendDiamondMsg();
            }else if (msg.what == 1){
//                talkTime += 1;
//                mTvTime.setText(MsgTimeUtil.countToTime(talkTime));
//                mHandler.sendEmptyMessageDelayed(1, 1000);
            }else if (msg.what == 2){
                count++;
                MyUtils.log(0,TAG,"count = "+count);
                if (count < 2){
                    mHandler.sendEmptyMessageDelayed(2, 1000);
                }else {

                }
            }else if (msg.what == 3){
                EndCallCount++;
                mHandler.sendEmptyMessageDelayed(3, 1000);
                if (EndCallCount > 30){
                    endCall();
                }
            }
        }
    };




    public void setBeauty(boolean enable){
        if (null != mRtcEngine){
            mRtcEngine.setBeautyEffectOptions(enable,
                    ConstantsChatter.DEFAULT_BEAUTY_OPTIONS);
        }
    }

    private void setupLocalVideo() {
        mLocalView = RtcEngine.CreateRendererView(getActivity());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ECLAIR) {
            mLocalView.setZOrderMediaOverlay(true);
        }
        mSmallContainer.addView(mLocalView);
        if (null != mRtcEngine){
            mRtcEngine.setupLocalVideo(new VideoCanvas(mLocalView, VideoCanvas.RENDER_MODE_HIDDEN, 0));
        }
    }

    private void leaveChannel() {
        if (mRtcEngine != null) {
            mRtcEngine.leaveChannel();
        }
    }

    private void endCall() {
        BaseApplication.canCall = true;
        if (mRtcEngine != null) {
            MyUtils.log(0,TAG,"endCall");
            removeLocalVideo();
            removeRemoteVideo();
            leaveChannel();
            CallUtils.endCall(mPeerUser,mSCBean.getRoomId());
        }
        if (null != getActivity()){
            getActivity().finish();
        }
    }

    private void removeLocalVideo() {
        if (mLocalView != null) {
            mSmallContainer.removeView(mLocalView);
        }
        mLocalView = null;
        mTvTime.setVisibility(View.GONE);
    }

    private void showButtons(boolean show) {
        int visibility = show ? View.VISIBLE : View.GONE;
        int callbtnVisible = !show ? View.VISIBLE : View.GONE;
        mMuteBtn.setVisibility(visibility);
        mVideoBtn.setVisibility(visibility);
        mSwitchCameraBtn.setVisibility(visibility);
        control_panel.setVisibility(visibility);
        mCallBtn.setVisibility(callbtnVisible);
        ll_call_btn.setVisibility(callbtnVisible);
    }

    private void playRing() {

        mMediaPlayer = MediaPlayer.create(getActivity(), R.raw.duo);
        mMediaPlayer.setLooping(true);
        mMediaPlayer.start();

    }

    private void stopRing() {
        if (null != mMediaPlayer){
            mMediaPlayer.release();
            mMediaPlayer = null;
        }
    }

    private void joinAgoraChannel(final String roomId) {
        if (getActivity() == null || getView() == null) {
            return;
        }
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                MyUtils.log(0,TAG,"joinAgoraChannel");
                if (null != mRtcEngine){
                    mRtcEngine.enableWebSdkInteroperability(true);
                    int code;
                    if (mSCBean != null && mSCBean.isApiClient()) {
                        code = mRtcEngine.joinChannel(mSCBean.getToken(), mSCBean.getRoomId(), "Extra Data", mSCBean.getRemoteId());
                    }else {
                         code = mRtcEngine.joinChannel(null, roomId
                                , "Extra Data", 0);
                    }

                    MyUtils.log(0,TAG,"joinAgoraChannel code = "+code);
//                    mRtcEngine.joinChannel(null, roomId, "Extra Data", 0);
                }
                setViewGone(R.id.ivAvater1, R.id.llUserInfo, R.id.bg);
                showButtons(true);
                stopRing();
            }
        });
    }
    private void setupRemoteVideo(final int uid) {
        if (getView() == null || getActivity() == null) {
            return;
        }
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                int count = mBigContainer.getChildCount();
                View view = null;
                for (int i = 0; i < count; i++) {
                    View v = mBigContainer.getChildAt(i);
                    if (v.getTag() instanceof Integer && ((int) v.getTag()) == uid) {
                        view = v;
                    }
                }
                if (view != null) {
                    return;
                }
                if (null !=getActivity()){
                    mRemoteView = RtcEngine.CreateRendererView(getActivity());
                    mBigContainer.addView(mRemoteView);
                    if (null != mRtcEngine){
                        mRtcEngine.setupRemoteVideo(new VideoCanvas(mRemoteView, VideoCanvas.RENDER_MODE_HIDDEN, uid));
                    }

                    if (MyUtils.isSelfAnchor()){
                        setBeauty(true);
                    }
                    mRemoteView.setTag(uid);
                }
            }
        });
    }
    private void onRemoteUserLeft() {
        if (getView() == null || getActivity() == null) {
            MyUtils.log(0, TAG, "onRemoteUserLeft： null" );
            return;
        }
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                removeRemoteVideo();
                if (null != getActivity() && getActivity() instanceof EmptyActivityChatter){
                    MyUtils.log(0, TAG, "onRemoteUserLeft： EmptyActivity" );
                    ((EmptyActivityChatter)getActivity()).finish();
                }
            }
        });
    }

    private void removeRemoteVideo() {
        if (mRemoteView != null) {
            mBigContainer.removeView(mRemoteView);
        }
        mRemoteView = null;
    }
    private void resetUserView() {
        if (getView() == null || mPeerUser == null) {
            return;
        }
        MyUtils.loadImage(0, mPeerUser.getAvatar(), (ImageView) fv(R.id.ivAvater1));
        MyUtils.loadImage(0, mPeerUser.getAvatar(), (ImageView) fv(R.id.ivAvater2));
        setText(R.id.tvName, mPeerUser.getNickname());


        if (MyUtils.isSelfAnchor()){
            if (mCallType == CallType.Call) {
                setText(R.id.tvTip, getString(R.string.chatter_anchor_call_hint,
                        mPeerUser.getNickname()));

            }else {
                setText(R.id.tvTip, getString(R.string.chatter_anchor_received_hint,
                        mPeerUser.getNickname()));
            }
        }else {
            if (mCallType == CallType.Call) {
                setText(R.id.tvTip,
                        /*String.format(LanguageUtil.getSetLanguageLocale(getContext()), R.string.user_call_hint,
                                mPeerUser.getNickname(), "\uD83D\uDE3B\uD83D\uDE3B")*/
                        getString(R.string.chatter_user_call_hint,
                        mPeerUser.getNickname(), "\uD83D\uDE3B\uD83D\uDE3B"));
            }else {
                setText(R.id.tvTip, getString(R.string.chatter_user_received_hint,
                        mPeerUser.getNickname(), "\uD83D\uDE3B\uD83D\uDE3B"));
            }
        }
    }

    private void checkPermissions() {
        permissionChecker.verifyPermissions(getActivity(), RequiredPermissions, new PermissionChecker.VerifyPermissionsCallback() {
            @Override
            public void onPermissionAllGranted() {
                initEngineAndJoinChannel();
            }

            @Override
            public void onPermissionDeny(String[] permissions) {
                showToast("Need permissions to access camera and mic, please set to continue.");
                getActivity().finish();
            }
        });
    }

    private void initUI(View view) {
        mSmallContainer = view.findViewById(R.id.local_video_view_container);
        mTvTime = view.findViewById(R.id.tv_time);
        mBigContainer = view.findViewById(R.id.remote_video_view_container);
        setViewsOnClickListener(R.id.local_video_view_container);

        mCallBtn = view.findViewById(R.id.btn_call);
        btn_refuse = view.findViewById(R.id.btn_refuse);
        ll_call_btn = (LinearLayout) view.findViewById(R.id.ll_call_btn);
        mMuteBtn = view.findViewById(R.id.btn_mute);
        mVideoBtn = view.findViewById(R.id.btn_video);
        mSwitchCameraBtn = view.findViewById(R.id.btn_switch_camera);
        control_panel = (LinearLayout)view.findViewById(R.id.control_panel);
        ivAvater_small = (ImageView)view.findViewById(R.id.ivAvater_small);
    }

    private void initEngineAndJoinChannel() {
        playRing();
        showButtons(false);
        if (mCallType == CallType.Call) {
            mCallBtn.setVisibility(View.GONE);
            ll_call_btn.setVisibility(View.GONE);


            String chatNo =  System.currentTimeMillis()+""+( new Random().nextInt(200)+1000);
            mPeerUser.setChatNo(chatNo);
           CallUtils.callUser(mPeerUser, new CallUtils.CallBack() {
                @Override
                public void callBack(final CallHttpBean bean) {
                    if (getActivity() == null) {
                        return;
                    }




                    if (!TextUtils.isEmpty(bean.getAgoId()) && !TextUtils.isEmpty(bean.getRoomId())
                            && !TextUtils.isEmpty(bean.getToken())){
                        if (mSCBean == null) {
                            mSCBean = new SCBean();
                        }
                        mSCBean.setAgoId(bean.getAgoId());
                        mSCBean.setRoomId(bean.getRoomId());
                        mSCBean.setRemoteUid(bean.getRemoteUid()+"");
                        mSCBean.setToken(bean.getToken());
                        mSCBean.setRemoteId(bean.getId());
                        mSCBean.setSdp(bean.getType());

                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if (!TextUtils.isEmpty(bean.getMsg())){
                                    ChatterToast.Toast(bean.getMsg());
                                }

                                initializeEngine();
                                setupVideoConfig();
                                setupLocalVideo();
                                joinAgoraChannel(mSCBean.getRoomId());
                            }
                        });
                    }else if (!TextUtils.isEmpty(bean.getMsg()) && "userDiamondNotEnoughEnd".equals(bean.getMsg())){
                        ShowChargeDiamondDialog("", new CallBack() {
                            @Override
                            public void callBack() {
                                acceptCall();
                            }
                        });
                    }

                }
            });
            commit("SC","0");
        }
    }




    private void setupVideoConfig() {
        if (null != mRtcEngine){
            mRtcEngine.enableVideo();
            mRtcEngine.setVideoEncoderConfiguration(new VideoEncoderConfiguration(
                    VideoEncoderConfiguration.VD_640x360,
                    VideoEncoderConfiguration.FRAME_RATE.FRAME_RATE_FPS_15,
                    VideoEncoderConfiguration.STANDARD_BITRATE,
                    VideoEncoderConfiguration.ORIENTATION_MODE.ORIENTATION_MODE_FIXED_PORTRAIT));
        }
    }

    private void initializeEngine() {
        try {

            String agoraId = "";
            if (mSCBean != null && mSCBean.isApiClient()) {
                agoraId = mSCBean.getAgoId();
            }

                mRtcEngine = RtcEngine.create(BaseApplication.getAppContext(),
                        agoraId,
                        new IRtcEngineEventHandler() {

                            @Override
                            public void onConnectionStateChanged(int state, int reason) {
                                MyUtils.log(0, TAG, "onConnectionStateChanged state == "+state);
                                if (state == CONNECTION_STATE_CONNECTED){
                                    MyUtils.log(0, TAG, "CONNECTION_STATE_CONNECTED ");
                                    mHandler.removeMessages(3);
                                    sendDiamondMsg();
                                    getActivity().runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            if (MyUtils.isSelfAnchor()){//计时器
                                                mHandler.sendEmptyMessageDelayed(1,1000);
                                            }
                                        }
                                    });


                                }else if (state == CONNECTION_STATE_DISCONNECTED){
                                    commit("CC","0");
                                    getActivity().runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            if (null != getContext()){
                                                ChatterToast.Toast(getContext().getResources().getString(R.string.chatter_error_template));
                                            }
                                            goBack();
                                        }
                                    });
                                }
                            }

                            @Override
                            public void onJoinChannelSuccess(String channel, final int uid, int elapsed) {
                                MyUtils.log(0, TAG, "Join channel success, uid: " + (uid & 0xFFFFFFFFL));

                                if (null != mRtcEngine){
                                    mRtcEngine.enableAudio();
                                }
                            }

                            @Override
                            public void onFirstRemoteVideoDecoded(final int uid, int width, int height, int elapsed) {
                                setupRemoteVideo(uid);
                            }

                            @Override
                            public void onUserOffline(final int uid, int reason) {
                                MyUtils.log(0, TAG, "onUserOffline： " + uid);
                                onRemoteUserLeft();
                            }


                            @Override
                            public void onStreamMessage(int uid, int streamId, byte[] data) {
                                super.onStreamMessage(uid, streamId, data);
                            }

                            @Override
                            public void onStreamMessageError(int uid, int streamId, int error, int missed, int cached) {
                                super.onStreamMessageError(uid, streamId, error, missed, cached);
                                //msg error
                                MyUtils.log(0,TAG,"uid == "+uid+
                                        "error=="+error);
                            }

                            @Override
                            public void onRemoteVideoStateChanged(int uid, final int state, int reason, int elapsed) {
                                super.onRemoteVideoStateChanged(uid, state, reason, elapsed);
                                MyUtils.log(0,TAG,"onRemoteVideoStateChanged == "+state+
                                        " reason=="+reason);


                                getActivity().runOnUiThread(new Runnable() {
                                    @RequiresApi(api = Build.VERSION_CODES.ECLAIR)
                                    @Override
                                    public void run() {
                                        if (state == REMOTE_AUDIO_STATE_STOPPED || mVideoMuted){
                                            mSmallContainer.setEnabled(false);
                                        }else {
                                            mSmallContainer.setEnabled(true);
                                        }
                                        if (state == REMOTE_AUDIO_STATE_STOPPED){

                                            if (isLocalSmall){
                                                setViewVisible(R.id.ivAvater1);
                                                mBigContainer.setVisibility(View.INVISIBLE);
                                            }else {
                                                setViewVisible(R.id.ivAvater_small);
                                                mSmallContainer.setVisibility(View.INVISIBLE);
                                                MyUtils.loadImage(0, mPeerUser.getAvatar(), (ImageView) ivAvater_small);
                                            }
                                        }else if (state == REMOTE_AUDIO_STATE_DECODING){
                                            if (isLocalSmall){
                                                setViewGone(R.id.ivAvater1);
                                                mBigContainer.setVisibility(View.VISIBLE);
                                            }else {
                                                setViewGone(R.id.ivAvater_small);
                                                mSmallContainer.setVisibility(View.VISIBLE);
                                                mLocalView.setZOrderMediaOverlay(false);
                                            }
                                        }
                                    }});
                            }
                        });


            mRtcEngine.setEnableSpeakerphone(true);

        } catch (Exception e) {
            MyUtils.log(1, TAG, Log.getStackTraceString(e));
            throw new RuntimeException("NEED TO check rtc sdk init fatal error\n" + Log.getStackTraceString(e));
        }
    }


}