package com.infendro.tasks2do.ui.main.user

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doOnTextChanged
import androidx.navigation.fragment.findNavController
import com.infendro.tasks2do.Account
import com.infendro.tasks2do.R
import com.infendro.tasks2do.Storage
import kotlinx.android.synthetic.main.fragment_login.*
import kotlinx.android.synthetic.main.fragment_login.editTextPassword
import kotlinx.android.synthetic.main.fragment_login.editTextUsername
import kotlinx.android.synthetic.main.fragment_login.imageButtonBack
import kotlinx.android.synthetic.main.fragment_signin.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class FragmentSignin : Fragment(), View.OnClickListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_signin, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        imageButtonBack.setOnClickListener {
            navigateBack()
        }

        editTextUsername.doOnTextChanged { text, _, _, _ ->
            if(Account.isValid(text.toString(), editTextPassword.text.toString())){
                valid()
            }else{
                invalid()
            }
        }

        editTextPassword.doOnTextChanged { text, _, _, _ ->
            if(Account.isValid(editTextUsername.text.toString(), text.toString())){
                valid()
            }else{
                invalid()
            }
        }
    }

    private fun valid(){
        buttonSignIn.setTextColor(requireActivity().getColor(R.color.colorAccent))
        buttonSignIn.setOnClickListener(this)
    }

    private fun invalid(){
        buttonSignIn.setTextColor(requireActivity().getColor(R.color.invalid))
        buttonSignIn.setOnClickListener(null)
    }

    override fun onClick(view: View?) {
        GlobalScope.launch {
            if (Storage.hasInternetConnection(requireActivity())) {
                buttonSignIn.setOnClickListener(null)
                if (Account.signIn(editTextUsername.text.toString(), editTextPassword.text.toString())) {
                    Account.changeLoginInfo(requireActivity(), editTextUsername.text.toString(), editTextPassword.text.toString())
                    Log.println(Log.INFO, "", "Successfully signed in")
                    navigateBack()
                } else {
                    //TODO feedback
                    Log.println(Log.INFO, "", "Failed to sign in: user already exists")
                }
                buttonSignIn.setOnClickListener(FragmentSignin())
            } else {
                //TODO feedback
                Log.println(Log.INFO, "", "Failed to sign in: no internet connection")
            }
        }
    }

    private fun navigateBack(){
        findNavController().navigate(R.id.action_fragmentSignin_to_fragmentUser)
    }

}