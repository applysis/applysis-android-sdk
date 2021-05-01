package io.applysis.sdk

import java.util.*

/**
 * @param text: mandatory, e.g review text, email body etc.
 * @param title: optional, e.g email subject, question, review title etc.
 * @param date: optional.
 * @param rating: optional, e.g review rating, we expect it to be minimum 1, maximum 5
 * @param author: optional, e.g user name, email address.
 * @param region: optional, it can hold any region value you desire e.g country, city, county.
 * @param version: optional, e.g 1.5, 1.6.1 etc.
 */
data class Feedback(
    val text: String,
    val title: String? = null,
    val date: Date? = null,
    val rating: Int? = null,
    val author: String? = null,
    val region: String? = null,
    val version: String? = null
)