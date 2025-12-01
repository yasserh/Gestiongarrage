package com.renault.garage.domain.exception;

/**
 * Exception de base pour toutes les exceptions métier de l'application.
 * 
 * Cette classe abstraite sert de base pour toutes les exceptions
 * liées à la logique métier de l'application.
 * 
 * Pattern utilisé: Exception Hierarchy
 * Principe SOLID: Open/Closed Principle
 * 
 * @author Renault Team
 * @version 1.0.0
 */
public abstract class BusinessException extends RuntimeException {

    /**
     * Code d'erreur métier
     */
    private final String errorCode;

    /**
     * Constructeur avec message
     * 
     * @param message le message d'erreur
     */
    protected BusinessException(String message) {
        super(message);
        this.errorCode = this.getClass().getSimpleName();
    }

    /**
     * Constructeur avec message et cause
     * 
     * @param message le message d'erreur
     * @param cause la cause de l'exception
     */
    protected BusinessException(String message, Throwable cause) {
        super(message, cause);
        this.errorCode = this.getClass().getSimpleName();
    }

    /**
     * Constructeur avec message et code d'erreur personnalisé
     * 
     * @param message le message d'erreur
     * @param errorCode le code d'erreur
     */
    protected BusinessException(String message, String errorCode) {
        super(message);
        this.errorCode = errorCode;
    }

    public String getErrorCode() {
        return errorCode;
    }
}
