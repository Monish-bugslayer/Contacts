package com.monish.contacts.interactors

import com.monish.contacts.data.ContactsRepository
import com.monish.contacts.domain.Contact

class UseCases(private val contactRepo: ContactsRepository) {
    suspend fun addContact(contact:Contact){contactRepo.addContact(contact)}
    suspend fun getContact()=contactRepo.getContact()
    suspend fun deleteContact(contact: MutableList<Contact>)=contactRepo.deleteContact(contact)
}