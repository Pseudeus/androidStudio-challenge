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
import androidx.appcompat.app.AlertDialog
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.lifecycle.Lifecycle
import androidx.navigation.findNavController
import androidx.navigation.fragment.navArgs
import com.example.challenge.CustomDatePicker
import com.example.challenge.MainActivity
import com.example.challenge.location.LocationCallback
import com.example.challenge.model.Employee
import com.example.challenge.viewmodel.EmployeeViewModel
import com.example.notesroompractice.R
import com.example.notesroompractice.databinding.FragmentEditEmployeeBinding

class EditEmployeeFragment : Fragment(R.layout.fragment_edit_employee), MenuProvider {

    private var editEmployeeBinding: FragmentEditEmployeeBinding? = null
    private val binding get() = editEmployeeBinding!!

    private lateinit var employeeViewModel: EmployeeViewModel
    private lateinit var currentEmployee: Employee

    private lateinit var etBirthDate: EditText
    private lateinit var datePicker: CustomDatePicker

    private var latitude = 0.0
    private var longitude = 0.0

    private val args: EditEmployeeFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        editEmployeeBinding = FragmentEditEmployeeBinding.inflate(inflater, container, false)

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
        currentEmployee = args.employee!!

        etBirthDate = binding.etBornDate
        datePicker = CustomDatePicker()

        etBirthDate.setOnClickListener {
            datePicker.showDatePickerDialog(requireContext(), etBirthDate)
        }

        binding.etAddEmployee.setText(currentEmployee.fullName)

        val dob = currentEmployee.dateOfBirth
        dob.removeRange(0, 5)
        binding.etBornDate.setText(dob)

        binding.etAvatar.setText(currentEmployee.avatar)

        binding.btnSave.setOnClickListener {
            val name = binding.etAddEmployee.text.toString().trim()
            val birth = binding.etBornDate.text.toString().trim()
            val avatar = binding.etAvatar.text.toString().trim()
            val currentUtcDate = datePicker.getCurrentUtcTime()
            val creationDate = currentEmployee.createdAt

            if (name.isNotEmpty() && birth.isNotEmpty() && avatar.isNotEmpty()) {
                val empl = Employee(
                    currentEmployee.id,
                    name,
                    birth,
                    avatar,
                    this.latitude,
                    this.longitude,
                    currentUtcDate,
                    creationDate
                )

                employeeViewModel.updateEmployee(empl)
                Toast.makeText(context, "Employee Saved", Toast.LENGTH_SHORT).show()
                view.findNavController().popBackStack(R.id.homeFragment, false)
            } else {
                Toast.makeText(context, "Please fill in the blanks", Toast.LENGTH_SHORT).show()
            }
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

    private fun deleteEmployee() {
        AlertDialog.Builder(requireActivity()).apply {
            setTitle("Delete Employee")
            setMessage("Are you sure you want to delete this employee?")
            setPositiveButton("Delete") { _, _ ->
                employeeViewModel.deleteEmployee(currentEmployee)
                Toast.makeText(context, "Employee Deleted", Toast.LENGTH_SHORT).show()
                view?.findNavController()?.popBackStack(R.id.homeFragment, false)
            }
            setNegativeButton("Cancel", null)
        }.create().show()
    }
    override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
        menu.clear()
        menuInflater.inflate(R.menu.menu_edit_empl, menu)
    }

    override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
        return when (menuItem.itemId) {
            R.id.deleteMenu -> {
                deleteEmployee()
                true
            }
            else -> false
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        editEmployeeBinding = null
    }
}