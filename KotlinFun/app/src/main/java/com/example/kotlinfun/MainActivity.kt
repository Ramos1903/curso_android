package com.example.kotlinfun

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import org.w3c.dom.Text
import java.lang.Integer.parseInt

class MainActivity : AppCompatActivity() {

    var count = 0

//    fun reset(view: View){
//        count = 0
//        //counterTv.setText(count.toString())
//    }
//
//    fun counterUp(view: View){
//        count++
//        //counterTv.setText(count.toString())
//    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        var counterUp = findViewById<Button>(R.id.countUpBtn)
        var counterDown = findViewById<Button>(R.id.countDownBtn)
        var counterTv = findViewById<TextView>(R.id.counter)
        counterTv.setText("00")

        counterUp.setOnClickListener(View.OnClickListener {
            //counterUp()
            count++
            counterTv.setText(count.toString())
        })

        counterDown.setOnClickListener(View.OnClickListener {
           // reset()
            count --
            counterTv.setText(count.toString())
        })



    }
}