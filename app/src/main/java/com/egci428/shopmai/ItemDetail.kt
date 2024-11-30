package com.egci428.shopmai

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.ListView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.egci428.shopmai.Adapter.ReviewDetailAdapter
import com.google.firebase.firestore.FirebaseFirestore
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageView
import com.egci428.shopmai.Model.Order
import com.egci428.shopmai.Model.ReviewDetail
import com.google.gson.Gson
import java.io.OutputStreamWriter

class ItemDetail : AppCompatActivity() {

    private lateinit var listView: ListView
    private lateinit var msgList: MutableList<ReviewDetail>
    private lateinit var adapter: ReviewDetailAdapter
    private lateinit var dataReference: FirebaseFirestore
    private val file = "order.txt"
    private var imgUrl: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        listView = findViewById(R.id.reviewList)
        msgList = mutableListOf()
        dataReference = FirebaseFirestore.getInstance()

        val title = findViewById<TextView>(R.id.titleInfo)
        val price = findViewById<TextView>(R.id.priceInfo)
        val desc = findViewById<TextView>(R.id.descInfo)
        val orderBtn = findViewById<Button>(R.id.orderBtn)
//        val img1 = findViewById<ImageView>(R.id.imgInfo)

        val bundle = intent.extras
        if (bundle != null) {
            title.text = bundle.getString("titleTextView")
            price.text = bundle.getString("priceTextView")
            desc.text = bundle.getString("descriptionTextView")
            imgUrl = bundle.getString("img1TextView")
        }
        readFirestoreData()
///////////
        orderBtn.setOnClickListener{
            val id = intent.getIntExtra("id", 0).toString()
            val Title = title.text.toString()
            val Img1 = imgUrl.toString()
            val Price = price.text.toString()
//            save(id, title, img1, price)
//            val line = "{\"id\": " + id +
//                    ",\"title\":" + title +
//                    ", \"price\":" + price +
//                    ", \"img1\": " + img1 +
//                    "}"
            val line = "{\"id\": $id, \"title\": \"$Title\", \"price\": $Price, \"img1\": \"$Img1\"}"
            save(line)
            finish()
        }
    //////////
    }

    private fun save(line: String) {
        try {
            // Open the file in append mode
            val fOut = openFileOutput(file, Context.MODE_APPEND)
            val writer = OutputStreamWriter(fOut)

            // Write the line (order details) into the file
            writer.write(line + "\n")
            writer.close()

            // Notify the adapter or UI if needed (optional)
            Toast.makeText(this, "added to cart", Toast.LENGTH_SHORT).show()
        } catch (e: Exception) {
            e.printStackTrace()
            Toast.makeText(this, "Failed to save order", Toast.LENGTH_SHORT).show()
        }
    }


//    try{
//            val fOut = openFileOutput(file, Context.MODE_APPEND)
//            val writer = OutputStreamWriter(fOut)
//            writer.write("{\"id\": \"1\",\"title\": \"Chocolate Cake\", \"price\": \"60.00\", \"img1\": \"chocolate_cake_1\"}" + "\n")
//            writer.close()
//            adapter.notifyDataSetChanged() } catch (e: Exception) {
//            e.printStackTrace()
//            }

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
    fun toCart(view: View) {
        Toast.makeText(this, "to cart!", Toast.LENGTH_SHORT).show()
        intent = Intent(this, OrderActivity::class.java)
        startActivity(intent)
    }
}