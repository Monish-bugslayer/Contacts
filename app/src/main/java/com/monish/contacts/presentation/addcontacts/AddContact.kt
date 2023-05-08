package com.monish.contacts.presentation.addcontacts


import android.content.Context
import android.content.Intent


import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher

import androidx.annotation.RequiresApi

import androidx.core.net.toUri
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController

import com.monish.contacts.CroppingImage
import com.monish.contacts.MainActivity
import com.monish.contacts.R

import com.monish.contacts.ShowFullImage
import com.monish.contacts.data.ContactsRepository
import com.monish.contacts.database.ContactsDao
import com.monish.contacts.database.ContactsDatabase

import com.monish.contacts.databinding.FragmentAddContactBinding
import com.monish.contacts.domain.Contact
import com.monish.contacts.interactors.UseCases

import java.util.*


open class AddContact : Fragment() {
    private var _binding: FragmentAddContactBinding? = null
    private val binding get() = _binding!!
    private var expandBool: Boolean = true
    private var namePrefix: String = ""
    private var nameSuffix: String = ""
    private var firstName: String = ""
    private var middleName: String = ""
    private var lastName: String = ""
    private  var mobile:String=""
    private var company:String=""
    private var mailId:String=""
    private var showImageBool:Boolean=false
    private var profileImageUri: String=""
    private lateinit var cropActivityResultLauncher: ActivityResultLauncher<Any?>
    private lateinit var contactsDao: ContactsDao
    private lateinit var viewModel: AddContactViewModel
    private lateinit var mainActivity: MainActivity


    @RequiresApi(Build.VERSION_CODES.P)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentAddContactBinding.inflate(inflater, container, false)
        contactsDao = ContactsDatabase.getInstance(requireContext()).contactDao()
        viewModel = ViewModelProvider(
            this,
            AddContactViewModelFactory(UseCases(ContactsRepository(requireContext(), contactsDao)))
        )[AddContactViewModel::class.java]
        mainActivity=activity as MainActivity
        mainActivity.binding.apply {
            recents.visibility=View.GONE
            recentsLine.visibility=View.GONE
            allContacts.visibility=View.GONE
            allContactsLine.visibility=View.GONE
            keypad.visibility=View.GONE
            keypadLine.visibility=View.GONE
        }
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        cropActivityResultLauncher=registerForActivityResult(CroppingImage.cropActivityResultContract){
            it.let {uri->
                if (uri != null) {
                    profileImageUri=uri.toString()
                    binding.profileImage.setImageURI(uri)
                }
            }
        }
        binding.addContactBack.setOnClickListener {
            findNavController().navigate(R.id.addContact_to_allContact)
        }
        binding.expandNamesIcon.setOnClickListener {
            if (expandBool) {
                binding.apply {
                    lastNameTextView.visibility = View.VISIBLE
                    middleNameTextView.visibility = View.VISIBLE
                    firstNameTextView.visibility = View.VISIBLE
                    nameSuffixTextView.visibility = View.VISIBLE
                    expandNamesIcon.animate().rotation(180f)
                }
                expandBool = false
            } else {
                binding.expandNamesIcon.animate().rotation(360f)
                binding.apply {
                    lastNameTextView.visibility = View.GONE
                    middleNameTextView.visibility = View.GONE
                    firstNameTextView.visibility = View.GONE
                    nameSuffixTextView.visibility = View.GONE
                }
                expandBool = true
            }
        }
        binding.profileImage.setOnClickListener {
            if(!showImageBool){
                cropActivityResultLauncher.launch(null)
                showImageBool=true
            }
            else{
                val intent= Intent(requireContext(),ShowFullImage::class.java)
                intent.data=profileImageUri.toUri()
                startActivity(intent)
            }

        }
        binding.saveButton.setOnClickListener {
            if(binding.namePrefixTextView.text.toString()!=""){
                binding.apply {
                    namePrefix=binding.namePrefixTextView.text.toString().capitalize(Locale.ROOT)
                    nameSuffix = nameSuffixTextView.text.toString()
                    firstName = firstNameTextView.text.toString()
                    middleName = middleNameTextView.text.toString()
                    lastName = lastNameTextView.text.toString()
                    mobile=mobileNumber.text.toString()
                    company=companyName.text.toString()
                    mailId=email.text.toString()
                    viewModel.addContact(Contact(
                        0,namePrefix,firstName,middleName,lastName,nameSuffix,mobile,mailId,company,profileImageUri
                    ))
                    Toast.makeText(requireContext(),"Saved",Toast.LENGTH_SHORT).show()
                }
            }
            else{
                Toast.makeText(requireContext(),"Contact name should not empty",Toast.LENGTH_SHORT).show()
            }

            Navigation.findNavController(binding.root).navigate(R.id.addContact_to_allContact)
        }
        binding.cancelButton2.setOnClickListener {
            Toast.makeText(requireContext(),"Canceled",Toast.LENGTH_SHORT).show()
            Navigation.findNavController(binding.root).navigate(R.id.addContact_to_allContact)
        }
    }

    override fun onResume() {
        println("Add contact resumed..................................")
        super.onResume()
        binding.namePrefixTextView.requestFocus()
        val imm = requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.showSoftInput(binding.namePrefixTextView, InputMethodManager.SHOW_IMPLICIT)
    }

    override fun onPause() {
        super.onPause()
        println("Add contact paused....................................")
        mainActivity.inAllContacts=true
        mainActivity.inAddContact=false
    }

    override fun onDestroy() {
        println("Add Contact Destroyed..............................................")
        super.onDestroy()
    }

    override fun onStop() {
        println("AddContact stopped.........................................")
        super.onStop()
    }
}