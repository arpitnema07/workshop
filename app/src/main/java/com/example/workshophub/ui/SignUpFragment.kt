package com.example.workshophub.ui

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.workshophub.R
import com.example.workshophub.WorkshopApplication
import com.example.workshophub.databinding.FragmentSignUpBinding
import com.example.workshophub.utils.Constant
import com.example.workshophub.utils.Constant.AUTH_PREFERENCE
import com.example.workshophub.utils.Constant.SIGNUP
import com.example.workshophub.utils.MainViewModel
import com.example.workshophub.utils.MainViewModelFactory


class SignUpFragment : Fragment() {

    private lateinit var binding: FragmentSignUpBinding
    private lateinit var sharedPreferences: SharedPreferences
    private val viewModel : MainViewModel by activityViewModels{
        MainViewModelFactory((requireActivity().application as WorkshopApplication).repository)
    }
    override fun onStart() {
        super.onStart()

        viewModel._current.value = SIGNUP
        val accessToken = sharedPreferences.getString(Constant.ACCESS_TOKEN,"0")
        if (accessToken != null && accessToken != "0"){
            findNavController().navigate(R.id.action_signUpFragment_to_workshopFragment)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSignUpBinding.inflate(inflater,container,false)
        sharedPreferences = requireActivity().getSharedPreferences(AUTH_PREFERENCE, Context.MODE_PRIVATE)


        binding.signup.setOnClickListener {
            val username = binding.username.text.toString()
            val email = binding.email.text.toString()
            val pass = binding.password.text.toString()
            if(email.isBlank() or pass.isBlank() or username.isBlank()){
                Toast.makeText(requireContext(),"Missing Required Field!", Toast.LENGTH_SHORT).show()
            } else{
                viewModel.register(username,email,pass)
                viewModel.isLoading(true)
            }
        }

        viewModel.loading.observe(viewLifecycleOwner){
            if (it){
                binding.loading.visibility = View.VISIBLE
                binding.signup.isEnabled = false
            } else {
                binding.loading.visibility = View.GONE
                binding.signup.isEnabled = true
            }
        }
        binding.loginHere.setOnClickListener {
            findNavController().navigate(R.id.action_signUpFragment_to_loginFragment)
        }
        return binding.root
    }


}