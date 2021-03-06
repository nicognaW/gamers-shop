/**
 * WOW-Market
 * No description provided (generated by Openapi Generator https://github.com/openapitools/openapi-generator)
 *
 * The version of the OpenAPI document: 1.0.0
 *
 *
 * NOTE: This class is auto generated by OpenAPI Generator (https://openapi-generator.tech).
 * https://openapi-generator.tech
 * Do not edit the class manually.
 */
package com.example.common.vo.basic

import com.google.gson.annotations.SerializedName
import kotlinx.serialization.SerialName


/**
 *
 * @param pageNumber
 * @param pageSize
 * @param order
 * @param pageTotal 仅响应使用
 * @param total 仅响应使用
 */
@kotlinx.serialization.Serializable
data class Page(
    @SerialName("page_number")
    @SerializedName("page_number")
    val pageNumber: Int = 1,
    @SerialName("page_size")
    @SerializedName("page_size")
    val pageSize: Int = 10,
    val order: String? = null,
    /* 仅响应使用 */
    @SerialName("page_total")
    @SerializedName("page_total")
    val pageTotal: Int? = null,
    /* 仅响应使用 */
    val total: Int? = null
)

typealias PageDTO = Page