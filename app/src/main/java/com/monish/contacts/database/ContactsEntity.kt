package com.monish.contacts.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey


@Entity("CONTACTS")
data class ContactsEntity(
    @PrimaryKey(true) val id:Int,
    @ColumnInfo(name = "NamePrefix") val namePrefix:String,
    @ColumnInfo(name = "FirstName") val firstName:String,
    @ColumnInfo(name = "MiddleName") val middleName:String,
    @ColumnInfo(name = "LastName") val lastName:String,
    @ColumnInfo(name = "NameSuffix") val nameSuffix:String,
    @ColumnInfo(name = "Mobile") val mobile:String,
    @ColumnInfo(name = "Email") val email:String,
    @ColumnInfo(name = "Company") val company:String,
    @ColumnInfo(name = "ImageUri") val imgUri:String,
){}