package linkpool.ui.api

import linkpool.application.*
import linkpool.security.LoginUser
import org.springframework.data.domain.PageRequest
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RequestMapping("/links")
@RestController
class LinkRestController(
    private val linkService: LinkService
) {

    @PostMapping
    fun create(
        @RequestBody request: SaveLinkRequest,
        @LoginUser uid: String
    ): ResponseEntity<ApiResponse<Unit>> {
        linkService.create(uid, request)
        return ResponseEntity.ok(ApiResponse.success())
    }

    @PatchMapping("/{linkId}")
    fun update(
        @PathVariable("linkId") linkId: Long,
        @RequestBody request: UpdateLinkRequest,
        @LoginUser uid: String
    ): ResponseEntity<ApiResponse<Unit>> {
        linkService.update(uid, linkId, request)
        return ResponseEntity.ok(ApiResponse.success())
    }

    @DeleteMapping("/{linkId}")
    fun delete(
        @PathVariable("linkId") linkId: Long,
        @LoginUser uid: String
    ): ResponseEntity<ApiResponse<Unit>> {
        linkService.delete(uid, linkId)
        return ResponseEntity.ok(ApiResponse.success())
    }

    @GetMapping
    fun getByUserId(
        @RequestParam(value = "page_no", defaultValue = "0") pageNo: Int,
        @RequestParam(value = "page_size", defaultValue = "10") pageSize: Int,
        @LoginUser uid: String
    ): ResponseEntity<ApiResponse<Page<LinkResponse>>> {
        return ResponseEntity.ok(ApiResponse.success(linkService.getByUserId(uid, PageRequest.of(pageNo, pageSize))))
    }

    @GetMapping("/unclassified")
    fun getLinksOfFolder(
        @RequestParam(value = "page_no", defaultValue = "0") pageNo: Int,
        @RequestParam(value = "page_size", defaultValue = "10") pageSize: Int,
        @LoginUser uid: String
    ): ResponseEntity<ApiResponse<Page<LinkResponse>>> {
        return ResponseEntity.ok(ApiResponse.success(linkService.getUnclassifiedLinks(uid, PageRequest.of(pageNo, pageSize))))
    }

    @GetMapping("/search")
    fun searchLinkByKeyword(
        @RequestParam(value = "my_links_only", required = true) myLinksOnly: Boolean,
        @RequestParam(value = "keyword", required = true) keyword: String,
        @RequestParam(value = "page_no", defaultValue = "0") pageNo: Int,
        @RequestParam(value = "page_size", defaultValue = "10") pageSize: Int,
        @LoginUser uid: String
    ): ResponseEntity<ApiResponse<Page<LinkWithUserResponse>>> {
        return ResponseEntity.ok(ApiResponse.success(linkService.searchByKeyword(myLinksOnly, uid, keyword, PageRequest.of(pageNo, pageSize))))
    }

}