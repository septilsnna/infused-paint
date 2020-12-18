package com.mobcom.infusedpaint

data class UserData(
    val username: String?,
    val password: String?,
    val name: String?,
    val email: String?,
    val photo: String?,
    val quota_today: Int?,
    val edit_freq: Int?,
    val share_freq: Int?,
    val created_at: String?
)