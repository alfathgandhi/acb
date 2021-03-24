package org.d3ifcool.materi

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.user_activity.*

class ListUser : AppCompatActivity(), UserAdapter.clickUser{
    val databaseReference = FirebaseDatabase.getInstance().reference.child("User")

    override fun editClicked(user: User) {

    }

    override fun deleteClicked(user: User) {
        Log.i("USERRRR",user.nama!!)
        val builder = AlertDialog.Builder(this)
                .setMessage("Yakin ingin menghapus item ini?")
                .setPositiveButton("Ya"){dialog, which ->
                    databaseReference.addValueEventListener(object : ValueEventListener{
                        override fun onDataChange(snapshot: DataSnapshot) {

                            for(data in snapshot.children){
                                if(data.getValue(User::class.java)?.nama.equals(user.nama)){
                                    data.ref.removeValue()

                                }
                            }


                        }

                        override fun onCancelled(error: DatabaseError) {

                        }
                    })

                }
                .setNegativeButton("Tidak"){dialog,_ ->
                    dialog.dismiss()
                }.create()

        builder.show()

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.user_activity)
        text_title_user.text = "User List"
        rv_user.layoutManager = LinearLayoutManager(this)


        val adapter = UserAdapter(this)

        backButton1_user.setOnClickListener {
            super.onBackPressed()
        }

        databaseReference.addValueEventListener(object : ValueEventListener{
            val array = arrayListOf<User>()

            override fun onDataChange(snapshot: DataSnapshot) {
                array.clear()

                for(data in snapshot.children){
                    array.add(data.getValue(User::class.java)!!)
                }

                adapter.data = array

            }

            override fun onCancelled(error: DatabaseError) {

            }
        })

        rv_user.adapter = adapter






    }


}