package apply.application

import java.time.LocalDateTime

data class SaveLinkWithFolderNameRequest(
    val url: String,
    val title: String? = null,
    val describe: String? = null,
    val image: String? = null,
    val folder_name: String? = null,
    val created_date_time: LocalDateTime? = null
)

data class SaveLinkRequest(
    val url: String,
    val title: String? = null,
    val describe: String? = null,
    val image: String? = null,
    val folder_id: Long? = null,
    val created_date_time: LocalDateTime? = null
)

data class UpdateLinkRequest(
    val url: String? = null,
    val title: String? = null,
    val describe: String? = null,
    val image: String? = null,
    val folder_id: Long? = null,
)


data class LinkResponse(
    val id: Long,
    val url: String,
    val title: String? = null,
    val image: String? = null,
    val folder_id: Long? = null,
    val describe: String? = null,
    val created_date_time: LocalDateTime,
)