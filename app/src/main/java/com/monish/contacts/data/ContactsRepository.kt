package com.monish.contacts.data

import android.content.Context
import androidx.core.net.toUri
import androidx.lifecycle.LiveData
import androidx.lifecycle.map
import com.monish.contacts.database.ContactsDao
import com.monish.contacts.database.ContactsEntity
import com.monish.contacts.domain.Contact

class ContactsRepository(private val context: Context, private val dataSource:ContactsDao) {
    suspend fun addContact(contact:Contact){
        dataSource.addContact(ContactsEntity(contact.id,
        contact.namePrefix,
        contact.firstName,
        contact.middleName,
        contact.lastName,
        contact.nameSuffix,
        contact.mobile,
        contact.email,
        contact.company,
        contact.imgUri))
    }
    suspend fun getContact():List<Contact>{

        return dataSource.getContact().map {
                Contact(id = it.id, namePrefix = it.namePrefix, firstName = it.firstName, middleName = it.middleName, lastName = it.lastName, nameSuffix = it.nameSuffix, mobile = it.mobile, email = it.email, company = it.company, imgUri = it.imgUri)
            }
        }
    suspend fun editContact(){}
    suspend fun deleteContact(contact:MutableList<Contact>){
        var id:ContactsEntity
        for(i in contact){
            id=dataSource.getSingleContact(i.namePrefix)
            dataSource.deleteContact(id)
        }
    }
}