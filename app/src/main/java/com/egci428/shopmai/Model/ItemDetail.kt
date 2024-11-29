package com.egci428.shopmai.Model

import android.os.Bundle
import android.widget.ListView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.egci428.shopmai.Adapter.ReviewDetailAdapter
import com.egci428.shopmai.R
import com.google.firebase.firestore.FirebaseFirestore
import android.util.Log
import android.widget.RatingBar

class ItemDetail : AppCompatActivity() {

    private lateinit var listView: ListView
    private lateinit var msgList: MutableList<ReviewDetail>
    private lateinit var adapter: ReviewDetailAdapter
    private lateinit var dataReference: FirebaseFirestore


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_item_detail)

        listView = findViewById(R.id.reviewList)
        msgList = mutableListOf()
        dataReference = FirebaseFirestore.getInstance()

        val title = findViewById<TextView>(R.id.titleInfo)
        val price = findViewById<TextView>(R.id.priceInfo)
        val desc = findViewById<TextView>(R.id.descInfo)
//        val rating = findViewById<RatingBar>(R.id.ratingBar)

        val bundle = intent.extras
        if (bundle != null) {
            title.text = bundle.getString("titleTextView")
            price.text = bundle.getString("priceTextView")
            desc.text = bundle.getString("descriptionTextView")
        }

        readFirestoreData()

    }


    private fun readFirestoreData() {

        val titleTextView = findViewById<TextView>(R.id.titleInfo)
        val titleText = titleTextView.text.toString()
//        val ratingReview = findViewById<RatingBar>(R.id.ratingBar)


        Log.d("Firestore", "Querying reviews for item: $titleText")

        val db = dataReference.collection("review")
            .whereEqualTo("item", titleText)

        db.orderBy("id")
            .get()
            .addOnSuccessListener { snapshot ->
                if (snapshot != null) {
                    msgList.clear()
                    val messages = snapshot.toObjects(ReviewDetail::class.java)
//                    val ratingReview
                    msgList.addAll(messages)

                    // Set up adapter and initialize currentList
                    adapter = ReviewDetailAdapter(this, R.layout.row_review, msgList)
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