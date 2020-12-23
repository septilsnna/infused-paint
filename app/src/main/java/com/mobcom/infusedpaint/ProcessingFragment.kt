package com.mobcom.infusedpaint

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.*
import android.graphics.Paint.ANTI_ALIAS_FLAG
import android.graphics.Paint.DITHER_FLAG
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Base64
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.ColorInt
import androidx.core.graphics.drawable.toBitmap
import androidx.fragment.app.Fragment
import com.amazonaws.auth.AWSCredentials
import com.amazonaws.auth.AWSCredentialsProvider
import com.amazonaws.auth.BasicAWSCredentials
import com.amazonaws.mobileconnectors.apigateway.ApiClientException
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
    lateinit var deepArtEffectsClient: DeepArtEffectsClient
    lateinit var mView: View
    lateinit var styleId: String
    lateinit var userData: UserData
    lateinit var imageBitmap: Bitmap
    lateinit var sharedPreferences: SharedPreferences
    lateinit var email: String
    lateinit var user_id: String

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mView = inflater.inflate(R.layout.fragment_processing, container, false)

        val arguments = arguments
        styleId = arguments?.getString("STYLE_ID")!!
        val byteArray = arguments.getByteArray("IMAGE")
        imageBitmap = BitmapFactory.decodeByteArray(byteArray, 0, byteArray!!.size)

        // load data user
        sharedPreferences = activity?.getSharedPreferences("sharedPrefs", Context.MODE_PRIVATE)!!
        email = sharedPreferences.getString("email", "").toString()
        user_id = sharedPreferences.getString("USER_ID", "").toString()
        getUser(email)

//        get key aws
        RetrofitClient.instance.getUser(
            "infusedpaint",
        ).enqueue(object : Callback<UserData?> {
            override fun onFailure(call: Call<UserData?>, t: Throwable) {
                Toast.makeText(activity, t.message, Toast.LENGTH_LONG).show()
            }

            override fun onResponse(call: Call<UserData?>, response: Response<UserData?>) =
                if (response.code() == 200) {
//                  AWS untuk akses api nya deepart
                    val factory = ApiClientFactory()
                        .apiKey(response.body()?.username)
                        .credentialsProvider(object : AWSCredentialsProvider {
                            override fun getCredentials(): AWSCredentials {
                                return BasicAWSCredentials(response.body()?.password, response.body()?.name)
                            }

                            override fun refresh() {}
                        }).region(Regions.EU_WEST_1.getName())
                    deepArtEffectsClient = factory.build(DeepArtEffectsClient::class.java)
//                  AWS untuk akses api nya deepart

                    uploadImage(styleId, imageBitmap, deepArtEffectsClient)
                } else {
                }

        })

        mView.save_result.setOnClickListener {
            Toast.makeText(activity, "saving image...", Toast.LENGTH_SHORT).show()
            saveResult()
        }

        mView.share_result.setOnClickListener {
            shareResult()
        }

        return mView
    }

    private fun uploadImage(styleId: String, imageBitmap: Bitmap, deepArtEffectsClient: DeepArtEffectsClient) {
        Thread {
            try {
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
                                    Glide.with(mView.context).load(result.url)
                                        .into(mView.result_image)

                                    // logging
                                    logging(user_id, "Mengedit gambar")
                                }
                            }
                        } catch (e: ApiClientException) {
                            Toast.makeText(
                                activity,
                                "We are sorry, our server is busy :( Please comeback in 24 hours",
                                Toast.LENGTH_LONG
                            ).show()
                            activity?.finish()
                            CustomIntent.customType(activity, "fadein-to-fadeout")
                        } catch (e: IllegalStateException) {
                        } catch (e: NullPointerException) {
                        }
                    }
                }, 2500, 36000000)
                val quota = sharedPreferences.getInt("quota", 0).minus(1)
                val editor = sharedPreferences.edit()
                editor?.putInt("quota", quota)
                editor?.apply()
                updateQuota(email, quota)
            } catch (e: ApiClientException) {
                runOnUiThread {
                    if (e.serviceName == "DeepArtEffectsClient") {
                        Toast.makeText(
                            activity,
                            "We are sorry, our server is busy :( Please comeback in 24 hours",
                            Toast.LENGTH_LONG
                        ).show()
                        activity?.finish()
                        CustomIntent.customType(activity, "fadein-to-fadeout")
                    }
                }
            } catch (e: java.net.ConnectException) {
                runOnUiThread {
                    Toast.makeText(
                        activity,
                        "Phone not connected to the internet",
                        Toast.LENGTH_LONG
                    ).show()
                    activity?.finish()
                    CustomIntent.customType(activity, "fadein-to-fadeout")
                }
            }
        }.start()
    }

    fun addWatermark(
        bitmap: Bitmap,
        watermarkText: String,
        options: WatermarkOptions = WatermarkOptions()
    ): Bitmap {
        val result = bitmap.copy(bitmap.config, true)
        val canvas = Canvas(result)
        val paint = Paint(ANTI_ALIAS_FLAG or DITHER_FLAG)
        paint.textAlign = when (options.corner) {
            Corner.TOP_LEFT,
            Corner.BOTTOM_LEFT -> Paint.Align.LEFT
            Corner.TOP_RIGHT,
            Corner.BOTTOM_RIGHT -> Paint.Align.RIGHT
        }
        val textSize = result.width * options.textSizeToWidthRatio
        paint.textSize = textSize
        paint.color = options.textColor
        if (options.shadowColor != null) {
            paint.setShadowLayer(textSize / 2, 0f, 0f, options.shadowColor)
        }
        if (options.typeface != null) {
            paint.typeface = options.typeface
        }
        val padding = result.width * options.paddingToWidthRatio
        val coordinates =
            calculateCoordinates(
                watermarkText,
                paint,
                options,
                canvas.width,
                canvas.height,
                padding
            )
        canvas.drawText(watermarkText, coordinates.x, coordinates.y, paint)
        return result
    }

    private fun calculateCoordinates(
        watermarkText: String,
        paint: Paint,
        options: WatermarkOptions,
        width: Int,
        height: Int,
        padding: Float
    ): PointF {
        val x = when (options.corner) {
            Corner.TOP_LEFT,
            Corner.BOTTOM_LEFT -> {
                padding
            }
            Corner.TOP_RIGHT,
            Corner.BOTTOM_RIGHT -> {
                width - padding
            }
        }
        val y = when (options.corner) {
            Corner.BOTTOM_LEFT,
            Corner.BOTTOM_RIGHT -> {
                height - padding
            }
            Corner.TOP_LEFT,
            Corner.TOP_RIGHT -> {
                val bounds = Rect()
                paint.getTextBounds(watermarkText, 0, watermarkText.length, bounds)
                val textHeight = bounds.height()
                textHeight + padding

            }
        }
        return PointF(x, y)
    }

    enum class Corner {
        TOP_LEFT,
        TOP_RIGHT,
        BOTTOM_LEFT,
        BOTTOM_RIGHT,
    }

    data class WatermarkOptions(
        val corner: Corner = Corner.BOTTOM_RIGHT,
        val textSizeToWidthRatio: Float = 0.04f,
        val paddingToWidthRatio: Float = 0.03f,
        @ColorInt val textColor: Int = Color.WHITE,
        @ColorInt val shadowColor: Int? = Color.BLACK,
        val typeface: Typeface? = null
    )

    private fun convertBitmapToBase64(bitmap: Bitmap?): String? {
        val stream = ByteArrayOutputStream()
        bitmap?.compress(Bitmap.CompressFormat.JPEG, 100, stream)
        val byteArray = stream.toByteArray()
        return Base64.encodeToString(byteArray, Base64.DEFAULT)
    }

    private fun saveResult() {
        val a = mView.result_image.drawable.toBitmap()
        val imageResult = addWatermark(a, "created on Infused Paint")


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
            "result_image" + "_" + userData.edit_freq!!.plus(1) + ".jpg",
            "Image of " + "result_image" + "_" + userData.edit_freq!!.plus(1) + ".jpg"

        )

        Uri.parse(savedImageURL)

        mView.save_result.isEnabled = false
        mView.save_result.isClickable = false
        Toast.makeText(activity, "Save Success!", Toast.LENGTH_SHORT).show()

        // logging
        logging(user_id, "Menyimpan hasil lukisan")

    }

    private  fun shareResult() {
        // Step 1: Create Share itent
        val intent = Intent(Intent.ACTION_SEND).setType("image/*")

        // Step 2: Get Bitmap from your imageView
        val a = mView.result_image.drawable.toBitmap()
        val bitmap = addWatermark(a, "created on Infused Paint")

        // Step 3: Compress image
        val bytes = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes)

        // Step 4: Save image & get path of it
        val path = MediaStore.Images.Media.insertImage(
            activity?.contentResolver,
            bitmap,
            "tempimage",
            null
        )

        // Step 5: Get URI of saved image
        val uri = Uri.parse(path)

        // Step 6: Put Uri as extra to share intent
        intent.putExtra(Intent.EXTRA_STREAM, uri)
        intent.putExtra(Intent.EXTRA_TEXT, "I made this with Infused Paint!");

        // logging
        logging(user_id, "Membagikan hasil lukisan")

        // add share freq to infused paint api
        RetrofitClient.instance.updateUserShareFreq(
            userData.email!!,
            userData.share_freq!!.plus(1)
        ).enqueue(object : Callback<UserData?> {
            override fun onFailure(call: Call<UserData?>, t: Throwable) {
                Toast.makeText(activity, t.message, Toast.LENGTH_LONG).show()
            }

            override fun onResponse(call: Call<UserData?>, response: Response<UserData?>) =
                if (response.code() == 200) {
                } else {
                }
        })

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

    private fun updateQuota(email: String, quota: Int) {
        RetrofitClient.instance.updateQuota(
            email,
            quota
        ).enqueue(object : Callback<UserData?> {
            override fun onFailure(call: Call<UserData?>, t: Throwable) {
            }

            override fun onResponse(call: Call<UserData?>, response: Response<UserData?>) {
            }

        })
    }

    private fun logging(user_id: String, action: String){
        RetrofitClient.instance.createLog(
            user_id, action
        ).enqueue(object : Callback<LogData> {
            override fun onFailure(call: Call<LogData?>, t: Throwable) {
//                Toast.makeText(activity, "Failed to register, please check your connection", Toast.LENGTH_SHORT).show()
            }
            override fun onResponse(call: Call<LogData?>, response: Response<LogData?>) {
                if (response.code() == 200) {
                } else {
                }
            }
        })
    }

}