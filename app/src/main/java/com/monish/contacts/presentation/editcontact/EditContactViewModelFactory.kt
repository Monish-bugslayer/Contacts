package com.monish.contacts.presentation.editcontact

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.CreationExtras
import com.monish.contacts.interactors.UseCases


class EditContactViewModelFactory(private val useCases: UseCases) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>, extras: CreationExtras): T {

        return if (modelClass.isAssignableFrom(EditContactViewModel::class.java)) {
            EditContactViewModel(this.useCases) as T
        } else {
            throw IllegalArgumentException("AWWW Man!!!!!!")
        }

    }
}