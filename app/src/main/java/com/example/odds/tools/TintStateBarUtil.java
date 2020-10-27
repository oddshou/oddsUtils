package com.example.odds.tools;

import android.app.Activity;
import android.graphics.Color;
import android.os.Build;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.blankj.utilcode.util.LogUtils;
import com.readystatesoftware.systembartint.SystemBarTintManager;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import androidx.annotation.NonNull;

/**
 * Created by Carl on 2015/12/31.
 */
public class TintStateBarUtil {

    private static final int DEFAULT_DARK_BAR_COLOR = 0x33000000;
    private static boolean isMiUi = isMiUiOS();
    public static String sMiuiVersion;
    private static boolean isFlyMe = isFlyMeOS();
    private static boolean isSupportDarkIcon = true;


    public static boolean isStatusBarTintSupported() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;
    }

    public static void setStatusBarTintCompat(Activity activity, boolean isMakeIconDark) {
        if (!isStatusBarTintSupported() || activity == null) {
            return;
        }
        setStatusBarTranslucent(activity);
        boolean success = setStatusBarIconDark(activity, isMakeIconDark);
        // 如果系统栏灰色icon失败，对状态栏整个进行着色，以突出文字颜色
        if (!success) {
            //给decView设置tag的目的是，在重复着色时，SystemBarTintManager会不停的new View，导致多层View叠加
            SystemBarTintManager tintManager = null;
            Window window = activity.getWindow();
            if (window == null) {
                return;
            }
            View decView = window.getDecorView();
            if (decView != null) {
                Object obj = window.getDecorView().getTag();
                if (obj != null && obj instanceof SystemBarTintManager) {
                    tintManager = (SystemBarTintManager) obj;
                }
            }
            if (tintManager == null) {
                tintManager = new SystemBarTintManager(activity);
            }
            tintManager.setStatusBarTintEnabled(true);
            tintManager.setNavigationBarTintEnabled(false);
            //当需要dark时，将状态栏着DEFAULT_DARK_BAR_COLOR色，否则为透明色
            if (Build.VERSION.SDK_INT == Build.VERSION_CODES.KITKAT || !isMakeIconDark) {
                tintManager.setStatusBarTintColor(Color.TRANSPARENT);
            } else {
                tintManager.setStatusBarTintColor(DEFAULT_DARK_BAR_COLOR);
            }
            if (decView != null && decView.getTag() == null) {
                decView.setTag(tintManager);
            }
        }
    }

    /**
     * 设置系统状态栏背景是否透明
     *
     * @param activity
     */
    public static void setStatusBarTranslucent(Activity activity) {
        if (isStatusBarTintSupported() && activity != null) {
            Window window = activity.getWindow();
            // Activity不可见
            if (window == null) {
                return;
            }
            if (Build.VERSION.SDK_INT >= 21) {           //只有5.0以上的系统才支持
                View decorView = window.getDecorView();
                int option = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
                decorView.setSystemUiVisibility(option);   //设置系统UI元素的可见性
                window.setStatusBarColor(Color.TRANSPARENT);   //将状态栏设置成透明色
            }
            //[5.0,6.0)区间的系统需设置一下代码，6.0设置如下代码会导致原生ROM及三星等机型会有着色
            if (Build.VERSION.SDK_INT >= 19 && !isAboveM()) {
                WindowManager.LayoutParams winParams = window.getAttributes();
                final int bits = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
                winParams.flags |= bits;
                window.setAttributes(winParams);
            }
        }
    }

    /**
     * 设置系统状态栏Icon是否为灰色 在小米、魅族及6.0+系统能设置成功
     *
     * @param activity
     * @param isDark
     * @return 返回设置是否成功
     */
    public static boolean setStatusBarIconDark(Activity activity, boolean isDark) {
        if (!isSupportDarkIcon || activity == null) {
            return false;
        }
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {//不支持沉浸式 icon默认为白色
            isSupportDarkIcon = false;
            return false;
        }
        Window window = activity.getWindow();
        // Activity不可见
        if (window == null) {
            return false;
        }
        boolean result = false;
        if (isAboveM()) {
            int lightFlag = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;
            int uiVisibility = window.getDecorView().getSystemUiVisibility();
            if (isDark) {
                uiVisibility |= lightFlag;
            } else {
                uiVisibility &= ~lightFlag;
            }
            window.getDecorView().setSystemUiVisibility(uiVisibility);
            result = true;
        }
        if (!result && isMiUi) {
            result = setMiUiStatusBarIconDark(window, isDark);
        }
        if (!result && isFlyMe) {
            result = setFlyMeStatusBarIconDark(window, isDark);
        }
        isSupportDarkIcon = result;
        return result;
    }

    public static boolean isAboveM() {
        return Build.VERSION.SDK_INT >= 23;
    }

    // MiUi
    private static boolean setMiUiStatusBarIconDark(@NonNull Window window, boolean isDark) {
        boolean result = false;
        try {
            Class<? extends Window> clazz = window.getClass();
            Class<?> layoutParams = Class.forName("android.view.MiuiWindowManager$LayoutParams");
            Field field = layoutParams.getField("EXTRA_FLAG_STATUS_BAR_DARK_MODE");
            int darkModeFlag = field.getInt(layoutParams);
            Method extraFlagField = clazz.getMethod("setExtraFlags", int.class, int.class);
            extraFlagField.invoke(window, isDark ? darkModeFlag : 0, darkModeFlag);
            result = true;
        } catch (Exception e) {
            LogUtils.e("MiUi", "setStatusBarDarkIcon: failed");
        }
        return result;
    }

    // FlyMe
    private static boolean setFlyMeStatusBarIconDark(@NonNull Window window, boolean isDark) {
        boolean result = false;
        try {
            WindowManager.LayoutParams lp = window.getAttributes();
            Field darkFlag = WindowManager.LayoutParams.class.getDeclaredField("MEIZU_FLAG_DARK_STATUS_BAR_ICON");
            Field meizuFlags = WindowManager.LayoutParams.class.getDeclaredField("meizuFlags");
            darkFlag.setAccessible(true);
            meizuFlags.setAccessible(true);
            int bit = darkFlag.getInt(null);
            int value = meizuFlags.getInt(lp);
            if (isDark) {
                value |= bit;
            } else {
                value &= ~bit;
            }
            meizuFlags.setInt(lp, value);
            window.setAttributes(lp);
            result = true;
        } catch (Exception e) {
            LogUtils.e("FlyMe", "setStatusBarDarkIcon: failed");
        }

        return result;
    }

    public static boolean isMiUi() {
        return isMiUi;
    }

    /**
     * android p手机测试不会抛异常
     *
     * @return
     */
    private static boolean isMiUiOS() {
        try {
            Class<?> sysClass = Class.forName("android.os.SystemProperties");
            Method getStringMethod = sysClass.getDeclaredMethod("get", String.class);
            String miuiVersion = (String) getStringMethod.invoke(sysClass, "ro.miui.ui.version.name");
            sMiuiVersion = miuiVersion;
            return (!TextUtils.isEmpty(miuiVersion));
        } catch (Exception var4) {
            LogUtils.e("isMiUiOS----->" + var4.getMessage());
        }
        return false;
    }

    public static boolean isFlyMe() {
        return isFlyMe;
    }

    private static boolean isFlyMeOS() {
        try {
            String display = Build.DISPLAY.toLowerCase();
            return display.contains("flyme");
        } catch (Exception var5) {
            LogUtils.e("isFlyMeOS----->" + var5.getMessage());
        }
        return false;
    }

    public static void setStatusBarDoNotHaveTitle(Activity context, View mVGBaseTitle) {
        if (!TintStateBarUtil.isStatusBarTintSupported()) {
            return;
        }

        int mStatusBarHeight = new SystemBarTintManager(context).getConfig().getStatusBarHeight();

        if (mVGBaseTitle.getLayoutParams() instanceof RelativeLayout.LayoutParams) {
            mVGBaseTitle.setLayoutParams(new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, mStatusBarHeight));
        } else if (mVGBaseTitle.getLayoutParams() instanceof LinearLayout.LayoutParams) {
            mVGBaseTitle.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, mStatusBarHeight));
        }
        mVGBaseTitle.requestLayout();
    }

    public static boolean isStatusBarTintSupport() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;
    }

}
