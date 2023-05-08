package com.monish.contacts.presentation.keypad

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.monish.contacts.domain.Contact
import com.monish.contacts.interactors.UseCases
import kotlinx.coroutines.launch

class KeypadViewModel (private val useCases: UseCases) : ViewModel(),java.io.Serializable {
    val contact: MutableLiveData<List<Contact>> = MutableLiveData()
    fun getContact() {
        viewModelScope.launch {
            contact.apply {
                value = useCases.getContact()
            }
        }
    }
}