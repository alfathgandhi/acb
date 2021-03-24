package org.d3ifcool.materi

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import androidx.fragment.app.DialogFragment

class AboutDialog :DialogFragment(){

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val inflater = LayoutInflater.from(requireContext())
        val view = inflater.inflate(R.layout.about_dialog, null, false)
        val builder = AlertDialog.Builder(requireContext())

        with(builder){
            setView(view)

        }

    return builder.create()
    }
}