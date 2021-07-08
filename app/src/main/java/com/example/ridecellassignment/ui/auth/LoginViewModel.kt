package com.example.ridecellassignment.ui.auth

import android.content.Context
import android.view.View
import androidx.databinding.ObservableInt
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ridecellassignment.ConstantClass
import com.example.ridecellassignment.MyPreferenses
import com.example.ridecellassignment.Utils
import com.example.ridecellassignment.network.api.repository.ApiRepository
import com.example.ridecellassignment.network.responses.Person
import com.google.gson.Gson
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.launch

class LoginViewModel @ViewModelInject constructor(
    @ApplicationContext
    val context: Context,
    private val apiRepository: ApiRepository,
) : ViewModel() {

    var isSuccessful: MutableLiveData<Boolean> = MutableLiveData()
    var isResetPassSuccessful: MutableLiveData<Boolean> = MutableLiveData()

    fun doSignIn(
        emailId: String?,
        pass: String?
    ) {
        viewModelScope.launch {
            val person = Person().apply {
                email = emailId
                password = pass
            }

            apiRepository.authenticate(person).let {
                try {
                    if (it.isSuccessful) {
                        isSuccessful.value = true

                        MyPreferenses(context).setString(
                            ConstantClass.TOKEN,
                            it.body()?.authenticationToken
                        )
                        MyPreferenses(context).setString(ConstantClass.EMAIl, emailId)
                        MyPreferenses(context).setString(ConstantClass.PASSWORD, pass)
                    } else {
                        isSuccessful.value = false
                        if (it.errorBody() != null) {
                            Utils.showToast(
                                context,
                                it.errorBody()?.string()
                                    ?.split(":")!![1].split(",")[0]
                            )
                        }
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                    isSuccessful.value = false
                }
            }
        }
    }

    fun doSignUp(
        name: String?,
        emailId: String?,
        pass: String?
    ) {
        viewModelScope.launch {
            val person = Person().apply {
                displayName = name
                email = emailId
                password = pass
            }

            apiRepository.signUp(person).let {
                try {
                    if (it.isSuccessful) {
                        isSuccessful.value = true
                        Utils.showToast(context, "User Registered Successfully.")
                    } else {
                        isSuccessful.value = false
                        if (it.errorBody() != null) {
                            Utils.showToast(
                                context,
                                it.errorBody()?.string()
                                    ?.split(":")!![1].split(",")[0]
                            )
                        }
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                    isSuccessful.value = false
                }
            }
        }
    }

    fun resetPassword(
        emailId: String?,
    ) {
        viewModelScope.launch {
            val person = Person().apply {
                email = emailId
            }

            apiRepository.resetPassword(person).let {
                try {
                    if (it.isSuccessful && it.body()?.message != null) {
                        isResetPassSuccessful.value = true
                        Utils.showToast(
                            context,
                            "Password Reset link has been sent to your Email Id"
                        )
                    } else {
                        isResetPassSuccessful.value = false
                        if (it.errorBody() != null) {
                            Utils.showToast(
                                context,
                                it.errorBody()?.string()
                                    ?.split(":")!![1].split(",")[0]
                            )
                        }
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                    isResetPassSuccessful.value = false
                }
            }
        }
    }
}