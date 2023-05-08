package com.monish.contacts.presentation.keypad


import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
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
import com.monish.contacts.databinding.FragmentKeypadBinding
import com.monish.contacts.domain.Contact


import com.monish.contacts.presentation.search.SearchDiffCallBack
import com.monish.contacts.presentation.search.TextHighlight

class KeypadAdapter(var keyPadBinding:FragmentKeypadBinding,var numberText:String) : ListAdapter<Contact, KeypadAdapter.ContactViewHolder>(SearchDiffCallBack()) {
    var queryText: String = ""
    var prevQuerySize = 0
    private lateinit var keypad:KeypadFragment
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
                mobile.visibility=View.GONE
                if (queryText.matches("[0-9]*".toRegex())) {
                    mobileNumberSearch.text = TextHighlight.highlight(queryText, contact.mobile)
                    name.text = contact.namePrefix
                } else {
                    name.text = TextHighlight.highlight(queryText, contact.namePrefix)
                }
                println("Contact.mobile ${contact.mobile}")
                profile.layoutParams.width = 125
                profile.layoutParams.height = 125
                profile.background = gradientDrawable
                if (contact.imgUri.isNotEmpty()) {

                    Glide.with(profile).load(contact.imgUri).into(profile)
                } else {

                    profile.setImageBitmap(Draw.drawText(contact.namePrefix, profile))
                }

                val bundle: Bundle = Bundle().apply {

                    this.putString("mobile", contact.mobile)

                }
                constrainLayout.setOnClickListener {
                    numberText=contact.mobile
                    keyPadBinding.keypadLayout.numberDisplay.setText(numberText)
                    keyPadBinding.keypadLayout.numberDisplay.setSelection(numberText.length)
                }
            }

        }

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
            notifyDataSetChanged()

        } else {
            if (query != " " || query != null) {
                queryText = query
                var newList: MutableList<Contact> = mutableListOf()
                for (i in originalDataList) {
                    if (i.namePrefix.lowercase().contains(query.lowercase())) {
                        newList.add(i)
                    }
                }
                println(newList)
                submitList(newList)
                notifyDataSetChanged()
            }
        }
    }

}