package com.github.pedroluiznogueira.microservices.messaging.service;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class AbonnementQueue {
    private String naam;
    private Status status;

    @JsonIgnore
    public boolean isGeactiveerd() {
        return Status.ACTIEF.equals(status);
    }

    public enum Status {
        ACTIEF, INACTIEF
    }
}
