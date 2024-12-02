package de.vfh.workhourstracker.usermanagement.domain.user.events;

import de.vfh.workhourstracker.usermanagement.domain.user.MailAddress;
import de.vfh.workhourstracker.usermanagement.domain.user.UserId;
import de.vfh.workhourstracker.usermanagement.domain.user.UserName;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

import java.time.LocalDateTime;

@Getter
public class UserCreated extends ApplicationEvent {
    private final Long userId;
    private final UserName userName;
    private final MailAddress mailAddress;
    private final LocalDateTime occurredAt;

    public UserCreated(Object source, Long userId, UserName userName, MailAddress mailAddress) {
        super(source);
        this.userId = userId;
        this.userName = userName;
        this.mailAddress = mailAddress;
        this.occurredAt = LocalDateTime.now();
    }
}
