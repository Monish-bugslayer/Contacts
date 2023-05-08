package com.monish.contacts

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Window
import com.monish.contacts.databinding.ActivityShowFullImageBinding

class ShowFullImage : AppCompatActivity() {
    private lateinit var binding:ActivityShowFullImageBinding
    private lateinit var window: Window
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityShowFullImageBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val imageUri=intent.data
        binding.showFullImage.setImageURI(imageUri)
        window=getWindow()
        window.statusBarColor=resources.getColor(R.color.black)

    }
}