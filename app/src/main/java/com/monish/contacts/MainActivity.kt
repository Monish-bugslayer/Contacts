package com.monish.contacts

import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import com.monish.contacts.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    private lateinit var view: View
    var inRecent:Boolean=true
    var inAllContacts:Boolean=false
    var inKeypad:Boolean=false
    var inSearch:Boolean=false
    var inAddContact:Boolean=false
    private lateinit var window:Window
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        view = binding.root
        window=getWindow()
        window.statusBarColor=resources.getColor(R.color.status_bar)
        setContentView(view)



        binding.allContacts.setOnClickListener {
            inAllContacts=true
            binding.apply {
                allContacts.setTextColor(resources.getColor(R.color.purple_700))
                allContactsLine.visibility=View.VISIBLE
            }
            if(inKeypad){
                binding.apply {
                    keypad.setTextColor(resources.getColor(R.color.bottomNav))
                    keypadLine.visibility=View.GONE
                }
                findNavController(R.id.fragmentContainerView6).navigate(R.id.action_keypadFragment_to_allContacts)
                inKeypad=false
                inRecent=false
                inSearch=false
            }
            else if(inRecent){
                binding.apply {
                    recents.setTextColor(resources.getColor(R.color.bottomNav))
                    recentsLine.visibility=View.GONE
                }
                findNavController(R.id.fragmentContainerView6).navigate(R.id.action_recents_to_allContacts)
                inRecent=false
                inKeypad=false
                inSearch=false
            }
            else if(inSearch){
                findNavController(R.id.fragmentContainerView6).navigate(R.id.action_searchFragment_to_allContacts)
                inSearch=false
                inKeypad=false
                inRecent=false
            }
        }
        binding.recents.setOnClickListener {
            inRecent=true
            println(inAddContact)
            println(inAllContacts)
            println(inSearch)
            println(inKeypad)
            binding.apply {
                recents.setTextColor(resources.getColor(R.color.purple_700))
                recentsLine.visibility=View.VISIBLE
            }
            if(inKeypad){
                binding.apply {
                    keypad.setTextColor(resources.getColor(R.color.bottomNav))
                    keypadLine.visibility=View.GONE
                }
                findNavController(R.id.fragmentContainerView6).navigate(R.id.action_keypadFragment_to_recents)
                inAllContacts=false
                inKeypad=false
                inSearch=false
            }
            else if(inAllContacts){
                binding.apply {
                    allContacts.setTextColor(resources.getColor(R.color.bottomNav))
                    allContactsLine.visibility=View.GONE
                }
                findNavController(R.id.fragmentContainerView6).navigate(R.id.action_allContacts_to_recents)
                inAllContacts=false
                inKeypad=false
                inSearch=false
            }
            else if(inSearch && !inAllContacts){
                binding.apply {
                    allContacts.setTextColor(resources.getColor(R.color.bottomNav))
                    allContactsLine.visibility=View.GONE
                }
                findNavController(R.id.fragmentContainerView6).navigate(R.id.action_searchFragment_to_recents)
                inSearch=false
                inKeypad=false
                inAllContacts=false
            }
        }

        binding.keypad.setOnClickListener{
            inKeypad=true
            println(inAddContact)
            println(inAllContacts)
            println(inSearch)
            println(inKeypad)
            binding.apply {
                keypad.setTextColor(resources.getColor(R.color.purple_700))
                keypadLine.visibility=View.VISIBLE
            }
            if(inRecent){
                binding.apply {
                    recents.setTextColor(resources.getColor(R.color.bottomNav))
                    recentsLine.visibility=View.GONE
                }
                findNavController(R.id.fragmentContainerView6).navigate(R.id.action_recents_to_keypadFragment)
                inRecent=false
                inAllContacts=false
                inSearch=false
            }
            else if(inAllContacts){
                binding.apply {
                    allContacts.setTextColor(resources.getColor(R.color.bottomNav))
                    allContactsLine.visibility=View.GONE
                }
                findNavController(R.id.fragmentContainerView6).navigate(R.id.action_allContacts_to_keypadFragment)
                inAllContacts=false
                inRecent=false
                inSearch=false
            }
            else if(inSearch){
                binding.apply {
                    allContacts.setTextColor(resources.getColor(R.color.bottomNav))
                    allContactsLine.visibility=View.GONE
                }
                findNavController(R.id.fragmentContainerView6).navigate(R.id.action_searchFragment_to_keypadFragment)
                inAllContacts=false
                inRecent=false
                inSearch=false
            }
        }
    }
}