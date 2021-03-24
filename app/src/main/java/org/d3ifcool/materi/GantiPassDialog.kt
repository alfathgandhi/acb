package org.d3ifcool.materi

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.DialogFragment


class GantiPassDialog() : DialogFragment() {
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val inflater = LayoutInflater.from(requireContext())
        val view = inflater.inflate(R.layout.ganti_pass_dialog, null, false)

        val builder = AlertDialog.Builder(requireContext())
        with(builder) {
            setView(view)
            setPositiveButton("Simpan") { _, _ ->
                val kumpPass = getData(view) ?: return@setPositiveButton
                val listener = requireActivity() as DialogListener

                listener.Lister(kumpPass)

            }
            setNegativeButton("Batal") { _, _ ->
                dismiss()

            }
        }


        return builder.create()
    }



    private fun getData(view: View):List<String>?{

        val pass0 = view.findViewById<EditText>(R.id.et_isi)
        val pass = view.findViewById<EditText>(R.id.et_ket)
        val pass1 = view.findViewById<EditText>(R.id.et_basic)

        val passlama = pass0.text.toString()
        val passbaru = pass.text.toString()
        val passbaru1 = pass1.text.toString()
        if (pass0.text.isEmpty() || pass1.text.isEmpty() || pass.text.isEmpty()) {
            showMessage(R.string.field)

            return null


        } else if (!pass.text.toString().equals(pass1.text.toString())) {
            showMessage(R.string.tidaksama)

            return null

        } else {
            var array: List<String>?=null

            array = listOf(passlama,passbaru,passbaru1)

            return array

        }

    }












    private fun showMessage(messageResId: Int) {
        val toast = Toast.makeText(context, messageResId, Toast.LENGTH_LONG)
        toast.setGravity(Gravity.CENTER, 0, 0)
        toast.show()
    }


    interface DialogListener{
        fun Lister(list:List<String>){

        }
    }

}
