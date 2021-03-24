package org.d3ifcool.materi


import android.app.ProgressDialog
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.MenuItem
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.net.toUri
import androidx.core.view.isVisible
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageTask
import com.google.firebase.storage.UploadTask
import kotlinx.android.synthetic.main.activity_main.*
import java.io.ByteArrayOutputStream


class MainActivity : AppCompatActivity(), GantiPassDialog.DialogListener, TambahTypeDialog.DialogListener2 ,TambahPartDialog.DialogListener3, Communicator {


    var databas = FirebaseDatabase.getInstance().reference
    private var session: Session?= null
    private lateinit var namauser:String
    var auth= FirebaseAuth.getInstance().currentUser
    var firebase= FirebaseDatabase.getInstance()
    var imageUri:Uri? = null
    var imageUri1:Uri? = null
    var arrayAdapter:ArrayAdapter<String>?= null
    var arrayType:Array<String>? =null
    var arrayType1:ArrayList<String>? =null
    private lateinit var imageBitmap:Bitmap
    var dataPart:String?=null

    var backTotype=true


    private lateinit var progressBar: ProgressDialog
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)



        session=Session(this)
        if(!session!!.loggedIn()){
            logout()
        }

        arrayType = arrayOf()
        arrayType1 = arrayListOf()






    val fragment = TypeFragment()
    val fragmentTransaction = supportFragmentManager.beginTransaction()
    fragmentTransaction.replace(R.id.frame_layout, fragment, "type")
    fragmentTransaction.commit()

    text_title.text = "Kategori"




        backButton.setOnClickListener {
            toFragmentType()
        }


        backButton1.setOnClickListener {


            val bundle = Bundle()


            bundle.putString("namaType", dataPart)

            val transaction = this.supportFragmentManager.beginTransaction()
            val fragmentPart = PartFragment()

            fragmentPart.arguments = bundle

            transaction.replace(R.id.frame_layout, fragmentPart, "part")

            transaction.commit()

            text_title.text = "Part"


            backButton.isVisible = true
            backButton.isEnabled = true

            backButton1.isVisible = false
            backButton1.isEnabled = false

        }


        val test = findViewById<androidx.appcompat.widget.Toolbar>(R.id.toolbar)

        val draw = ContextCompat.getDrawable(this, R.drawable.ic_baseline_dehaze_24)

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {

            test.inflateMenu(R.menu.menu_atas)

            test.overflowIcon = draw
        }


        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            test.setOnMenuItemClickListener { item ->
                if (item.itemId == R.id.bt_logout) {

                    logout()
                }else if(item.itemId == R.id.ganti_pass){

                    GantiPassDialog().show(supportFragmentManager, "Ganti Password")
                }else if(item.itemId == R.id.tambah_type){

                    TambahTypeDialog().show(supportFragmentManager, "Tambah Type")
                }else if(item.itemId== R.id.tambah_part){
                    TambahPartDialog(arrayType!!).show(supportFragmentManager, "Tambah Part")

                }else if(item.itemId== R.id.about){
                    AboutDialog().show(supportFragmentManager, "About Fragment")
                }else if(item.itemId== R.id.firebase){
                    startActivity(Intent(this@MainActivity, WebView::class.java))

                }
                false
            }
        }



        databas.child("User").child(auth!!.uid).addListenerForSingleValueEvent(object : ValueEventListener {


            override fun onDataChange(snapshot: DataSnapshot) {
                namauser = snapshot.getValue(User::class.java)?.nama!!


            }

            override fun onCancelled(error: DatabaseError) {

            }
        })



        databas.child("Type").addValueEventListener(object : ValueEventListener {


            override fun onDataChange(snapshot: DataSnapshot) {
                arrayType1?.clear()
                for (data in snapshot.children) {
                    arrayType1?.add(data.getValue(Type::class.java)?.nama!!)
                }

                setAdapter(arrayType1!!)

                arrayType = arrayType1!!.toTypedArray()




            }


            override fun onCancelled(error: DatabaseError) {

            }
        })
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.bt_logout -> {
            }
        }

        return true
    }
    override fun Lister(list: List<String>) {
        Log.i("testrr", list[0])
        Log.i("testrr1", list[1])
        Log.i("testrr2", list[2])

        val kreden = EmailAuthProvider.getCredential(auth?.email.toString(), list[0])

        auth?.reauthenticate(kreden)?.addOnCompleteListener {
            if(it.isSuccessful){
                auth!!.updatePassword(list[1])
                Toast.makeText(this, "Update password berhasil", Toast.LENGTH_LONG).show()
            }else{
                Toast.makeText(this, "Password anda salah", Toast.LENGTH_LONG).show()
            }
        }
    }
    override fun Lister2(list: List<String>){
        progressBar = ProgressDialog(this)
        progressBar.setMessage("Mohon tunggu..")
        progressBar.setCanceledOnTouchOutside(false)
        progressBar.setCancelable(false)
        progressBar.show()
        imageUri = list[1].toUri()

        uploadImageAndSaveUri(list[0], 1, null, null, null)
    }
    fun setAdapter(adapterArray: ArrayList<String>){
        arrayAdapter =  ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, adapterArray)
    }
    private fun uploadImageAndSaveUri(namaobj: String?, indicator: Int?, isi: String?, keterangan: String?, namaType: List<String>?) {
        if(imageUri!=null)
        {
            imageBitmap = MediaStore.Images.Media.getBitmap(this.contentResolver, imageUri)
            val baos= ByteArrayOutputStream()
            imageBitmap.compress(Bitmap.CompressFormat.JPEG, 20, baos)
            val image=baos.toByteArray()
            val storageRef = FirebaseStorage.getInstance()
                .reference.child("pics/${namaobj}")

            val uploadTask : StorageTask<*>

            uploadTask = storageRef.putBytes(image)

            uploadTask.continueWithTask(com.google.android.gms.tasks.Continuation<UploadTask.TaskSnapshot, Task<Uri>> { task ->
                if (!task.isSuccessful) {
                    task.exception?.let {
                        throw it
                    }
                }
                return@Continuation storageRef.downloadUrl

            }).addOnCompleteListener { task ->

                if(task.isSuccessful){

                    val downloadUri = task.result
                    val url = downloadUri.toString()
                    if(indicator==1) {
                        databas.child("Type")
                            .push().setValue(Type(namaobj, url, namauser))
                    }else{
                        databas.child("Part").push().setValue(Part(namaobj, isi, url, namauser, keterangan, namaType))
                    }

                    progressBar.dismiss()

                    Toast.makeText(this, "Tambah Data Berhasil", Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    private fun logout() {
        session!!.setLoggedin(false)
        finish()
        FirebaseAuth.getInstance().signOut()
        startActivity(Intent(this@MainActivity, LoginActivity::class.java))

    }

    fun toFragmentType(){
        val fragment = TypeFragment()
        val fragmentTransaction = supportFragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.frame_layout, fragment, "type")
        fragmentTransaction.commit()
        text_title.text = "Kategori"



        backButton.isVisible = false
        backButton.isEnabled = false
    }

    fun toFragmentPart(){
        val bundle = Bundle()


        bundle.putString("namaType", dataPart)

        val transaction = this.supportFragmentManager.beginTransaction()
        val fragmentPart = PartFragment()

        fragmentPart.arguments = bundle

        transaction.replace(R.id.frame_layout, fragmentPart, "part")

        transaction.commit()

        text_title.text = "Materi"


        backButton.isVisible = true
        backButton.isEnabled = true

        backButton1.isVisible = false
        backButton1.isEnabled = false

    }


    override fun Lister3(list: List<String>?, arrayType: List<String>?) {
        progressBar = ProgressDialog(this)
        progressBar.setMessage("Mohon tunggu..")
        progressBar.setCanceledOnTouchOutside(false)
        progressBar.setCancelable(false)
        progressBar.show()



        imageUri= list?.get(1)?.toUri()

        uploadImageAndSaveUri(list?.get(2), 2, list?.get(0), list?.get(3), arrayType)




    }

    override fun sendString(send: String) {
        val bundle = Bundle()
        dataPart = send

        bundle.putString("namaType", send)

        val transaction = this.supportFragmentManager.beginTransaction()
        val fragmentPart = PartFragment()

        fragmentPart.arguments = bundle

        transaction.replace(R.id.frame_layout, fragmentPart, "part")

        transaction.commit()

        text_title.text = "Materi"

        backButton.isVisible = true
        backButton.isEnabled = true









    }

    override fun onBackPressed() {
        val frag1= supportFragmentManager.findFragmentByTag("show")
        val frag2= supportFragmentManager.findFragmentByTag("type")
        val frag3= supportFragmentManager.findFragmentByTag("part")


        if (frag1 != null) {
            if(frag1.isVisible){

                toFragmentPart()


            }
        }

        if(frag2 !=null){
            if(frag2.isVisible){
                super.onBackPressed()
            }

        }

        if(frag3 != null){
            if(frag3.isVisible){
                toFragmentType()
            }

        }


    }

    override fun sendData(send: String, isi: String, keterangan: String, photo: String, dibuat: List<String>, nama: String) {
        val bundle = Bundle()

        bundle.putStringArray("Part", arrayOf(send, isi, keterangan, photo, nama))
        bundle.putStringArray("namaType", dibuat.toTypedArray())

        val transaction = this.supportFragmentManager.beginTransaction()
        val fragmentPart = AcitivyShowImage()

        fragmentPart.arguments = bundle

        transaction.replace(R.id.frame_layout, fragmentPart, "show")

        transaction.commit()

        text_title.text = send

        backButton1.isVisible = true
        backButton1.isEnabled = true

        backButton.isVisible = false
        backButton.isEnabled = false
    }
}