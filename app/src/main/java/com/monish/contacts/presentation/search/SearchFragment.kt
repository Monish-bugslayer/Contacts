package com.monish.contacts.presentation.search

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnFocusChangeListener
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import com.monish.contacts.MainActivity
import com.monish.contacts.databinding.FragmentSearchBinding
import com.monish.contacts.domain.Contact
import com.monish.contacts.presentation.allcontacts.AllContactsViewModel


class SearchFragment : Fragment() {
    private var _binding: FragmentSearchBinding? = null
    private lateinit var searchAdapter: SearchBarAdapter
    private val binding get() = _binding!!
    private lateinit var viewModel: AllContactsViewModel
    private var data: MutableList<Contact> = mutableListOf()
    private var contactList: MutableList<Contact> = mutableListOf()
    private lateinit var mainActivity: MainActivity


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mainActivity = activity as MainActivity
        this.arguments?.let {
            this.viewModel=it.getSerializable("viewModel") as AllContactsViewModel
        }
        _binding = FragmentSearchBinding.inflate(inflater, container, false)
        viewModel.contact.observe(viewLifecycleOwner) {
            data.addAll(it.toMutableList())
            for (i in data) {
                if (i.namePrefix.length >=1) {
                    contactList.add(i)
                }
            }
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.textView.visibility = View.GONE
        searchAdapter = SearchBarAdapter()
        searchAdapter.submitList(contactList)
        binding.searchRV.adapter = searchAdapter
        searchAdapter.originalDataList=contactList

        binding.apply {
            searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String?): Boolean {
                    return false
                }
                override fun onQueryTextChange(newText: String?): Boolean {
                    searchAdapter.filter(newText!!)
                    return false
                }

            })
            searchView.setOnQueryTextFocusChangeListener(OnFocusChangeListener { view, hasFocus ->
                if (hasFocus) {
                    showInputMethod(view.findFocus())
                }
            })

        }
    }
    private fun showInputMethod(view: View) {
        val imm: InputMethodManager? =
            requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager?
        if (imm != null) {
            imm.showSoftInput(view, 0)
        }
    }

    override fun onPause() {
        println("Search onPause.................................")
        if(!mainActivity.inRecent && !mainActivity.inKeypad){
            mainActivity.inAllContacts = true
        }
        super.onPause()
    }

    override fun onResume() {
        println("Search onResume.....................")
        binding.searchView.requestFocus()
        super.onResume()

    }
    override fun onDestroy() {
        println("Search Contact Destroyed..............................................")
        super.onDestroy()
    }

    override fun onStop() {
        println("Search Contact stopped.........................................")
        super.onStop()
    }
}