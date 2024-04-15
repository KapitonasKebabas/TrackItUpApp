package com.example.trackitupapp

import com.example.trackitupapp.apiServices.responses.MedicineResponse
import com.example.trackitupapp.dataHolder.UserMedicine
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.MockitoAnnotations

class UserMedicineUnitTest {
    @Mock
    private lateinit var mockUserMedicine: UserMedicine

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
    }

    @Test
    fun testUserMedicineAddToList() {
        val medicineList = listOf(
            MedicineResponse(pk = 1, medecine_name = "Medicine 1", medecine_is_prescription = true, medecine_description = "Test", medecine = 1, qty = 5, exp_date = "2024-10-10", is_shared = false, shared_qty = 0),
            MedicineResponse(pk = 2, medecine_name = "Medicine 2", medecine_is_prescription = false, medecine_description = "Test", medecine = 3, qty = 5, exp_date = "2024-10-10", is_shared = false, shared_qty = 0)
        )

        `when`(mockUserMedicine.getList()).thenReturn(medicineList)

        UserMedicine.addToList(medicineList)

        assertEquals(medicineList.size, UserMedicine.getList().size)
    }


}