package com.example.trackitupapp

import android.content.Context
import com.example.trackitupapp.activities.AskForMedicineActivity
import com.example.trackitupapp.apiServices.ApiCalls
import com.example.trackitupapp.apiServices.Callbacks.OrderCallback
import com.example.trackitupapp.apiServices.responses.OrderResponse
import com.example.trackitupapp.apiServices.responses.SharedMedicineResponse
import com.example.trackitupapp.helpers.OrderManager
import org.junit.Before
import org.junit.Test
import org.mockito.ArgumentCaptor
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.MockitoAnnotations

class AskForMedicineActivityTest {

    @Mock
    private lateinit var mockApiCalls: ApiCalls

    @Mock
    private lateinit var mockContext: Context

    @Mock
    private lateinit var mockSharedMedicineResponse: SharedMedicineResponse

    private lateinit var orderManager: OrderManager

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        orderManager = OrderManager(mockApiCalls)
    }

    @Test
    fun testCreateOrder_Success() {
        // Arrange
        `when`(mockSharedMedicineResponse.user_pk).thenReturn(1)
        `when`(mockSharedMedicineResponse.pk).thenReturn(1)
        val quantity = 5

        // Create an OrderResponse object matching your OrderResponse data class
        val orderResponse = OrderResponse(
            pk = 1,
            aproved_medecine = 101,
            medecine = 102,
            medecine_name = "Paracetamol",
            user_seller_pk = 2,
            user_seller_username = "seller1",
            user_buyer_pk = 1,
            user_buyer_username = "buyer1",
            user_medicine_pk = 102,
            qty = quantity,
            status = 1,
            status_name = "Pending",
            status_helptext = "Waiting for confirmation"
        )

        // Set up a callback capture to simulate success
        val callbackCaptor = ArgumentCaptor.forClass(OrderCallback::class.java)
        doAnswer {
            callbackCaptor.value.onSuccess(orderResponse)
            null
        }.`when`(mockApiCalls).callAddOrder(eq(mockContext), any(), callbackCaptor.capture())

        // Act
        orderManager.createOrder(mockContext, mockSharedMedicineResponse, quantity, callbackCaptor.value)

        // Assert
        verify(mockApiCalls).callAddOrder(eq(mockContext), any(), any())
    }

    @Test
    fun testCreateOrder_Failure() {
        // Arrange
        `when`(mockSharedMedicineResponse.user_pk).thenReturn(1)
        `when`(mockSharedMedicineResponse.pk).thenReturn(1)
        val quantity = 5

        // Create an OrderResponse object to simulate failure
        val orderResponse = OrderResponse(
            pk = 1,
            aproved_medecine = 101,
            medecine = 102,
            medecine_name = "Paracetamol",
            user_seller_pk = 2,
            user_seller_username = "seller1",
            user_buyer_pk = 1,
            user_buyer_username = "buyer1",
            user_medicine_pk = 102,
            qty = quantity,
            status = 0, // Failure status
            status_name = "Failed",
            status_helptext = "Order failed due to invalid data"
        )

        // Set up a callback capture to simulate failure
        val callbackCaptor = ArgumentCaptor.forClass(OrderCallback::class.java)
        doAnswer {
            callbackCaptor.value.onFailure("Failed to create order")
            null
        }.`when`(mockApiCalls).callAddOrder(eq(mockContext), any(), callbackCaptor.capture())

        // Act
        orderManager.createOrder(mockContext, mockSharedMedicineResponse, quantity, callbackCaptor.value)

        // Assert
        verify(mockApiCalls).callAddOrder(eq(mockContext), any(), any())
    }

    @Test
    fun testInvalidQuantity() {
        val activity = AskForMedicineActivity()
        val result = activity.validateQuantity("-1")

        assert(!result) { "Expected false for invalid quantity" }
    }

    @Test
    fun testValidQuantity() {
        val activity = AskForMedicineActivity()
        val result = activity.validateQuantity("5")

        assert(result) { "Expected true for valid quantity" }
    }
}
