package com.mobcom.paintly

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.amazonaws.auth.AWSCredentials
import com.amazonaws.auth.AWSCredentialsProvider
import com.amazonaws.auth.BasicAWSCredentials
import com.amazonaws.mobileconnectors.apigateway.ApiClientFactory
import com.amazonaws.regions.Regions
import com.deeparteffects.sdk.android.DeepArtEffectsClient
import kotlinx.android.synthetic.main.activity_home.view.*
import org.jetbrains.anko.support.v4.runOnUiThread
import java.util.*


class HomeFragment : Fragment()
    , StyleAdapter.ClickListener
{
    private val API_KEY = "1N9PVfY0se8IHx5Pb8ekI5T6bhLdhNyZazBCMwgi"
    private val ACCESS_KEY = "AKIA3XE3HF7SZPDDBT6B"
    private val SECRET_KEY = "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"
    private var deepArtEffectsClient: DeepArtEffectsClient? = null

//    private val styleList = generateDummyList(12)
//    private val adapter = StyleAdapter(styleList, this)
    lateinit var mView: View
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mView = inflater.inflate(R.layout.activity_home, container, false)
//        mView.btn_starryNight.setOnClickListener {
//
//            UploadSheetFragment.show(this.childFragmentManager, "UploadSheetDialog")
//        }

        // AWS untuk akses api nya deepart
        val factory = ApiClientFactory()
            .apiKey(API_KEY)
            .credentialsProvider(object : AWSCredentialsProvider {
                override fun getCredentials(): AWSCredentials {
                    return BasicAWSCredentials(ACCESS_KEY, SECRET_KEY)
                }

                override fun refresh() {}
            }).region(Regions.EU_WEST_1.getName())
        deepArtEffectsClient = factory.build(DeepArtEffectsClient::class.java)
        // AWS untuk akses api nya deepart

//        mView.rv_style.layoutManager = LinearLayoutManager(activity)
//            GridLayoutManager(this, 4) // layout manager rv nya di set jadi grid dan dibagi 4 kolom
//        mView.rv_style.itemAnimator = DefaultItemAnimator() // animasi item rv nya di set default

        loadingStyles()

//        val styles = deepArtEffectsClient?.stylesGet()
//        mView.rv_style.adapter = StyleAdapter(styles!!, this)
//        mView.rv_style.layoutManager = LinearLayoutManager(context)
//        mView.rv_style.setHasFixedSize(true)

        return mView
    }

    override fun onResume() {
        super.onResume()
        // Set title bar
        (activity as BottomNavActivity)
            .setActionBarTitle("Pick Your Style")
    }

    //handle result of picked image
//    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
//        if (resultCode == Activity.RESULT_OK && requestCode == IMAGE_PICK_CODE) {
//            val selectedImage : Uri? = data?.data
//
//
//            val image_stream =
//                selectedImage?.let { context?.getContentResolver()?.openInputStream(it) };
//            val getBitmap = BitmapFactory.decodeStream(image_stream);
//            img_result.setImageBitmap(getBitmap)
//
//        }
//    }

    private fun loadingStyles() {
//        mStatusText.setText(R.string.loading) // status text di set yang di layout xml
        Thread {
            val styles =
                deepArtEffectsClient!!.stylesGet() // semua style yang didapat dari api, di simpan di variable styles yang bentuknya Style (package com.deeparteffects.sdk.android.model)
            val styleAdapter = StyleAdapter( // deklarasi styleAdapter yang bentuknya StyleAdapter()
                // deklarasi styleAdapter yang bentuknya StyleAdapter()
                this.requireContext(),
                styles,
                object : StyleAdapter.ClickListener {
                    override fun onClick(styleId: String?) {
//                        if (!isProcessing) {
//                            if (mImageBitmap != null) {
//                                Log.d(
//                                    MainActivity.TAG,
//                                    String.format("Style with ID %s clicked.", styleId)
//                                )
//                                isProcessing = true
//                                mProgressbarView.setVisibility(View.VISIBLE)
//                                uploadImage(styleId)
//                            } else {
//                                Toast.makeText(
//                                    mActivity, "Please choose a picture first",
//                                    Toast.LENGTH_SHORT
//                                ).show()
//                            }
//                        }
                    }
                }
            )
            runOnUiThread(){
                mView.rv_style.adapter =
                    styleAdapter // adapter dari rv nya di set jadi styleAdapter (dideclare di atas)
                mView.rv_style.layoutManager = LinearLayoutManager(context)
                //                mProgressbarView.setVisibility(View.GONE) // visibilitas progressive barnya di set gone (ilang)
                //                mStatusText.setText("") // statusnya di set jadi kosong ("")
            }
        }.start() // mulai ehehehehehe

    }

    private fun generateDummyList(size: Int): List<StyleItem> {
        val list = ArrayList<StyleItem>()
        for (i in 0 until size) {
            val drawable = when (i % 3) {
                0 -> R.drawable.starry_night
                1 -> R.drawable.looking_for
                else -> R.drawable.the_great
            }

            val text1 = when (i % 3) {
                0 -> "Starry Night"
                1 -> "Looking For"
                else -> "The Great"
            }

            val text2 = when (i % 3) {
                0 -> "Starry Night"
                1 -> "Looking For"
                else -> "The Great"
            }

            val item = StyleItem(drawable, text1, text2)
            list += item
        }
        return list
    }

    override fun onClick(styleId: String?) {
        TODO("Not yet implemented")
    }

//    fun onItemClick(position: Int) {
////        Toast.makeText(activity, "Item $position clicked", Toast.LENGTH_SHORT).show()
//        adapter.notifyItemChanged(position)
//        UploadSheetFragment().show(this.childFragmentManager, "UploadSheetDialog")
//    }
}


