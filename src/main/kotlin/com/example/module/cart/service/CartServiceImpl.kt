package com.example.module.cart.service

import com.example.common.DatabaseFactory
import com.example.common.OperateFailCauses
import com.example.common.OperateResultDTO
import com.example.common.PageDataDTO
import com.example.common.dao.CartItemEntity
import com.example.common.dao.ProductEntity
import com.example.common.dao.UserIdEntity
import com.example.common.table.CartItems
import com.example.common.vo.CartItem
import com.example.common.vo.basic.PageDTO
import com.example.springify.Service
import org.jetbrains.exposed.sql.and
import java.math.BigDecimal

@Service
class CartServiceImpl : CartService {
    override suspend fun getCartItemsByPage(userId: Int, page: PageDTO): PageDataDTO<CartItem> =
        DatabaseFactory.dbQuery {
            val list = CartItemEntity.find { CartItems.user eq userId }.toList()
            PageDataDTO(
                data = list.map {
                    CartItem(
                        productId = it.product.id.value,
                        title = it.product.title,
                        price = (it.product.price * BigDecimal.valueOf(it.quantity.toLong())).toDouble(),
                        quantity = it.quantity
                    )
                }, total = list.size
            )
        } ?: PageDataDTO(emptyList(), 0)

    override suspend fun addToCart(userId: Int, id: Int, quantity: Int): OperateResultDTO =
        DatabaseFactory.dbQuery {
            val product = ProductEntity.findById(id) ?: return@dbQuery OperateResultDTO(
                false,
                "商品",
                OperateFailCauses.RECORD_NOT_EXIST
            )
            val exist = CartItemEntity.find { (CartItems.user eq userId) and (CartItems.product eq id) }
            if (exist.count() > 0) {
                if (exist.count() > 1) {
                    return@dbQuery OperateResultDTO(
                        false,
                        "数据异常",
                        OperateFailCauses.UNKNOWN
                    )
                } else {
                    exist.toList().first().quantity += quantity
                    return@dbQuery OperateResultDTO(true)
                }
            } else {
                val user = UserIdEntity.findById(userId) ?: return@dbQuery OperateResultDTO(
                    false,
                    "用户",
                    OperateFailCauses.UNKNOWN
                )
                CartItemEntity.new {
                    this.user = user
                    this.product = product
                    this.quantity = quantity
                }
                return@dbQuery OperateResultDTO(true)
            }
        } ?: OperateResultDTO(false, "数据库事务未知异常", OperateFailCauses.UNKNOWN)

    override suspend fun removeCartItems(userId: Int, ids: List<Int>): OperateResultDTO = DatabaseFactory.dbQuery {
        val itemsToDelete = mutableListOf<CartItemEntity>()
        ids.forEach {
            val found = CartItemEntity.find { CartItems.product eq it }
            if (found.count() > 0) {
                assert(found.count() == 1L)
                itemsToDelete.add(
                    found.firstOrNull() ?: return@dbQuery OperateResultDTO(
                        false,
                        "$it",
                        OperateFailCauses.RECORD_NOT_EXIST
                    )
                )
            } else {
                return@dbQuery OperateResultDTO(false, "$it", OperateFailCauses.RECORD_NOT_EXIST)
            }
        }
        itemsToDelete.forEach { it.delete() }
        return@dbQuery OperateResultDTO(true, "删除成功")
    } ?: OperateResultDTO(false, "数据库事务未知异常", OperateFailCauses.UNKNOWN)

    override suspend fun editCartItem(userId: Int, id: Int, quantity: Int): OperateResultDTO =
        DatabaseFactory.dbQuery {
            val exist = CartItemEntity.find { (CartItems.user eq userId) and (CartItems.product eq id) }
            if (exist.count() > 0) {
                if (exist.count() > 1) {
                    return@dbQuery OperateResultDTO(
                        false,
                        "数据异常",
                        OperateFailCauses.UNKNOWN
                    )
                } else {
                    exist.toList().first().quantity = quantity
                    return@dbQuery OperateResultDTO(true)
                }
            } else {
                return@dbQuery OperateResultDTO(false, "商品不存在", OperateFailCauses.RECORD_NOT_EXIST)
            }
        } ?: OperateResultDTO(false, "数据库事务未知异常", OperateFailCauses.UNKNOWN)
}