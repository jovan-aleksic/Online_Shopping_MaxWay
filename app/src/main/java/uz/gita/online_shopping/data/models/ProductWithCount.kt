package uz.gita.online_shopping.data.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import uz.gita.online_shopping.data.models.dto.OrderItemDto

// Created by Jamshid Isoqov an 10/10/2022

@Parcelize
data class ProductWithCount(
    val productData: ProductData,
    var count: Int = 0
) : Parcelable {
    fun toProductOrder() = ProductOrder(productId = productData.id, count)

    fun toOrderItem() = OrderItemDto(productData.id, count)
}
