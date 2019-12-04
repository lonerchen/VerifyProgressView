package com.loner.verifyview

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        verify_progress.completeListener = {
            verify_progress.invalidate()
            Toast.makeText(this,"验证成功",Toast.LENGTH_LONG).show()
        }
    }
}
