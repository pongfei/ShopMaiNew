package com.egci428.shopmai.Model

import android.content.Context
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.egci428.shopmai.R
import com.google.gson.Gson
import java.io.OutputStreamWriter

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
    private fun save(order: Order){
        try{
            val json = Gson().toJson(order)
            val fOut = openFileOutput(file, Context.MODE_APPEND)
            val writer = OutputStreamWriter(fOut)
            writer.write(json + "\n")
            writer.close()
            adapter.notifyDataSetChanged()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
    private fun update() {
        try {
            // Overwrite file with updated orderList
            val fOut = openFileOutput(file, Context.MODE_PRIVATE)
            val writer = OutputStreamWriter(fOut)
            val gson = Gson()

            if (orderList.isEmpty()) {
                writer.write("")  // Clear file content if list is empty
            } else {
                orderList.forEach { order ->
                    writer.write(gson.toJson(order) + "\n")
                }
            }

            writer.close()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}