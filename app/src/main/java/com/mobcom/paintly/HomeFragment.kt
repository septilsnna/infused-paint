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


class HomeFragment : Fragment(){
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

    //mikum
  val API_KEY = "xHjbgEGgt61mlW9uLxpQZ5WehhJcXm8X5LdyGXR0"
    val ACCESS_KEY = "AKIA3XE3HF7S3QE6DBPY"
    val SECRET_KEY = "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"

    var deepArtEffectsClient: DeepArtEffectsClient? = null

    lateinit var mView: View

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mView = inflater.inflate(R.layout.activity_home, container, false)

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

        loadingStyles()

        return mView
    }

    override fun onResume() {
        super.onResume()
        // Set title bar
        (activity as BottomNavActivity)
            .setActionBarTitle("Pick Your Style")
    }

    private fun loadingStyles() {
        Thread {
            val styles =
                deepArtEffectsClient!!.stylesGet() // semua style yang didapat dari api, di simpan di variable styles yang bentuknya Style (package com.deeparteffects.sdk.android.model)
            val styleAdapter = StyleAdapter( // deklarasi styleAdapter yang bentuknya StyleAdapter()
                this.requireContext(),
                styles,
                object : StyleAdapter.ClickListener {
                    override fun onClick(styleId: String?) {
                        val arguments = Bundle()
                        arguments.putString("styleId", styleId)
                        val fragment = UploadSheetFragment()
                        fragment.arguments = arguments
                        fragment.show(childFragmentManager, "UploadSheetDialog")
                    }
                }
            )
            runOnUiThread {
                mView.rv_style.adapter =
                    styleAdapter // adapter dari rv nya di set jadi styleAdapter (dideclare di atas)
                mView.rv_style.layoutManager = LinearLayoutManager(context)
            }
        }.start() // mulai ehehehehehe
    }

}


