package apply.application

import java.time.LocalDateTime

data class Page<T> (
    val page_no: Int,
    val page_size: Int,
    val total_count: Int,
    val total_page: Int,
    val contents: List<T>
)