package com.monish.contacts.presentation.addcontacts


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.monish.contacts.domain.Contact
import com.monish.contacts.interactors.UseCases
import com.monish.contacts.presentation.allcontacts.AllContactsViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class AddContactViewModel(private val useCases: UseCases): ViewModel() {
//    val contact: MutableLiveData<List<Contact>> = MutableLiveData()
    val allContactViewModel:AllContactsViewModel=AllContactsViewModel(useCases)
    fun addContact(contact: Contact){
        viewModelScope.launch {
            withContext(Dispatchers.IO){
                useCases.addContact(contact)
            }
//            allContactViewModel.getContact()
        }
    }
}