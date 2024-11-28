package de.vfh.workhourstracker.usermanagement.domain.user;

import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@Getter
@NoArgsConstructor
public class MailAddress {
    private String mailAddress;

    public MailAddress(String mailAddress) {
        this.mailAddress = mailAddress;
    }

}
