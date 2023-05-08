package com.monish.contacts.presentation.search

import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.NonNull
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.monish.contacts.Draw
import com.monish.contacts.R
import com.monish.contacts.databinding.ContactItemForSearchBinding
import com.monish.contacts.domain.Contact


class SearchBarAdapter :
    ListAdapter<Contact, SearchBarAdapter.ContactViewHolder>(SearchDiffCallBack()) {
    var queryText: String = ""
    var prevQuerySize = 0
    lateinit var originalDataList: MutableList<Contact>

    inner class ContactViewHolder(private val binding: ContactItemForSearchBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(contact: Contact) {
            val colors = intArrayOf(
                Color.MAGENTA, R.drawable.cardview_gradiant
            )
            val gradientDrawable = GradientDrawable(GradientDrawable.Orientation.BR_TL, colors)
            val isExpandable = contact.isExpanded
            binding.apply {
                if (contact.mobile.isNotEmpty()) {
                    mobileNumberSearch.text = "+91 " + contact.mobile
                    mobile.text = "Mobile +91 " + contact.mobile
                } else {
                    mobileNumberSearch.text = ""
                    mobile.text = ""
                }

                if (queryText.matches("[0-9]*".toRegex())) {
                    mobileNumberSearch.text = TextHighlight.highlight(queryText, currentList[adapterPosition].mobile)
                    name.text = contact.namePrefix
                } else {
                    name.text = TextHighlight.highlight(queryText, currentList[adapterPosition].namePrefix)
                }
                profile.layoutParams.width = 125
                profile.layoutParams.height = 125
                profile.background = gradientDrawable
                if (contact.imgUri.isNotEmpty()) {

                    Glide.with(profile).load(contact.imgUri).into(profile)
                } else {

                    profile.setImageBitmap(Draw.drawText(contact.namePrefix, profile))
                }
                if (isExpandable) {
                    expand()
                } else {
                    collapse()
                }
                constrainLayout.setOnClickListener {
                    isAnyItemExpanded(adapterPosition)
                    contact.isExpanded = !contact.isExpanded
                    notifyItemChanged(adapterPosition)
                }
            }

        }


        private fun isAnyItemExpanded(position: Int) {
            val temp = currentList.indexOfFirst {
                it.isExpanded
            }
            if (temp >= 0 && temp != position) {
                currentList[temp].isExpanded = false
                notifyItemChanged(temp, 0)
            }
        }


        fun collapse() {
            binding.apply {
                mobile.visibility = View.GONE
                call.visibility = View.GONE
                message.visibility = View.GONE
                info.visibility = View.GONE
            }

        }

        fun expand() {
            binding.apply {
                mobile.visibility = View.VISIBLE
                call.visibility = View.VISIBLE
                message.visibility = View.VISIBLE
                info.visibility = View.VISIBLE
            }
        }

    }

    override fun onBindViewHolder(
        holder: ContactViewHolder, position: Int, payloads: MutableList<Any>
    ) {
        if (payloads.isNotEmpty() && payloads[0] == 0) {
            holder.collapse()

        } else {
            super.onBindViewHolder(holder, position, payloads)
        }

    }

    fun filter(query: String) {

        if (query.matches("[0-9]*".toRegex())) {
                queryText = query
                var newList: MutableList<Contact> = mutableListOf()
                prevQuerySize = query.length
                for (i in originalDataList) {
                    if (i.mobile.contains(query)) {
                        newList.add(i)
                    }
                }
                submitList(newList)

        } else {
            if (query != null) {
                queryText = query
                var newList: MutableList<Contact> = mutableListOf()
                for (i in originalDataList) {
                    if (i.namePrefix.lowercase().contains(query.lowercase())) {
                        newList.add(i)
                    }
                }
                submitList(newList)
            }
        }
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContactViewHolder {
        return ContactViewHolder(
            ContactItemForSearchBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }

    override fun onBindViewHolder(@NonNull holder: ContactViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}


