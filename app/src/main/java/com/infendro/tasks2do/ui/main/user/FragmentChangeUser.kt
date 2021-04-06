package com.infendro.tasks2do.ui.main.user

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doOnTextChanged
import androidx.navigation.fragment.findNavController
import com.infendro.tasks2do.Storage.Account
import com.infendro.tasks2do.R
import com.infendro.tasks2do.Storage.Connection.Companion.hasInternetConnection
import com.infendro.tasks2do.Storage.Storage
import kotlinx.android.synthetic.main.fragment_login.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class FragmentChangeUser : Fragment(), View.OnClickListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_login, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        imageButtonBack.setOnClickListener {
            navigateBack()
        }

        editTextUsername.setText(Account.username)
        editTextUsername.doOnTextChanged { text, _, _, _ ->
            validate(text.toString(), editTextPassword.text.toString())
        }

        editTextPassword.setText(Account.password)
        editTextPassword.doOnTextChanged { text, _, _, _ ->
            validate(editTextUsername.text.toString(), text.toString())
        }

        validate(Account.username, Account.password)
    }

    private fun validate(username: String, password: String){
        if(Account.isValid(username, password)){
            valid()
        }else{
            invalid()
        }
    }

    private fun valid(){
        buttonLogIn.setTextColor(requireActivity().getColor(R.color.colorAccent))
        setOnClickListener()
    }

    private fun setOnClickListener(){
        buttonLogIn.setOnClickListener(this)
    }

    private fun invalid(){
        buttonLogIn.setTextColor(requireActivity().getColor(R.color.invalid))
        buttonLogIn.setOnClickListener(null)
    }

    override fun onClick(view: View?) {
        GlobalScope.launch {
            if(hasInternetConnection(requireActivity())){
                buttonLogIn.setOnClickListener(null)
                if(Account.isCorrect(editTextUsername.text.toString(),editTextPassword.text.toString())){
                    Account.changeLoginInfo(requireActivity(),editTextUsername.text.toString(),editTextPassword.text.toString())
                    Log.println(Log.INFO,"","Successfully logged in")
                    navigateBack()
                }else{
                    //TODO feedback
                    Log.println(Log.INFO,"","Failed to log in: wrong information")
                }
                setOnClickListener()
            }else{
                //TODO feedback
                Log.println(Log.INFO,"","Failed to log in: no internet connection")
            }
        }
    }

    private fun navigateBack(){
        //requireActivity().currentFocus?.clearFocus()
        findNavController().navigate(R.id.action_fragmentChangeUser_to_fragmentUser)
    }
}