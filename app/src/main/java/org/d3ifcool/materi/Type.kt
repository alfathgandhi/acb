package org.d3ifcool.materi

class Type {
    var nama:String?=null
    //var arrayPart:ArrayList<Part>?=null
    var gambar:String?=null
    var dibuat:String?=null


    constructor(){}

    constructor(nama:String?, arrayPart: String?, dibuat: String?){
        this.dibuat=dibuat
        this.nama = nama
        this.gambar = arrayPart

    }

}