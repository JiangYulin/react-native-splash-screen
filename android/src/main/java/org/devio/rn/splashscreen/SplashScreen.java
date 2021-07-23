package org.devio.rn.splashscreen;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Point;
import android.os.Build;
import android.util.Log;
import android.view.Display;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;

import java.lang.ref.WeakReference;
import java.lang.reflect.Array;

/**
 * SplashScreen
 * 启动屏
 * from：http://www.devio.org
 * Author:CrazyCodeBoy
 * GitHub:https://github.com/crazycodeboy
 * Email:crazycodeboy@gmail.com
 */
public class SplashScreen {
    private static Dialog mSplashDialog;
    private static WeakReference<Activity> mActivity;

    /**
     * 打开启动屏
     */
    public static void show(final Activity activity, final int themeResId) {
        if (activity == null) return;
        mActivity = new WeakReference<Activity>(activity);
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (!activity.isFinishing()) {
                    mSplashDialog = new Dialog(activity, themeResId);
                    mSplashDialog.setContentView(R.layout.launch_screen);
                    ViewGroup vb = (ViewGroup) mSplashDialog.getWindow().getDecorView();


                    ImageView splashScreen = vb.findViewWithTag("splash_screen");

                    int[] screenSize = getScreenSize(mSplashDialog.getContext());
                    mSplashDialog.setCancelable(false);

                    if (!mSplashDialog.isShowing()) {
                        mSplashDialog.show();
                    }
                    int width = splashScreen.getDrawable().getIntrinsicWidth();
                    int height = splashScreen.getDrawable().getIntrinsicHeight();
                    Log.d("info", "宽度为:" + width);
                    Log.d("info", splashScreen.toString());
                    Log.d("info", "高度为:" + height);
                    Log.d("info", splashScreen.toString());

                    ViewGroup.LayoutParams splashParams = splashScreen.getLayoutParams();
                    splashParams.width = screenSize[0];
                    splashParams.height = (height*screenSize[0])/width;
                    Log.d("info", "宽度为:" + splashParams.width);
                    Log.d("info", "高度为:" + splashParams.height);
                    splashScreen.setLayoutParams(splashParams);
                }
            }
        });
    }

    public static int[] getScreenSize(Context context){
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display defaultDisplay = windowManager.getDefaultDisplay();
        Point point = new Point();
        defaultDisplay.getSize(point);
        int x = point.x;
        int y = point.y;
        int[] size = new int[2];
        size[0] = x;
        size[1] = y;
        return size;
    }

    /**
     * 打开启动屏
     */
    public static void show(final Activity activity, final boolean fullScreen) {
        int resourceId = fullScreen ? R.style.SplashScreen_Fullscreen : R.style.SplashScreen_SplashTheme;

        show(activity, resourceId);
    }

    /**
     * 打开启动屏
     */
    public static void show(final Activity activity) {
        show(activity, false);
    }

    /**
     * 关闭启动屏
     */
    public static void hide(Activity activity) {
        if (activity == null) {
            if (mActivity == null) {
                return;
            }
            activity = mActivity.get();
        }

        if (activity == null) return;

        final Activity _activity = activity;

        _activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (mSplashDialog != null && mSplashDialog.isShowing()) {
                    boolean isDestroyed = false;

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                        isDestroyed = _activity.isDestroyed();
                    }

                    if (!_activity.isFinishing() && !isDestroyed) {
                        mSplashDialog.dismiss();
                    }
                    mSplashDialog = null;
                }
            }
        });
    }
}
