package com.example.workshophub.ui

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.workshophub.R
import com.example.workshophub.WorkshopApplication
import com.example.workshophub.databinding.FragmentLoginBinding
import com.example.workshophub.utils.Constant.ACCESS_TOKEN
import com.example.workshophub.utils.Constant.AUTH_PREFERENCE
import com.example.workshophub.utils.Constant.LOGIN
import com.example.workshophub.utils.MainViewModel
import com.example.workshophub.utils.MainViewModelFactory


class LoginFragment : Fragment() {

    private lateinit var binding: FragmentLoginBinding
    private lateinit var sharedPreferences: SharedPreferences
    private val viewModel : MainViewModel by activityViewModels{
        MainViewModelFactory((requireActivity().application as WorkshopApplication).repository)
    }
    override fun onStart() {
        super.onStart()

        viewModel._current.value = LOGIN
        val accessToken = sharedPreferences.getString(ACCESS_TOKEN,"0")
        if (accessToken != null && accessToken != "0"){
            findNavController().navigate(R.id.action_loginFragment_to_workshopFragment)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentLoginBinding.inflate(inflater,container,false)
        sharedPreferences = requireActivity().getSharedPreferences(AUTH_PREFERENCE, Context.MODE_PRIVATE)


        binding.login.setOnClickListener {
            val email = binding.username.text.toString()
            val pass = binding.password.text.toString()
            if(email.isBlank() or pass.isBlank()){
                Toast.makeText(requireContext(),"Missing Required Field!", Toast.LENGTH_SHORT).show()
            } else{
                viewModel.login(email,pass)
                viewModel.isLoading(true)
            }
        }

        viewModel.loading.observe(viewLifecycleOwner){
            if (it){
                binding.loading.visibility = View.VISIBLE
                binding.login.isEnabled = false
            } else {
                binding.loading.visibility = View.GONE
                binding.login.isEnabled = true
            }
        }

        binding.registerHere.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_signUpFragment)
        }
        return binding.root
    }
}