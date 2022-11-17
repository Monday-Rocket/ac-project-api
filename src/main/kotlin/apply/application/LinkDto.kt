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