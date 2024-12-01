package com.egci428.shopmai.Adapter

import android.hardware.SensorManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.egci428.shopmai.Model.Order
import com.egci428.shopmai.OrderActivity
import com.egci428.shopmai.R

class OrderAdapter(
    private val orderObject: ArrayList<Order>,
    private val listener: OnItemClickListener,
    private val sensorManager: SensorManager,
    private var total: Float = 0.0f
) : RecyclerView.Adapter<OrderAdapter.OrderViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.cart_row, parent, false)
        return OrderViewHolder(itemView)
    }

    override fun getItemCount(): Int = orderObject.size

    override fun onBindViewHolder(holder: OrderViewHolder, position: Int) {
        val order = orderObject[position]
        holder.txtTitle.text = order.title
        holder.txtPrice.text = "฿ "+ order.price.toString()
//        holder.txtPrice.text = "฿ ${ order.price}"
        total+= order.price

        (holder.itemView.context as OrderActivity).totalText.text = "Total: ฿ $total"

        // Resolve image resource
        val imgName = order.img1
        val context = holder.imgView.context
        val resId = imgName.let { context.resources.getIdentifier(it, "drawable", context.packageName) } ?: 0
        if (resId != 0) {
            holder.imgView.setImageResource(resId)
        } else {
            holder.imgView.setImageResource(R.drawable.ic_launcher_foreground)
        }
    }

    interface OnItemClickListener {
        fun onItemClick(position: Int)
    }

    inner class OrderViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val txtTitle: TextView = itemView.findViewById(R.id.itemTitle)
        val txtPrice: TextView = itemView.findViewById(R.id.itemPrice)
        val imgView: ImageView = itemView.findViewById(R.id.imgView)

        init {
            itemView.setOnClickListener {
                if (adapterPosition != RecyclerView.NO_POSITION) {
                    listener.onItemClick(adapterPosition)
                }
            }
        }
    }

}
