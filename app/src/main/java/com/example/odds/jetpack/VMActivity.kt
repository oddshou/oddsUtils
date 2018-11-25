package com.example.odds.jetpack

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.odds.jetpack.ui.vm.VmFragment
import com.example.odds.R

class VMActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.vm_activity)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                    .replace(R.id.container, VmFragment.newInstance())
                    .commitNow()
        }
    }

}
