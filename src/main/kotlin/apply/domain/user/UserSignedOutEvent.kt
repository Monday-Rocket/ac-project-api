package apply.domain.user

import org.springframework.context.ApplicationEvent

class UserSignedOutEvent(
    val userId: Long
): ApplicationEvent(userId)