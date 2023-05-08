package com.monish.contacts.domain


data class Contact(
    val id: Int,
    var namePrefix: String,
    val firstName: String,
    val middleName: String,
    val lastName: String,
    val nameSuffix: String,
    val mobile: String,
    val email: String,
    val company: String,
    val imgUri: String,
    var isExpanded: Boolean=false,
    var isSelected:Boolean=false,
    var isFavourite:Boolean=false
):java.io.Serializable