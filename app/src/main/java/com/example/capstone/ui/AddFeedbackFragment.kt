package com.example.capstone.ui

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.capstone.databinding.FragmentAddFeedbackBinding
import com.example.capstone.model.Feedback
import com.example.capstone.utils.Constant
import com.example.capstone.utils.Helper

class AddFeedbackFragment : Fragment() {

    private lateinit var fragmentAddFeedbackFragment: FragmentAddFeedbackBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        fragmentAddFeedbackFragment = FragmentAddFeedbackBinding.inflate(inflater, container, false)
        return fragmentAddFeedbackFragment.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //Fetch Email
        val sharedPreference =
            requireActivity().getSharedPreferences(Constant.LOGIN_CREDENTIAL, Context.MODE_PRIVATE)
        val emailData = sharedPreference.getString("email", "")

        fragmentAddFeedbackFragment.btnSubmit.setOnClickListener {

            val feedbackMessage = fragmentAddFeedbackFragment.etMessage.text.toString()

            if(Helper.nullCheck(feedbackMessage)){
                Toast.makeText(requireActivity(), "Please enter a message or Feedback!", Toast.LENGTH_LONG)
                    .show()
            }else{
                if(fragmentAddFeedbackFragment.cbEmail.isChecked){
                    //Share email as User
                    val key = Constant.databaseReference.child("feedback").push()
                    val feedBackData = Feedback(emailData,feedbackMessage)
                    key.setValue(feedBackData).addOnSuccessListener {
                        Toast.makeText(requireActivity(), "Successfully added!", Toast.LENGTH_LONG)
                            .show()
                        fragmentAddFeedbackFragment.etMessage.text.clear()
                    }
                }else{
                    //Send Feedback as anonymous user
                    val key = Constant.databaseReference.child("feedback").push()
                    val feedBackData = Feedback("Anonymous",feedbackMessage)
                    key.setValue(feedBackData).addOnSuccessListener {
                        Toast.makeText(requireActivity(), "Successfully added!", Toast.LENGTH_LONG)
                            .show()
                        fragmentAddFeedbackFragment.etMessage.text.clear()
                    }
                }

            }
        }
    }

}