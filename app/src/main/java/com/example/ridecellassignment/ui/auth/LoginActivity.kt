package com.example.ridecellassignment.ui.auth

import android.app.Dialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.text.method.PasswordTransformationMethod
import android.widget.EditText
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import com.example.ridecellassignment.R
import com.example.ridecellassignment.Utils
import com.example.ridecellassignment.Validations
import com.example.ridecellassignment.databinding.ActivityLoginBinding
import com.example.ridecellassignment.databinding.ActivitySplashBinding
import com.example.ridecellassignment.databinding.BottomsheetForgotPassBinding
import com.example.ridecellassignment.ui.dashboard.DashboardActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_sign_up.*

@AndroidEntryPoint
class LoginActivity : AppCompatActivity() {

    val viewModel: LoginViewModel by viewModels()
    lateinit var binding: ActivityLoginBinding
    lateinit var bindingBottomsheet: BottomsheetForgotPassBinding
    private var isShow = false
    lateinit var pDialog: Dialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        pDialog = Utils.generateProgressDialog(this)!!

        onSignIn()
        observeResetPass()

        binding.imageVisible.setOnClickListener {
            if (edit_password.text.toString().isNotEmpty()) {
                if (!isShow) {
                    edit_password.transformationMethod = null
                    image_visible.setImageResource(R.drawable.ic_visibility_off)
                    isShow = true
                } else {
                    edit_password.transformationMethod = PasswordTransformationMethod()
                    image_visible.setImageResource(R.drawable.ic_visibility)
                    isShow = false
                }
                edit_password.setSelection(edit_password.text.toString().length)
            }
        }

        binding.editEmailid.afterTextChanged {
            Validations.emailValidation(edit_emailid)
        }

        binding.textSignUp.setOnClickListener {
            startActivity(Intent(this, SignUpActivity::class.java))
        }
        binding.btnLogin.setOnClickListener {
            if (!Validations.emailValidation(edit_emailid))
                return@setOnClickListener
            else if (!Validations.edtValidation(edit_password, "Enter Password"))
                return@setOnClickListener
            else
                signInUser()
        }

        binding.textForgotPassword.setOnClickListener {
            setBottomSheetDialogForgotPass()
        }
    }

    private fun setBottomSheetDialogForgotPass() {
        val bottomSheetDialog = com.google.android.material.bottomsheet.BottomSheetDialog(
            this,
            R.style.MyBottomSheetDialogTheme
        )
        bindingBottomsheet = DataBindingUtil.inflate<BottomsheetForgotPassBinding>(
            layoutInflater,
            R.layout.bottomsheet_forgot_pass,
            null,
            false
        )
        bottomSheetDialog.setContentView(bindingBottomsheet.root)
        bindingBottomsheet.executePendingBindings()

        bindingBottomsheet.editEmailid.afterTextChanged {
            Validations.emailValidation(bindingBottomsheet.editEmailid)
        }

        bindingBottomsheet.btnSubmit.setOnClickListener {
            if (!Validations.emailValidation(bindingBottomsheet.editEmailid))
                return@setOnClickListener
            else resetPassword()

            bottomSheetDialog.dismiss()
        }

        bottomSheetDialog.show()
    }

    private fun observeResetPass() {
        viewModel.isResetPassSuccessful.observe(this, Observer {
            pDialog.dismiss()
        })
    }

    private fun resetPassword() {
        if (Utils.isDataConnected(this)) {
            viewModel.resetPassword(
                emailId = bindingBottomsheet.editEmailid.text.toString(),
            )
            pDialog.show()
        } else {
            Utils.showSNACK_BAR_NO_INTERNET(this, localClassName)
        }
    }

    private fun signInUser() {
        if (Utils.isDataConnected(this)) {
            viewModel.doSignIn(
                emailId = binding.editEmailid.text.toString(),
                pass = binding.editPassword.text.toString()
            )
            pDialog.show()
        } else {
            Utils.showSNACK_BAR_NO_INTERNET(this, localClassName)
        }
    }

    private fun onSignIn() {
        viewModel.isSuccessful.observe(this, androidx.lifecycle.Observer { isSuccess ->
            if (isSuccess) {
                startActivity(Intent(this, DashboardActivity::class.java))
                finish()
            }
            pDialog.dismiss()
        })
    }
}


fun EditText.afterTextChanged(afterTextChanged: (String) -> Unit) {
    this.addTextChangedListener(object : TextWatcher {

        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

        override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

        override fun afterTextChanged(p0: Editable?) {
            afterTextChanged.invoke(p0.toString())
        }
    })
}