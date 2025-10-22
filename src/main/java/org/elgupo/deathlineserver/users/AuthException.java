package org.elgupo.deathlineserver.users;

public class AuthException extends Exception {
    public AuthException() {
        super("Bad auth");
    }
}
