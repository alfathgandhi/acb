package org.d3ifcool.materi

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class UserAdapter(var clickListener: clickUser): RecyclerView.Adapter<UserAdapter.ItemHolder>(){

    var data = listOf<User>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemHolder {
        val itemHolder = LayoutInflater.from(parent.context).
        inflate(R.layout.user_list,parent,false)
        return ItemHolder(itemHolder)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: ItemHolder, position: Int) {
        val type = data.get(position)

        holder.init(type)


    }

    inner class ItemHolder(itemView: View): RecyclerView.ViewHolder(itemView){

        val nama = itemView.findViewById<TextView>(R.id.tv_user)

        fun init(data:User){

            nama.setText(data.nama)

            itemView.findViewById<ImageView>(R.id.hapus_user).setOnClickListener {
                clickListener.deleteClicked(data)
            }

        }




    }

    interface clickUser{
        fun editClicked(user:User)
        fun deleteClicked(user:User)

    }

}