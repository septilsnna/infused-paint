package com.mobcom.paintly

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.amazonaws.mobileconnectors.apigateway.ApiClientException
import kotlinx.android.synthetic.main.activity_home.view.*
import maes.tech.intentanim.CustomIntent
import org.jetbrains.anko.support.v4.runOnUiThread
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeFragment : Fragment(){
    lateinit var mView: View
    lateinit var quota: String

    val SHARED_PREFS = "sharedPrefs"
    val QUOTA = "quota"

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mView = inflater.inflate(R.layout.activity_home, container, false)

        try {
            val sharedPreferences =
                activity?.getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE)
            quota = sharedPreferences?.getInt(QUOTA, 0)!!.toInt().toString()

            RetrofitClient.instance.getStyles(
            ).enqueue(object : Callback<List<StyleData>> {
                override fun onFailure(call: Call<List<StyleData>>, t: Throwable) {
                    mView.failed_styles.visibility = View.VISIBLE
                }

                override fun onResponse(
                    call: Call<List<StyleData>>,
                    response: Response<List<StyleData>>
                ) {
                    if (response.code() == 200) {
                        loadingStyles(response.body()!!)
                    } else {
                    }
                }
            })
        } catch (e: ApiClientException) {
            val intent = Intent(activity, BottomNavActivity::class.java)
            startActivity(intent)
            activity?.finish()
            CustomIntent.customType(activity, "fadein-to-fadeout")
        }

        return mView
    }

    override fun onResume() {
        super.onResume()
        // Set title bar
        (activity as BottomNavActivity)
            .setActionBarTitle("Pick Your Style")
//            .setActionBarTitle("Pick Your Style (quota/day: $quota)")
    }

    private fun loadingStyles(styleData: List<StyleData>) {
            val styleAdapter = StyleAdapter( // deklarasi styleAdapter yang bentuknya StyleAdapter()
                mView.context,
                styleData,
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
                mView.rv_style.adapter =
                    styleAdapter // adapter dari rv nya di set jadi styleAdapter (dideclare di atas)
                mView.rv_style.layoutManager = LinearLayoutManager(context)
                mView.progress_bar_style.visibility = View.GONE
    }

}


