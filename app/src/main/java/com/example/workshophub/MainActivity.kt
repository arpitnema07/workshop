package com.example.workshophub

import android.app.AlertDialog
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import com.example.workshophub.databinding.ActivityMainBinding
import com.example.workshophub.models.User
import com.example.workshophub.utils.Constant
import com.example.workshophub.utils.Constant.WORKSHOP
import com.example.workshophub.utils.Constant.ACCESS_TOKEN
import com.example.workshophub.utils.Constant.DASHBOARD
import com.example.workshophub.utils.Constant.LOGIN
import com.example.workshophub.utils.Constant.SIGNUP
import com.example.workshophub.utils.Constant.USER_ID
import com.example.workshophub.utils.Constant.USER_MODEL_KEY
import com.example.workshophub.utils.MainViewModel
import com.example.workshophub.utils.MainViewModelFactory
import com.google.gson.Gson

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var sharedPreferences: SharedPreferences

    private val viewModel: MainViewModel by viewModels {
        MainViewModelFactory((application as WorkshopApplication).repository)
    }

    override fun onStart() {
        super.onStart()
        viewModel._current.value = WORKSHOP
        val accessToken = sharedPreferences.getString(ACCESS_TOKEN,"0")
        if (accessToken == null || accessToken == "0"){
            viewModel._isLoggedIn.value = false
        } else {
            viewModel._isLoggedIn.value = true
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        sharedPreferences = getSharedPreferences(Constant.AUTH_PREFERENCE, Context.MODE_PRIVATE)

        viewModel.isLoggedIn.observe(this){
            if (it){
                binding.loginSignup.visibility = View.GONE
                binding.profile.visibility = View.VISIBLE
            } else if(viewModel.current.value == WORKSHOP){
                binding.loginSignup.visibility = View.VISIBLE
                binding.profile.visibility = View.GONE
            } else{
                binding.profile.visibility = View.GONE
            }
        }

        viewModel.current.observe(this){
            if(it == WORKSHOP && viewModel._isLoggedIn.value == false){
                binding.loginSignup.visibility = View.VISIBLE
                binding.back.visibility = View.GONE
            } else if(it == WORKSHOP) {
                binding.loginSignup.visibility = View.GONE
                binding.profile.visibility = View.VISIBLE
                binding.logout.visibility = View.GONE
                binding.back.visibility = View.GONE
            } else if(it == DASHBOARD){
                binding.back.visibility = View.VISIBLE
                binding.profile.visibility = View.GONE
                binding.logout.visibility = View.VISIBLE
            } else {
                binding.back.visibility = View.VISIBLE
                binding.loginSignup.visibility = View.GONE
                binding.profile.visibility = View.GONE
                binding.logout.visibility = View.GONE
            }

        }

        viewModel.authResult.observe(this) {
            val result = it ?: return@observe

            viewModel.isLoading(false)
            if (result.isFailure) {
                result.exceptionOrNull()?.localizedMessage?.let { it1 -> showLoginFailed(it1) }
            }
            if (result.isSuccess) {
                updateUiWithUser(result.getOrDefault(User("","",ArrayList(),"","","")))
            }

        }

        binding.back.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }
        binding.loginSignup.setOnClickListener {
            findNavController(R.id.nav_host_fragment).navigate(R.id.action_workshopFragment_to_loginFragment)
        }

        binding.profile.setOnClickListener{
            findNavController(R.id.nav_host_fragment).navigate(R.id.action_workshopFragment_to_dashboardFragment)
        }

        binding.logout.setOnClickListener {
            askToLogout()
        }
    }


    fun logout(){
        val edit = sharedPreferences.edit()
        edit.clear()
        edit.apply()
        viewModel._isLoggedIn.value = false
        viewModel.clearAll()
        findNavController(R.id.nav_host_fragment).popBackStack()
    }

    fun askToLogout() {
        val alertDialogBuilder = AlertDialog.Builder(this)
        alertDialogBuilder.apply {
            setTitle("Logout")
            setMessage("Are you sure you want to logout?")
            setPositiveButton("Yes") { dialog, _ ->
                // Call the ViewModel's logout function
                logout()
                dialog.dismiss()
            }
            setNegativeButton("No") { dialog, _ ->
                dialog.dismiss()
            }
        }
        val alertDialog = alertDialogBuilder.create()
        alertDialog.show()
    }

    private fun updateUiWithUser(model: User) {
        val welcome = getString(R.string.welcome)
        val displayName = model.username

        Toast.makeText(
            this,
            "$welcome $displayName!",
            Toast.LENGTH_LONG
        ).show()
        val edit = sharedPreferences.edit()
        edit.putString(ACCESS_TOKEN,model.token)
        edit.putString(USER_ID,model._id)
        edit.putString(USER_MODEL_KEY, Gson().toJson(model))
        edit.apply()
        viewModel._isLoggedIn.value = true

        when(viewModel.current.value) {
            LOGIN -> findNavController(R.id.nav_host_fragment).navigate(R.id.action_loginFragment_to_dashboardFragment)
            SIGNUP -> findNavController(R.id.nav_host_fragment).navigate(R.id.action_signUpFragment_to_dashboardFragment)
        }

    }
    private fun showLoginFailed(errorString: String) {
        Toast.makeText(this, errorString, Toast.LENGTH_SHORT).show()
    }
}