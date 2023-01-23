package com.inkrodriguez.convidadosapproom

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.lifecycleScope
import com.inkrodriguez.convidadosapproom.databinding.ActivityAddUserBinding
import kotlinx.coroutines.launch

class AddUserActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAddUserBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddUserBinding.inflate(layoutInflater)
        setContentView(binding.root)


        binding.btnAddUser.setOnClickListener {
            addUser()
        }

    }

    private fun addUser() {
        val firstName = binding.edFirstName.text.toString()
        val lastName = binding.edLastName.text.toString()

        lifecycleScope.launch {
            val user = User(firstName, lastName)
            AppDataBase(this@AddUserActivity).getUserDao().addUser(user)
            finish()
        }



    }
}