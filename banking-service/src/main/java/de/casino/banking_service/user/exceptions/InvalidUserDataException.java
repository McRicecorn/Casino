package de.casino.banking_service.user.exceptions;

public class InvalidUserDataException extends RuntimeException {

  public InvalidUserDataException(String message) {
    super(message);
  }
}