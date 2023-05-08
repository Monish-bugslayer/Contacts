package com.monish.contacts.presentation.allcontacts

import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.graphics.Paint
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.view.*
import android.view.View.OnTouchListener
import android.widget.TextView
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.monish.contacts.ContactDiffCallBack
import com.monish.contacts.Draw
import com.monish.contacts.MainActivity
import com.monish.contacts.R
import com.monish.contacts.databinding.BothViewTypeBinding
import com.monish.contacts.databinding.ContactItemBinding
import com.monish.contacts.domain.Contact
import com.monish.contacts.presentation.editcontact.EditContact

class AllContactsAdapter(
    private var activity: MainActivity,
    private val recyclerView: RecyclerView,
    val shouDialogue: () -> Unit,
    val hideDialogue: () -> Unit
) : ListAdapter<Contact, RecyclerView.ViewHolder>(ContactDiffCallBack()), java.io.Serializable {
    var selectedContacts: MutableList<Contact> = mutableListOf()
    var isLongPressed: Boolean = false
    lateinit var allContacts: AllContacts
    private lateinit var editContact: EditContact

    companion object {
        const val ALPHABET_SEPARATOR_VIEW_TYPE = 0
        const val CONTACT_VIEW_TYPE = 1
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

    override fun onBindViewHolder(
        holder: RecyclerView.ViewHolder, position: Int, payloads: MutableList<Any>
    ) {
        if (payloads.isNotEmpty() && payloads[0] == 0) {
            if (holder is ContactViewHolder) {
                holder.collapse()
            }
            if (holder is AlphabetSeparatorViewHolder) {
                holder.collapse()
            }
        } else {
            super.onBindViewHolder(holder, position, payloads)
        }
    }

    inner class ContactViewHolder(private val binding: ContactItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        var firstLetter = ""
        fun contactBind(contact: Contact) {
            editContact = EditContact()

            val colors = intArrayOf(
                Color.MAGENTA, R.drawable.cardview_gradiant
            )
            val gradientDrawable = GradientDrawable(GradientDrawable.Orientation.BR_TL, colors)
            val isExpandable: Boolean = contact.isExpanded
            binding.apply {
                name.text = contact.namePrefix
                firstLetter = contact.namePrefix[0].toString()
                mobile.text = contact.mobile
                profile.layoutParams.width = 125
                profile.layoutParams.height = 125
                profile.background = gradientDrawable
                selected.setImageResource(R.drawable.baseline_check_24)
                if (!contact.isSelected) {
                    selected.visibility = View.GONE
                    constrainLayout.setBackgroundColor(activity.resources.getColor(R.color.white))
                } else {
                    selected.visibility = View.VISIBLE
                    constrainLayout.setBackgroundColor(activity.resources.getColor(R.color.selected_contact))
                }

                if (isExpandable) {
                    expand()
                } else {
                    collapse()
                }
                if (!contact.imgUri.isNullOrEmpty()) {
                    Glide.with(profile).load(contact.imgUri).into(profile)
                } else {
                    profile.setImageBitmap(Draw.drawText(contact.namePrefix, profile))
                }
                info.setOnClickListener {
                    val bundle: Bundle = Bundle().apply {
                        this.putString("name", contact.namePrefix)
                        this.putString("mobile", contact.mobile)
                        this.putString("img", contact.imgUri)

                    }
                    editContact.arguments=bundle
                    Navigation.findNavController(allContacts.binding.root)
                        .navigate(R.id.action_allContacts_to_editContact)
                }
                constrainLayout.setOnLongClickListener {
                    shouDialogue()
                    constrainLayout.setBackgroundColor(activity.resources.getColor(R.color.selected_contact))
                    selected.visibility = View.VISIBLE
                    contact.isSelected = true
                    selectedContacts.add(contact)
                    isLongPressed = true
                    true
                }
                constrainLayout.setOnClickListener {
                    isAnyItemExpanded(adapterPosition)
                    contact.isExpanded = !contact.isExpanded
                    if (selectedContacts.contains(contact)) {
                        selected.visibility = View.GONE
                        contact.isSelected = false
                        selectedContacts.remove(contact)
                        constrainLayout.setBackgroundColor(activity.resources.getColor(R.color.white))
                        if (selectedContacts.isEmpty()) {
                            hideDialogue()
                            isLongPressed = false
                        }
                    } else if (isLongPressed) {

                        selected.visibility = View.VISIBLE
                        contact.isSelected = true
                        selectedContacts.add(contact)
                        constrainLayout.setBackgroundColor(activity.resources.getColor(R.color.selected_contact))
                    }
                    notifyItemChanged(adapterPosition)
                }
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

        private fun expand() {
            binding.apply {
                mobile.visibility = View.VISIBLE
                call.visibility = View.VISIBLE
                message.visibility = View.VISIBLE
                info.visibility = View.VISIBLE
            }
        }
    }

    inner class AlphabetSeparatorViewHolder(private val binding: BothViewTypeBinding) :
        RecyclerView.ViewHolder(binding.root) {
        var separatorText: String = "A"
        var firstLetter = "A"
        fun alphabetBind(contact: Contact) {
            editContact = EditContact()
            binding.apply {
                firstLetter = contact.namePrefix.first().uppercaseChar().toString()
                alphabetSeparatorTextview.alphabetSeparatorTextview.text = firstLetter
                separatorText = contactItem.name.text.toString()[0].toString()
                val colors = intArrayOf(
                    Color.MAGENTA, R.drawable.cardview_gradiant
                )
                val gradientDrawable = GradientDrawable(GradientDrawable.Orientation.BR_TL, colors)
                val isExpandable: Boolean = contact.isExpanded
                contactItem.name.text = contact.namePrefix
                if(contact.mobile!=""){
                    contactItem.mobile.text = "Mobile +91 " + contact.mobile
                }
                else{
                    contactItem.mobile.text=""
                }
                contactItem.profile.layoutParams.width = 125
                contactItem.profile.layoutParams.height = 125
                contactItem.profile.background = gradientDrawable
                contactItem.selected.setImageResource(R.drawable.baseline_check_24)
                if (!contact.isSelected) {
                    contactItem.selected.visibility = View.GONE
                    contactItem.constrainLayout.setBackgroundColor(activity.resources.getColor(R.color.white))
                } else {
                    contactItem.selected.visibility = View.VISIBLE
                    contactItem.constrainLayout.setBackgroundColor(activity.resources.getColor(R.color.selected_contact))
                }

                if (isExpandable) {
                    expand()
                } else {
                    collapse()
                }
                if (!contact.imgUri.isNullOrEmpty()) {
                    Glide.with(contactItem.profile).load(contact.imgUri).into(contactItem.profile)
                } else {
                    contactItem.profile.setImageBitmap(
                        Draw.drawText(
                            contact.namePrefix,
                            contactItem.profile
                        )
                    )
                }
                contactItem.info.setOnClickListener {
                    allContacts.inEditContact = true
                    val bundle: Bundle = Bundle().apply {
                        this.putString("name", contact.namePrefix)
                        this.putString("mobile", contact.mobile)
                        this.putString("img", contact.imgUri)
                        this.putString("id",contact.id.toString())
                    }
                    Navigation.findNavController(allContacts.binding.root)
                        .navigate(R.id.action_allContacts_to_editContact,bundle)

                }
                contactItem.constrainLayout.setOnLongClickListener {
                    shouDialogue()
                    allContacts.isLongPressed = true
                    contactItem.constrainLayout.setBackgroundColor(activity.resources.getColor(R.color.selected_contact))
                    contactItem.selected.visibility = View.VISIBLE
                    contact.isSelected = true
                    selectedContacts.add(contact)
                    isLongPressed = true
                    true
                }
                contactItem.constrainLayout.setOnClickListener {
                    isAnyItemExpanded(adapterPosition)
                    contact.isExpanded = !contact.isExpanded
                    if (selectedContacts.contains(contact)) {
                        contactItem.selected.visibility = View.GONE
                        contact.isSelected = false
                        selectedContacts.remove(contact)
                        contactItem.constrainLayout.setBackgroundColor(activity.resources.getColor(R.color.white))
                        if (selectedContacts.isEmpty()) {
                            hideDialogue()
                            isLongPressed = false
                            allContacts.isLongPressed = false
                        }
                    } else if (isLongPressed) {

                        contactItem.selected.visibility = View.VISIBLE
                        contact.isSelected = true
                        allContacts.isLongPressed = true
                        selectedContacts.add(contact)
                        contactItem.constrainLayout.setBackgroundColor(activity.resources.getColor(R.color.selected_contact))
                    }
                    notifyItemChanged(adapterPosition)
                }

            }
        }

        fun collapse() {
            binding.apply {
                contactItem.mobile.visibility = View.GONE
                contactItem.call.visibility = View.GONE
                contactItem.message.visibility = View.GONE
                contactItem.info.visibility = View.GONE
            }

        }

        private fun expand() {
            binding.apply {
                contactItem.mobile.visibility = View.VISIBLE
                contactItem.call.visibility = View.VISIBLE
                contactItem.message.visibility = View.VISIBLE
                contactItem.info.visibility = View.VISIBLE
            }
        }

    }

    override fun onCreateViewHolder(
        parent: ViewGroup, viewType: Int
    ): RecyclerView.ViewHolder {
        return when (viewType) {
            ALPHABET_SEPARATOR_VIEW_TYPE -> {
                AlphabetSeparatorViewHolder(
                    BothViewTypeBinding.inflate(
                        LayoutInflater.from(
                            parent.context
                        ), parent, false
                    )
                )
            }
            else -> {
                ContactViewHolder(
                    ContactItemBinding.inflate(
                        LayoutInflater.from(parent.context), parent, false
                    )
                )
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is ContactViewHolder) {
            holder.contactBind(getItem(position))
        } else if (holder is AlphabetSeparatorViewHolder) {
            holder.alphabetBind(getItem(position))
        }
        var prevChar:String=""
        var toastShown = false
        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val firstVisibleItemPosition = (recyclerView.layoutManager as LinearLayoutManager).findFirstVisibleItemPosition()
                val contact = (recyclerView.adapter as AllContactsAdapter).currentList[firstVisibleItemPosition].namePrefix[0]
                if(!toastShown){
                    if(contact.toString()!=prevChar){
                        Toast.makeText(activity, contact.toString(), Toast.LENGTH_SHORT).makeCustomToast(contact.toString(), activity)
                        toastShown=true
                        prevChar=contact.toString()
                    }
                }
            }
        })
    }

    override fun getItemViewType(position: Int): Int {

        return if (isAlphabetSeparator(position)) {
            ALPHABET_SEPARATOR_VIEW_TYPE
        } else {
            CONTACT_VIEW_TYPE
        }
    }

    private fun isAlphabetSeparator(position: Int): Boolean {
        if (position == 0) {
            return true
        }
        val current = currentList[position].namePrefix.first().uppercaseChar()
        val previous = currentList[position - 1].namePrefix.first().uppercaseChar()
        return current != previous
    }

    fun Toast.makeCustomToast(char: String, activity: Activity) {
        val toast = activity.layoutInflater.inflate(
            R.layout.custom_toast, activity.findViewById(R.id.toast_custom)
        )
        val text = toast.findViewById<TextView>(R.id.alpha)
        text.text = char
        this.apply {
            setGravity(Gravity.TOP, 0, 200)
            duration = Toast.LENGTH_SHORT
            view = toast
            Thread {
                show()
                Thread.sleep(200)
                cancel()
            }.start()
        }
    }
}