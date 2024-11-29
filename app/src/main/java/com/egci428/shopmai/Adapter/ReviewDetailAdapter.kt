package com.egci428.shopmai.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.RatingBar
import android.widget.TextView
import com.egci428.shopmai.Model.ReviewDetail
import com.egci428.shopmai.R

class ReviewDetailAdapter (
    val mContext: Context,
    val layoutResId: Int,
    val reviewList: List<ReviewDetail>
): ArrayAdapter<ReviewDetail>(mContext, layoutResId, reviewList)
{
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        //inflate is the row.xml
        val inflater = LayoutInflater.from(mContext)
        val view = inflater.inflate(layoutResId, null)


        // Bind views

        val user = view.findViewById<TextView>(R.id.reviewUser)
        val date = view.findViewById<TextView>(R.id.reviewDate)
        val text = view.findViewById<TextView>(R.id.reviewText)
        var ratingBar: RatingBar = view.findViewById(R.id.ratingBar)

        // Set values from the message object
        val review = reviewList[position]
        user.text = review.user
        date.text = review.date
        text.text = review.review
        ratingBar.rating = review.rating!!.toFloat()

        return view
    }
}