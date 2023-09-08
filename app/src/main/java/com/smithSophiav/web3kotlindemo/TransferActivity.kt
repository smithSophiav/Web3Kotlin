package com.smithSophiav.web3kotlindemo

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.webkit.WebView
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.smithsophiav.web3kotlin.ETHMainNet
import com.smithsophiav.web3kotlin.ETHWeb

class TransferActivity : AppCompatActivity(){
    private var title: TextView? = null
    private var hashValue: TextView? = null
    private var privateKeyEditText: EditText? = null
    private var receiveEditText: EditText? = null
    private var amountEditText: EditText? = null
    private var erc20TokenEditText: EditText? = null
    private var transferBtn: Button? = null
    private var detailBtn: Button? = null
    private var estimateTransactionFeeBtn: Button? = null
    private var web3: ETHWeb? = null
    private var mWebView: WebView? = null
    private var type: String = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.transfer_layout)
        setupContent()
        getData()
    }
    private fun setupContent(){
        erc20TokenEditText  = findViewById(R.id.erc20Token)
        privateKeyEditText = findViewById(R.id.private_key)
        receiveEditText = findViewById(R.id.receive_address)
        amountEditText = findViewById(R.id.amount)
        title = findViewById(R.id.title)
        hashValue = findViewById(R.id.hashValue)
        transferBtn = findViewById(R.id.btn_transfer)
        estimateTransactionFeeBtn = findViewById(R.id.btn_estimateTransactionFee)
        detailBtn = findViewById(R.id.btn_detail)
        mWebView = findViewById(R.id.webView)
        web3 = ETHWeb(context = this, _webView = mWebView!!)
        transferBtn?.setOnClickListener{
            transfer()
        }
        detailBtn?.setOnClickListener{
            val hash = hashValue?.text.toString()
            if (hash.length < 20) { return@setOnClickListener}
            val urlString = "https://etherscan.io/tx/$hash"
            try {
                val intent = Intent(Intent.ACTION_VIEW)
                intent.addCategory(Intent.CATEGORY_BROWSABLE);
                intent.setData(Uri.parse(urlString))
                startActivity(intent)
            } catch (e: Exception) {
                println("The current phone does not have a browser installed")
            }
        }
        estimateTransactionFeeBtn?.setOnClickListener{
            estimateTransactionFee()
        }
    }
    private fun estimateTransactionFee(){
        val onCompleted = {result : Boolean ->
            if (type == "ETH") estimateETHTransactionFee() else estimateERC20TransactionFee()
        }
        if (web3?.isWeb3LoadFinished == false) {
            web3?.setup(true,onCompleted)
        } else  {
            if (type == "ETH") estimateETHTransactionFee() else estimateERC20TransactionFee()
        }
    }

    @SuppressLint("SetTextI18n")
    private fun estimateETHTransactionFee() {
        val toAddress = receiveEditText?.text.toString()
        val amount = amountEditText?.text.toString()
        val senderAddress = "0x2bD47B6fbCb229dDc69534Ac564D93C264F70453"
        if (toAddress.isNotEmpty() && amount.isNotEmpty()) {
            val onCompleted = {result : Boolean, estimateETHTransactionFee: String ->
                this.runOnUiThread {
                    hashValue?.text = estimateETHTransactionFee + "ETH"
                }
            }
            web3?.estimateETHTransactionFee(toAddress,senderAddress,amount,onCompleted = onCompleted)
        }
    }

    @SuppressLint("SetTextI18n")
    private fun estimateERC20TransactionFee() {
        val toAddress = receiveEditText?.text.toString()
        val senderAddress = "0x2bD47B6fbCb229dDc69534Ac564D93C264F70453"
        val amount = amountEditText?.text.toString()
        val erc20TokenAddress = erc20TokenEditText?.text.toString()
        if (toAddress.isNotEmpty() && amount.isNotEmpty() && erc20TokenAddress.isNotEmpty()) {
            val onCompleted = {result : Boolean, estimateETHTransactionFee: String ->
                this.runOnUiThread {
                    hashValue?.text = estimateETHTransactionFee + "ETH"
                }
            }
            web3?.estimateERC20TransactionFee(toAddress,senderAddress,amount,erc20TokenAddress,onCompleted = onCompleted)
        }
    }

    private fun transfer(){
        val onCompleted = {result : Boolean ->
            if (type == "ETH") ethTransfer() else erc20TokenTransfer()
        }
        if (web3?.isWeb3LoadFinished == false) {
            web3?.setup(true,onCompleted)
        } else  {
            if (type == "ETH") ethTransfer() else erc20TokenTransfer()
        }
    }
    private fun ethTransfer() {
        val privateKey = privateKeyEditText?.text.toString()
        val toAddress = receiveEditText?.text.toString()
        val amount = amountEditText?.text.toString()
        if (toAddress.isNotEmpty() && amount.isNotEmpty() && privateKey.isNotEmpty()) {
            val onCompleted = {result : Boolean, txid: String ->
                this.runOnUiThread {
                    hashValue?.text = txid
                }
            }
            web3?.ethTransfer(toAddress,amount,privateKey,onCompleted = onCompleted)
        }
    }
    private fun erc20TokenTransfer() {
        val privateKey = privateKeyEditText?.text.toString()
        val toAddress = receiveEditText?.text.toString()
        val amount = amountEditText?.text.toString()
        val erc20TokenAddress = erc20TokenEditText?.text.toString()
        if (toAddress.isNotEmpty() && amount.isNotEmpty() && privateKey.isNotEmpty() && erc20TokenAddress.isNotEmpty()) {
            val onCompleted = {result : Boolean, txid: String ->
                this.runOnUiThread {
                    hashValue?.text = txid
                }
            }
            web3?.erc20TokenTransfer(
                amount,
                privateKey,
                toAddress,
                decimal= 6.0,
                providerUrl = ETHMainNet,
                erc20ContractAddress = erc20TokenAddress,
                onCompleted = onCompleted)
        }
    }
    private fun getData() {
        val bundle: Bundle? = intent.extras
        if (bundle != null) {
            type = bundle.getString("type") ?: ""
            title?.text = if (type == "ETH") "ETH Transfer" else "ERC20Token Transfer"
            if (type == "ETH") {
                erc20TokenEditText?.setVisibility(View.GONE)
            }
        }
    }
}