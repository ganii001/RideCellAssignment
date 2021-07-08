package com.example.ridecellassignment.network.responses

import com.google.gson.annotations.SerializedName

data class LoginResponse(

    @field:SerializedName("person")
    var person: Person? = null,

    @field:SerializedName("authentication_token")
    var authenticationToken: String? = null,

    var message: String? = null
)

data class Role(

    @field:SerializedName("rank")
    var rank: Int? = null,

    @field:SerializedName("key")
    var key: String? = null
)

data class Person(

    @field:SerializedName("role")
    var role: Role? = null,

    @field:SerializedName("app_info_keys")
    var appInfoKeys: List<Any?>? = null,

    @field:SerializedName("display_name")
    var displayName: String? = null,
    var email: String? = null,
    var password: String? = null,
    var created_at: String? = null,
    var updated_at: String? = null,

    @field:SerializedName("key")
    var key: String? = null
)
