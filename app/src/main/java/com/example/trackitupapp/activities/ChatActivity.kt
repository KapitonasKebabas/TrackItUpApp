package com.example.trackitupapp.activities

import ChatAdapter
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.trackitupapp.R
import com.example.trackitupapp.apiServices.ApiCalls
import com.example.trackitupapp.apiServices.Callbacks.OrderCallback
import com.example.trackitupapp.apiServices.responses.ChatMessageResponse
import com.example.trackitupapp.apiServices.responses.OrderResponse
import com.example.trackitupapp.dataHolder.Orders

class ChatActivity : AppCompatActivity() {

    private lateinit var chatAdapter: ChatAdapter
    private lateinit var order: OrderResponse
    private lateinit var calls: ApiCalls
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)

        val orderId = intent.getIntExtra("orderId", -1)
        if(orderId == -1)
        {
            finish()
        }

        calls = ApiCalls()

        order = Orders.getObjectByPk(orderId)!!

        setupViews()
        setupListeners()
    }

    private fun setupViews() {
        chatAdapter = ChatAdapter()

        val recycler = findViewById<RecyclerView>(R.id.rv_chat_messages)
        recycler.layoutManager = LinearLayoutManager(this)
        recycler.adapter = chatAdapter
    }

    private fun setupListeners() {
        val btnReg = findViewById<Button>(R.id.btn_chat_sendMessage)
        btnReg.setOnClickListener {
            sendMessage()
        }

        val btnCancelStatus = findViewById<Button>(R.id.btn_cancel_status)
        btnCancelStatus.setOnClickListener {
            cancelOrder()
        }

        val btnFinishStatus = findViewById<Button>(R.id.btn_finish_status)
        btnFinishStatus.setOnClickListener {
            finishOrder()
        }

    }

    private fun finishOrder() {
        order.status = 2

        calls.callOrderUpdate(
            applicationContext,
            order,
            object : OrderCallback {
                override fun onSuccess(medicine: OrderResponse) {
                    finish()
                }

                override fun onFailure(message: String) {
                    Toast.makeText(this@ChatActivity, "Nepavayko užbaigti užsakymą", Toast.LENGTH_SHORT).show()
                }

            }
        )
    }

    private fun cancelOrder() {
        order.status = 1

        calls.callOrderUpdate(
            applicationContext,
            order,
            object : OrderCallback {
                override fun onSuccess(medicine: OrderResponse) {
                    finish()
                }

                override fun onFailure(message: String) {
                    Toast.makeText(this@ChatActivity, "Nepavayko atšaukti užsakymą", Toast.LENGTH_SHORT).show()
                }

            }
        )
    }


    private fun sendMessage() {
        val message = findViewById<EditText>(R.id.et_chat_chatboxMessage).text.toString().trim()
        if (message.isNotEmpty()) {
            chatAdapter.addMessage(ChatMessageResponse(message, true))
            findViewById<EditText>(R.id.et_chat_chatboxMessage).text.clear()
            findViewById<RecyclerView>(R.id.rv_chat_messages).scrollToPosition(chatAdapter.itemCount - 1)

            simulateOtherUserResponse()
        } else {
            Toast.makeText(this, "Please enter a message", Toast.LENGTH_SHORT).show()
        }
    }

    private fun simulateOtherUserResponse() {
        Handler(Looper.getMainLooper()).postDelayed({
            val response = "Response from other user"
            chatAdapter.addMessage(ChatMessageResponse(response, false))
            findViewById<RecyclerView>(R.id.rv_chat_messages).scrollToPosition(chatAdapter.itemCount - 1)
        }, 1000)
    }

}
