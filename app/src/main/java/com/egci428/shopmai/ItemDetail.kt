package com.egci428.shopmai

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.provider.ContactsContract.CommonDataKinds.Im
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
    private lateinit var imgInfo: ImageView
    private lateinit var msgList: MutableList<ReviewDetail>
    private lateinit var adapter: ReviewDetailAdapter
    private lateinit var dataReference: FirebaseFirestore
    private val file = "order.txt"
    private var imgUrl: String? = null
    private var imgUrl1: String? = null
    private var imgUrl2: String? = null

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
        imgInfo = findViewById<ImageView>(R.id.imgInfo)
        val backImgBtn = findViewById<ImageView>(R.id.backImgBtn)
        val backImgBtn2 = findViewById<ImageView>(R.id.backImgBtn2)

        val bundle = intent.extras
        if (bundle != null) {
            title.text = bundle.getString("titleTextView")
            price.text = "Price: ฿" + bundle.getFloat("priceTextView").toString()
            desc.text = bundle.getString("descriptionTextView")
            imgUrl1 = bundle.getString("img1TextView")
            imgUrl2 = bundle.getString("img2TextView")
            imgUrl = imgUrl1
        }
        readFirestoreData()

        backImgBtn.setOnClickListener{
            if(imgUrl==imgUrl1 && imgUrl != null)
                imgUrl = imgUrl2
            else imgUrl = imgUrl1
            showImg()
        }
        backImgBtn2.setOnClickListener{
            if(imgUrl==imgUrl1 && imgUrl != null)
                imgUrl = imgUrl2
            else imgUrl = imgUrl1
            showImg()
        }
        showImg()

        orderBtn.setOnClickListener{
            val id = intent.getIntExtra("id", 0).toString()
            val Title = title.text.toString()
            val Img1 = imgUrl1.toString()
            val Price = intent.getFloatExtra("priceTextView",0f)
//            val Price = intent.getFloatExtra("price",0F)
            val line = "{\"id\": $id, \"title\": \"$Title\", \"price\": $Price, \"img1\": \"$Img1\"}"
            save(line)
            finish()
        }
    }
    private fun showImg() {
        val context = imgInfo.context
        val resId = imgUrl?.let { context.resources.getIdentifier(it, "drawable", context.packageName) } ?: 0

        if (resId != 0) {
            imgInfo.setImageResource(resId)
        } else {
            imgInfo.setImageResource(R.drawable.ic_launcher_foreground)
        }
    }

    fun back(){
        intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }

    private fun save(line: String) {
        try {
            val fOut = openFileOutput(file, Context.MODE_APPEND)
            val writer = OutputStreamWriter(fOut)

            writer.write(line + "\n")
            writer.close()

            Toast.makeText(this, "added to cart", Toast.LENGTH_SHORT).show()
        } catch (e: Exception) {
            e.printStackTrace()
            Toast.makeText(this, "Failed to save order", Toast.LENGTH_SHORT).show()
        }
    }

    private fun readFirestoreData() {

        val titleTextView = findViewById<TextView>(R.id.titleInfo)
        val titleText = titleTextView.text.toString()

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