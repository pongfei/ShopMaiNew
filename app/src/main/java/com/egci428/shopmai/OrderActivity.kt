package com.egci428.shopmai

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

class OrderActivity :  AppCompatActivity(), OrderAdapter.OnItemClickListener
    {
    lateinit var recyclerView: RecyclerView
    lateinit var adapter: OrderAdapter
    private val orderList = ArrayList<Order>()

//    private var sensorManager : SensorManager? = null //sensor
//    private var lastUpdate: Long = 0 //sensor
    lateinit var totalText: TextView

    private lateinit var dataReference: FirebaseFirestore

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

        dataReference = FirebaseFirestore.getInstance()

        adapter = OrderAdapter(orderList, this)
        recyclerView.adapter = adapter

        val homeBtn = findViewById<Button>(R.id.homeBtn)
        val checkBtn = findViewById<Button>(R.id.checkBtn)
        totalText = findViewById(R.id.totalTextView)

        readFirestoreData()

        homeBtn.setOnClickListener(){
            val intent = Intent(this, MainActivity::class.java) //from: this to SaveCookies
            startActivity(intent)
        }
        checkBtn.setOnClickListener(){
            Toast.makeText(this, "Order submitted!", Toast.LENGTH_SHORT).show()
//            val intent = Intent(this, ReviewActivity::class.java) //from: this to SaveCookies
//            intent.putExtras("itemname","chocolate cake")
//            startActivity(intent)
//            finish()
        }
    }
        private fun readFirestoreData() {
            dataReference.collection("order")
                .get()
                .addOnSuccessListener {result ->
                    val newOrders = result.toObjects(Order::class.java)
                    adapter.updateOrders(newOrders)
                }
                .addOnFailureListener {
                    Toast.makeText(
                        applicationContext,
                        "Failed to read message from Firestore!",
                        Toast.LENGTH_SHORT
                    ).show()
                }
        }

        override fun onItemClick(position: Int) {
            val viewHolder = recyclerView.findViewHolderForAdapterPosition(position)
            viewHolder?.itemView?.animate()?.alpha(0F)?.setDuration(300)?.withEndAction {
                orderList.removeAt(position)
                adapter.notifyItemRemoved(position)
            }
        }
    }