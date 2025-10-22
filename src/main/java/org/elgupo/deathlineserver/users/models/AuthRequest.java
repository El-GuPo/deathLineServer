package org.elgupo.deathlineserver.users.models;

import lombok.Builder;

@Builder
public class AuthRequest {

    public String email;

    public String password;

}
