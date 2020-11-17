package com.mobcom.paintly

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.activity_home.view.*

class HomeFragment : Fragment(), StyleAdapter.OnItemClickListener {
    private val styleList = generateDummyList(12)
    private val adapter = StyleAdapter(styleList, this)
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

        mView.rv_style.adapter = adapter
        mView.rv_style.layoutManager = LinearLayoutManager(activity)
        mView.rv_style.setHasFixedSize(true)

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

    override fun onItemClick(position: Int) {
//        Toast.makeText(activity, "Item $position clicked", Toast.LENGTH_SHORT).show()
        UploadSheetFragment().show(this.childFragmentManager, "UploadSheetDialog")
        adapter.notifyItemChanged(position)
    }
}


