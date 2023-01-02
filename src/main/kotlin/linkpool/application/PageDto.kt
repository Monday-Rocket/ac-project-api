package linkpool.application

data class Page<T> (
    val page_no: Int,
    val page_size: Int,
    val total_count: Long,
    val total_page: Int,
    val contents: List<T>
)