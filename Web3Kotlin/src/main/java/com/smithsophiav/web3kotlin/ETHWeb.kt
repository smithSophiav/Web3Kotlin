package com.smithsophiav.web3kotlin

import android.content.Context
import android.graphics.Bitmap
import android.webkit.*
import java.lang.reflect.InvocationTargetException

public const val ETHMainNet: String = "https://mainnet.infura.io/v3/fe816c09404d406f8f47af0b78413806"
public const val ERC20TokenUSDT: String = "0xdAC17F958D2ee523a2206206994597C13D831ec7"

public class ETHWeb(context: Context, _webView: WebView) {
    private val webView = _webView
    public var isWeb3LoadFinished: Boolean = false
    private var bridge = WebViewJavascriptBridge(_context = context,_webView = webView)
    var onCompleted = { _: Boolean  -> }
    private var showLog: Boolean = false
    init {
        setAllowUniversalAccessFromFileURLs(webView)
        webView.getSettings().setAllowFileAccess(true);
        webView.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE)
    }
    public fun setup(showLog: Boolean = true, onCompleted: (Boolean) -> Unit) {
        this.showLog = showLog
        this.onCompleted = onCompleted
        webView.webViewClient = webClient
        if (showLog) {
            bridge.consolePipe = object : ConsolePipe {
                override fun post(string : String){
                    println("Next line is javascript console.log->>>")
                    println(string)
                }
            }
        }
        webView.loadUrl("file:///android_asset/ETHIndex.html")
        val handler = object :Handler {
            override fun handler(map: HashMap<String, Any>?, callback: Callback) {
                isWeb3LoadFinished = true
                println("js load finished")
                onCompleted(true)
            }
        }
        bridge.register("FinishLoad",handler)
    }
    public fun getETHBalance(address: String,
                             providerUrl: String = ETHMainNet,
                              onCompleted: (Boolean,String) -> Unit) {
        val data = java.util.HashMap<String, Any>()
        data["address"] = address
        data["providerUrl"] = providerUrl
        bridge.call("getETHBalance", data, object : Callback {
            override fun call(map: HashMap<String, Any>?){
                if (showLog) {
                    println(map)
                }
                val state =  map!!["state"] as Boolean
                val balance = if (!state) "error" else  map["balance"] as String
                onCompleted(state,balance)
            }
        })
    }
    public fun getERC20TokenBalance(address: String,
                                    contractAddress: String = ERC20TokenUSDT,
                                    decimals: Double = 6.0,
                                    providerUrl: String = ETHMainNet,
                                  onCompleted: (Boolean,String) -> Unit) {
        val data = java.util.HashMap<String, Any>()
        data["address"] = address
        data["contractAddress"] = contractAddress
        data["providerUrl"] = providerUrl
        data["decimals"] = decimals
        bridge.call("getERC20TokenBalance", data, object : Callback {
            override fun call(map: HashMap<String, Any>?){
                if (showLog) {
                    println(map)
                }
                val state =  map!!["state"] as Boolean
                val balance = if (!state) "error" else  map["balance"] as String
                onCompleted(state,balance)
            }
        })
    }

    public fun ethTransfer(recipientAddress: String,
                           amount: String,
                           senderPrivateKey: String,
                           providerUrl: String = ETHMainNet,
                                     onCompleted: (Boolean,String) -> Unit) {
        val data = java.util.HashMap<String, Any>()
        data["recipientAddress"] = recipientAddress
        data["amount"] = amount
        data["providerUrl"] = providerUrl
        data["senderPrivateKey"] = senderPrivateKey
        bridge.call("ETHTransfer", data, object : Callback {
            override fun call(map: HashMap<String, Any>?){
                if (showLog) {
                    println(map)
                }
                val state = map!!["state"] as Boolean
                val txid = map["txid"].toString()
                onCompleted(state,txid)
            }
        })
    }
    public fun erc20TokenTransfer(amount: String,
                                  senderPrivateKey: String,
                                  recipientAddress:String,
                                  decimal:Double = 6.0,
                                  providerUrl: String = ETHMainNet,
                                  erc20ContractAddress: String = ERC20TokenUSDT,
                                   onCompleted: (Boolean,String) -> Unit) {
        val data = java.util.HashMap<String, Any>()
        data["recipientAddress"] = recipientAddress
        data["providerUrl"] = providerUrl
        data["senderPrivateKey"] = senderPrivateKey
        data["contractAddress"] = erc20ContractAddress
        data["amount"] = amount
        data["decimal"] = decimal
        bridge.call("ERC20Transfer", data, object : Callback {
            override fun call(map: HashMap<String, Any>?){
                if (showLog) {
                    println(map)
                }
                val state =  map!!["state"] as Boolean
                val txid =   map["txid"].toString()
                onCompleted(state,txid)
            }
        })
    }

    public fun estimateETHTransactionFee(recipientAddress: String,
                                         senderAddress: String,
                                         amount: String,
                           providerUrl: String = ETHMainNet,
                           onCompleted: (Boolean,String) -> Unit) {
        val data = java.util.HashMap<String, Any>()
        data["recipientAddress"] = recipientAddress
        data["providerUrl"] = providerUrl
        data["senderAddress"] = senderAddress
        data["amount"] = amount
        bridge.call("estimateETHTransactionFee", data, object : Callback {
            override fun call(map: HashMap<String, Any>?){
                if (showLog) {
                    println(map)
                }
                val state =  map!!["state"] as Boolean
                val estimateTransactionFee = if (!state) "error" else  map["estimateTransactionFee"] as String
                onCompleted(state,estimateTransactionFee)
            }
        })
    }

    public fun estimateERC20TransactionFee(recipientAddress: String,
                                           senderAddress: String,
                                           amount: String,
                                           contractAddress:String,
                                           decimal: Double = 6.0,
                                           providerUrl: String = ETHMainNet,
                                           onCompleted: (Boolean,String) -> Unit) {
        val data = java.util.HashMap<String, Any>()
        data["recipientAddress"] = recipientAddress
        data["providerUrl"] = providerUrl
        data["senderAddress"] = senderAddress
        data["amount"] = amount
        data["decimal"] = decimal
        data["contractAddress"] = contractAddress
        bridge.call("estimateERC20TransactionFee", data, object : Callback {
            override fun call(map: HashMap<String, Any>?){
                if (showLog) {
                    println(map)
                }
                val state =  map!!["state"] as Boolean
                val estimateTransactionFee = if (!state) "error" else  map["estimateTransactionFee"] as String
                onCompleted(state,estimateTransactionFee)
            }
        })
    }

    private val webClient = object : WebViewClient() {
        override fun shouldOverrideUrlLoading(view: WebView?, request: WebResourceRequest?): Boolean {
            println("shouldOverrideUrlLoading")
            return false
        }
        override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
            println("onPageStarted")
            bridge.injectJavascript()
        }
        override fun onPageFinished(view: WebView?, url: String?) {
            println("onPageFinished")
        }
    }
    //Allow Cross Domain
    private fun setAllowUniversalAccessFromFileURLs(webView: WebView) {
        try {
            val clazz: Class<*> = webView.settings.javaClass
            val method = clazz.getMethod(
                "setAllowUniversalAccessFromFileURLs", Boolean::class.javaPrimitiveType
            )
            method.invoke(webView.settings, true)
        } catch (e: IllegalArgumentException) {
            e.printStackTrace()
        } catch (e: NoSuchMethodException) {
            e.printStackTrace()
        } catch (e: IllegalAccessException) {
            e.printStackTrace()
        } catch (e: InvocationTargetException) {
            e.printStackTrace()
        }
    }
}