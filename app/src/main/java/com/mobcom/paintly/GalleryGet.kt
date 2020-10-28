package com.mobcom.paintly

class GalleryGet(
    val photo_id: Int,
    val user_id: String,
    val style_id: Int,
    val content_image: String,
    val file_result: String,
    val share_ig: Boolean,
    val share_twitter: Boolean,
    val share_fb: Boolean,
    val created_at: String
)