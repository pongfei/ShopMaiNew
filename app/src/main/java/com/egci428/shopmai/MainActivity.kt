package com.egci428.shopmai

import android.os.Bundle
import android.view.View
import android.widget.ListView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.egci428.shopmai.Adapter.MenuAdapter
import com.egci428.shopmai.Model.Menu
import com.google.firebase.firestore.FirebaseFirestore

class MainActivity : AppCompatActivity() {

    private lateinit var listView: ListView
    private lateinit var msgList: MutableList<Menu>
    private lateinit var adapter: MenuAdapter
    private lateinit var dataReference: FirebaseFirestore


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        listView = findViewById(R.id.menuList)
        msgList = mutableListOf()
        dataReference = FirebaseFirestore.getInstance()

        readFirestoreData()
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

    // (view:View) is used when interacting with UI components without binding
    fun toCart(view: View) {
        Toast.makeText(this, "to cart!", Toast.LENGTH_SHORT).show()
    }

    fun search(view:View){
        Toast.makeText(this, "Search clicked!", Toast.LENGTH_SHORT).show()
    }

    fun sort(view: View) {

        val checkedItem = intArrayOf(-1)
        val alertDialog = AlertDialog.Builder(this)
        alertDialog.setIcon(R.drawable.sort)
        alertDialog.setTitle("Sort Price By:")
        val listItems = arrayOf("High To Low", "Low To High")

        alertDialog.setSingleChoiceItems(listItems, checkedItem[0]) { dialog, which ->
            checkedItem[0] = which
            when (which) {
                0 -> { msgList.sortByDescending { it.price } }
                1 -> { msgList.sortBy { it.price } }
            }
            adapter.notifyDataSetChanged()
            dialog.dismiss()
        }
        alertDialog.setNegativeButton("Cancel") { dialog, which -> } // Do nothing on cancel
        val customAlertDialog = alertDialog.create()
        customAlertDialog.show()
    }

}