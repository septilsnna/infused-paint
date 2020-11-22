package com.mobcom.paintly

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Base64
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.amazonaws.auth.AWSCredentials
import com.amazonaws.auth.AWSCredentialsProvider
import com.amazonaws.auth.BasicAWSCredentials
import com.amazonaws.mobileconnectors.apigateway.ApiClientFactory
import com.amazonaws.regions.Regions
import com.bumptech.glide.Glide
import com.deeparteffects.sdk.android.DeepArtEffectsClient
import com.deeparteffects.sdk.android.model.UploadRequest
import com.mobcom.paintly.R.layout.activity_result
import kotlinx.android.synthetic.main.activity_result.*
import java.io.ByteArrayOutputStream

class ProcessingActivity() : AppCompatActivity() {
    private val isProcessing = false
    var uriResult: String? = null
    lateinit var styleId: String
    lateinit var imageBitmap: Bitmap
    lateinit var deepArtEffectsClient: DeepArtEffectsClient


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(activity_result)

        // AWS untuk akses api nya deepart
        val factory = ApiClientFactory()
            .apiKey(HomeFragment().API_KEY)
            .credentialsProvider(object : AWSCredentialsProvider {
                override fun getCredentials(): AWSCredentials {
                    return BasicAWSCredentials(HomeFragment().ACCESS_KEY, HomeFragment().SECRET_KEY)
                }

                override fun refresh() {}
            }).region(Regions.EU_WEST_1.getName())
        deepArtEffectsClient = factory.build(DeepArtEffectsClient::class.java)
        // AWS untuk akses api nya deepart

        styleId = intent.getStringExtra("STYLE_ID")
        val byteArray = intent.getByteArrayExtra("IMAGE")
        imageBitmap = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.size)

//        image_result.setImageBitmap(imageBitmap)
//        processing.setVisibility(View.GONE)
//        processing_img.setVisibility(View.VISIBLE)
        uploadImage()
//        Toast.makeText(this, uriResult, Toast.LENGTH_SHORT).show()
//        image_result.setImageURI(Uri.parse(uriResult))
    }

//    private class ImageReadyCheckTimer internal constructor(submissionId: String) : TimerTask() {
//        private val mSubmissionId: String
//        override fun run() {
//            try {
//                val result: Result = HomeFragment().deepArtEffectsClient!!.resultGet(mSubmissionId)
//                val submissionStatus = result.status
//                if (submissionStatus == "finished") {
//                    runOnUiThread(Runnable {
//                        Glide.with(this).load(result.url).centerCrop().crossFade()
//                            .into(R.id.image_result)
//                        mProgressbarView.setVisibility(View.GONE)
//                        mImageView.setVisibility(View.VISIBLE)
//                        mStatusText.setText("")
//                    })
//                    isProcessing = false
//                    cancel()
//                }
//            } catch (e: Exception) {
//                cancel()
//            }
//        }
//
//        init {
//            mSubmissionId = submissionId
//        }
//    }

    private fun uploadImage() {
//        Thread {
//            val uploadRequest = UploadRequest()
//            uploadRequest.styleId = styleId
//            uploadRequest.imageBase64Encoded = convertBitmapToBase64(imageBitmap)
//            val response = deepArtEffectsClient?.uploadPost(uploadRequest)
//            val submissionId = response?.submissionId
////            val timer = Timer()
////            timer.scheduleAtFixedRate(
////                ImageReadyCheckTimer(submissionId),
////                HomeFragment().CHECK_RESULT_INTERVAL_IN_MS.toLong(),
////                HomeFragment().CHECK_RESULT_INTERVAL_IN_MS.toLong()
////            )
//            runOnUiThread() {
//                val result = deepArtEffectsClient?.resultGet(submissionId)
////                val submissionStatus = result.status
////                if (submissionStatus == "finished") {
////                    runOnUiThread(Runnable {
////            image_result.setImageURI(Uri.parse(result?.url))
////                    })
//                Toast.makeText(this, result?.status, Toast.LENGTH_SHORT).show()
////            Glide.with(this).load(result?.url).centerCrop()
////            .into(image_result)
//            }
////                mStatusText.setText(R.string.processing)
////            }
//        }.start()

        Thread {
            val uploadRequest = UploadRequest()
            uploadRequest.styleId = styleId
            uploadRequest.imageBase64Encoded = convertBitmapToBase64(imageBitmap)
            val response = deepArtEffectsClient!!.uploadPost(uploadRequest)
            val submissionId = response.submissionId
            try {
                val result = deepArtEffectsClient!!.resultGet(submissionId)
                val submissionStatus = result.status
                if (submissionStatus != "error") {
                    runOnUiThread {
                        Toast.makeText(this, result.status, Toast.LENGTH_LONG).show()
                        Glide.with(this).load(result.url).into(result_image)
                    }
                }
            } catch (e: Exception) {
            }
//            runOnUiThread {
//                uriResult = submissionId
//            }
        }.start()

    }

    private fun convertBitmapToBase64(bitmap: Bitmap?): String? {
        val stream = ByteArrayOutputStream()
        bitmap?.compress(Bitmap.CompressFormat.JPEG, 100, stream)
        val byteArray = stream.toByteArray()
        return Base64.encodeToString(byteArray, 0)
    }

}
