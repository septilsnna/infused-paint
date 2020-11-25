package com.mobcom.paintly

data class UserData(
    val username: String?,
    val password: String?,
    val name: String?,
    var email: String?,
    val photo: String?,
    val edit_freq: Int?,
    val share_freq: Int?,
    val created_at: String?
)