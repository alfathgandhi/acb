package org.d3ifcool.materi

interface RecycleListClickListener {

    fun onClicked(position:Int, part:Part?, type:Type?)

    fun ItemClicked(position:Int, id:String?)

    fun longClicked(position: Int, nama:String?)




}