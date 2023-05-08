package com.monish.contacts.presentation.search

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.CreationExtras
import com.monish.contacts.interactors.UseCases


class SearchViewModelFactory(private val useCases: UseCases) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>, extras: CreationExtras): T {
        return if (modelClass.isAssignableFrom(SearchViewModel::class.java)) {
            SearchViewModel(this.useCases) as T
        } else {
            throw IllegalArgumentException()
        }

    }
}
