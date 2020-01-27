package com.kumuda.bookshopandroid.api

import retrofit2.Call

interface IRequestInterface {
    fun<T> callApi(call: Call<T>, reqType: String)
}