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
import kotlinx.android.synthetic.main.fragment_edit_profile.*
import maes.tech.intentanim.CustomIntent
import org.jetbrains.anko.support.v4.runOnUiThread
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*

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

            val date = sharedPreferences?.getString("date", "")
            val calendar = Calendar.getInstance()
            val dateFormat = SimpleDateFormat("MM/dd/yyyy")
            val date_today = dateFormat.format(calendar.time)

            if (date != date_today) {
                val email = sharedPreferences?.getString("email", "")!!
                updateQuota(email)
            }

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
            .setActionBarTitle("Pick Your Style (quota/day: $quota)")
    }

    private fun loadingStyles(styleData: List<StyleData>) {
            val styleAdapter = StyleAdapter( // deklarasi styleAdapter yang bentuknya StyleAdapter()
                mView.context,
                styleData,
                object : StyleAdapter.ClickListener {
                    override fun onClick(styleId: String?) {
                        if (quota.toInt() > 0) {
                            val arguments = Bundle()
                            arguments.putString("styleId", styleId)
                            val fragment = UploadSheetFragment()
                            fragment.arguments = arguments
                            fragment.show(childFragmentManager, "UploadSheetDialog")
                        } else {
                            Toast.makeText(activity, "Ups, you reached your limit today, try again tomorrow", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            )
                mView.rv_style.adapter =
                    styleAdapter // adapter dari rv nya di set jadi styleAdapter (dideclare di atas)
                mView.rv_style.layoutManager = LinearLayoutManager(context)
                mView.progress_bar_style.visibility = View.GONE
    }

    private fun updateQuota(email: String) {
        RetrofitClient.instance.updateQuota(
            email,
            5
        ).enqueue(object : Callback<UserData?> {
            override fun onFailure(call: Call<UserData?>, t: Throwable) {
            }

            override fun onResponse(call: Call<UserData?>, response: Response<UserData?>) {
                val sharedPreferences = activity?.getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE)
                val editor = sharedPreferences?.edit()

                val calendar = Calendar.getInstance()
                val dateFormat = SimpleDateFormat("MM/dd/yyyy")
                val date = dateFormat.format(calendar.time)

                editor?.putString("date", date)
                editor?.putInt(QUOTA, 5)
                editor?.apply()
            }

        })
    }
}


