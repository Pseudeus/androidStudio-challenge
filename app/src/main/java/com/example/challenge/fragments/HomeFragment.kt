package com.example.challenge.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.lifecycle.Lifecycle
import androidx.navigation.findNavController
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.challenge.MainActivity
import com.example.challenge.adapter.EmployeeAdapter
import com.example.challenge.model.Employee
import com.example.challenge.viewmodel.EmployeeViewModel
import com.example.notesroompractice.R
import com.example.notesroompractice.databinding.FragmentHomeBinding


class HomeFragment : Fragment(R.layout.fragment_home), SearchView.OnQueryTextListener, MenuProvider {

    private var homeBinding: FragmentHomeBinding? = null
    private val binding get() = homeBinding!!

    private lateinit var employeeViewModel: EmployeeViewModel
    private lateinit var employeeAdapter: EmployeeAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        homeBinding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val menuHost: MenuHost = requireActivity()
        menuHost.addMenuProvider(this, viewLifecycleOwner, Lifecycle.State.RESUMED)

        employeeViewModel = (activity as MainActivity).employeeViewModel


        setupHomeRecyclerView()

        binding.fbAddEmployee.setOnClickListener {
            it.findNavController().navigate(R.id.action_homeFragment_to_addEmployeeFragment)
        }
    }

    private fun updateUI(employee: List<Employee>?) {
        if (!employee.isNullOrEmpty()) {
            binding.ivEmplyEmployeesImg.visibility = View.GONE
            binding.rbHome.visibility = View.VISIBLE
        } else {
            binding.ivEmplyEmployeesImg.visibility = View.VISIBLE
            binding.rbHome.visibility = View.GONE
        }
    }

    private fun setupHomeRecyclerView() {
        employeeAdapter = EmployeeAdapter()
        binding.rbHome.apply {
            layoutManager = StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL)
            setHasFixedSize(true)
            adapter = employeeAdapter
        }
        activity?.let {
            employeeViewModel.getAllEmployees().observe(viewLifecycleOwner) { employee ->
                employeeAdapter.submitList(employee)
                updateUI(employee)
            }
        }
    }

    private fun searchEmployee(query: String?) {
        val searchQery = "%$query"

        employeeViewModel.searchEmployee(query).observe(this) { list ->
            employeeAdapter.submitList(list)
        }
    }

    override fun onQueryTextSubmit(p0: String?): Boolean {
        return false
    }

    override fun onQueryTextChange(newText: String?): Boolean {
        if (newText != null) {
            searchEmployee(newText)
        }

        return true
    }

    override fun onDestroy() {
        super.onDestroy()
        homeBinding = null
    }

    override fun onResume() {
        super.onResume()
        setupHomeRecyclerView()
    }

    override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
        menu.clear()
        menuInflater.inflate(R.menu.home_menu, menu)

        val menuSearch = menu.findItem(R.id.searchMenu).actionView as SearchView
        menuSearch.isSubmitButtonEnabled = false
        menuSearch.setOnQueryTextListener(this)
    }

    override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
        return false
    }
}