package com.egci428.shopmai

import android.content.Context
import android.content.Intent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.egci428.shopmai.Adapter.OrderAdapter
import com.egci428.shopmai.Model.Order
import com.google.firebase.firestore.FirebaseFirestore
import java.io.BufferedReader
import java.io.InputStreamReader
import java.io.OutputStreamWriter
import com.google.gson.Gson

class OrderActivity :  AppCompatActivity(), OrderAdapter.OnItemClickListener {
    lateinit var recyclerView: RecyclerView
    lateinit var adapter: OrderAdapter
    private val orderList = ArrayList<Order>()

    private val file = "order.txt"

    //    private var sensorManager : SensorManager? = null //sensor
//    private var lastUpdate: Long = 0 //sensor
    lateinit var totalText: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_cart)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        recyclerView = findViewById(R.id.RecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)

        adapter = OrderAdapter(orderList, this)
        recyclerView.adapter = adapter

////////////sending data across pages
        val bundle = intent.extras
        var title = "" //assign string value
        var img1 = ""
        var price = 0

        if(bundle != null) //check if can receive data from previous page
        {
            title = bundle.getString("title").toString()
            img1 = bundle.getString("img1").toString()
            price = bundle.getInt("price")

            if (title.isNotBlank()) {
                val order = Order(title,img1,price)
                orderList.add(order)
                adapter.notifyItemInserted(orderList.size - 1)
            }
        }
        
        val homeBtn = findViewById<Button>(R.id.homeBtn)
        val checkBtn = findViewById<Button>(R.id.checkBtn)
        totalText = findViewById(R.id.totalTextView)

        homeBtn.setOnClickListener() {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
        checkBtn.setOnClickListener() {
            Toast.makeText(this, "Order submitted!", Toast.LENGTH_SHORT).show()
//            val intent = Intent(this, ReviewActivity::class.java)
//            intent.putExtras("itemname","chocolate cake")
//            startActivity(intent)
//            finish()
        }
    }
    ///put in item detail
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

    private fun read() {
        try {
            val fIn = openFileInput(file)
            val reader = BufferedReader(InputStreamReader(fIn))

            orderList.clear()
            var line: String?

            while (reader.readLine().also { line = it } != null) {
                val order = Gson().fromJson(line, Order::class.java)
                orderList.add(order)
            }
            reader.close()
            fIn.close()

            adapter.notifyDataSetChanged()

        } catch (e: Exception) {
            e.printStackTrace()
            Toast.makeText(baseContext, "No saved data found.", Toast.LENGTH_SHORT).show()
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

    override fun onItemClick(position: Int) {
        val viewHolder = recyclerView.findViewHolderForAdapterPosition(position)
        viewHolder?.itemView?.animate()?.alpha(0F)?.setDuration(300)?.withEndAction {
            orderList.removeAt(position)
            adapter.notifyItemRemoved(position)
            update()
        }
    }
}