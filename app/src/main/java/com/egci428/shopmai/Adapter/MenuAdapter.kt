package com.egci428.shopmai.Adapter

import android.content.Context
import android.content.Intent
import android.os.Message
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.egci428.shopmai.Model.ItemDetail
import com.egci428.shopmai.Model.Menu
import com.egci428.shopmai.R

class MenuAdapter(
    val mContext: Context,
    val layoutResId: Int,
    val menuList: List<Menu>

) : ArrayAdapter<Menu>(mContext, layoutResId, menuList) {
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        //inflate is the row.xml
        val inflater = LayoutInflater.from(mContext)
        val view = inflater.inflate(layoutResId, null)

        // Bind views
        val titleTextView = view.findViewById<TextView>(R.id.messageTitle)
        val img1TextView = view.findViewById<TextView>(R.id.messageImg1)
        val img2TextView = view.findViewById<TextView>(R.id.messageImg2)
        val priceTextView = view.findViewById<TextView>(R.id.messagePrice)
        val descriptionTextView = view.findViewById<TextView>(R.id.messageDescription)
        val orderBtn = view.findViewById<Button>(R.id.orderBtn)

        // Set values from the message object
        val menu = menuList[position]
        titleTextView.text = menu.title
        img1TextView.text = "Image 1 URL: ${menu.img1}"
        img2TextView.text = "Image 2 URL: ${menu.img2}"
        priceTextView.text = "Price: $${menu.price}"
        descriptionTextView.text = menu.description

        // Handle button click
        orderBtn.setOnClickListener {

            val intent = Intent(mContext, ItemDetail::class.java)
            intent.putExtra("titleTextView", titleTextView.text.toString())
//            intent.putExtra("img1TextView", TextView.text.toString())
            intent.putExtra("priceTextView", priceTextView.text.toString())
            intent.putExtra("descriptionTextView", descriptionTextView.text.toString())

            mContext.startActivity(intent) //use mContext when in adapter class

            Toast.makeText(mContext, "Order submitted for ${menu.title}!", Toast.LENGTH_SHORT).show()

        }

        return view
    }
}
