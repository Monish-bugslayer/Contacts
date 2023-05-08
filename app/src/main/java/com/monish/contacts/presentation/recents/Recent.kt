package com.monish.contacts.presentation.recents


import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.net.toUri
import androidx.core.view.ViewCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.appbar.AppBarLayout
import com.monish.contacts.MainActivity
import com.monish.contacts.R
import com.monish.contacts.databinding.FragmentRecentsBinding
import com.monish.contacts.domain.Contact
import com.trendyol.bubblescrollbarlib.BubbleTextProvider
import kotlin.math.abs


class Recent : Fragment() {
    private var _binding: FragmentRecentsBinding? = null
    private val binding get() = _binding!!
    private lateinit var recentsAdapter: RecentsAdapter
    private lateinit var rView: RecyclerView
    private lateinit var contacts: MutableList<Contact>
    private lateinit var appBarLayout: AppBarLayout
    private lateinit var toolBar: androidx.appcompat.widget.Toolbar
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {

        _binding = FragmentRecentsBinding.inflate(inflater, container, false)
        toolBar = binding.toolBar
        toolBar.post {
            toolBar.setOnMenuItemClickListener {
                if (it.itemId == R.id.filter) {
                    Toast.makeText(requireContext(), "Comming Soon", Toast.LENGTH_SHORT).show()
                }
                else if(it.itemId==R.id.search_badge_menu){
                    Toast.makeText(requireContext(), "Comming Soon", Toast.LENGTH_SHORT).show()
                }
                true
            }
        }
        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        contacts = mutableListOf()
        contacts.add(
            Contact(
                1, "Contact 1", "", "", "", "", "6382691813", "", "", ""
            )
        )
        contacts.add(
            Contact(
                1, "Contact 2", "", "", "", "", "6382691813", "", "", ""
            )
        )
        recentsAdapter = RecentsAdapter(contacts, activity as MainActivity, binding.recyclerView)
        rView = binding.recyclerView
        rView.adapter = recentsAdapter
        val layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        rView.layoutManager = layoutManager
        recentsAdapter.submitList(contacts)
        rView.isVerticalScrollBarEnabled = true
        binding.bubbleScroll.attachToRecyclerView(rView)
        ViewCompat.setNestedScrollingEnabled(binding.bubbleScroll, false)
        appBarLayout = binding.appbar
        appBarLayout.addOnOffsetChangedListener { appBarLayout, verticalOffset ->
            val isCollapsed = abs(verticalOffset) == appBarLayout.totalScrollRange
            if (isCollapsed) {
                binding.bubbleScroll.visibility = View.VISIBLE
                binding.bubbleScroll.bubbleTextProvider = BubbleTextProvider {
                    recentsAdapter.contacts[it].namePrefix[0].toString()
                }
            } else {
                binding.bubbleScroll.visibility = View.GONE
            }
        }

    }

    override fun onDestroy() {
        println("RecentStopper..................")
        super.onDestroy()
        _binding = null
    }


    override fun onStop() {
        println("Recent Contact stopped.........................................")
        super.onStop()
    }

    override fun onPause() {
        super.onPause()
        println("Recent onPause.....................")
    }

    override fun onResume() {
        super.onResume()
        println("Recent resume...................................")
    }


}