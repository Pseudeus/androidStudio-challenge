package com.example.challenge.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.challenge.fragments.HomeFragmentDirections
import com.example.challenge.model.Employee
import com.example.notesroompractice.databinding.EmployeeLayoutBinding

class EmployeeAdapter: RecyclerView.Adapter<EmployeeAdapter.EmployeeViewHolder>() {

    class EmployeeViewHolder(val itemBinding: EmployeeLayoutBinding): RecyclerView.ViewHolder(itemBinding.root) {
        fun bind(employee: Employee) {
            itemBinding.tvEmployeeName.text = employee.fullName
            itemBinding.tvDayOfBirth.text = "DOB: ${employee.dateOfBirth}"
        }
    }

    private val differCallback = object: DiffUtil.ItemCallback<Employee>() {
        override fun areItemsTheSame(oldItem: Employee, newItem: Employee): Boolean {
            return oldItem.id == newItem.id &&
                    oldItem.fullName == newItem.fullName &&
                    oldItem.avatar == newItem.avatar
        }

        override fun areContentsTheSame(oldItem: Employee, newItem: Employee): Boolean {
            return oldItem == newItem
        }
    }

    private val differ = AsyncListDiffer(this, differCallback)

    fun submitList(list: List<Employee>) {
        differ.submitList(list)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EmployeeViewHolder {
        return EmployeeViewHolder(
            EmployeeLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    override fun onBindViewHolder(holder: EmployeeViewHolder, position: Int) {
        val currentEmployee = differ.currentList[position]

        holder.itemBinding.tvEmployeeName.text = currentEmployee.fullName
        holder.itemBinding.tvDayOfBirth.text = "DOB: ${currentEmployee.dateOfBirth}"

        holder.itemView.setOnClickListener {
            val direction = HomeFragmentDirections.actionHomeFragmentToEditEmployeeFragment(currentEmployee)
            it.findNavController().navigate(direction)
        }
    }
}