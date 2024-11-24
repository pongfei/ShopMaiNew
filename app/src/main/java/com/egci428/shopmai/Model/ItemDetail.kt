package com.egci428.shopmai.Model

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.egci428.shopmai.R

class ItemDetail : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_item_detail)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        //init these only after setContentView (idk why)
        val title = findViewById<TextView>(R.id.titleInfo)
        val price = findViewById<TextView>(R.id.priceInfo)
        val desc = findViewById<TextView>(R.id.descInfo)
//        val orderBtn = findViewById<Button>(R.id.orderBtn)

        val bundle = intent.extras
        if (bundle != null) {
            title.text = bundle.getString("titleTextView")
            price.text = bundle.getString("priceTextView")
            desc.text = bundle.getString("descriptionTextView")
        }

    }
}