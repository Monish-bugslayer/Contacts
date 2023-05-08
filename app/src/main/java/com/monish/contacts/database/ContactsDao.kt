package com.monish.contacts.database

import androidx.lifecycle.LiveData
import androidx.room.*
import com.monish.contacts.domain.Contact
import kotlinx.coroutines.flow.Flow

@Dao
interface ContactsDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addContact(contact:ContactsEntity)
    @Query("SELECT * FROM CONTACTS ORDER BY NamePrefix ASC")
    suspend fun getContact(): List<ContactsEntity>
    @Query("SELECT * FROM CONTACTS WHERE NamePrefix=:name")
    suspend fun getSingleContact(name:String):ContactsEntity
    @Delete
    suspend fun deleteContact(contact:ContactsEntity)
//    suspend fun editContact()
}