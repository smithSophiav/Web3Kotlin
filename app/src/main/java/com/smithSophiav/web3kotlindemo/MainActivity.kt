package com.smithSophiav.web3kotlindemo

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class MainActivity : AppCompatActivity() {
    private var getETHBalance: Button? = null
    private var getERC20Balance: Button? = null
    private var ETHTransferBtn: Button? = null
    private var ERC20TransferBtn: Button? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setupContent()
    }
    private fun setupContent(){
        getETHBalance = findViewById(R.id.getETHBalance)
        getERC20Balance = findViewById(R.id.getERC20Balance)
        ETHTransferBtn = findViewById(R.id.btn_ETHTransfer)
        ERC20TransferBtn = findViewById(R.id.btn_ERC20Transfer)
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
    }

    private fun getBalance(type: String){
        val intent = Intent(this@MainActivity, GetBalanceActivity::class.java)
        intent.putExtra("type", type)
        startActivity(intent)
    }

    private fun transfer(type: String){
        val intent = Intent(this@MainActivity, TransferActivity::class.java)
        intent.putExtra("type", type)
        startActivity(intent)
    }
}