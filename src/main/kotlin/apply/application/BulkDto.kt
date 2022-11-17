package apply.application

data class BulkCreateRequest(
    val new_folders: List<SaveFolderRequest>,
    val new_links: List<SaveLinkWithFolderNameRequest>,
)