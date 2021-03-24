package org.d3ifcool.materi

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class TypeFragment:Fragment(),RecycleListClickListener{
    override fun longClicked(position: Int, nama: String?) {
        if(FirebaseAuth.getInstance().currentUser!!.email.equals("admin@admin.com")||FirebaseAuth.getInstance().currentUser!!.email.equals("ahmadkundori02@gmail.com")){ databaseReference = FirebaseDatabase.getInstance().reference.child("Type")

            val builder = AlertDialog.Builder(requireActivity())
                .setMessage("Yakin ingin menghapus item ini?")
                .setPositiveButton("Ya"){dialog, which ->
                    databaseReference.addValueEventListener(object :  ValueEventListener{

                        override fun onDataChange(snapshot: DataSnapshot) {

                            for (data in snapshot.children){

                                val x = data.getValue(Part::class.java)?.nama

                                if(x==nama){
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

            builder.show()}

    }

    override fun ItemClicked(position: Int, id: String?) {

    }

    private lateinit var databaseReference: DatabaseReference
    private lateinit var view_fragment:View
    val instace = FirebaseAuth.getInstance()
    private lateinit var gridLayoutManager:GridLayoutManager
   private lateinit var typeAdapter: TypeAdapter
   private var rv: RecyclerView ?= null
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        view_fragment = inflater.inflate(R.layout.fragment_type,container,false)


        rv = view_fragment.findViewById(R.id.rvQuiz)
        gridLayoutManager = GridLayoutManager(context,2, LinearLayoutManager.VERTICAL,false)

         rv?.setLayoutManager(gridLayoutManager)

        rv?.setHasFixedSize(true)

        val array = arrayListOf<Type>()


        val adapter = TypeAdapter(this)
        communicator = activity as Communicator

        databaseReference = FirebaseDatabase.getInstance().reference.child("Type")

        databaseReference.addValueEventListener(object  : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                    array.clear()

                for (data in snapshot.children){
                    array.add(data.getValue(Type::class.java)!!)

                    }
                adapter.data = array

            }

            override fun onCancelled(error: DatabaseError) {

            }
        })




        rv?.adapter = adapter

//        activity.SupportActionBar()!!.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM)
//        getSupportActionBar()!!.setCustomView(R.layout.actionbar)
//


        return view_fragment

    }
private lateinit var communicator: Communicator
    override fun onClicked(position: Int, part: Part?, type: Type?) {

        communicator.sendString(type?.nama!!)



    }
}