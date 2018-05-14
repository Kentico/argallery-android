package com.sumera.argallery.ui.feature.unityactivity

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.sumera.argallery.unity.UnityPlayerActivity

class UnityStarterActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val intent = Intent(this, UnityPlayerActivity::class.java)
        startActivity(intent)
    }
}