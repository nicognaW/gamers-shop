package com.example.module.product.service

import com.example.common.DatabaseFactory
import com.example.common.PageDataDTO
import com.example.common.dao.ProductEntity
import com.example.common.dao.TagEntity
import com.example.common.table.ProductTags
import com.example.common.table.Products
import com.example.common.vo.basic.Page
import com.example.common.vo.Product
import com.example.common.vo.Tag
import com.example.springify.Service
import org.jetbrains.exposed.sql.select

@Service
class ProductServiceImpl : ProductService {
    override suspend fun getTags(): List<Tag> {
        return DatabaseFactory.dbQuery {
            return@dbQuery TagEntity.all().map {
                Tag(it.id.value, it.tagName)
            }
        } ?: emptyList()
    }

    /**
     * Get product by multiple tags' intersection
     *
     * @param page the page VO
     * @param tags tag ids to filter by
     * @return products fit all tags and split into pages
     */
    override suspend fun getTaggedProductsByPage(page: Page, tags: List<Tag>): PageDataDTO<Product> =
        DatabaseFactory.dbQuery {
            val productIds: MutableSet<Int> = mutableSetOf()
            ProductTags
                .slice(ProductTags.product, ProductTags.tag)
                .select { ProductTags.tag inList tags.map { tag -> tag.id!! } }
                .groupBy { it -> it[ProductTags.product] }
                .forEach { (entityID, resultRows) ->
                    if (resultRows.map { row -> row[ProductTags.tag].value }
                            .containsAll(tags.map { tag -> tag.id!! })) productIds.add(entityID.value)
                }
            PageDataDTO(
                data = ProductEntity.find { Products.id inList productIds }.map {
                    Product(
                        it.id.value,
                        it.title,
                        it.price.toInt(),
                        it.tags.map { tagEntity -> Tag(tagEntity.id.value, tagEntity.tagName) },
                        it.status,
                        it.images.map { imageEntity -> imageEntity.imageUrl }
                    )
                }, total = ProductEntity.count().toInt()
            )
        } ?: PageDataDTO(emptyList(), 0)

    override suspend fun getProductsByPage(page: Page): PageDataDTO<Product> = DatabaseFactory.dbQuery {
        PageDataDTO(
            data = ProductEntity.all().map {
                Product(
                    it.id.value,
                    it.title,
                    it.price.toInt(),
                    it.tags.map { tagEntity -> Tag(tagEntity.id.value, tagEntity.tagName) },
                    it.status,
                    it.images.map { imageEntity -> imageEntity.imageUrl }
                )
            }, total = ProductEntity.count().toInt()
        )
    } ?: PageDataDTO(emptyList(), 0)
}