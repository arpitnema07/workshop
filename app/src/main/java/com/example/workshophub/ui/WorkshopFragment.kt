package com.example.workshophub.ui

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.workshophub.R
import com.example.workshophub.WorkshopApplication
import com.example.workshophub.databinding.FragmentWorkshopBinding
import com.example.workshophub.models.Workshop
import com.example.workshophub.utils.Constant
import com.example.workshophub.utils.Constant.AUTH_PREFERENCE
import com.example.workshophub.utils.Constant.WORKSHOP
import com.example.workshophub.utils.MainViewModel
import com.example.workshophub.utils.MainViewModelFactory
import com.example.workshophub.utils.WorkshopAdapter

class WorkshopFragment : Fragment(), WorkshopAdapter.WorkshopClickListener {

    private lateinit var binding: FragmentWorkshopBinding
    private val viewModel : MainViewModel by activityViewModels{
        MainViewModelFactory((requireActivity().application as WorkshopApplication).repository)
    }
    private lateinit var sharedPreferences: SharedPreferences

    override fun onStart() {
        super.onStart()

        viewModel._current.value = WORKSHOP
        val accessToken = sharedPreferences.getString(Constant.ACCESS_TOKEN,"0")
        if (accessToken != null && accessToken != "0"){
            viewModel._isLoggedIn.value = true
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentWorkshopBinding.inflate(inflater,container,false)
        sharedPreferences = requireActivity().getSharedPreferences(AUTH_PREFERENCE, Context.MODE_PRIVATE)


        val adapter = WorkshopAdapter(this)
        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext(),LinearLayoutManager.VERTICAL,false)
        binding.recyclerView.adapter = adapter

        viewModel.allWorkshop.observe(viewLifecycleOwner){
            if(it.isEmpty()){
                binding.noItem.visibility = View.VISIBLE
                binding.recyclerView.visibility = View.GONE
            } else {
                binding.noItem.visibility = View.GONE
                binding.recyclerView.visibility = View.VISIBLE
            }
            it.let { adapter.submitList(it) }
        }
        return binding.root
    }

    override fun onItemClick(workshop: Workshop) {
        if (viewModel._isLoggedIn.value != true){
            findNavController().navigate(R.id.action_workshopFragment_to_loginFragment)
        }else{
            if(workshop.applied){
                viewModel.withdraw(workshop)
                Toast.makeText(requireContext(),"Application Withdrawn!", Toast.LENGTH_SHORT).show()
            }else{
                viewModel.apply(workshop)
                Toast.makeText(requireContext(),"Applied Successfully!", Toast.LENGTH_SHORT).show()

            }
        }
    }
}