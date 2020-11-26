package com.mobcom.paintly

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_home.view.*
import org.jetbrains.anko.support.v4.runOnUiThread
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeFragment : Fragment(){
    lateinit var mView: View

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mView = inflater.inflate(R.layout.activity_home, container, false)

        RetrofitClient.instance.getStyles(
        ).enqueue(object : Callback<List<StyleData>> {
            override fun onFailure(call: Call<List<StyleData>>, t: Throwable) {
                Toast.makeText(activity, t.message, Toast.LENGTH_SHORT).show()
            }

            override fun onResponse(call: Call<List<StyleData>>, response: Response<List<StyleData>>) {
                if (response.code() == 200) {
                    loadingStyles(response.body()!!)
                } else {
                }
            }
        })

        return mView
    }

    override fun onResume() {
        super.onResume()
        // Set title bar
        (activity as BottomNavActivity)
            .setActionBarTitle("Pick Your Style")
    }

    private fun loadingStyles(styleData: List<StyleData>) {
        mView.progress_bar_style.visibility = View.VISIBLE
        Thread {
            val styleAdapter = StyleAdapter( // deklarasi styleAdapter yang bentuknya StyleAdapter()
                this.requireContext(),
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
            runOnUiThread {
                mView.rv_style.adapter =
                    styleAdapter // adapter dari rv nya di set jadi styleAdapter (dideclare di atas)
                mView.rv_style.layoutManager = LinearLayoutManager(context)
                mView.progress_bar_style.visibility = View.GONE
            }
        }.start() // mulai ehehehehehe
    }

}


