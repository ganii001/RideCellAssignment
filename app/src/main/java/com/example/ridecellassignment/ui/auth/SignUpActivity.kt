package com.example.ridecellassignment.ui.auth

import android.app.Dialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.method.PasswordTransformationMethod
import android.view.View
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import com.example.ridecellassignment.R
import com.example.ridecellassignment.Utils
import com.example.ridecellassignment.Validations
import com.example.ridecellassignment.databinding.ActivityLoginBinding
import com.example.ridecellassignment.databinding.ActivitySignUpBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_sign_up.*

@AndroidEntryPoint
class SignUpActivity : AppCompatActivity() {

    val viewModel: LoginViewModel by viewModels()
    lateinit var binding: ActivitySignUpBinding
    private var isShow1 = false
    private var isShow2 = false
    lateinit var pDialog: Dialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setDisplayShowHomeEnabled(true)
        binding.toolbar.setNavigationOnClickListener { finish() }
        pDialog = Utils.generateProgressDialog(this)!!

        onSignUp()

        binding.imageVisiblep.setOnClickListener {
            if (edit_pass.text.toString().isNotEmpty()) {
                if (!isShow1) {
                    edit_pass.transformationMethod = null
                    image_visiblep.setImageResource(R.drawable.ic_visibility_off)
                    isShow1 = true
                } else {
                    edit_pass.transformationMethod = PasswordTransformationMethod()
                    image_visiblep.setImageResource(R.drawable.ic_visibility)
                    isShow1 = false
                }
                edit_pass.setSelection(edit_pass.text.toString().length)
            }
        }

        binding.imageVisiblecp.setOnClickListener {
            if (edit_confirm_password.text.toString().isNotEmpty()) {
                if (!isShow2) {
                    edit_confirm_password.transformationMethod = null
                    image_visiblecp.setImageResource(R.drawable.ic_visibility_off)
                    isShow2 = true
                } else {
                    edit_confirm_password.transformationMethod = PasswordTransformationMethod()
                    image_visiblecp.setImageResource(R.drawable.ic_visibility)
                    isShow2 = false
                }
                edit_confirm_password.setSelection(edit_confirm_password.text.toString().length)
            }
        }
        binding.editEmail.afterTextChanged {
            Validations.emailValidation(edit_email)
        }
        binding.editPass.afterTextChanged {
            if (it.length <= 5) {
                ll_weak_pass.visibility = View.VISIBLE
                ll_strong_pass.visibility = View.GONE
                ll_very_strong_pass.visibility = View.GONE
            } else if (it.length in 6..8) {
                ll_weak_pass.visibility = View.GONE
                ll_strong_pass.visibility = View.VISIBLE
                ll_very_strong_pass.visibility = View.GONE
            } else if (it.length > 8) {
                ll_weak_pass.visibility = View.GONE
                ll_strong_pass.visibility = View.GONE
                ll_very_strong_pass.visibility = View.VISIBLE
            }
        }

        binding.btnSignup.setOnClickListener {
            if (!Validations.edtValidation(edit_name, "Enter Your Name"))
                return@setOnClickListener
            else if (!Validations.emailValidation(edit_email))
                return@setOnClickListener
            else if (!Validations.passwordValidation(edit_pass))
                return@setOnClickListener
            else if (!Validations.emailValidation(edit_email))
                return@setOnClickListener
            else if (!Validations.passwordValidation(edit_confirm_password))
                return@setOnClickListener
            else if (edit_pass.text.toString() != edit_confirm_password.text.toString()) {
                Utils.showToast(this, "Password & Confirm Password must be equal")
            } else {
                signUpUser()
            }
        }
    }

    private fun signUpUser() {
        if (Utils.isDataConnected(this)) {
            viewModel.doSignUp(
                name = binding.editName.text.toString(),
                emailId = binding.editEmail.text.toString(),
                pass = binding.editConfirmPassword.text.toString()
            )
            pDialog.show()
        } else {
            Utils.showSNACK_BAR_NO_INTERNET(this, localClassName)
        }
    }

    private fun onSignUp() {
        viewModel.isSuccessful.observe(this, androidx.lifecycle.Observer { isSuccess ->
            if (isSuccess) {
                startActivity(Intent(this, LoginActivity::class.java))
                finish()
            }
            pDialog.dismiss()
        })
    }
}