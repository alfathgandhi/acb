package org.d3ifcool.materi

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.app.Dialog
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.widget.*
import androidx.core.content.ContextCompat
import androidx.fragment.app.DialogFragment
import com.bumptech.glide.Glide
import java.util.*

import kotlin.text.StringBuilder

class TambahPartDialog(adapternya:Array<String>):DialogFragment(){
    private val REQUEST_IMAGE_CAPTURE =100
    lateinit var imageUri:Uri
    var boolean = false
    val x = adapternya
    val arraylist= Arrays.asList(*x)
    val array2 = arrayListOf<String>()

    companion object{
        lateinit var alertDialog:AlertDialog

    }
    lateinit var viewe: View


    private val PERMISSION_CODE =1001

    var arrayboolean:BooleanArray = BooleanArray(x.size)




    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        val inflater = LayoutInflater.from(requireContext())

        viewe = inflater.inflate(R.layout.tambah_part_dialog, null, false)
        val autocomplete = viewe.findViewById<EditText>(R.id.et_part_type)

        for(i in 0 until x.size-1){

            arrayboolean[i] = false

        }

        autocomplete.setOnFocusChangeListener { view, b ->
            if(view.isFocused){
                val yaya : AlertDialog.Builder = AlertDialog.Builder(context)

                yaya.setTitle("List Type")


                yaya.setCancelable(false)








                yaya.setMultiChoiceItems(x,arrayboolean) {dialog, which, ischecked ->

                    if(ischecked){

                        arrayboolean[which] = ischecked
                        val current = arraylist[which]

                        array2.add(current)





                    }else{

                        array2.remove(arraylist[which])

                    }

                }



                yaya.setPositiveButton("OK"){dialog, pos->
                    val stringBuilder = StringBuilder()

                    for(y in 0 until array2.size){
                        stringBuilder.append(array2.get(y))
                        if(y != array2.size-1){
                            stringBuilder.append(", ")
                        }


                    }

                    autocomplete.setText(stringBuilder, TextView.BufferType.EDITABLE)


                }

                yaya.create().show()





            }

        }


        autocomplete.setOnClickListener{
            val yaya : AlertDialog.Builder = AlertDialog.Builder(context)

            yaya.setTitle("List Type")


            yaya.setCancelable(false)








            yaya.setMultiChoiceItems(x,arrayboolean) {dialog, which, ischecked ->

                if(ischecked){

                    arrayboolean[which] = ischecked
                    val current = arraylist[which]

                    array2.add(current)





                }else{

                    array2.remove(arraylist[which])

                }

            }



            yaya.setPositiveButton("OK"){dialog, pos->
                val stringBuilder = StringBuilder()

                for(y in 0 until array2.size){
                    stringBuilder.append(array2.get(y))
                    if(y != array2.size-1){
                        stringBuilder.append(", ")
                    }


                }

                autocomplete.setText(stringBuilder, TextView.BufferType.EDITABLE)


            }

            yaya.create().show()





        }


        viewe.findViewById<ImageView>(R.id.addFhoto).setOnClickListener {

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (ContextCompat.checkSelfPermission(
                        this.requireContext(),
                        Manifest.permission.READ_EXTERNAL_STORAGE
                    ) == PackageManager.PERMISSION_DENIED
                ) {
                    val permission = arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE)
                    requestPermissions(permission, PERMISSION_CODE)
                } else {

                    takeImage()



                }


            }
        }




        val builder = AlertDialog.Builder(requireContext())
        builder.setView(viewe)
        builder.setPositiveButton("Simpan",null)
        builder.setNegativeButton("Batal",null)

        alertDialog = builder.create()

        alertDialog.show()




        val watcher = object :  TextWatcher{
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                val plane = viewe.findViewById<EditText>(R.id.et_isi).text.toString().trim()
                val namaPart = viewe.findViewById<EditText>(R.id.et_namaPart).text.toString().trim()


                val line = viewe.findViewById<EditText>(R.id.et_ket).text.toString().trim()
                val namaType = viewe.findViewById<EditText>(R.id.et_part_type).text.toString().trim()



               alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).isEnabled = !(plane.isEmpty()||boolean==false||namaPart.isEmpty()||line.isEmpty()||namaType.isEmpty())


            }

            override fun afterTextChanged(p0: Editable?) {

            }
        }

       alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).isEnabled = false

        viewe.findViewById<EditText>(R.id.et_isi).addTextChangedListener(watcher)
         viewe.findViewById<EditText>(R.id.et_namaPart).addTextChangedListener(watcher)


          viewe.findViewById<EditText>(R.id.et_ket).addTextChangedListener(watcher)
          viewe.findViewById<EditText>(R.id.et_part_type).addTextChangedListener(watcher)



        alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener {

            val kumpPass = getData(viewe)

                    val listener = requireActivity() as DialogListener3

                    listener.Lister3(kumpPass, array2)
            alertDialog.dismiss()




        }

        alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE).setOnClickListener {
            alertDialog.dismiss()
        }



//        with(builder) {
//
//            setView(viewe)


//            setPositiveButton("Simpan") { _, _ ->
//                val kumpPass = getData(viewe) ?: return@setPositiveButton
//
//                if(kumpPass.isEmpty()){
//                    viewe.findViewById<EditText>(R.id.et_plane).requestFocus()
//                }else{
//
//
//                }
//

//            //}
//            setNegativeButton("Batal") { _, _ ->
//                dismiss()
//
//            }
//        }


        return alertDialog
    }



    private fun getData(view: View):List<String>?{

        val isi = view.findViewById<EditText>(R.id.et_isi).text.toString()
        val namaPart = view.findViewById<EditText>(R.id.et_namaPart).text.toString()
       // val basic = view.findViewById<EditText>(R.id.et_basic).text.toString()

        val keterangan = view.findViewById<EditText>(R.id.et_ket).text.toString()
        val namaType = view.findViewById<EditText>(R.id.et_part_type).text.toString()








        var gambar : String? = null
        if(boolean==true){
            gambar = imageUri.toString()


        }



        if(isi.isEmpty()||boolean==false||namaPart.isEmpty()||keterangan.isEmpty()||namaType.isEmpty()){
            showMessage(R.string.field)

            return null

        }else{
            return  listOf(isi,gambar!!,namaPart,keterangan)
        }







    }
    fun takeImage(){


        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, REQUEST_IMAGE_CAPTURE)




    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)


        if(requestCode == REQUEST_IMAGE_CAPTURE && resultCode == Activity.RESULT_OK){



            imageUri = data?.data!!
            Glide.with(this)
                .load(imageUri).into(viewe.findViewById(R.id.imageCircle))

            boolean = true


            val plane = viewe.findViewById<EditText>(R.id.et_isi).text.toString().trim()
            val namaPart = viewe.findViewById<EditText>(R.id.et_namaPart).text.toString().trim()


            val line = viewe.findViewById<EditText>(R.id.et_ket).text.toString().trim()
            val namaType = viewe.findViewById<EditText>(R.id.et_part_type).text.toString().trim()

           alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).isEnabled = !(plane.isEmpty()||boolean==false||namaPart.isEmpty()||line.isEmpty()||namaType.isEmpty())










        }
    }

    private fun showMessage(messageResId: Int) {
        val toast = Toast.makeText(context, messageResId, Toast.LENGTH_LONG)
        toast.setGravity(Gravity.CENTER, 0, 0)
        toast.show()
    }


    interface DialogListener3{
        fun Lister3(list:List<String>?, namaType:List<String>?){

        }
    }

}

