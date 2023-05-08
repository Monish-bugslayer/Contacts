package com.monish.contacts.presentation.search

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.monish.contacts.domain.Contact
import com.monish.contacts.interactors.UseCases
import kotlinx.coroutines.launch

class SearchViewModel(private val useCases: UseCases) : ViewModel() {
    val contact: MutableLiveData<List<Contact>> = MutableLiveData()

    fun getContact() {
        viewModelScope.launch {
            contact.apply {
                value = useCases.getContact()
            }
        }
    }
}