package com.infendro.tasks2do.ui.main.logininfo

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.doOnTextChanged
import androidx.navigation.fragment.findNavController
import com.infendro.tasks2do.R
import com.infendro.tasks2do.Register
import com.infendro.tasks2do.Storage
import com.infendro.tasks2do.ui.main.MainActivity
import kotlinx.android.synthetic.main.fragment_login_info.*
import kotlinx.android.synthetic.main.fragment_login_info.buttonSave

class FragmentLoginInfo : Fragment(), View.OnClickListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_login_info, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        imageButtonBack.setOnClickListener { navigateBack() }

        buttonSave.setOnClickListener (this)

        editTextUsername.doOnTextChanged { text, _, _, _ -> checkValid(text.toString(),editTextPassword.text.toString()) }
        editTextUsername.setText(MainActivity.username)
        editTextPassword.doOnTextChanged { text, _, _, _ -> checkValid(editTextUsername.text.toString(),text.toString()) }
        editTextPassword.setText(MainActivity.password)
    }

    private fun checkValid(username: String, password: String){
        if(username.matches(Regex("[A-Za-z0-9]+")) && password.matches(Regex("[A-Za-z0-9]*"))){ //TODO change if password cannot be ""
            makeValid(username, password)
        }else{
            makeInvalid()
        }
    }

    private fun makeValid(username: String, password: String){
        buttonSave.setTextColor(requireActivity().getColor(R.color.colorAccent))
        buttonSave.setOnClickListener (this)
    }

    private fun makeInvalid(){
        buttonSave.setTextColor(requireActivity().getColor(R.color.invalid))
        buttonSave.setOnClickListener(null)
    }

    override fun onClick(view: View?) {
        val result = MainActivity.changeLoginInfo(editTextUsername.text.toString(),editTextPassword.text.toString())
        if(result && Storage.hasInternetConnection(requireActivity())){
            Register.register(editTextUsername.text.toString(),editTextPassword.text.toString())
        }else if(!Storage.hasInternetConnection(requireActivity())){
            Toast.makeText(requireActivity(), "No internet connection",Toast.LENGTH_LONG).show() //TODO
        }

        navigateBack()
    }

    private fun navigateBack(){
        findNavController().navigate(R.id.action_fragmentLoginInfo_to_fragment_Main)
        requireActivity().currentFocus?.clearFocus()
    }
}