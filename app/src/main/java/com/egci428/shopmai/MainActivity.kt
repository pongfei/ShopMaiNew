package com.egci428.shopmai

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.EditText
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
    private lateinit var searchEditText: EditText
    private lateinit var currentList: MutableList<Menu>


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        listView = findViewById(R.id.menuList)
        msgList = mutableListOf()
        dataReference = FirebaseFirestore.getInstance()
        searchEditText = findViewById(R.id.searchEditText)

        readFirestoreData()
    }

    //read from menu collection in firestore
    private fun readFirestoreData() {
        val db = dataReference.collection("menu")
        db.orderBy("id").get()
            .addOnSuccessListener { snapshot ->
                if (snapshot != null) {
                    msgList.clear()
                    val messages = snapshot.toObjects(Menu::class.java)
                    msgList.addAll(messages)

                    // Set up adapter and initialize currentList
                    currentList = msgList.toMutableList()
                    adapter = MenuAdapter(this, R.layout.row, currentList)
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

    // NOTE: (view:View) is used when interacting with UI components without binding
    fun toCart(view: View) {
        Toast.makeText(this, "to cart!", Toast.LENGTH_SHORT).show()
        intent = Intent(this, OrderActivity::class.java)
        startActivity(intent)
    }

    //search function to find the item
    fun search(view: View) {
        //.trim() ignores the spaces/tab
        val query = searchEditText.text.toString().trim()
        if (query.isNotEmpty()) {
            currentList = msgList.filter { menu ->
                menu.title.contains(query, ignoreCase = true)
            }.toMutableList()
        } else {
            currentList = msgList.toMutableList() // Reset to full list
            Toast.makeText(this, "Showing all items.", Toast.LENGTH_SHORT).show()
        }

        // Update adapter with the current list
        adapter = MenuAdapter(this, R.layout.row, currentList)
        listView.adapter = adapter
    }

    //sort function based on price (low to high & high to low)
    fun sort(view: View) {
        val checkedItem = intArrayOf(-1)
        val alertDialog = AlertDialog.Builder(this)
        alertDialog.setIcon(R.drawable.sort)
        alertDialog.setTitle("Sort Price By:")
        val listItems = arrayOf("High To Low", "Low To High")

        alertDialog.setSingleChoiceItems(listItems, checkedItem[0]) { dialog, which ->
            checkedItem[0] = which
            when (which) {
                0 -> currentList.sortByDescending { it.price }
                1 -> currentList.sortBy { it.price }
            }

            adapter.notifyDataSetChanged()
            dialog.dismiss()
        }
        alertDialog.setNegativeButton("Cancel") { dialog, _ -> dialog.dismiss() }
        val customAlertDialog = alertDialog.create()
        customAlertDialog.show()
    }


}