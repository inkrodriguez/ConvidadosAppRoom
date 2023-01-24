package com.inkrodriguez.convidadosapproom

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.lifecycleScope
import com.inkrodriguez.convidadosapproom.databinding.ActivityAddUserBinding
import kotlinx.coroutines.launch

class AddUserActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddUserBinding
    private var user: User? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddUserBinding.inflate(layoutInflater)
        setContentView(binding.root)

        user = intent.getSerializableExtra("Data") as? User

        if(user == null) binding.btnAddOrUpdateUser.text = "Add User"
        else {
            binding.btnAddOrUpdateUser.text = "Update User"
            binding.edFirstName.setText(user?.firstName.toString())
            binding.edLastName.setText(user?.lastName.toString())
        }

        binding.btnAddOrUpdateUser.setOnClickListener {
            addUser()
        }

    }

    private fun addUser() {
        val firstName = binding.edFirstName.text.toString()
        val lastName = binding.edLastName.text.toString()

        lifecycleScope.launch {
            if (user == null) {
                val user = User(firstName, lastName)
                AppDataBase(this@AddUserActivity).getUserDao().addUser(user)
                finish()
            } else {
                val u = User(firstName, lastName)
                u.id = user?.id ?: 0
                AppDataBase(this@AddUserActivity).getUserDao().updateUser(u)
                finish()
            }
        }

    }

}