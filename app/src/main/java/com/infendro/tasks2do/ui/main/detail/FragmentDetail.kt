package com.infendro.tasks2do.activities.ui.fragments.detail

import android.location.Location
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doOnTextChanged
import androidx.navigation.fragment.findNavController
import com.infendro.tasks2do.R
import com.infendro.tasks2do.Task
import kotlinx.android.synthetic.main.fragment_detail.*
import com.infendro.tasks2do.List
import com.infendro.tasks2do.LocationInfo
import com.infendro.tasks2do.Connection.Account
import com.infendro.tasks2do.Connection.Connection
import com.infendro.tasks2do.Connection.Storage
import com.infendro.tasks2do.ui.main.DialogDateTimePicker
import com.infendro.tasks2do.ui.main.MainActivity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class FragmentDetail : Fragment() {
    private var list: List? = null
    private var task: Task? = null
    private var index = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            list = it.get("LIST") as List
            task = it.get("TASK") as Task
            index = it.getInt("INDEX")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_detail, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        imageButtonBack.setOnClickListener {
            navigateBack()
        }

        imageButtonDelete.setOnClickListener {
            when(task?.checked){
                true -> {
                    list?.checkedTasks?.removeAt(index)
                }
                false -> {
                    list?.uncheckedTasks?.removeAt(index)
                }
            }
            if(Account.isLoggedIn()){
                if(Connection.hasInternetConnection(requireActivity())){
                    GlobalScope.launch(Dispatchers.IO) {
                        Storage.removeTask(task!!)
                    }
                }else{
                    //TODO
                }
            }
            navigateBack()
        }

        editTextTitle.setText(task?.title)
        editTextTitle.doOnTextChanged { text, _, _, _ ->
            task?.title = text.toString()

            MainActivity.save(requireActivity())
            GlobalScope.launch(Dispatchers.IO) {
                setLocationInfo()
                withContext(Dispatchers.Main){
                    updateLocationInfo()
                }

                Storage.editTask(list!!,task!!)
            }
        }

        editTextDetails.setText(task?.details)
        editTextDetails.doOnTextChanged{ text, _, _, _ ->
            task?.details = when(text.toString()) {"" -> null else -> text.toString()}

            MainActivity.save(requireActivity())
            GlobalScope.launch(Dispatchers.IO) {
                setLocationInfo()
                withContext(Dispatchers.Main){
                    updateLocationInfo()
                }

                Storage.editTask(list!!,task!!)
            }
        }

        val dueString = task?.getDueString(requireActivity().getString(R.string.pattern_date),requireActivity().getString(R.string.pattern_time))
        if(dueString!=null){
            textViewDue.hint = ""
            textViewDue.text = dueString
            imageButtonRemove.visibility=View.VISIBLE
        }

        updateLocationInfo()

        due.setOnClickListener {
            val dateTimePicker = DialogDateTimePicker(requireActivity(),task)
            dateTimePicker.setOnDismissListener {
                val dueString = task?.getDueString(requireActivity().getString(R.string.pattern_date,),requireActivity().getString(R.string.pattern_time,))
                if(dueString!=null){
                    textViewDue.hint = ""
                    textViewDue.text = dueString
                    imageButtonRemove.visibility=View.VISIBLE
                }

                MainActivity.save(requireActivity())
                GlobalScope.launch(Dispatchers.IO) {
                    setLocationInfo()
                    withContext(Dispatchers.Main){
                        updateLocationInfo()
                    }

                    Storage.editTask(list!!,task!!)
                }

            }
            dateTimePicker.show()
        }

        imageButtonRemove.setOnClickListener {
            textViewDue.hint = activity?.getString(R.string.add_datetime)
            textViewDue.text = ""
            imageButtonRemove.visibility=View.GONE
            task?.dueTime=null
            task?.dueDate=null

            updateLocationInfo()

            MainActivity.save(requireActivity())
            GlobalScope.launch(Dispatchers.IO) {
                setLocationInfo()

                Storage.editTask(list!!,task!!)
            }
        }

        setCheckedImage()
        imageButtonCheck.setOnClickListener {
            when(task?.checked){
                true -> {
                    list?.uncheck(index)
                }
                false -> {
                    list?.check(index)
                }
            }
            setCheckedImage()

            MainActivity.save(requireActivity())
            GlobalScope.launch(Dispatchers.IO) {
                setLocationInfo()

                Storage.editTask(list!!,task!!)
            }

            navigateBack()
        }

    }

    private suspend fun setLocationInfo(){
        val locationInfo = com.infendro.tasks2do.Connection.Location.getLastKnownLocation(requireActivity())

        if(locationInfo!=null){
            task?.locationInfo = locationInfo
        }else{
            task?.locationInfo=null
        }
    }

    private fun updateLocationInfo(){
        textViewLocation.text = if(task?.locationInfo!=null){
            task?.locationInfo?.toString(getString(R.string.longitude),getString(R.string.latitude))
        }else{
            getString(R.string.not_available)
        }
    }

    private fun setCheckedImage(){
        when(task?.checked){
            true -> {
                imageButtonCheck.setImageResource(R.drawable.ic_checked)
            }
            false -> {
                imageButtonCheck.setImageResource(R.drawable.ic_unchecked)
            }
        }
    }

    private fun navigateBack(){
        MainActivity.save(requireActivity())
        requireActivity().currentFocus?.clearFocus()
        findNavController().navigate(R.id.action_fragmentDetail_to_fragment_Main)
    }

}