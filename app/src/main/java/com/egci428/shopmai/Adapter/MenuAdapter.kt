package com.egci428.shopmai.Adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.egci428.shopmai.ItemDetail
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
//        val img1TextView = view.findViewById<TextView>(R.id.messageImg1)
//        val img2TextView = view.findViewById<TextView>(R.id.messageImg2)
        val priceTextView = view.findViewById<TextView>(R.id.messagePrice)
        val orderBtn = view.findViewById<ImageView>(R.id.orderBtn)
        val imgView = view.findViewById<ImageView>(R.id.imgView)

        // Set values from the message object
        val menu = menuList[position]
        titleTextView.text = menu.title
        priceTextView.text = "Price: ฿${menu.price}"

        val imgName = menu.img1
        val resId = mContext.resources.getIdentifier(imgName, "drawable", mContext.packageName) ?: 0
        if (resId != 0) {
            imgView.setImageResource(resId)
        } else {
            imgView.setImageResource(R.drawable.ic_launcher_foreground) // Default image
        }

        //when a row clicked
        view.setOnClickListener {
            val intent = Intent(mContext, ItemDetail::class.java)
            intent.putExtra("id", menu.id)
            intent.putExtra("titleTextView", titleTextView.text.toString())
            intent.putExtra("img1TextView", menu.img1)
            intent.putExtra("img2TextView", menu.img2)
            intent.putExtra("priceTextView", menu.price)
            intent.putExtra("descriptionTextView", menu.description)

            mContext.startActivity(intent) //use mContext when in adapter class

            Toast.makeText(mContext, "${menu.title}!", Toast.LENGTH_SHORT).show()
        }

        return view
    }
}
