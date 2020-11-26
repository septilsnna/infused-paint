package com.mobcom.paintly

import android.content.Context
import android.content.ContextWrapper
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Base64
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.toBitmap
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
import maes.tech.intentanim.CustomIntent
import org.jetbrains.anko.support.v4.runOnUiThread
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.*
import java.util.*

class ProcessingFragment : Fragment() {
    //septilsnna
//    val API_KEY = "1N9PVfY0se8IHx5Pb8ekI5T6bhLdhNyZazBCMwgi"
//    val ACCESS_KEY = "AKIA3XE3HF7SZPDDBT6B"
//    val SECRET_KEY = "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"

    //soegiebawi
//    val API_KEY = "9f6oJPpUCc8T8znstCo0q6VxfNEvP0Xfa6iZ1zzH"
//    val ACCESS_KEY = "AKIA3XE3HF7SQUKGI4ES"
//    val SECRET_KEY = "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"

    //alohaloha
//    val API_KEY = "yG2xlNBFk76Q9jOBqP4753QgRqtMuYUn6BaUr6bD"
//    val ACCESS_KEY = "AKIA3XE3HF7S3JCV6XUT"
//    val SECRET_KEY = "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"

    //leejaewook
    val API_KEY = "abSVcg3xL88BnTj54AouR6qD0ZB6RICw2k60eZGV"
    val ACCESS_KEY = "AKIA3XE3HF7S3HGDGES6"
    val SECRET_KEY = "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"

    lateinit var deepArtEffectsClient: DeepArtEffectsClient
    lateinit var mView: View
    lateinit var userData: UserData
    lateinit var styleId: String
    lateinit var imageBitmap: Bitmap

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
        styleId = arguments?.getString("STYLE_ID")!!
        val byteArray = arguments?.getByteArray("IMAGE")
        imageBitmap = BitmapFactory.decodeByteArray(byteArray, 0, byteArray!!.size)

        // load data user
        val sharedPreferences = activity?.getSharedPreferences("sharedPrefs", Context.MODE_PRIVATE)
        val email = sharedPreferences?.getString("email", "").toString()
        getUser(email)

        uploadImage(styleId!!, imageBitmap)

        mView.save_result.setOnClickListener {
            saveResult()
        }

        mView.share_result.setOnClickListener {
            shareResult()
        }

        return mView
    }

    override fun onStop() {
        CustomIntent.customType(activity, "fadein-to-fadeout")
        super.onStop()
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
        return Base64.encodeToString(byteArray, Base64.DEFAULT)
    }

    private fun saveResult() {
        val imageResult = mView.result_image.drawable.toBitmap()

        RetrofitClient.instance.updateUserEditFreq(
            userData.email!!,
            userData.edit_freq!!.plus(1)
        ).enqueue(object : Callback<UserData?> {
            override fun onFailure(call: Call<UserData?>, t: Throwable) {
                Toast.makeText(activity, t.message, Toast.LENGTH_LONG).show()
            }

            override fun onResponse(call: Call<UserData?>, response: Response<UserData?>) =
                if (response.code() == 200) {
                } else {
                }
        })

        //save to gallery (infused paint api)
        RetrofitClient.instance.addResult(
            userData.email!!,
            styleId,
            "",
            convertBitmapToBase64(imageResult)!!
        ).enqueue(object : Callback<GalleryData?> {
            override fun onFailure(call: Call<GalleryData?>, t: Throwable) {
                Toast.makeText(activity, t.message, Toast.LENGTH_LONG).show()
            }

            override fun onResponse(call: Call<GalleryData?>, response: Response<GalleryData?>) =
                if (response.code() == 200) {
                } else {
                }
        })

        // Save image to gallery
        val savedImageURL = MediaStore.Images.Media.insertImage(
            activity?.contentResolver,
            imageResult,
            "result_image" +  "_" + userData.edit_freq!!.plus(1) + ".jpg",
            "Image of " + "result_image" +  "_" + userData.edit_freq!!.plus(1) + ".jpg"
        )
        Uri.parse(savedImageURL)

        mView.save_result.isEnabled = false
//        mView.save_result.isClickable = false
        Toast.makeText(activity, "Save Success!", Toast.LENGTH_SHORT).show()

    }

    private  fun shareResult() {
        // Step 1: Create Share itent
        val intent = Intent(Intent.ACTION_SEND).setType("image/*")

        // Step 2: Get Bitmap from your imageView
        val bitmap = mView.result_image.drawable.toBitmap()


        // Step 3: Compress image
        val bytes = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes)

        // Step 4: Save image & get path of it
        val path = MediaStore.Images.Media.insertImage(requireContext().contentResolver, bitmap, "tempimage", null)

        // Step 5: Get URI of saved image
        val uri = Uri.parse(path)

        // Step 6: Put Uri as extra to share intent
        intent.putExtra(Intent.EXTRA_STREAM, uri)
        intent.putExtra(Intent.EXTRA_TEXT,"I made this with Infused Paint!");

        // Step 7: Start/Launch Share intent
        startActivity(intent)

    }

    private fun getUser(email: String) {
        RetrofitClient.instance.getUser(
            email,
        ).enqueue(object : Callback<UserData?> {
            override fun onFailure(call: Call<UserData?>, t: Throwable) {
                Toast.makeText(activity, t.message, Toast.LENGTH_LONG).show()
            }

            override fun onResponse(call: Call<UserData?>, response: Response<UserData?>) =
                if (response.code() == 200) {
                    userData = response.body()!!
                } else {
                }

        })
    }

}