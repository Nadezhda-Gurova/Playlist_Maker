package com.example.vehicle_shop_clean.domain.consumer

interface Consumer<T> {
    fun consume(data: ConsumerData<T>)
}
