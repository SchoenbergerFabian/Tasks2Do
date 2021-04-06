package com.infendro.tasks2do.ui.main.user

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.infendro.tasks2do.Storage.Account
import com.infendro.tasks2do.R
import kotlinx.android.synthetic.main.fragment_user.*

class FragmentUser : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_user, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        imageButtonBack.setOnClickListener { navigateBack() }

        if(Account.isLoggedIn()){
            textViewUsername.text = Account.username
            textViewPassword.text = Account.password
            layoutLoggedIn.visibility=View.VISIBLE
            layoutLoggedOut.visibility=View.GONE
        }

        buttonLogIn.setOnClickListener {
            toLogIn()
        }

        buttonSignIn.setOnClickListener {
            toSignIn()
        }

        buttonChange.setOnClickListener {
            toChange()
        }

        buttonLogOut.setOnClickListener {
            //TODO are you sure?
            Account.changeLoginInfo(requireActivity(), "","")
            navigateBack()
        }

        /*editTextUsername.doOnTextChanged { text, _, _, _ -> checkValid(text.toString(),editTextPassword.text.toString()) }
        editTextUsername.setText(MainActivity.username)
        editTextPassword.doOnTextChanged { text, _, _, _ -> checkValid(editTextUsername.text.toString(),text.toString()) }
        editTextPassword.setText(MainActivity.password)*/
    }

    /*private fun checkValid(username: String, password: String){
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
    }*/

    private fun toLogIn(){
        findNavController().navigate(R.id.action_fragmentUser_to_fragmentLogin)
    }

    private fun toSignIn(){
        findNavController().navigate(R.id.action_fragmentUser_to_fragmentSignin)
    }

    private fun toChange(){
        findNavController().navigate(R.id.action_fragmentUser_to_fragmentChangeUser)
    }

    private fun navigateBack(){
        findNavController().navigate(R.id.action_fragmentUser_to_fragment_Main)
    }
}