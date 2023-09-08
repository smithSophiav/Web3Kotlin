package com.smithsophiav.web3kotlin

interface Handler {
    fun handler(map: HashMap<String, Any>?, callback: Callback)
}