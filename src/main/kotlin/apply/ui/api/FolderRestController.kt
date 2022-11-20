package apply.ui.api

import apply.application.*
import apply.security.LoginUser
import org.springframework.data.domain.PageRequest
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RequestMapping("/folders")
@RestController
class FolderRestController(
    private val folderService: FolderService,
    private val linkService: LinkService
) {

    @PostMapping
    fun create(
        @RequestBody request: SaveFolderRequest,
        @LoginUser uid: String
    ): ResponseEntity<ApiResponse<Unit>> {
        folderService.create(uid, request)
        return ResponseEntity.ok(ApiResponse.success())
    }

    @PatchMapping("/{folderId}")
    fun update(
        @PathVariable("folderId") folderId: Long,
        @RequestBody request: UpdateFolderRequest,
        @LoginUser uid: String
    ): ResponseEntity<ApiResponse<Unit>> {
        folderService.update(uid, folderId, request)
        return ResponseEntity.ok(ApiResponse.success())
    }

    @DeleteMapping("/{folderId}")
    fun update(
        @PathVariable("folderId") folderId: Long,
        @LoginUser uid: String
    ): ResponseEntity<ApiResponse<Unit>> {
        folderService.delete(uid, folderId)
        return ResponseEntity.ok(ApiResponse.success())
    }

    @GetMapping
    fun getByUserId(
        @LoginUser uid: String
    ): ResponseEntity<ApiResponse<List<GetByUserIdResponse>>> {
        return ResponseEntity.ok(ApiResponse.success(folderService.getByUserId(uid)))
    }

    @GetMapping("/{folderId}/links")
    fun getLinksOfFolder(
        @PathVariable("folderId") folderId: Long,
        @RequestParam(value = "page_no", required = false) pageNo: Int = 0,
        @RequestParam(value = "page_size", required = false) pageSize: Int = 10,
        @LoginUser uid: String
    ): ResponseEntity<ApiResponse<Page<LinkResponse>>> {
        return ResponseEntity.ok(ApiResponse.success(folderService.getLinksByFolderId(uid, folderId, PageRequest.of(pageNo, pageSize))))
    }
}