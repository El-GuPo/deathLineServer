package org.elgupo.deathlineserver.users.models;

import java.io.Serializable;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class AuthResponse implements Serializable {
    public Long id;

    public String message;
}
