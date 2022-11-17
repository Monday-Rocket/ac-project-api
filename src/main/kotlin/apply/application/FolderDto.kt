package apply.application

import java.time.LocalDateTime


data class SaveFolderRequest(
    val name: String,
    val visible: Boolean? = false,
    val created_date_time: LocalDateTime? = null
)