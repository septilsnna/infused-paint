package com.mobcom.paintly

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.fragment_result.view.*

class ResultFragment : Fragment() {
    lateinit var mView: View
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mView = inflater.inflate(R.layout.fragment_result, container, false)

        val arguments = arguments
        val urlResult = arguments?.getString("urlResult")

        Glide.with(mView.context).load(urlResult).centerCrop().into(
                            mView.result_image)

        return mView
    }
}