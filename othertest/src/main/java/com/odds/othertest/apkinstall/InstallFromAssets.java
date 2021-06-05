
package com.odds.othertest.apkinstall;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;

import com.odds.othertest.R;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class InstallFromAssets extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_install_from_assets);
    }
    
    public void clickButton(View v){
        installServiceFrameworkApk(this);
    }
    
    private void installServiceFrameworkApk(Context context)
    {

        String psfAPKName = "CSGameCenter.apk";
        Intent intent = new Intent();
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setAction(Intent.ACTION_VIEW);
        String type = "application/vnd.android.package-archive";
        AssetManager assets = context.getAssets();
        try
        {
            // 当文件比较大的时候不能用这个方法 来读取Stream ss.read(buffer) = -1 我的apk大小为5M
            InputStream is = assets.open(psfAPKName);
            // 使用下面这个方法 没问题
            // InputStream is =
            // getClass().getResourceAsStream("/assets/AsrService.apk");

            FileOutputStream fos = context.openFileOutput(psfAPKName, Context.MODE_PRIVATE
                    + Context.MODE_WORLD_READABLE);
            byte[] buffer = new byte[1024];
            int len = 0;
            while ((len = is.read(buffer)) != -1)
            {
                fos.write(buffer, 0, len);
            }
            fos.flush();
            is.close();
            fos.close();
        }catch (IOException e1){
            e1.printStackTrace();
            return;
        }
        File f = new File(context.getFilesDir().getPath() + "/" + psfAPKName);
        intent.setDataAndType(Uri.fromFile(f), type);
        context.startActivity(intent);
    }
}
