package com.mobcom.paintly

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import maes.tech.intentanim.CustomIntent

class ProcessingActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_processing)

        val styleId = intent.getStringExtra("STYLE_ID")
        val byteArray = intent.getByteArrayExtra("IMAGE")

        val processing_fragment = ProcessingFragment()
        val arguments = Bundle()
        arguments.putString("STYLE_ID", styleId)
        arguments.putByteArray("IMAGE", byteArray)
        processing_fragment.arguments = arguments
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.fl_processing, processing_fragment)

            commit()
        }
    }

    override fun finish() {
        CustomIntent.customType(this, "fadein-to-fadeout")
        super.finish()
    }
}

//class ProcessingActivity : AppCompatActivity() {
//    val API_KEY = "1N9PVfY0se8IHx5Pb8ekI5T6bhLdhNyZazBCMwgi"
//    val ACCESS_KEY = "AKIA3XE3HF7SZPDDBT6B"
//    val SECRET_KEY = "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"
//
//    lateinit var result_image: ImageView
//    lateinit var submissionId: String
//    lateinit var styleId: String
//    lateinit var imageBitmap: Bitmap
//    lateinit var deepArtEffectsClient: DeepArtEffectsClient
//
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContentView(fragment_result)

        // AWS untuk akses api nya deepart
//        val factory = ApiClientFactory()
//            .apiKey(API_KEY)
//            .credentialsProvider(object : AWSCredentialsProvider {
//                override fun getCredentials(): AWSCredentials {
//                    return BasicAWSCredentials(ACCESS_KEY, SECRET_KEY)
//                }
//
//                override fun refresh() {}
//            }).region(Regions.EU_WEST_1.getName())
//        deepArtEffectsClient = factory.build(DeepArtEffectsClient::class.java)
//        deepArtEffectsClient = HomeFragment().deepArtEffectsClient!!
        // AWS untuk akses api nya deepart

//        styleId = intent.getStringExtra("STYLE_ID")
//        val byteArray = intent.getByteArrayExtra("IMAGE")
//        imageBitmap = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.size)
//        result_image = findViewById(R.id.result_image)

//        image_result.setImageBitmap(imageBitmap)
//        processing.setVisibility(View.GONE)
//        processing_img.setVisibility(View.VISIBLE)
//        uploadImage()
//        Toast.makeText(this, uriResult, Toast.LENGTH_SHORT).show()
//        image_result.setImageURI(Uri.parse(uriResult))
//    }

//    private fun uploadImage() {
//        Thread {
//            val uploadRequest = UploadRequest()
//            uploadRequest.styleId = styleId
//            uploadRequest.imageBase64Encoded = convertBitmapToBase64(imageBitmap)
//            val response = deepArtEffectsClient.uploadPost(uploadRequest)
//            submissionId = response.submissionId
//            val timer = Timer()
//            timer.scheduleAtFixedRate(
//                ImageReadyCheckTimer(submissionId),
//                2500.toLong(),
//                2500.toLong()
//            )
//            runOnUiThread {
//                Toast.makeText(this, submissionId, Toast.LENGTH_SHORT).show()
//            }
//        }.start()
//    }
//
//    private fun convertBitmapToBase64(bitmap: Bitmap?): String? {
//        val stream = ByteArrayOutputStream()
//        bitmap?.compress(Bitmap.CompressFormat.JPEG, 100, stream)
//        val byteArray = stream.toByteArray()
//        return Base64.encodeToString(byteArray, 0)
//    }
//
//    private class ImageReadyCheckTimer(submissionId: String) : TimerTask() {
//        private var mSubmissionId = submissionId
//        private val deepArtEffectsClient = ProcessingActivity().deepArtEffectsClient
//        override fun run() {
//            try {
//                val result = deepArtEffectsClient.resultGet(mSubmissionId)
//                val submissionStatus = result.status
//                if (submissionStatus == "finished") {
//                    ProcessingActivity().runOnUiThread {
//                        Glide.with(ProcessingActivity()).load(result.url).centerCrop().into(
//                            ProcessingActivity().result_image
//                        )
//                    }
////                    isProcessing = false
//                    cancel()
//                }
//            } catch (e: java.lang.Exception) {
//                cancel()
//            }
//        }
////
////        init {
////            mSubmissionId = submissionId
////        }
//    }

//}

