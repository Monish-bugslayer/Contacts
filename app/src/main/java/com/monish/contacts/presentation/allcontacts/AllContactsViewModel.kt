package com.monish.contacts.presentation.allcontacts

import androidx.lifecycle.*
import com.monish.contacts.database.ContactsEntity
import com.monish.contacts.domain.Contact
import com.monish.contacts.interactors.UseCases
import com.monish.contacts.presentation.search.SearchFragment
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch


class AllContactsViewModel(private val useCases: UseCases) : ViewModel(),java.io.Serializable {




    var contact: MutableLiveData<List<Contact>> = MutableLiveData()

    fun getContact(){

        viewModelScope.launch {
            contact.apply {
                value = useCases.getContact()
            }
        }
    }

    fun deleteContact(contact: MutableList<Contact>) {
        viewModelScope.launch {
            useCases.deleteContact(contact)
            getContact()
        }
    }
}