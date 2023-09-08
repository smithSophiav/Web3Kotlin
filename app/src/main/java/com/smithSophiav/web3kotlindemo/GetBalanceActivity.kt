package com.smithSophiav.web3kotlindemo
import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import android.webkit.WebView
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.smithsophiav.web3kotlin.ETHWeb

class GetBalanceActivity : AppCompatActivity(){
    private var title: TextView? = null
    private var balance: TextView? = null
    private var address: EditText? = null
    private var ERC20TokenAddress: EditText? = null
    private var getBalanceBtn: Button? = null
    private var mWebView: WebView? = null
    private var web3: ETHWeb? = null
    private var type: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.balance_layout)
        setupContent()
        getData()
    }
    private fun setupContent() {
        title = findViewById(R.id.title)
        balance = findViewById(R.id.balance)
        address = findViewById(R.id.address)
        ERC20TokenAddress = findViewById(R.id.ERC20TokenAddress)
        getBalanceBtn = findViewById(R.id.btn_getBalance)
        mWebView =  findViewById(R.id.webView)
        web3 = ETHWeb(this, _webView = mWebView!!)
        getBalanceBtn?.setOnClickListener{
            getBalance()
        }
    }
    private fun getBalance() {
        val onCompleted = {result : Boolean ->
            println("ETHWeb setup Completed------->>>>>")
            println(result)
            if (type == "ETH") getETHBalance() else getERC20TokenBalance()
        }
        if (web3?.isWeb3LoadFinished == false) {
            web3?.setup(true,onCompleted = onCompleted)
        }  else  {
            if (type == "ETH") getETHBalance() else  getERC20TokenBalance()
        }
    }
    @SuppressLint("SetTextI18n")
    private fun getETHBalance() {
        val address = address?.text.toString()
        if (address.isNotEmpty()) {
            val onCompleted = {result : Boolean, amount: String ->
                this.runOnUiThread {
                    val  titleTip = if(type == "ETH") "ETH Balance: " else "ERC20Token Balance: "
                    balance?.text = titleTip + amount
                }
            }
            balance?.text = "fetching..."
            web3?.getETHBalance(address,onCompleted = onCompleted)
        }
    }
    @SuppressLint("SetTextI18n")
    private fun getERC20TokenBalance() {
        val address = address?.text.toString()
        val erc20TokenAddress = ERC20TokenAddress?.text.toString()
        if (address.isNotEmpty() && erc20TokenAddress.isNotEmpty()) {
            val onCompleted = {result : Boolean, amount: String ->
                this.runOnUiThread {
                    val  titleTip = if(type == "ETH") "ETH Balance: " else "ERC20Token Balance: "
                    balance?.text = titleTip + amount
                }
            }
            balance?.text = "fetching..."
            web3?.getERC20TokenBalance(address,onCompleted = onCompleted)
        }
    }
    private fun getData() {
        //接收传值
        val bundle: Bundle? = intent.extras
        if (bundle != null) {
            type = bundle.getString("type") ?: ""
            title?.text = if (type == "ETH") "GET ETH Balance" else "GET ERC20Token Balance"
            if (type == "ETH") {
                ERC20TokenAddress?.setVisibility(View.GONE)
            }
        }
    }
}