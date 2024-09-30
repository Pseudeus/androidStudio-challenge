package com.example.challenge

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import com.example.challenge.database.EmployeeDatabase
import com.example.challenge.repository.EmployeeRepository
import com.example.challenge.viewmodel.EmployeeViewModel
import com.example.challenge.viewmodel.EmployeeViewModelFactory
import com.example.notesroompractice.R

class MainActivity : AppCompatActivity() {
    lateinit var employeeViewModel: EmployeeViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        setupViewModel()
    }

    private fun setupViewModel() {
        val employeeRepository = EmployeeRepository(EmployeeDatabase(this))
        val viewModelProviderFactory = EmployeeViewModelFactory(application, employeeRepository)
        employeeViewModel = ViewModelProvider(this, viewModelProviderFactory)[EmployeeViewModel::class.java]
    }
}