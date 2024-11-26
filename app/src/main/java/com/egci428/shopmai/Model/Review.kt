package com.egci428.shopmai.Model

data class Review (
    val itemId: Int,
    val userName: String,
    val timestamp: String,
    val reviewImg: String,
    val ratingStar: Int,
    val commentText: String)