package org.d3ifcool.materi

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.SearchView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.*

class PartFragment: Fragment(), RecycleListClickListener{
    override fun longClicked(position: Int, nama: String?) {

    }

    private lateinit var communicator:Communicator

    override fun onClicked(position: Int, part: Part?, type: Type?) {

        communicator.sendData(part?.nama!!,part.isi!!,part.keterangan!!,part.photo!!,part.kategori!!,part.dibuat!!)


    }





    override fun ItemClicked(position: Int, id: String?) {
        databaseReference = FirebaseDatabase.getInstance().reference.child("Part")

        val builder = AlertDialog.Builder(requireActivity())
            .setMessage("Yakin ingin menghapus item ini?")
            .setPositiveButton("Ya"){dialog, which ->
                databaseReference.addValueEventListener(object :  ValueEventListener{

                    override fun onDataChange(snapshot: DataSnapshot) {

                        for (data in snapshot.children){

                            val x = data.getValue(Part::class.java)?.nama

                            if(x==id){
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

    var pesan: String? = ""
    private lateinit var rv:RecyclerView
    private lateinit var databaseReference: DatabaseReference
    private lateinit var arraypart:ArrayList<Part>

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_part, container, false)


    pesan = arguments?.getString("namaType")
    rv = view.findViewById(R.id.my_rv)
        rv.layoutManager = LinearLayoutManager(context)

        arraypart= arrayListOf()

        databaseReference  = FirebaseDatabase.getInstance().reference.child("Part")

        val adapter = PartAdapter(this)

        view.findViewById<Button>(R.id.button_reset).setOnClickListener {
            adapter.data = arraypart
            rv.adapter = adapter

        }

        communicator= activity as Communicator

        databaseReference.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
        arraypart.clear()
                for(data in snapshot.children){
                    val y=data.getValue(Part::class.java)?.kategori

                    if(y?.contains(pesan)!!){
                        arraypart.add(data.getValue(Part::class.java)!!)

                    }
                    

                }

                adapter.data = arraypart




            }

            override fun onCancelled(error: DatabaseError) {

            }
        })

        rv.adapter = adapter
val search =  view.findViewById<SearchView>(R.id.search_view)
val tersaring:ArrayList<Part> = arrayListOf()
   search.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
        override fun onQueryTextSubmit(p0: String?): Boolean {

            search.clearFocus()
            var boolean = false
            tersaring.clear()

            for(data in arraypart){

                if( data.nama!!.contains(p0!!,true)){
                    tersaring.add(data)
                    Log.i("dataaa", data.nama.toString())
                    boolean= true


                }


            }

            if(boolean){

                adapter.data = tersaring

                if(rv.adapter==null){
                    rv.adapter = adapter
                }

            }else{
                Toast.makeText(context,"Data tidak ditemukan",Toast.LENGTH_SHORT).show()
                rv.adapter = null
            }
             return false
        }

        override fun onQueryTextChange(p0: String?): Boolean {
            return false
        }
    })


    return view
    }


}