package linkpool.domain.report

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.repository.findByIdOrNull

fun ReportRepository.getById(id: Long): Report = findByIdOrNull(id)
    ?: throw NoSuchElementException("신고가 존재하지 않습니다. id: $id")

interface ReportRepository : JpaRepository<Report, Long> {
    fun findByReporterIdAndTarget(reporterId: Long, target: ReportTarget): Report?
}