package com.monish.contacts.presentation.search

import androidx.recyclerview.widget.DiffUtil
import com.monish.contacts.domain.Contact

class SearchDiffCallBack: DiffUtil.ItemCallback<Contact>() {
    override fun areItemsTheSame(oldItem: Contact, newItem: Contact): Boolean {
        return oldItem==newItem
    }

    override fun areContentsTheSame(oldItem: Contact, newItem: Contact): Boolean {
        return oldItem==newItem
    }
}