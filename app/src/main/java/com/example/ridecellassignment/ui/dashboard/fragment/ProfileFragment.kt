package com.example.ridecellassignment.ui.dashboard.fragment

import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.example.ridecellassignment.MyPreferenses
import com.example.ridecellassignment.R
import com.example.ridecellassignment.Utils
import com.example.ridecellassignment.databinding.FragmentProfileBinding
import com.example.ridecellassignment.ui.auth.LoginActivity
import com.example.ridecellassignment.ui.dashboard.DashboardViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlin.time.ExperimentalTime

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [ProfileFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
@AndroidEntryPoint
class ProfileFragment : Fragment() {


    lateinit var binding: FragmentProfileBinding
    val viewModel: DashboardViewModel by viewModels()
    lateinit var pDialog: Dialog

    @ExperimentalTime
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_profile, container, false)
        val view: View = binding.root
        binding.executePendingBindings()
        binding.viewModel = viewModel
        pDialog = Utils.generateProgressDialog(activity)!!


        binding.btnLogout.setOnClickListener {
            MyPreferenses(context!!).clear()
            startActivity(Intent(activity, LoginActivity::class.java))
            activity?.finish()
        }

        observerProfileData()
        getProfileData()

        return view
    }

    private fun observerProfileData() {
        viewModel.isSuccessful.observe(activity!!, Observer {
            pDialog.dismiss()
        })
    }

    @ExperimentalTime
    private fun getProfileData() {
        if (Utils.isDataConnected(context)) {
            viewModel.getProfileData()
            pDialog.show()
        } else {
            Utils.showSNACK_BAR_NO_INTERNET(requireActivity(), activity?.localClassName!!)
        }
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment ProfileFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            ProfileFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}