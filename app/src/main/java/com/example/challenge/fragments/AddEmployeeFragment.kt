package com.example.challenge.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.lifecycle.Lifecycle
import androidx.navigation.findNavController
import com.example.challenge.CustomDatePicker
import com.example.challenge.MainActivity
import com.example.challenge.location.LocationCallback
import com.example.challenge.model.Employee
import com.example.challenge.viewmodel.EmployeeViewModel
import com.example.notesroompractice.R
import com.example.notesroompractice.databinding.FragmentAddEmployeeBinding

class AddEmployeeFragment : Fragment(R.layout.fragment_add_employee), MenuProvider {

    private var addEmployeeBinding: FragmentAddEmployeeBinding? = null
    private val binding get() = addEmployeeBinding!!

    private lateinit var employeeViewModel: EmployeeViewModel
    private lateinit var addEmployeeView: View

    private lateinit var etBirthDate: EditText
    private lateinit var datePicker: CustomDatePicker

    private var latitude = 0.0
    private var longitude = 0.0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        addEmployeeBinding = FragmentAddEmployeeBinding.inflate(inflater, container, false)

        getLocation { lat, lon ->
            latitude = lat
            longitude = lon
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val menuHost: MenuHost = requireActivity()
        menuHost.addMenuProvider(this, viewLifecycleOwner, Lifecycle.State.RESUMED)

        employeeViewModel = (activity as MainActivity).employeeViewModel
        addEmployeeView = view

        etBirthDate = binding.etBornDate
        datePicker = CustomDatePicker()

        etBirthDate.setOnClickListener {
            datePicker.showDatePickerDialog(requireContext(), etBirthDate)
        }
    }

    private fun getLocation(callback: (Double, Double) -> Unit) {
        (activity as MainActivity).getLastLocation(object: LocationCallback {

            override fun onLocationReceived(latitude: Double, longitude: Double) {
                callback(latitude, longitude)
            }

            override fun onLocationFailed(errorMessage: String) {
                Toast.makeText(requireContext(), errorMessage, Toast.LENGTH_SHORT).show()
                callback(-1.0, -1.0)
            }
        })
    }

    private fun saveEmployee(view: View) {
        val name = binding.etAddEmployee.text.toString().trim()
        val birth = binding.etBornDate.text.toString().trim()
        val avatar = binding.etAvatar.text.toString().trim()
        val dateUtc = datePicker.getCurrentUtcTime()

        if (name.isNotEmpty() && birth.isNotEmpty()) {
            val employee = Employee(
                id = null,
                fullName = name,
                dateOfBirth = birth,
                avatar = avatar,
                latitude = latitude,
                longitude = longitude,
                utcDate = dateUtc,
                createdAt = dateUtc
            )

            employeeViewModel.addEmployee(employee)

            Toast.makeText(addEmployeeView.context, "Employee Saved", Toast.LENGTH_SHORT).show()
            view.findNavController().popBackStack(R.id.homeFragment, false)
        } else {
            Toast.makeText(addEmployeeView.context, "Please enter name and birth date", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
        menu.clear()
        menuInflater.inflate(R.menu.menu_add_empl, menu)
    }

    override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
        return when (menuItem.itemId) {
            R.id.saveMenu -> {
                saveEmployee(addEmployeeView)
                true
            }
            else -> false
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        addEmployeeBinding = null
    }
}