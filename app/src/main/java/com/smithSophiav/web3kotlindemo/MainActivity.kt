package com.smithSophiav.web3kotlindemo

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.Toast

class MainActivity : AppCompatActivity() {
    private var generateAccountBtn: Button? = null
    private var importAccountFromKeystoreBtn: Button? = null
    private var importAccountFromPrivateKeyBtn: Button? = null
    private var importAccountFromMnemonicBtn: Button? = null
    private var getETHBalance: Button? = null
    private var getERC20Balance: Button? = null
    private var ETHTransferBtn: Button? = null
    private var ERC20TransferBtn: Button? = null
    private var changeIntoMainChainBtn: Button? = null
    private var changeIntoGoerliChainBtn: Button? = null
    private var chainType: String = "main"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setupContent()
    }
    private fun setupContent(){
        generateAccountBtn = findViewById(R.id.generateAccount)
        importAccountFromKeystoreBtn = findViewById(R.id.importAccountFromKeystore)
        importAccountFromPrivateKeyBtn = findViewById(R.id.importAccountFromPrivateKey)
        importAccountFromMnemonicBtn = findViewById(R.id.importAccountFromMnemonic)

        getETHBalance = findViewById(R.id.getETHBalance)
        getERC20Balance = findViewById(R.id.getERC20Balance)
        ETHTransferBtn = findViewById(R.id.btn_ETHTransfer)
        ERC20TransferBtn = findViewById(R.id.btn_ERC20Transfer)
        changeIntoMainChainBtn = findViewById(R.id.mainChain)
        changeIntoGoerliChainBtn = findViewById(R.id.goerliChain)
        generateAccountBtn?.setOnClickListener{
            generateAccount()
        }
        importAccountFromKeystoreBtn?.setOnClickListener{
            importAccountFromKeystore()
        }
        importAccountFromMnemonicBtn?.setOnClickListener{
            importAccountFromMnemonic()
        }
        importAccountFromPrivateKeyBtn?.setOnClickListener{
            importAccountFromPrivateKey()
        }
        getETHBalance?.setOnClickListener{
            getBalance(type = "ETH")
        }
        getERC20Balance?.setOnClickListener{
            getBalance(type = "ERC20Token")
        }
        ETHTransferBtn?.setOnClickListener{
            transfer(type = "ETH")
        }
        ERC20TransferBtn?.setOnClickListener{
            transfer(type = "ERC20Token")
        }
        changeIntoMainChainBtn?.setOnClickListener{
            chainType = "main"
            val toast = Toast.makeText(applicationContext, "Changed into MainNet", Toast.LENGTH_SHORT)
            // 显示Toast消息
            toast.show()
        }
        changeIntoGoerliChainBtn?.setOnClickListener{
            chainType = "goerli"
            val toast = Toast.makeText(applicationContext, "Changed into GoerliNet", Toast.LENGTH_SHORT)
            // 显示Toast消息
            toast.show()
        }
    }
    private fun generateAccount(){
        val intent = Intent(this@MainActivity, GenerateAccount::class.java)
        startActivity(intent)
    }
    private fun importAccountFromKeystore(){
        val intent = Intent(this@MainActivity, ImportAccountFromKeystore::class.java)
        startActivity(intent)
    }
    private fun importAccountFromPrivateKey(){
        val intent = Intent(this@MainActivity, ImportAccountFromPrivateKey::class.java)
        startActivity(intent)
    }
    private fun importAccountFromMnemonic(){
        val intent = Intent(this@MainActivity, ImportAccountFromMnemonic::class.java)
        startActivity(intent)
    }

    private fun getBalance(type: String){
        val intent = Intent(this@MainActivity, GetBalanceActivity::class.java)
        intent.putExtra("type", type)
        intent.putExtra("chainType", chainType)
        startActivity(intent)
    }

    private fun transfer(type: String){
        val intent = Intent(this@MainActivity, TransferActivity::class.java)
        intent.putExtra("type", type)
        intent.putExtra("chainType", chainType)
        startActivity(intent)
    }
}