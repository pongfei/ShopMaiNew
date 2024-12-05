package com.egci428.shopmai

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.hardware.Sensor
import android.hardware.SensorEvent
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
import java.io.BufferedReader
import java.io.InputStreamReader
import java.io.OutputStreamWriter
import com.google.gson.Gson

class OrderActivity :  AppCompatActivity(), OrderAdapter.OnItemClickListener, SensorEventListener {
    lateinit var recyclerView: RecyclerView
    lateinit var adapter: OrderAdapter
    private val orderList = ArrayList<Order>()

    private val file = "order.txt"

    private var sensorManager : SensorManager? = null //sensor
    private var lastUpdate: Long = 0 //sensor

    lateinit var totalText: TextView

    private var locationClick: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_cart)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        lastUpdate = System.currentTimeMillis()

        recyclerView = findViewById(R.id.RecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)

        adapter = OrderAdapter(orderList, this, sensorManager!!)
        recyclerView.adapter = adapter

        val homeBtn = findViewById<Button>(R.id.homeBtn)
        val checkBtn = findViewById<Button>(R.id.checkBtn)
        val locationBtn = findViewById<Button>(R.id.locationBtn)
        totalText = findViewById(R.id.totalTextView)

        read()

        //go back to home button
        homeBtn.setOnClickListener() {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        //check if cart is empty or not
        checkBtn.setOnClickListener() {
            if (locationClick) {
                if (orderList.isEmpty()) {
                    Toast.makeText(this, "Cart is empty!", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }
                Toast.makeText(this, "Order submitted!", Toast.LENGTH_SHORT).show()
                emptyCart()
                val intent = Intent(this, ReviewActivity::class.java)
                intent.putExtra("itemname", orderList.get(0).title)
                intent.putExtra("id", orderList.get(0).id)
                startActivity(intent)
                finish()
            }
            else{
                Toast.makeText(this, "Selected Delivery Location First!", Toast.LENGTH_SHORT).show()
            }
        }

        locationBtn.setOnClickListener{
            val intent = Intent(this, MapsActivity::class.java)
            startActivity(intent)
            locationClick = true
        }

    }

    override fun onSensorChanged(event: SensorEvent) {
        if (event.sensor.type == Sensor.TYPE_ACCELEROMETER) {
            getAccelerometer(event)
        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) { }

    @SuppressLint("SetTextI18n")
    private fun getAccelerometer(event: SensorEvent) {
        val values = event.values
        val x = values[0]
        val y = values[1]
        val z = values[2]

        val accel = (x * x + y * y + z * z) / (SensorManager.GRAVITY_EARTH * SensorManager.GRAVITY_EARTH)

        val actualTime = System.currentTimeMillis()
        if (accel >= 2) {
            if (actualTime - lastUpdate < 200) {
                return //exit from function
            }
            lastUpdate = actualTime
            orderList.clear()
            emptyCart()
            read()
            Toast.makeText(this, "empty cart", Toast.LENGTH_SHORT).show()
            finish()
        }
    }

    override fun onPause() {
        super.onPause()
        sensorManager!!.unregisterListener(this)
    }

    override fun onResume() {
        super.onResume()
        sensorManager!!.registerListener(this, sensorManager!!.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_NORMAL)
    }
    ///end sensor
    private fun emptyCart(){
        try{
            val fOut = openFileOutput(file, Context.MODE_PRIVATE)
            val writer = OutputStreamWriter(fOut)
            writer.write("")
            writer.close()
            adapter.notifyDataSetChanged()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    //read from the file
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

    //remove the order if clicked on the order
    override fun onItemClick(position: Int) {
        val viewHolder = recyclerView.findViewHolderForAdapterPosition(position)
        viewHolder?.itemView?.animate()?.alpha(0F)?.setDuration(300)?.withEndAction {
            orderList.removeAt(position)
            adapter.notifyItemRemoved(position)
            update()
            finish()
            startActivity(intent)
        }
    }
}