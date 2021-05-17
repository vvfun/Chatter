package com.project.chatter.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.project.chatter.fragment.BaseFragmentChatter;
import com.project.chatter.http.OnHttpErrorListener;

import java.util.ArrayList;
import java.util.List;

/**
 * 应用首界面
 */
public abstract class BaseActivityChatter extends AppCompatActivity implements OnHttpErrorListener {

    private DisplayMetrics mDisplaymetrics;

    private boolean mIsFullScreen;

//    private MyDialogFragmentM mErrorDialog;

    private static boolean isNotComeFromBG;  //改为静态的，防止多个Activity会调用背景到前景的方法


    private static ArrayList<String> mActivityNameList;  //当mActivityNameList size为0时表示到了后台
    private static final ArrayList<BaseActivityChatter> mActivityList = new ArrayList<>();


//    private ACProgressBaseDialog mDlgLoading;



    private int mSelectPhotoType;


    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().getDecorView().setBackground(null);
//        /*
//         * 这里判断是否从splashActivity过来，是的话当作从后台到前台处理
//         */
        isNotComeFromBG = !isComeFromSplash();
        mActivityList.add(this);


    }

    @Override
    public void onConnectError(Throwable e) {

    }
    @Override
    public synchronized void onServerError(int errorCode, String errorMsg) {

    }
    protected boolean isComeFromSplash() {
        return getIntent().getBooleanExtra("key_come_from_splash", false);
    }



    /**
            * 页面跳转，如果返回true,则基类已经处理，否则没有处理
     *
             * @param pagerClass
     * @param bundle
     * @return
             */
    public boolean gotoPager(Class<?> pagerClass, Bundle bundle) {

        if (Activity.class.isAssignableFrom(pagerClass)) {
            Intent intent = new Intent(this, pagerClass);
            if (bundle != null) {
                intent.putExtras(bundle);
            }
            startActivity(intent);
            return true;
        } else {
            String name = pagerClass.getName();
            Intent intent = new Intent(this, EmptyActivityChatter.class);
            if (bundle != null) {
                intent.putExtras(bundle);
            }
            intent.putExtra("FRAGMENT_NAME", name);
            startActivity(intent);
            return true;
        }
    }


    /**
            * 根据name获取fragment
     *
             * @param name
     * @return
             */
    public BaseFragmentChatter getFragmentByName(String name) {
        BaseFragmentChatter fragment = (BaseFragmentChatter) getSupportFragmentManager()
                .findFragmentByTag(name);
        if (fragment == null) {
            fragment = (BaseFragmentChatter) Fragment.instantiate(this, name);
        }
        return fragment;
    }

    /**
            * 返回，如果stack中还有Fragment的话，则返回stack中的fragment，否则 finish当前的Activity
     */
    public void goBack() {

        getSupportFragmentManager().executePendingTransactions();
        int nSize = getSupportFragmentManager().getBackStackEntryCount();
        if (nSize > 1) {
            getSupportFragmentManager().popBackStack();
        } else {
            finish();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.e("requestCode","requestCode=="+requestCode+"  resultCode=="+resultCode);
        BaseFragmentChatter visibleFragment = getVisibleFragment();
        if (visibleFragment != null) {
            getVisibleFragment().onReturnResult(requestCode, resultCode, data);
        }
    }



    public BaseFragmentChatter getVisibleFragment() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        @SuppressLint("RestrictedApi") List<Fragment> fragments = fragmentManager.getFragments();
        if (fragments == null) {
            return null;
        }
        for (Fragment fragment : fragments) {
            if (fragment instanceof BaseFragmentChatter && fragment.isVisible())
                return (BaseFragmentChatter) fragment;
        }
        return null;
    }
/*
    public void setTitle(String title) {
        setTopBar(title, 0);
    }


    public void setTopBar(String title, int rightRes) {
//        ((TextView) findViewById(R.id.tvTitle)).setText(title);
//        if (rightRes > 0) {
//            ImageView ivRight = findViewById(R.id.ivRight);
//            ivRight.setVisibility(View.VISIBLE);
//            ivRight.setImageResource(rightRes);
//        }
    }

    public void showLoadingDialog() {
//        Log.e("BaseActivity","showLoadingDialog"+mDlgLoading);
//        if (!isFinishing()){
//            showLoadingDialog("", null, true);
//        }
    }

    public void showLoadingDialog(String text) {
        showLoadingDialog(text, null, true);
    }

    *//**
     * 显示Loading 页面， listener可为空
     *
     * @param strTitle
     * @param listener
     * @param isCancelByUser:用户是否可点击屏幕，或者Back键关掉对话框
     *//*
    public void showLoadingDialog(String strTitle, final DialogInterface.OnCancelListener listener, boolean isCancelByUser) {
//        if (mDlgLoading == null) {
//            mDlgLoading = new ACProgressFlower.Builder(this)
//                    .direction(ACProgressConstant.DIRECT_CLOCKWISE)
//                    .themeColor(Color.WHITE)  // loading花瓣颜色
//                    .text(strTitle)
//                    .fadeColor(Color.DKGRAY).build(); // loading花瓣颜色
//        }
//
//        mDlgLoading.setMessage(TextUtils.isEmpty(strTitle) ? "" : strTitle);
//
//        if (listener != null) {
//            mDlgLoading.setOnCancelListener(listener);
//        }
//
//        if (isCancelByUser) {
//            mDlgLoading.setCanceledOnTouchOutside(true);
//            mDlgLoading.setOnKeyListener(new DialogInterface.OnKeyListener() {
//                @Override
//                public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
//                    return false;
//                }
//            });
//        } else {
//            mDlgLoading.setCanceledOnTouchOutside(false);
//            //防止用户点击Back键，关掉此对话框
//            mDlgLoading.setOnKeyListener(new DialogInterface.OnKeyListener() {
//                @Override
//                public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
//                    if (keyCode == KeyEvent.KEYCODE_BACK)
//                        return true;
//                    return false;
//                }
//            });
//        }
//
//        mDlgLoading.setMessage(strTitle);
//        mDlgLoading.show();
    }


    *//**
     * 关闭loading的页面
     *//*
    public void hideLoadingDialog() {
//        Log.e("BaseActivity","hideLoadingDialog"+mDlgLoading);
//        if (mDlgLoading != null) {
//            mDlgLoading.dismiss();
//        }
    }

    public void requestPermission(int permissionReqCode, String... permissions) {
//        ArrayList<String> uncheckPermissions = null;
//        for (String permission : permissions) {
//            if (!MyUtils.isGrantPermission(this, permission)) {
//                //进行权限请求
//                if (uncheckPermissions == null) {
//                    uncheckPermissions = new ArrayList<>();
//                }
//                uncheckPermissions.add(permission);
//            }
//        }
//        if (uncheckPermissions != null && !uncheckPermissions.isEmpty()) {
//            String[] array = new String[uncheckPermissions.size()];
//            ActivityCompat.requestPermissions(this, uncheckPermissions.toArray(array), permissionReqCode);
//        }
    }

    *//**
     * 判断是否从splashActivity过来
     *
     * @return true将被当作从后台到前台处理
     *//*
    protected boolean isComeFromSplash() {
        return getIntent().getBooleanExtra("key_come_from_splash", false);
    }


    public DisplayMetrics getDisplaymetrics() {
        if (mDisplaymetrics == null) {
            mDisplaymetrics = new DisplayMetrics();
            ((WindowManager) getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay().getMetrics(mDisplaymetrics);
        }
        return mDisplaymetrics;
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (mActivityNameList == null) {
            mActivityNameList = new ArrayList<>();
        }

        mActivityNameList.add(getClass().getName());

        if (!isNotComeFromBG) {// 这里表示是从后台到前台
            onFromBackground();
            // 重置
            isNotComeFromBG = true;
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//            requestPermission(0,
//                    Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }
    }


    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    protected void onStop() {
        // TODO Auto-generated method stub
        super.onStop();

        if (mActivityNameList != null) {
            mActivityNameList.remove(getClass().getName());
        }
        if (isBackground()) {
            isNotComeFromBG = false;
            onToBackground();
        } else {
            isNotComeFromBG = true;
        }
    }


    *//**
     * 用于在onStop后判断应用是否已经退到后台
     *
     * @return
     *//*
    private boolean isBackground() {
        return mActivityNameList == null || mActivityNameList.isEmpty();
    }


    public void showOneBtnDialog(final String title, final String msg, final String btnText) {
//        final MyDialogFragmentM dialogFragment = new MyDialogFragmentM();
//        dialogFragment.setLayout(R.layout.layout_one_btn_dialog);
//        dialogFragment.setOnMyDialogListener(new MyDialogFragmentM.OnMyDialogListener() {
//            @Override
//            public void initView(View view) {
//                if (TextUtils.isEmpty(title)) {
//                    view.findViewById(R.id.tv1).setVisibility(View.GONE);
//                } else {
//                    ((TextView) view.findViewById(R.id.tv1)).setText(title);
//                }
//                ((TextView) view.findViewById(R.id.tv2)).setText(msg);
//                ((TextView) view.findViewById(R.id.btn2)).setText(btnText);
//                dialogFragment.setDialogViewsOnClickListener(view, R.id.btn2);
//            }
//
//            @Override
//            public void onViewClick(int viewId) {
//
//            }
//        });
//        dialogFragment.show(getSupportFragmentManager(), "MyDialogFragment");
    }

    public void errorCodeDo(final int errorCode, final String message) {
//        if (!TextUtils.isEmpty(message)) {
//            mErrorDialog = new MyDialogFragmentM();
//            mErrorDialog.setLayout(R.layout.layout_one_btn_dialog);
//            mErrorDialog.setOnMyDialogListener(new MyDialogFragmentM.OnMyDialogListener() {
//                @Override
//                public void initView(View view) {
//                    view.findViewById(R.id.tv1).setVisibility(View.GONE);
//                    ((TextView) view.findViewById(R.id.tv2)).setText(message);
//                    ((TextView) view.findViewById(R.id.btn2)).setText("Ok");
//                    mErrorDialog.setDialogViewsOnClickListener(view, R.id.btn2);
//                }
//
//                @Override
//                public void onViewClick(int viewId) {
//
//                }
//            });
//            mErrorDialog.show(getSupportFragmentManager(), "MyDialogFragment");
//
//            mErrorDialog.setOnDismiss(new MyDialogFragmentM.IDismissListener() {
//                @Override
//                public void onDismiss() {
//
//                }
//            });
//        }
    }








    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
//        if (requestCode == SelectPhotoUtil.ASK_CAMERA_PERMISSION) {
//            if (permissions != null && permissions.length > 0 && permissions[0].equals(Manifest.permission.CAMERA)) {
//                if (grantResults != null && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                    SelectPhotoUtil.goSystemCameraPage(BaseActivityPoChat.this);
//                }
//            }
//        } else if (requestCode == SelectPhotoUtil.ASK_EXTENAL_PERMISSION) {
//            if (permissions != null && permissions.length > 0 && permissions[0].equals(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
//                if (grantResults != null && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                    SelectPhotoUtil.goAlbum(BaseActivityPoChat.this);
//                }
//            }
//        } else {
            if (null != getVisibleFragment()){
                getVisibleFragment().onRequestPermissionsResult(requestCode, permissions, grantResults);
            }
//        }
    }

    *//**
     * 适配api19及以上,根据uri获取图片的绝对路径
     *
     * @param uri 图片的Uri
     * @return 如果Uri对应的图片存在, 那么返回该图片的绝对路径, 否则返回null
     *//*
    @SuppressLint("NewApi")
    public String getRealPathFromUriAboveApi19(Uri uri) {
        String filePath = null;
        if (DocumentsContract.isDocumentUri(this, uri)) {
            // 如果是document类型的 uri, 则通过document id来进行处理
            String documentId = DocumentsContract.getDocumentId(uri);
            if (isMediaDocument(uri)) { // MediaProvider
                // 使用':'分割
                String id = documentId.split(":")[1];

                String selection = MediaStore.Images.Media._ID + "=?";
                String[] selectionArgs = {id};
                filePath = getDataColumn(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, selection, selectionArgs);
            } else if (isDownloadsDocument(uri)) { // DownloadsProvider
                Uri contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"), Long.valueOf(documentId));
                filePath = getDataColumn(contentUri, null, null);
            }
        } else if ("content".equalsIgnoreCase(uri.getScheme())) {
            // 如果是 content 类型的 Uri
            filePath = getDataColumn(uri, null, null);
        } else if ("file".equals(uri.getScheme())) {
            // 如果是 file 类型的 Uri,直接获取图片对应的路径
            filePath = uri.getPath();
        }
        return filePath;
    }

    *//**
     * 适配api19以下(不包括api19),根据uri获取图片的绝对路径
     *
     * @param uri 图片的Uri
     * @return 如果Uri对应的图片存在, 那么返回该图片的绝对路径, 否则返回null
     *//*
    public String getRealPathFromUriBelowAPI19(Uri uri) {
        return getDataColumn(uri, null, null);
    }

    *//**
     * 获取数据库表中的 _data 列，即返回Uri对应的文件路径
     *
     * @return
     *//*
    private String getDataColumn(Uri uri, String selection, String[] selectionArgs) {
        String path = null;

        String[] projection = new String[]{MediaStore.Images.Media.DATA};
        Cursor cursor = null;
        try {
            cursor = getContentResolver().query(uri, projection, selection, selectionArgs, null);
            if (cursor != null && cursor.moveToFirst()) {
                int columnIndex = cursor.getColumnIndexOrThrow(projection[0]);
                path = cursor.getString(columnIndex);
            }
        } catch (Exception e) {
            if (cursor != null) {
                cursor.close();
            }
        }


        if (android.os.Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
            return path;
        } else {//安卓10
            ParcelFileDescriptor descriptor = null;
            try {
                descriptor = getContentResolver().openFile(uri, "r", null);
                FileInputStream inputStream = new FileInputStream(descriptor.getFileDescriptor());
                byte[] byteArray = readBinaryStream(inputStream, (int) descriptor.getStatSize());
                String[] split = path.split("/");
                if (split.length != 0) {
                    File cachedFile = new File(getCacheDir(), split[split.length - 1]);
                    boolean fileSaved = writeFile(cachedFile, byteArray);
                    if (fileSaved) {
                        return cachedFile.getAbsolutePath();
                    } else {
                        return null;
                    }
                }
            } catch (FileNotFoundException e) {
                return null;
            } finally {
                if (descriptor != null) {
                    try {
                        descriptor.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        return path;
    }


    private boolean writeFile(File cachedFile, byte[] data) {
        try {
            BufferedOutputStream output = null;
            try {
                output = new BufferedOutputStream(new FileOutputStream(cachedFile));
                output.write(data);
                output.flush();
                return true;
            } finally {
                output.close();
            }
        } catch ( Exception e) {
            return false;
        }

    }


    private byte[] readBinaryStream(InputStream stream, int byteCount) {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        try {
            int count = byteCount > 0?byteCount : 4096;
            byte[] buffer = new byte[count];
            int read;
//            while (stream.read(buffer).also { read = it } >= 0) {
//                output.write(buffer, 0, read);
//            }
            while ((read = stream.read(buffer)) != -1) {
                output.write(buffer, 0, read);
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                stream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return output.toByteArray();
    }




    *//**
     * @param uri the Uri to check
     * @return Whether the Uri authority is MediaProvider
     *//*
    private boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

    *//**
     * @param uri the Uri to check
     * @return Whether the Uri authority is DownloadsProvider
     *//*
    private boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    protected void onToBackground() {
        Preferences.getInstacne().setIsRunning(false);
        Preferences.getInstacne().setLastCloseAppTime();
    }

    protected void onFromBackground() {
        Preferences.getInstacne().setIsRunning(true);
*//*
        *//**//**
         * 从后台回来检查启动
         *//**//*

        Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(ObservableEmitter<String> emitter) throws Exception {
                if (!TextUtils.isEmpty(DataManager.getInstance().getToken())) {
                    SingletonWsClient.getSingletonClient();
                }
                emitter.onComplete();
            }
        }).subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<String>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(String s) {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });*//*

    }


    public void onResume() {
        super.onResume();
    }

    public void onDestroy() {
        super.onDestroy();
        hideLoadingDialog();
        mActivityList.remove(this);
    }

    public void finishAllOtherActivity() {
        for (BaseActivityPoChat activity : mActivityList) {
//            if (!(activity instanceof MainActivityPoChat)) {
//                activity.finish();
//            }
        }
    }

    public void finishAllActivity() {
        for (BaseActivityPoChat activity : mActivityList) {
            activity.finish();
        }
    }


    public static ArrayList<BaseActivityPoChat> getmActivityList() {
        return mActivityList;
    }

    @Override
    public void onConnectError(Throwable e) {
//        if (!NetUtil.isConnected(this)) {
//
//        } else if (e instanceof UnknownHostException
//                || e instanceof JSONException
//                || e instanceof retrofit2.HttpException) {
//
//        } else if (e instanceof SocketTimeoutException
//                || e instanceof ConnectException
//                || e instanceof TimeoutException) {
//
//        } else {
//
//        }
//        showToast(R.string.please_check_network);
    }


    @Override
    public synchronized void onServerError(int errorCode, String errorMsg) {
        //   stopHttpLoad();
        errorCodeDo(errorCode, errorMsg);
      *//*  if (!TextUtils.isEmpty(errorMsg)) {
            showToast(errorMsg);
        }*//*
    }

    public void onBackClick(View view) {
        goBack();
    }

    public void setScreenFull(boolean isFull) {
        if (mIsFullScreen == isFull) {
            return;
        }

        if (isFull) {
            WindowManager.LayoutParams params = getWindow().getAttributes();
            params.flags |= WindowManager.LayoutParams.FLAG_FULLSCREEN;
            getWindow().setAttributes(params);
            mIsFullScreen = true;
        } else {
            WindowManager.LayoutParams params = getWindow().getAttributes();
            params.flags &= (~WindowManager.LayoutParams.FLAG_FULLSCREEN);
            getWindow().setAttributes(params);
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
            mIsFullScreen = false;
        }

    }




    public void showToast(int textId) {
        Toast.makeText(this, getString(textId), Toast.LENGTH_LONG).show();
    }

    public void showToast(String str) {
        Toast.makeText(this, str, Toast.LENGTH_LONG).show();
    }

    @Override
    protected void attachBaseContext(Context newBase) {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState, @Nullable PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
    }*/
}
