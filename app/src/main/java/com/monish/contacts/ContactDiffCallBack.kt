package com.monish.contacts

import androidx.recyclerview.widget.DiffUtil
import com.monish.contacts.domain.Contact

class ContactDiffCallBack:DiffUtil.ItemCallback<Contact>() {
    override fun areContentsTheSame(oldItem: Contact, newItem: Contact): Boolean {
        return oldItem.mobile==newItem.mobile || oldItem.imgUri==newItem.imgUri
    }

    override fun areItemsTheSame(oldItem: Contact, newItem: Contact): Boolean {
        return oldItem==newItem
    }
}