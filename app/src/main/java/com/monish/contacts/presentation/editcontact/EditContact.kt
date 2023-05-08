package com.monish.contacts.presentation.editcontact

import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.net.toUri
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.monish.contacts.Draw
import com.monish.contacts.MainActivity
import com.monish.contacts.R
import com.monish.contacts.ShowFullImage
import com.monish.contacts.data.ContactsRepository
import com.monish.contacts.database.ContactsDao
import com.monish.contacts.database.ContactsDatabase
import com.monish.contacts.databinding.FragmentEditContactBinding
import com.monish.contacts.domain.Contact
import com.monish.contacts.interactors.UseCases
import com.monish.contacts.presentation.allcontacts.AllContacts
import com.monish.contacts.presentation.allcontacts.AllContactsViewModel
import com.monish.contacts.presentation.allcontacts.AllContactsViewModelFactory


class EditContact: Fragment() {
    private var _binding:FragmentEditContactBinding?=null
    private val binding get() = _binding!!
    private lateinit var mainActivity: MainActivity
    private var name=""
    private var mobile=""
    private var imgUri=""
    private var id=""
    private lateinit var allContacts:AllContacts
    private var favouriteBool:Boolean=false
    private lateinit var contactsDao: ContactsDao
    private lateinit var viewModel: EditContactViewModel
    private lateinit var contact:MutableList<Contact>
    private lateinit var allContactsViewModel: AllContactsViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        this.arguments?.let {
            name= it.getSerializable("name") as String
            mobile=it.getSerializable("mobile") as String
            imgUri=it.getSerializable("img") as String
            id=it.getSerializable("id") as String
        }

        contactsDao = ContactsDatabase.getInstance(requireContext()).contactDao()
        viewModel = ViewModelProvider(
            this,
            EditContactViewModelFactory(UseCases(ContactsRepository(requireContext(), contactsDao)))
        )[EditContactViewModel::class.java]
        _binding= FragmentEditContactBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        contact= mutableListOf()
        viewModel.getContact()
        viewModel.contact.observe(viewLifecycleOwner){it->
            for(i in it){
                if(i.id==id.toInt()){
                    contact.add(i)
                }
            }

        }
        mainActivity=requireActivity() as MainActivity
        allContacts=AllContacts()
        val colors = intArrayOf(
            Color.MAGENTA,R.drawable.cardview_gradiant) // define your gradient colors
        val gradientDrawable = GradientDrawable(GradientDrawable.Orientation.BR_TL, colors)
        binding.apply {
            back.setOnClickListener {
                allContacts.inEditContact=false
                findNavController().navigate(R.id.action_editContact_to_allContacts)
            }
            mainActivity.binding.apply {
                recents.visibility=View.GONE
                recentsLine.visibility=View.GONE
                allContacts.visibility=View.GONE
                allContactsLine.visibility=View.GONE
                keypad.visibility=View.GONE
                keypadLine.visibility=View.GONE
                favourites.visibility=View.VISIBLE
                editContactInEditPage.visibility=View.VISIBLE
                shareInEdit.visibility=View.VISIBLE
                deleteInEdit.visibility=View.VISIBLE
                favouritesTV.visibility=View.VISIBLE
                shareTV.visibility=View.VISIBLE
                deleteTV.visibility=View.VISIBLE
                editTV.visibility=View.VISIBLE
                favourites.setOnClickListener {
                    if(!favouriteBool){
                        favourites.setImageResource(R.drawable.baseline_favorite_24)
                        favouriteBool=true
                    }
                    else{
                        favourites.setImageResource(R.drawable.baseline_favorite_border_24)
                        favouriteBool=false
                    }
                }
            }
            editContactIV.background = gradientDrawable
            contactName.text=name
            contactMobile.text="+91 "+mobile
            if (!imgUri.isNullOrEmpty()) {
                Glide.with(editContactIV).load(imgUri).into(editContactIV)
                editContactIV.setOnClickListener{
                    val intent= Intent(requireContext(), ShowFullImage::class.java)
                    intent.data=imgUri.toUri()
                    startActivity(intent)
                }
            } else {
                editContactIV.setImageBitmap(Draw.drawText(name,editContactIV,2))
            }
            imageView.setOnClickListener {
                Toast.makeText(requireContext(),"Coming soon...", Toast.LENGTH_SHORT).show()
            }
            imageView2.setOnClickListener {
                Toast.makeText(requireContext(),"Coming soon...",Toast.LENGTH_SHORT).show()
            }
            videoCall.setOnClickListener {
                Toast.makeText(requireContext(),"Coming soon...",Toast.LENGTH_SHORT).show()
            }
            mainActivity.binding.deleteInEdit.setOnClickListener {
                viewModel.deleteContact(contact)
                allContacts.inEditContact=false
                findNavController().navigate(R.id.action_editContact_to_allContacts)

            }
        }
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onPause() {
        println("edit contact paused..............................")
        mainActivity.inAllContacts=true
        mainActivity.binding.apply {
            favourites.visibility=View.GONE
            deleteInEdit.visibility=View.GONE
            shareInEdit.visibility=View.GONE
            editContactInEditPage.visibility=View.GONE
            favouritesTV.visibility=View.GONE
            shareTV.visibility=View.GONE
            deleteTV.visibility=View.GONE
            editTV.visibility=View.GONE
        }
        super.onPause()
    }

    override fun onDestroy() {
        allContacts.inEditContact=false
        println("edit Contact Destroyed..............................................")
        super.onDestroy()
    }

    override fun onStop() {
        println("Edit Contact stopped.........................................")
        super.onStop()
    }

    override fun onResume() {
        super.onResume()
        mainActivity.binding.apply {
            favourites.visibility=View.VISIBLE
            editContactInEditPage.visibility=View.VISIBLE
            shareInEdit.visibility=View.VISIBLE
            deleteInEdit.visibility=View.VISIBLE
            favouritesTV.visibility=View.VISIBLE
            shareTV.visibility=View.VISIBLE
            deleteTV.visibility=View.VISIBLE
            editTV.visibility=View.VISIBLE
        }
        println("Edit comntact on Resumen...................")
    }
}