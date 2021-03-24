package org.d3ifcool.materi

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class TypeAdapter(var clickListener: RecycleListClickListener):RecyclerView.Adapter<TypeAdapter.ItemHolder>(){

    var data = listOf<Type>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemHolder {
       val itemHolder = LayoutInflater.from(parent.context).
       inflate(R.layout.record_list,parent,false)
        return ItemHolder(itemHolder)
    }

    override fun getItemCount(): Int {
       return data.size
    }

    override fun onBindViewHolder(holder: ItemHolder, position: Int) {
        val type = data.get(position)

holder.init(type)


    }

    inner class ItemHolder(itemView: View):RecyclerView.ViewHolder(itemView){
        val gambar = itemView.findViewById<ImageView>(R.id.typeimage)
        val text = itemView.findViewById<TextView>(R.id.type )
        val haha = itemView.context
        val parah = itemView.findViewById<TextView>(R.id.dibuat_oleh)

        fun init(data:Type){
            text.text = data.nama
            Glide.with(haha)
                .load(data.gambar).into(gambar)
            parah.text = data.dibuat


        itemView.setOnClickListener {
            clickListener.onClicked(adapterPosition,null,data)
        }
            itemView.setOnLongClickListener {

                clickListener.longClicked(adapterPosition, data.nama)
                return@setOnLongClickListener true
            }

        }




    }
}