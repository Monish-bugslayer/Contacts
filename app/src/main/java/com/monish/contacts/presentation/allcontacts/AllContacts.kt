package com.monish.contacts.presentation.allcontacts


import android.os.Bundle
import android.view.LayoutInflater

import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils

import androidx.core.view.ViewCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.appbar.AppBarLayout
import com.monish.contacts.MainActivity
import com.monish.contacts.R
import com.monish.contacts.data.ContactsRepository
import com.monish.contacts.database.ContactsDao
import com.monish.contacts.database.ContactsDatabase
import com.monish.contacts.databinding.FragmentAllContactsBinding
import com.monish.contacts.interactors.UseCases
import com.monish.contacts.presentation.search.SearchFragment
import com.trendyol.bubblescrollbarlib.BubbleTextProvider
import kotlin.math.abs


class AllContacts : Fragment(){
    private var _binding: FragmentAllContactsBinding? = null
    val binding get() = _binding!!
    private lateinit var rView: RecyclerView
    private lateinit var appBarLayout: AppBarLayout
    private lateinit var allContactsAdapter: AllContactsAdapter
    private lateinit var contactsDao: ContactsDao
   lateinit var viewModel: AllContactsViewModel
    private lateinit var toolBar: androidx.appcompat.widget.Toolbar
    private lateinit var mainActivity: MainActivity
    var inEditContact:Boolean=false
    var isLongPressed:Boolean=false


    override fun onCreateView(

        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        println()
        _binding = FragmentAllContactsBinding.inflate(inflater, container, false)
        contactsDao = ContactsDatabase.getInstance(requireContext()).contactDao()
        viewModel = ViewModelProvider(
            this,
            AllContactsViewModelFactory(UseCases(ContactsRepository(requireContext(), contactsDao)))
        )[AllContactsViewModel::class.java]

        mainActivity=activity as MainActivity
        toolBar = binding.toolBar
        toolBar.post {
            toolBar.setOnMenuItemClickListener { item ->
                if (item.itemId == R.id.add_contact_menu) {
                    mainActivity.inAddContact=true
                    mainActivity.inAllContacts=false
                    Navigation.findNavController(binding.root)
                        .navigate(R.id.action_allContacts_to_addContact)
                } else if (item.itemId == R.id.search_badge_menu) {
                    val searchFragment:SearchFragment = SearchFragment()
                    mainActivity.inSearch=true
                    mainActivity.inAllContacts=false
                    val bundle = Bundle().apply {
                        this.putSerializable("viewModel",viewModel)
                    }
                    searchFragment.arguments=bundle
                    mainActivity=activity as MainActivity
                    Navigation.findNavController(binding.root)
                        .navigate(R.id.action_allContacts_to_searchFragment,bundle)
                }
                true
            }

        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.getContact()
        rView = binding.recyclerView
        val layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        rView.layoutManager = layoutManager
        viewModel.contact.observe(viewLifecycleOwner, Observer {
            for (i in it){
                println(i)
            }
            allContactsAdapter =
                AllContactsAdapter(activity as MainActivity, binding.recyclerView, shouDialogue = { showDialogue() },
                    { hideDialogue() })
            rView.adapter = allContactsAdapter
            allContactsAdapter.submitList(it)
            allContactsAdapter.allContacts=this
            if(allContactsAdapter.currentList.isEmpty()){
                mainActivity.binding.noContacts.visibility=View.VISIBLE
            }
            else{
                mainActivity.binding.noContacts.visibility=View.GONE
            }
        })
        rView.isVerticalScrollBarEnabled = true
        binding.bubbleScroll.attachToRecyclerView(rView)
        ViewCompat.setNestedScrollingEnabled(binding.bubbleScroll, false)
        appBarLayout = binding.appbar
        appBarLayout.addOnOffsetChangedListener { appBarLayout, verticalOffset ->
            val isCollapsed = abs(verticalOffset) == appBarLayout.totalScrollRange
            if (isCollapsed) {
                binding.bubbleScroll.visibility = View.VISIBLE
                binding.bubbleScroll.bubbleTextProvider = BubbleTextProvider {
                    allContactsAdapter.currentList[it].namePrefix[0].toString()
                }
            } else {
                binding.bubbleScroll.visibility = View.GONE
            }
        }
    }
    private fun showDialogue(){
        val mainActivity:MainActivity=requireActivity() as MainActivity
        val animation: Animation? =AnimationUtils.loadAnimation(requireContext(),R.anim.slide_from_bottom)
        mainActivity.binding.apply {
            keypad.visibility=View.GONE
            allContacts.visibility=View.GONE
            recents.visibility=View.GONE
            allContactsLine.visibility=View.GONE
            deleteInBottomNav.visibility=View.VISIBLE
            messageInBottomNav.visibility=View.VISIBLE
            shareInBottomNav.visibility=View.VISIBLE
            deleteInBottomNav.startAnimation(animation)
            messageInBottomNav.startAnimation(animation)
            shareInBottomNav.startAnimation(animation)

            deleteInBottomNav.setOnClickListener {
                println("Selected Contacts${allContactsAdapter.selectedContacts}..........")
                viewModel.deleteContact(allContactsAdapter.selectedContacts)
                hideDialogue()
            }
        }
    }
    private fun hideDialogue(){
        val mainActivity:MainActivity=requireActivity() as MainActivity
        mainActivity.binding.apply {
            keypad.visibility=View.VISIBLE
            allContacts.visibility=View.VISIBLE
            recents.visibility=View.VISIBLE
            allContactsLine.visibility=View.VISIBLE
            deleteInBottomNav.visibility=View.GONE
            messageInBottomNav.visibility=View.GONE
            shareInBottomNav.visibility=View.GONE
        }
    }
    override fun onPause() {
        println("All contact onPause...................")
        if(!mainActivity.inAllContacts){
            mainActivity.binding.noContacts.visibility=View.GONE
        }
        super.onPause()
    }

    override fun onResume() {
        super.onResume()
        mainActivity.inAllContacts=true
        mainActivity.inSearch=false
        inEditContact=false
        println("AllCointact resumed.......................")
        viewModel.contact.observe(viewLifecycleOwner) {
            for (i in it) {
                println("Resume $it")
            }
        }
        if(mainActivity.inAllContacts && !inEditContact && !isLongPressed){
            mainActivity.binding.apply {
                recents.visibility=View.VISIBLE
                recentsLine.visibility=View.GONE
                allContacts.visibility=View.VISIBLE
                allContactsLine.visibility=View.VISIBLE
                keypad.visibility=View.VISIBLE
                keypadLine.visibility=View.GONE
            }
        }
    }

    override fun onDestroy() {
        println(" all contact Destroiyed...........................")
        super.onDestroy()
    }



    override fun onStop() {
        println("AllContact stopped.........................................")
        super.onStop()
    }

}