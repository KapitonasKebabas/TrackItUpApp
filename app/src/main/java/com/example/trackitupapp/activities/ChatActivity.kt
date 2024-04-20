package com.example.trackitupapp.activities

import ChatAdapter
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.Gravity
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)

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

        val btnCallOrderUpdate = findViewById<Button>(R.id.btn_call_order_update)
        btnCallOrderUpdate.setOnClickListener {
            callOrderUpdate()
        }
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

    private fun callOrderUpdate() {
        val intent = intent
        if (intent != null) {
            val orderId = intent.getIntExtra("orderId", -1)
            val order = Orders.getObjectByPk(orderId)
            if (order == null) {
                Toast.makeText(this, "No order selected", Toast.LENGTH_SHORT).show()
                return
            }
            ApiCalls().callOrderUpdate(applicationContext, order, object : OrderCallback {
                override fun onSuccess(orderResponse: OrderResponse) {
                    Toast.makeText(this@ChatActivity, "Order updated successfully", Toast.LENGTH_SHORT).show()
                }

                override fun onFailure(errorMessage: String) {
                    Toast.makeText(this@ChatActivity, "Failed to update order: $errorMessage", Toast.LENGTH_SHORT).show()
                }
            })
        }
    }
}
