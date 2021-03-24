package org.d3ifcool.materi

class Part{
    var nama : String?= null
    var isi: String?=null
    var keterangan :String?=null
    var dibuat: String?=null
    var photo: String?=null
    var kategori:List<String>?=null

    constructor(){}

    constructor(nama: String?, line:String?, profile:String?, dibuat:String?, keterangan:String?, type:List<String>?){
        this.nama = nama
        this.isi = line
        this.keterangan = keterangan
        this.dibuat = dibuat
        this.photo = profile
        this.kategori = type

    }
}