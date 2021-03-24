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
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.DialogFragment
import com.bumptech.glide.Glide

class TambahTypeDialog:DialogFragment(){
    private val REQUEST_IMAGE_CAPTURE =100
lateinit var imageUri:Uri
    var boolean = false

    lateinit var viewe: View


    private val PERMISSION_CODE =1001




        override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

            val inflater = LayoutInflater.from(requireContext())

            viewe = inflater.inflate(R.layout.tambah_type, null, false)


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
            with(builder) {
                setView(viewe)
                setPositiveButton("Simpan") { _, _ ->
                    val kumpPass = getData(viewe) ?: return@setPositiveButton
                    val listener = requireActivity() as DialogListener2

                    listener.Lister2(kumpPass)

                }
                setNegativeButton("Batal") { _, _ ->
                    dismiss()

                }
            }


            return builder.create()
        }



        private fun getData(view: View):List<String>?{

            val namaType = view.findViewById<EditText>(R.id.et_isi).text.toString()
            var gambar : String? = null
          if(boolean==true){
              gambar = imageUri.toString()


          }



            if(namaType.isEmpty()||boolean==false){
                showMessage(R.string.field)

                return null

            }else{
                return  listOf(namaType,gambar!!)
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





        }
    }

    private fun showMessage(messageResId: Int) {
            val toast = Toast.makeText(context, messageResId, Toast.LENGTH_LONG)
            toast.setGravity(Gravity.CENTER, 0, 0)
            toast.show()
        }


        interface DialogListener2{
            fun Lister2(list:List<String>){

            }
        }

    }

