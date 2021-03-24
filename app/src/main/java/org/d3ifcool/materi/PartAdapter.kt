package org.d3ifcool.materi



import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import de.hdodenhof.circleimageview.CircleImageView

class PartAdapter(var clickListener: RecycleListClickListener):RecyclerView.Adapter<PartAdapter.ItemHolder>(){

    var data= listOf<Part>()
        set(value) {
            field = value
            notifyDataSetChanged()

        }

    companion object{
        var user = FirebaseAuth.getInstance().currentUser?.email

    }



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemHolder {
        val itemHolder = LayoutInflater.from(parent.context).
        inflate(R.layout.part_list,parent,false)
        return ItemHolder(itemHolder)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: ItemHolder, position: Int) {
        val part = data.get(position)

        holder.init(part)


    }

    inner class ItemHolder(itemView: View):RecyclerView.ViewHolder(itemView){

        val namaPart= itemView.findViewById<TextView>(R.id.tv_part)
        val gambarPart= itemView.findViewById<CircleImageView>(R.id.pict_part)
        val hapus = itemView.findViewById<ImageView>(R.id.hapus)


        fun init(data:Part){
         namaPart.text = data.nama
         Glide.with(itemView.context).load(data.photo).into(gambarPart)

            Log.i("userr", user.toString())

            if(user.equals("admin@admin.com")||user.equals("ahmadkundori02@gmail.com")){
                hapus.visibility = View.VISIBLE
            }

            itemView.findViewById<ImageView>(R.id.hapus).setOnClickListener {

                clickListener.ItemClicked(adapterPosition,data.nama)
            }

            namaPart.setOnClickListener {

                    clickListener.onClicked(adapterPosition,data,null)




                 }

            gambarPart.setOnClickListener {
                clickListener.onClicked(adapterPosition,data,null)

            }

        }




    }
}