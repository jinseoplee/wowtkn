package com.wowtkn.backend.exception;

public class BattleNetOAuthException extends RuntimeException {

    public BattleNetOAuthException(String message) {
        super(message);
    }

    public BattleNetOAuthException(String message, Throwable cause) {
        super(message, cause);
    }
}
