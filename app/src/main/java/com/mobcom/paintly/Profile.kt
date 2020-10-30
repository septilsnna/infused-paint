package com.mobcom.paintly

import android.os.Bundle
import android.view.*
import android.widget.Button
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.activity_profile.view.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class Profile : Fragment(){
    lateinit var mView: View
    private lateinit var about_app_button: Button

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mView = inflater.inflate(R.layout.activity_profile, container, false)
        getUser("septilusianna19@gmail.com")

        about_app_button = mView.button_about
        about_app_button.setOnClickListener(){
            AboutAppDialog().show(this.childFragmentManager, "About App")
//            aboutApp.showsDialog
//            listener?.onAboutApp(aboutApp)
        }

        return mView
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.logout, menu)
        return super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.getItemId()) {
            R.id.button_logout -> logout()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun getUser(email: String) {
        RetrofitClient.instance.getUser(
            email,
        ).enqueue(object : Callback<UserGet?> {
            override fun onFailure(call: Call<UserGet?>, t: Throwable) {
                //            Toast.makeText(this@Profile, t.message, Toast.LENGTH_SHORT).show()
            }

            override fun onResponse(call: Call<UserGet?>, response: Response<UserGet?>) {
                if (response.code() == 200) {
                    mView.name.setText(response.body()?.name)
                    mView.email.setText(response.body()?.email)
                    mView.created_at.setText(
                        "Joined At: " + response.body()?.created_at?.split(" ")?.get(
                            0
                        )
                    )
                    mView.number_artwork.setText("Number of Artwork: " + response.body()?.edit_freq)
                } else {
                    //                Toast.makeText(this@Profile, "Failed to load user", Toast.LENGTH_SHORT)
                    //                    .show()
                }
            }

        })
    }

    private fun logout() {

    }

//    private fun about_app() {
//        val exampleDialog = AboutAppDialog()
////        UploadSheetFragment.show(supportFragmentManager, "UploadSheetDialog")
//    }

}