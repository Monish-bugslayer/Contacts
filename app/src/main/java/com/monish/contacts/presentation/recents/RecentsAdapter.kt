package com.monish.contacts.presentation.recents

import android.app.Activity
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.monish.contacts.ContactDiffCallBack
import com.monish.contacts.MainActivity
import com.monish.contacts.R
import com.monish.contacts.databinding.AlphabetSeparatorItemBinding
import com.monish.contacts.databinding.ContactItemBinding
import com.monish.contacts.domain.Contact


class RecentsAdapter(
    val contacts: MutableList<Contact>,
    private val activity: MainActivity,
    private val recyclerView: RecyclerView
) : ListAdapter<Contact, RecyclerView.ViewHolder>(ContactDiffCallBack()) {
    companion object {
        const val ALPHABET_SEPARATOR_VIEW_TYPE = 0
        const val CONTACT_VIEW_TYPE = 1
        const val BOTH_VIEW_TYPE=-1
    }

    inner class ContactViewHolder(private val binding: ContactItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun contactBind(contact: Contact) {
            var bool = false
            binding.apply {
                name.text = contact.namePrefix
                if(!contact.imgUri.isNullOrEmpty()){
                    Glide.with(profile).load("")

                }
                name.setOnClickListener {
                    if (!bool) {
                        mobile.visibility = View.VISIBLE
                        call.visibility = View.VISIBLE
                        message.visibility = View.VISIBLE
                        info.visibility = View.VISIBLE
                        bool = true
                    } else {
                        mobile.visibility = View.GONE
                        call.visibility = View.GONE
                        message.visibility = View.GONE
                        info.visibility = View.GONE
                        bool = false
                    }
                }
            }
        }

    }

    class AlphabetSeparatorViewHolder(private val binding: AlphabetSeparatorItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        var separatorText: String = "A"
        var firstLetter = "A"
        fun alphabetBind(contact: Contact) {
            binding.apply {
                firstLetter = contact.namePrefix.first().uppercaseChar().toString()
                alphabetSeparatorTextview.text = firstLetter
                separatorText = alphabetSeparatorTextview.text.toString()
            }
        }

    }

    override fun onCreateViewHolder(
        parent: ViewGroup, viewType: Int
    ): RecyclerView.ViewHolder {
        return when (viewType) {
            ALPHABET_SEPARATOR_VIEW_TYPE -> {
                AlphabetSeparatorViewHolder(
                    AlphabetSeparatorItemBinding.inflate(
                        LayoutInflater.from(
                            parent.context
                        ), parent, false
                    )
                )
            }
            BOTH_VIEW_TYPE -> {
                ContactViewHolder(
                    ContactItemBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false
                    )
                )
                AlphabetSeparatorViewHolder(
                    AlphabetSeparatorItemBinding.inflate(
                        LayoutInflater.from(
                            parent.context
                        ), parent, false
                    )
                )
            }
            else->{
                ContactViewHolder(
                    ContactItemBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false
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

        val layoutManager = recyclerView.layoutManager as LinearLayoutManager
        val pos = layoutManager.findFirstCompletelyVisibleItemPosition()
        val viewHolder = recyclerView.findViewHolderForAdapterPosition(pos)
        if (viewHolder is AlphabetSeparatorViewHolder?) {
            if (viewHolder?.separatorText == null) {
                Toast.makeText(activity, "A", Toast.LENGTH_SHORT).makeCustomToast("A", activity)
            } else if (position != 0 && position != 1) {
                viewHolder?.separatorText?.let {
                    Toast.makeText(
                        activity, viewHolder.separatorText, Toast.LENGTH_SHORT
                    ).makeCustomToast(it, activity)
                }
            } else if (position == 0 || position == 1) {
                Toast.makeText(activity, viewHolder?.firstLetter.toString(), Toast.LENGTH_SHORT)
                    .makeCustomToast(viewHolder?.firstLetter.toString(), activity)
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (isAlphabetSeparator(position)==-1) {
            println("BOTH_VIEW_TYPE............................................................")
            BOTH_VIEW_TYPE
        }
        else if(isAlphabetSeparator(position)==1) {
            println("CONTACT_VIEW_TYPE............................................................")
            CONTACT_VIEW_TYPE
        }
        else{
            println("ALPHABET_VIEW_TYPE............................................................")
            ALPHABET_SEPARATOR_VIEW_TYPE
        }
    }

    private fun isAlphabetSeparator(position: Int):Int {
        if (position == 0) {
            return -1
        }
        val current = contacts[position].namePrefix.first().uppercaseChar()
        val previous = contacts[position - 1].namePrefix.first().uppercaseChar()
        if(current != previous){
            return 0
        }
        return 1
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