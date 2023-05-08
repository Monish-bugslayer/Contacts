package com.monish.contacts.presentation.keypad

import android.content.Context
import android.os.Bundle
import android.view.*
import android.view.View.OnTouchListener
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.core.content.ContextCompat.getSystemService
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.monish.contacts.MainActivity
import com.monish.contacts.data.ContactsRepository
import com.monish.contacts.database.ContactsDao
import com.monish.contacts.database.ContactsDatabase
import com.monish.contacts.databinding.FragmentKeypadBinding
import com.monish.contacts.domain.Contact
import com.monish.contacts.interactors.UseCases


class KeypadFragment : Fragment() {
    private var _binding: FragmentKeypadBinding? = null
    private val binding get() = _binding!!
    private lateinit var contactsDao: ContactsDao
    private lateinit var viewModel: KeypadViewModel
    private lateinit var keypadAdapter: KeypadAdapter
    private var data: MutableList<Contact> = mutableListOf()
    private var contactList: MutableList<Contact> = mutableListOf()
    private var emptyList:MutableList<Contact> = mutableListOf()
    var numberText:String=""
    private lateinit var mainActivity:MainActivity

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentKeypadBinding.inflate(inflater, container, false)
        contactsDao = ContactsDatabase.getInstance(requireContext()).contactDao()
        viewModel = ViewModelProvider(
            this,
            KeypadViewModelFactory(UseCases(ContactsRepository(requireContext(), contactsDao)))
        )[KeypadViewModel::class.java]
        viewModel.getContact()
        activity?.getWindow()?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mainActivity=requireActivity() as MainActivity
        viewModel.contact.observe(viewLifecycleOwner) {

            data.addAll(it.toMutableList())
            for (i in data) {
                if (i.namePrefix.length >=1) {
                    contactList.add(i)
                }
            }
            keypadAdapter= KeypadAdapter(binding,numberText)
            binding.recyclerView.adapter=keypadAdapter
            keypadAdapter.originalDataList=contactList
        }

        binding.keypadLayout.numberDisplay.setOnTouchListener(OnTouchListener { v, event ->
            val action = event.action
            if (action == MotionEvent.ACTION_UP) {
                val imm: InputMethodManager? =
                    requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager?
                imm?.hideSoftInputFromWindow(v.windowToken, 0)
                return@OnTouchListener true
            }
            false
        })
        binding.apply {
            keypadLayout.buttons.apply {
                one.setOnClickListener{
                    clear.visibility=View.VISIBLE
                    videoCall.visibility=View.VISIBLE
                    numberText+=one.text
                    binding.keypadLayout.numberDisplay.setText(numberText)
                    binding.keypadLayout.numberDisplay.setSelection(numberText.length)
                    keypadAdapter.filter(numberText)
                }
                two.setOnClickListener{
                    clear.visibility=View.VISIBLE
                    videoCall.visibility=View.VISIBLE
                    numberText+=two.text
                    binding.keypadLayout.numberDisplay.setText(numberText)
                    binding.keypadLayout.numberDisplay.setSelection(numberText.length)
                    keypadAdapter.filter(numberText)
                }
                three.setOnClickListener{
                    clear.visibility=View.VISIBLE
                    videoCall.visibility=View.VISIBLE
                    numberText+=three.text
                    binding.keypadLayout.numberDisplay.setText(numberText)
                    binding.keypadLayout.numberDisplay.setSelection(numberText.length)
                    keypadAdapter.filter(numberText)
                }
                four.setOnClickListener{
                    clear.visibility=View.VISIBLE
                    videoCall.visibility=View.VISIBLE
                    numberText+=four.text
                    binding.keypadLayout.numberDisplay.setText(numberText)
                    binding.keypadLayout.numberDisplay.setSelection(numberText.length)
                    keypadAdapter.filter(numberText)
                }
                five.setOnClickListener{
                    clear.visibility=View.VISIBLE
                    videoCall.visibility=View.VISIBLE
                    numberText+=five.text
                    binding.keypadLayout.numberDisplay.setText(numberText)
                    binding.keypadLayout.numberDisplay.setSelection(numberText.length)
                    keypadAdapter.filter(numberText)
                }
                six.setOnClickListener{
                    clear.visibility=View.VISIBLE
                    videoCall.visibility=View.VISIBLE
                    numberText+=six.text
                    binding.keypadLayout.numberDisplay.setText(numberText)
                    binding.keypadLayout.numberDisplay.setSelection(numberText.length)
                    keypadAdapter.filter(numberText)
                }
                seven.setOnClickListener{
                    clear.visibility=View.VISIBLE
                    videoCall.visibility=View.VISIBLE
                    numberText+=seven.text
                    binding.keypadLayout.numberDisplay.setText(numberText)
                    binding.keypadLayout.numberDisplay.setSelection(numberText.length)
                    keypadAdapter.filter(numberText)
                }
                eight.setOnClickListener{
                    clear.visibility=View.VISIBLE
                    videoCall.visibility=View.VISIBLE
                    numberText+=eight.text
                    binding.keypadLayout.numberDisplay.setText(numberText)
                    binding.keypadLayout.numberDisplay.setSelection(numberText.length)
                    keypadAdapter.filter(numberText)
                }
                nine.setOnClickListener{
                    clear.visibility=View.VISIBLE
                    videoCall.visibility=View.VISIBLE
                    numberText+=nine.text
                    binding.keypadLayout.numberDisplay.setText(numberText)
                    binding.keypadLayout.numberDisplay.setSelection(numberText.length)
                    keypadAdapter.filter(numberText)
                }
                zero.setOnClickListener{
                    clear.visibility=View.VISIBLE
                    videoCall.visibility=View.VISIBLE
                    numberText+=zero.text
                    binding.keypadLayout.numberDisplay.setText(numberText)
                    binding.keypadLayout.numberDisplay.setSelection(numberText.length)
                    keypadAdapter.filter(numberText)
                }
                asterisk.setOnClickListener{
                    clear.visibility=View.VISIBLE
                    videoCall.visibility=View.VISIBLE
                    numberText+=asterisk.text
                    binding.keypadLayout.numberDisplay.setText(numberText)
                    binding.keypadLayout.numberDisplay.setSelection(numberText.length)
                    keypadAdapter.filter(numberText)
                }
                hash.setOnClickListener{
                    clear.visibility=View.VISIBLE
                    videoCall.visibility=View.VISIBLE
                    numberText+=hash.text
                    binding.keypadLayout.numberDisplay.setText(numberText)
                    binding.keypadLayout.numberDisplay.setSelection(numberText.length)
                    keypadAdapter.filter(numberText)
                }
                videoCall.setOnClickListener{
                    Toast.makeText(requireContext(),"Coming soon...",Toast.LENGTH_SHORT).show()
                }
                clear.setOnClickListener{
                    numberText=numberText.removeSuffix(numberText.last().toString())
                    binding.keypadLayout.numberDisplay.setText(numberText)
                    binding.keypadLayout.numberDisplay.setSelection(numberText.length)
                    keypadAdapter.filter(numberText)
                    if(numberText.length==0){
                        clear.visibility=View.GONE
                        videoCall.visibility=View.GONE
                        keypadAdapter.submitList(emptyList)
                    }
                }
                recyclerView.setOnClickListener {

                }
                recyclerView.setOnTouchListener(object : View.OnTouchListener {
                    override fun onTouch(v: View, event: MotionEvent): Boolean {
                        when (event.action) {
                            MotionEvent.ACTION_DOWN -> {
                                mainActivity.binding.keyboard.visibility=View.VISIBLE
                                keypadLayout.keypadResultLayout.visibility=View.GONE
                                mainActivity.binding.apply {
                                    recents.visibility=View.GONE
                                    recentsLine.visibility=View.GONE
                                    allContacts.visibility=View.GONE
                                    allContactsLine.visibility=View.GONE
                                    keypad.visibility=View.GONE
                                    keypadLine.visibility=View.GONE
                                }

                            }
                            MotionEvent.ACTION_UP -> {
                                keypadLayout.keypadResultLayout.visibility=View.GONE
                                mainActivity.binding.keyboard.visibility=View.VISIBLE
                                mainActivity.binding.apply {
                                    recents.visibility=View.GONE
                                    recentsLine.visibility=View.GONE
                                    allContacts.visibility=View.GONE
                                    allContactsLine.visibility=View.GONE
                                    keypad.visibility=View.GONE
                                    keypadLine.visibility=View.GONE
                                    keyboard.setOnClickListener {
                                        keypadLayout.keypadResultLayout.visibility=View.VISIBLE
                                        keyboard.visibility=View.GONE
                                        recents.visibility=View.VISIBLE
                                        recentsLine.visibility=View.GONE
                                        allContacts.visibility=View.VISIBLE
                                        allContactsLine.visibility=View.GONE
                                        keypad.visibility=View.VISIBLE
                                        keypadLine.visibility=View.VISIBLE
                                    }
                                }
                                return true
                            }
                        }
                        return false
                    }
                })

            }

        }
    }

    override fun onResume() {
        binding.keypadLayout.numberDisplay.requestFocus()
        super.onResume()
    }


}