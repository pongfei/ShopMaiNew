package com.egci428.shopmai

import android.os.Bundle
import android.widget.ListView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.egci428.shopmai.Adapter.MenuAdapter
import com.egci428.shopmai.Model.Menu
import com.google.firebase.firestore.FirebaseFirestore

class MainActivity : AppCompatActivity() {

    private lateinit var listView: ListView
    private lateinit var msgList: MutableList<Menu>
    private lateinit var adapter: MenuAdapter
    private lateinit var dataReference: FirebaseFirestore
//    private lateinit var orderBtn: Button


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        listView = findViewById(R.id.menuList)
        msgList = mutableListOf()
        dataReference = FirebaseFirestore.getInstance()
//        orderBtn = findViewById(R.id.orderBtn)


//        orderBtn.setOnClickListener{
//            submitOrder()
//        }

        readFirestoreData()
    }
    private fun submitOrder() {
        Toast.makeText(applicationContext, "Order submitted!", Toast.LENGTH_SHORT).show()
    }

    private fun readFirestoreData() {
        val db = dataReference.collection("menu")
        db.orderBy("id").get()
            .addOnSuccessListener { snapshot ->
                if (snapshot != null) {
                    msgList.clear()
                    val messages = snapshot.toObjects(Menu::class.java)
                    msgList.addAll(messages)

                    // Set up adapter
                    adapter = MenuAdapter(this, R.layout.row, msgList)
                    listView.adapter = adapter
                }
            }
            .addOnFailureListener {
                Toast.makeText(
                    applicationContext,
                    "Failed to read message from Firestore!",
                    Toast.LENGTH_SHORT
                ).show()
            }
    }
}