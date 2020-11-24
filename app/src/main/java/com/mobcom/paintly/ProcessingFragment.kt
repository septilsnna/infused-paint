package com.mobcom.paintly

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Base64
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.amazonaws.auth.AWSCredentials
import com.amazonaws.auth.AWSCredentialsProvider
import com.amazonaws.auth.BasicAWSCredentials
import com.amazonaws.mobileconnectors.apigateway.ApiClientFactory
import com.amazonaws.regions.Regions
import com.bumptech.glide.Glide
import com.deeparteffects.sdk.android.DeepArtEffectsClient
import com.deeparteffects.sdk.android.model.UploadRequest
import kotlinx.android.synthetic.main.fragment_processing.view.*
import org.jetbrains.anko.support.v4.runOnUiThread
import java.io.ByteArrayOutputStream
import java.util.*

class ProcessingFragment : Fragment() {
    val API_KEY = "1N9PVfY0se8IHx5Pb8ekI5T6bhLdhNyZazBCMwgi"
    val ACCESS_KEY = "AKIA3XE3HF7SZPDDBT6B"
    val SECRET_KEY = "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"

    lateinit var deepArtEffectsClient: DeepArtEffectsClient
    lateinit var mView: View
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mView = inflater.inflate(R.layout.fragment_processing, container, false)

//        AWS untuk akses api nya deepart
        val factory = ApiClientFactory()
            .apiKey(API_KEY)
            .credentialsProvider(object : AWSCredentialsProvider {
                override fun getCredentials(): AWSCredentials {
                    return BasicAWSCredentials(ACCESS_KEY, SECRET_KEY)
                }

                override fun refresh() {}
            }).region(Regions.EU_WEST_1.getName())
        deepArtEffectsClient = factory.build(DeepArtEffectsClient::class.java)
//        AWS untuk akses api nya deepart

        val arguments = arguments
        val styleId = arguments?.getString("STYLE_ID")
        val byteArray = arguments?.getByteArray("IMAGE")
        val imageBitmap = BitmapFactory.decodeByteArray(byteArray, 0, byteArray!!.size)

        uploadImage(styleId!!, imageBitmap)

        return mView
    }

    private fun uploadImage(styleId: String, imageBitmap: Bitmap) {
        Thread {
            val uploadRequest = UploadRequest()
            uploadRequest.styleId = styleId
            uploadRequest.imageBase64Encoded = convertBitmapToBase64(imageBitmap)
            val response = deepArtEffectsClient.uploadPost(uploadRequest)
            val submissionId = response.submissionId
            val timer = Timer()
            timer.scheduleAtFixedRate(object : TimerTask() {
                override fun run() {
                    try {
                        val result = deepArtEffectsClient.resultGet(submissionId)
                        val submissionStatus = result.status
                        if (submissionStatus == "finished") {
                            runOnUiThread {
                                mView.processing.visibility = View.GONE
                                mView.result.visibility = View.VISIBLE
                                Glide.with(mView.context).load(result.url).into(mView.result_image)
                            }
                        }
                    } catch (e: java.lang.Exception) {
                        cancel()
                    }
                }
            }, 2500.toLong(), 2500.toLong())
            runOnUiThread { }
        }.start()
    }

    private fun convertBitmapToBase64(bitmap: Bitmap?): String? {
        val stream = ByteArrayOutputStream()
        bitmap?.compress(Bitmap.CompressFormat.JPEG, 100, stream)
        val byteArray = stream.toByteArray()
        return Base64.encodeToString(byteArray, 0)
    }

}