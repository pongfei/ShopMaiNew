package com.egci428.shopmai.Adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.egci428.shopmai.Model.Order
import com.egci428.shopmai.R

class OrderAdapter(private val orderObject: MutableList<Order>, private val listener: OnItemClickListener):RecyclerView.Adapter<OrderAdapter.OrderViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.row,parent,false)
        return OrderViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return orderObject.size
    }

    override fun onBindViewHolder(holder: OrderViewHolder, position: Int)
    {

//        holder.txtTitle.text = orderObject[position].title
//        holder.txtPrice.text = orderObject[position].price.toString()
//        holder.imgView.setImageResource(orderObject[position].img1)
//        val Img = "R.drawable" + orderObject[position].img1
//        holder.imgView.setImage(Img)
    }

    interface OnItemClickListener {
        fun onItemClick(position: Int)
    }

    inner class OrderViewHolder(itemView: View):RecyclerView.ViewHolder(itemView) {
//        var txtTitle = itemView.findViewById<TextView>(R.id.itemTitle)
//        var txtPrice = itemView.findViewById<TextView>(R.id.itemPrice)
//        var imgView = itemView.findViewById<ImageView>(R.id.imgView)

        init {
            // Set the click listener for the entire item view
            itemView.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    listener.onItemClick(position)
                }
            }
        }
    }
}

