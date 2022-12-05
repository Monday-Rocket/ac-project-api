package apply.application

import apply.domain.folder.FolderLinkUpdatedEvent
import apply.domain.jobgroup.getById
import apply.domain.link.Link
import apply.domain.link.LinkRepository
import apply.domain.link.getById
import apply.domain.user.User
import apply.domain.user.UserSignedOutEvent
import apply.exception.CustomException
import apply.ui.api.ResponseCode
import org.springframework.context.ApplicationEventPublisher
import org.springframework.data.domain.PageRequest
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.transaction.event.TransactionPhase
import org.springframework.transaction.event.TransactionalEventListener
import java.time.LocalDateTime

@Transactional
@Service
class LinkService(
    private val userService: UserService,
    private val jobGroupService: JobGroupService,
    private val linkRepository: LinkRepository,
    private val applicationEventPublisher: ApplicationEventPublisher
) {
    fun createBulk(user: User, request: List<SaveLinkRequest>) {
        val links = linkRepository.saveAll(request.map {
            Link(
                userId = user.id,
                url = it.url,
                title = it.title,
                describe = it.describe,
                image = it.image,
                folderId = it.folder_id,
                createdDateTime = it.created_date_time ?: LocalDateTime.now()
            )
        })
        val folderIdSet = mutableSetOf<Long>()
        links.forEach {
            it.folderId ?.let  { folderId ->
                folderIdSet.add(folderId)
            }
        }
        folderIdSet.forEach {
            applicationEventPublisher.publishEvent(FolderLinkUpdatedEvent(it))
        }
    }

    fun countByFolderId(folderId: Long)
        = linkRepository.countByFolderId(folderId)

    fun countUnClassified(userId: Long)
        = linkRepository.countByUserIdAndFolderIdIsNull(userId)

    fun getByFolderId(folderId: Long, pageRequest: PageRequest): Page<LinkResponse> {
        return linkRepository.findPageByFolderIdOrderByCreatedDateTimeDesc(folderId, pageRequest).let {
            Page(
                page_no = it.number,
                page_size = it.size,
                total_count = it.totalElements,
                total_page = it.totalPages,
                contents = it.content.map { link ->
                    LinkResponse(
                        id = link.id,
                        url = link.url,
                        title = link.title,
                        image = link.image,
                        folder_id = link.folderId,
                        describe = link.describe,
                        created_date_time = link.createdDateTime,
                    )
                }
            )
        }
    }

    @TransactionalEventListener(phase = TransactionPhase.BEFORE_COMMIT)
    fun deleteSignedOutUsersLinks(event: UserSignedOutEvent) {
        linkRepository.deleteBatch(event.userId)
    }

    fun getUnclassifiedLinks(uid: String, pageRequest: PageRequest): Page<LinkResponse>? {
        val user = userService.getByUid(uid)
        return linkRepository.findPageByUserIdAndFolderIdIsNullOrderByCreatedDateTimeDesc(user.id, pageRequest).let {
            Page(
                page_no = it.number,
                page_size = it.size,
                total_count = it.totalElements,
                total_page = it.totalPages,
                contents = it.content.map { link ->
                    LinkResponse(
                        id = link.id,
                        url = link.url,
                        title = link.title,
                        image = link.image,
                        folder_id = link.folderId,
                        describe = link.describe,
                        created_date_time = link.createdDateTime,
                    )
                }
            )
        }
    }

    fun create(uid: String, request: SaveLinkRequest) {
        val user = userService.getByUid(uid)
        if (linkRepository.existsByUserIdAndUrl(user.id, request.url)) {
            throw CustomException(ResponseCode.DUPLICATE_LINK_URL)
        }
        val link = linkRepository.save(
            Link(
                userId = user.id,
                url = request.url,
                title = request.title,
                describe = request.describe,
                image = request.image,
                folderId = request.folder_id,
                createdDateTime = request.created_date_time ?: LocalDateTime.now()
            )
        )
        link.folderId ?.let {
            applicationEventPublisher.publishEvent(FolderLinkUpdatedEvent(it, link.image))
        }
    }

    fun update(uid: String, linkId: Long, request: UpdateLinkRequest) {
        val user = userService.getByUid(uid)
        var link = linkRepository.getById(linkId)
        if (link.userId != user.id) throw CustomException(ResponseCode.NOT_AUTHORIZED_FOR_THE_DATA)
        request.url ?.let {
            link.url = it
        }
        request.title ?.let {
            link.title = it
        }
        request.describe ?.let {
            link.describe = it
        }
        request.image ?.let {
            link.image = it
        }
        link.folderId ?.let {
            applicationEventPublisher.publishEvent(FolderLinkUpdatedEvent(it))
        }
        request.folder_id ?.let {
            link.folderId = it
            applicationEventPublisher.publishEvent(FolderLinkUpdatedEvent(it))
        }

    }

    fun delete(uid: String, linkId: Long) {
        val user = userService.getByUid(uid)
        val link = linkRepository.getById(linkId)
        if (link.userId != user.id) throw CustomException(ResponseCode.NOT_AUTHORIZED_FOR_THE_DATA)
        link.folderId ?.let {
            applicationEventPublisher.publishEvent(FolderLinkUpdatedEvent(it))
        }
        link.delete()
    }

    fun getByUserId(uid: String, pageRequest: PageRequest): Page<LinkResponse> {
        val user = userService.getByUid(uid)
        return linkRepository.findPageByUserIdOrderByCreatedDateTimeDesc(user.id, pageRequest).let {
            Page(
                page_no = it.number,
                page_size = it.size,
                total_count = it.totalElements,
                total_page = it.totalPages,
                contents = it.content.map { link ->
                    LinkResponse(
                        id = link.id,
                        url = link.url,
                        title = link.title,
                        image = link.image,
                        folder_id = link.folderId,
                        describe = link.describe,
                        created_date_time = link.createdDateTime,
                    )
                }
            )
        }
    }

    fun getCurrentLinkByFolderId(folderId: Long): Link?
      = linkRepository.findFirst1ByFolderIdOrderByCreatedDateTime(folderId)

    fun getPageByUserId(users: List<User>, pageRequest: PageRequest): Page<Link> {
        return linkRepository.findVisiblePageByUserIdIn(users.map { it.id }, pageRequest).let {
            Page(
                page_no = it.number,
                page_size = it.size,
                total_count = it.totalElements,
                total_page = it.totalPages,
                contents = it.content
            )
        }
    }

    fun getLinksByJobGroupId(id: Long, pageRequest: PageRequest): Page<LinkWithUserResponse> {
        val job = jobGroupService.getById(id)
        val users = userService.getByJobGroupId(job.id)
        return getPageByUserId(users, pageRequest).let {
            Page(
                page_no = it.page_no,
                page_size = it.page_size,
                total_count = it.total_count,
                total_page = it.total_page,
                contents = it.contents.map { link ->
                    LinkWithUserResponse(users.find { user -> user.id == link.userId }!!, job, link)
                }
            )
        }
    }

    fun searchByKeyword(myLinksOnly: Boolean, uid: String, keyword: String, pageRequest: PageRequest): Page<LinkWithUserResponse> {
        if (myLinksOnly) {
            val me = userService.getByUid(uid)
            val job = jobGroupService.getById(me.info!!.jobGroupId)
            return linkRepository.findPageByUserIdAndTitleContains(me.id, keyword, pageRequest).let {
                Page(
                    page_no = it.number,
                    page_size = it.size,
                    total_count = it.totalElements,
                    total_page = it.totalPages,
                    contents = it.content.map { link ->
                        LinkWithUserResponse(
                            me, job, link
                        )
                    }
                )
            }
        } else {
            return linkRepository.findPageByTitleContains(keyword, pageRequest).let {
                Page(
                    page_no = it.number,
                    page_size = it.size,
                    total_count = it.totalElements,
                    total_page = it.totalPages,
                    contents = it.content.map { link ->
                        val user = userService.getById(link.userId)
                        val job = jobGroupService.getById(user.info!!.jobGroupId)
                        LinkWithUserResponse(
                            user, job, link
                        )
                    }
                )
            }
        }
    }
}
