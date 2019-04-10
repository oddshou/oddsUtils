package com.example.odds.route

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity

import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.View
import android.widget.TextView
import android.widget.Toast

import com.blankj.utilcode.util.LogUtils
import com.example.odds.R
import com.example.odds.custom_view.TimeProgressView
import com.example.odds.java_main.TestPreIntentActivity
import com.odds.annotation.processor.PreIntent

import java.util.ArrayList

class MainRouteActivity : AppCompatActivity() {

    var job: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        getScreenSize(this)

        val appLinkIntent = intent
        val appLinkAction = appLinkIntent.action
        val appLinkData = appLinkIntent.data


        init()
        //todo test
        val view = findViewById<TimeProgressView>(R.id.time_progress_view)
        view.setShowCircle(true)
    }


    /**
     * 未w
     */
    private fun init() {
        //通过包名获取主目录
        val packageManager = packageManager
        var packageInfo: PackageInfo? = null
        try {
            packageInfo = packageManager.getPackageInfo(
                    packageName, PackageManager.GET_ACTIVITIES)
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
        }


        for (activity in packageInfo!!.activities) {
            try {
                val aClass = Class.forName(activity.name)
                LogUtils.i(aClass)
            } catch (e: ClassNotFoundException) {
                e.printStackTrace()
            }

        }
    }


    fun getScreenSize(mContext: Context) {

        val densityDpi = mContext.resources.displayMetrics.densityDpi
        val scaledDensity = mContext.resources.displayMetrics.scaledDensity
        val density = mContext.resources.displayMetrics.density
        val xdpi = mContext.resources.displayMetrics.xdpi
        val ydpi = mContext.resources.displayMetrics.ydpi
        val widthPixels = mContext.resources.displayMetrics.widthPixels
        val heightPixels = mContext.resources.displayMetrics.heightPixels
        //density = densitydpi/160
        //widthdp = widthPixels/density


        val texts = "densityDpi: " + densityDpi + "\n" +
                "scaledDensity: " + scaledDensity + "\n" +
                "density: " + density + "\n" +
                "xdpi: " + xdpi + "\n" +
                "ydpi: " + ydpi + "\n" +
                "widthPixels: " + widthPixels + "\n" +
                "heightPixels: " + heightPixels + "\n" +
                "density=densityDpi/160:= " + densityDpi / 160.0 + "\n" +
                "widthdp=widthPixels/density:= " + widthPixels / density


        //        // 这样可以计算屏幕的物理尺寸
        //        float width2 = (width / xdpi)*(width / xdpi);
        //        float height2 = (height / ydpi)*(width / xdpi);
        //
        //        return (float) Math.sqrt(width2+height2);

        (findViewById<View>(R.id.text) as TextView).text = texts
    }

    fun clickBtn(view: View) {
        val intent = Intent(this, BActivity::class.java)
        //        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
        intent.putExtra("extra1", "fromeMain")
        startActivityForResult(intent, 330)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        Toast.makeText(this, "A:" + if (data == null) data else data.getStringExtra("data"), Toast.LENGTH_SHORT).show()
        //        System.out.println("A:" + (data == null ? data : data.getStringExtra("data")));
    }

    fun toRxActivity(view: View) {
        startActivity(Intent(this, RxActivity::class.java))
    }

    fun toThreadActivity(view: View) {
        startActivity(Intent(this, ThreadActivity::class.java))
    }

    fun toDataBinding(view: View) {
        startActivity(Intent(this, DataBindingActivity::class.java))
    }

    fun toJavaMain(view: View) {
        val bundle = Bundle()
        bundle.putString("bundle", "bundle-bundle")
        val list = ArrayList<String>()
        list.add("11")
        list.add("22")
        list.add("33")

        val serialize66 = TestPreIntentActivity.SerializeClass("Serialize66")
        val parcelable55 = TestPreIntentActivity.ParcelableClass("Parcelable55")
        val list1 = ArrayList<TestPreIntentActivity.ParcelableClass>()
        list1.add(parcelable55)
        val list2 = ArrayList<TestPreIntentActivity.SerializeClass>()
        list2.add(serialize66)
        val intent = PreIntent.preIntent_TestPreIntentActivity(this, "哈哈哈", true, 111, bundle,
                list, parcelable55, serialize66, arrayOf(parcelable55), arrayOf(serialize66),
                list1, list2)
        startActivity(intent)
    }

    /**
     * 点击进入自定义View
     * @param view
     */
    fun toCustomView(view: View) {

    }
}
