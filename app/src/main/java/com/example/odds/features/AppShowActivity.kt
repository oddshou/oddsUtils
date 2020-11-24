package com.example.odds.features

import android.Manifest
import android.content.pm.ApplicationInfo
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.os.Bundle
import android.os.Environment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.RecyclerView
import com.blankj.utilcode.util.FileUtils
import com.blankj.utilcode.util.ImageUtils
import com.example.odds.R
import com.example.odds.tools.ExcelUtils
import kotlinx.android.synthetic.main.activity_app_show.*
import kotlinx.android.synthetic.main.item_app_show.view.*
import java.io.File


class AppShowActivity : AppCompatActivity(), View.OnClickListener {

    lateinit var list: List<PackageInfo>

    companion object{
        val titleList = arrayOf("icon", "lable", "packageName", "Version")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_app_show)

        list = packageManager.getInstalledPackages(PackageManager.GET_ACTIVITIES).filter {
            (ApplicationInfo.FLAG_SYSTEM.and(it.applicationInfo.flags)) == 0
        }

        recycleView.adapter = Adapter(list, packageManager)

        textAppSize.text = "size:${list.size} path:sdcard/Record/"
        btnToExcel.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        val list1 = list.flatMap {
            arrayListOf(Entity(ImageUtils.drawable2Bytes(it.applicationInfo.loadIcon(packageManager), Bitmap.CompressFormat.JPEG, 100),
                    it.applicationInfo.loadLabel(packageManager).toString(),
                    it.packageName.toString(),
                    it.versionName.toString()))
        }

        FileUtils.createOrExistsDir(getFirstSDPath() + "/Record")
        ExcelUtils.initExcel(getFirstSDPath() + "/Record" + "/InstallApp.xls", titleList.toMutableList());
        ExcelUtils.writeObjListToExcel(list1, getFirstSDPath() + "/Record" + "/InstallApp.xls", this);
    }

    fun getFirstSDPath(): String {
        return if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            Environment.getExternalStorageDirectory().absolutePath
        } else {
            ""
        }
    }

    fun makeDir(dir: File) {
        if (!dir.parentFile.exists()) {
            makeDir(dir.parentFile)
        }
        dir.mkdir()
    }

    public class Entity(
        val byte: ByteArray,
        val lable: String,
        val packageName: String,
        val version: String
    )

    class Adapter(val list: List<PackageInfo>,
                  val pm: PackageManager): RecyclerView.Adapter<Adapter.ViewHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Adapter.ViewHolder {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.item_app_show, parent, false)
            return ViewHolder(view)
        }

        override fun getItemCount(): Int {
            return list.size
        }

        override fun onBindViewHolder(holder: Adapter.ViewHolder, position: Int) {
            val item = list[position]
            holder.imgIcon.setImageDrawable(item.applicationInfo.loadIcon(pm))
            holder.textLabel.text = item.applicationInfo.loadLabel(pm).toString()
            holder.textPackageName.text = item.packageName.toString()
            holder.textVersion.text = item.versionName?.toString()
        }

        inner class ViewHolder(val mView: View) : RecyclerView.ViewHolder(mView) {
            val textLabel: TextView = mView.textLabel
            val textPackageName: TextView = mView.textPackageName
            val textVersion: TextView = mView.textVersion
            val imgIcon: ImageView = mView.imgIcon
        }

    }
}
