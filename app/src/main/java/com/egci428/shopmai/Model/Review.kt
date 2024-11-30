package com.egci428.shopmai.Model

data class Review (
    val item: String,
    val userName: String,
    val date: String,
    val reviewImg: String,
    val ratingStar: Int,
    val commentText: String)