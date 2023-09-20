package com.smithSophiav.web3kotlindemo

import android.annotation.SuppressLint
import android.os.Bundle
import android.webkit.WebView
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.smithsophiav.web3kotlin.ETHWeb

class ImportAccountFromKeystore: AppCompatActivity() {
    private var title: TextView? = null
    private var password: TextView? = null
    private var keystoreEditText: EditText? = null
    private var walletDetailEditText: EditText? = null
    private var importAccountFromKeystoreBtn: Button? = null
    private var mWebView: WebView? = null
    private var web3: ETHWeb? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.import_account_from_keystore)
        setupContent()
    }
    private fun setupContent() {
        title = findViewById(R.id.title)
        password = findViewById(R.id.password)
        keystoreEditText = findViewById(R.id.keystore)
        walletDetailEditText = findViewById(R.id.wallet_detail)
        importAccountFromKeystoreBtn = findViewById(R.id.btn_import_account_from_keystore)
        mWebView =  findViewById(R.id.webView)
        web3 = ETHWeb(this, _webView = mWebView!!)
        importAccountFromKeystoreBtn?.setOnClickListener{
            importAccountFromKeystore()
        }
    }
    private fun importAccountFromKeystore() {
        val onCompleted = {result : Boolean ->
            println("ETHWeb setup Completed------->>>>>")
            println(result)
            importAccountFromKeystoreAction()
        }
        if (web3?.isWeb3LoadFinished == false) {
            web3?.setup(true,onCompleted = onCompleted)
        }  else  {
            importAccountFromKeystoreAction()
        }
    }
    @SuppressLint("SetTextI18n")
    private fun importAccountFromKeystoreAction() {
        val password = password?.text.toString()
        val keystore = keystoreEditText?.getText().toString();
        if (password.isNotEmpty()) {
            val onCompleted = {state : Boolean, address: String,privateKey: String,error: String ->
                this.runOnUiThread {
                    if (state) {
                        val text =
                            "address: " + address + "\n\n" +
                            "privateKey: " + privateKey
                        walletDetailEditText?.setText(text)
                    } else {
                        walletDetailEditText?.setText(error)
                    }
                }
            }
            walletDetailEditText?.setText("Import Accounting.......")
            web3?.importAccountFromKeystore(password,keystore,onCompleted = onCompleted)

        }
    }
}