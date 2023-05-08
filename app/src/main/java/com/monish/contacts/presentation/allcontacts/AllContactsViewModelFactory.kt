package com.monish.contacts.presentation.allcontacts

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.CreationExtras
import com.monish.contacts.interactors.UseCases


class AllContactsViewModelFactory(private val useCases: UseCases) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>, extras: CreationExtras): T {
        Log.e(
            "Entering viewModel Factory",
            "ViewModel Factory....................................."
        )
        return if (modelClass.isAssignableFrom(AllContactsViewModel::class.java)) {
            AllContactsViewModel(this.useCases) as T
        } else {
            throw IllegalArgumentException("AWWW Man!!!!!!")
        }

    }
}