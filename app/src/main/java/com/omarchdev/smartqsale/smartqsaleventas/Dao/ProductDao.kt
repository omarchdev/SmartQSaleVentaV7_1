package com.omarchdev.smartqsale.smartqsaleventas.Dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.omarchdev.smartqsale.smartqsaleventas.dto.mProductDto

@Dao
interface ProductDao {
    @Insert
    fun insert(product: mProductDto?)

    @get:Query("SELECT * FROM mProduct")
    val allProducts: List<mProductDto?>?

    @Query("DELETE FROM mProduct")
    fun deleteAll()
}