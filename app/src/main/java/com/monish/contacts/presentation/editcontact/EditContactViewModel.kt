package com.monish.contacts.presentation.editcontact

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.monish.contacts.domain.Contact
import com.monish.contacts.interactors.UseCases
import kotlinx.coroutines.launch

class EditContactViewModel(private val useCases: UseCases) : ViewModel() {
    val contact: MutableLiveData<List<Contact>> = MutableLiveData()

    fun getContact() {
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