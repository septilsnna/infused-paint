package com.mobcom.infusedpaint

import android.app.Dialog
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatDialogFragment


class AboutAppDialog : AppCompatDialogFragment() {
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = AlertDialog.Builder(requireActivity())

        val inflater = requireActivity().layoutInflater
        val view: View = inflater.inflate(R.layout.fragment_about_app, null)

        builder.setView(view).setTitle("About App")

        return builder.create()

    }
}
