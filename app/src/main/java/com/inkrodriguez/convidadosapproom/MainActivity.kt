package com.inkrodriguez.convidadosapproom

import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.inkrodriguez.convidadosapproom.databinding.ActivityMainBinding
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var viewModel: MainViewModel

    private var mAdapter: UserAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(this)[MainViewModel::class.java]

        binding.btnAdd.setOnClickListener {
            val intent = Intent(this, AddUserActivity::class.java)
            startActivity(intent)
        }
    }

    private fun setAdapter(list: List<User>){
        mAdapter?.setData(list)
    }


    override fun onResume() {
        super.onResume()

        lifecycleScope.launch {
            val userList = AppDataBase(this@MainActivity).getUserDao().getAllUser()

            mAdapter = UserAdapter()
            binding.recyclerView.apply {
                layoutManager = LinearLayoutManager(this@MainActivity)
                adapter = mAdapter
                setAdapter(userList)

                    mAdapter?.setOnActionEditListener {
                        val intent = Intent(this@MainActivity, AddUserActivity::class.java)
                        intent.putExtra("Data", it)
                        startActivity(intent)
                    }

                    mAdapter?.setOnActionDeleteListener {
                        val builder = AlertDialog.Builder(this@MainActivity)
                        builder.setMessage("Are you sure you want to delete?")
                        builder.setPositiveButton("Yes"){ p0, p1 ->
                            lifecycleScope.launch {
                                AppDataBase(this@MainActivity).getUserDao().deleteUser(it)
                                val userList = AppDataBase(this@MainActivity).getUserDao().getAllUser()
                                setAdapter(userList)
                            }
                                p0.dismiss()
                        }
                        builder.setNegativeButton("No"){ p0, p1 ->
                            p0.dismiss()
                        }
                        val dialog = builder.create()
                        dialog.show()
                    }


                }
            }
        }
    }