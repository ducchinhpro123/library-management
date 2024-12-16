package com.example.library_management.validator;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;

import com.example.library_management.model.Account;


@Component
public class AccountValidator implements org.springframework.validation.Validator {

  @Override
  public boolean supports(Class<?> clazz) {
    return Account.class.equals(clazz);
  }

  @Override
  public void validate(Object target, Errors errors) {
    Account account = (Account) target;
   
    System.out.println("METHOD IS CALLED");

    if (account.getPassword() != null && account.getPasswordConfirm() != null) {
      if (!account.getPassword().equals(account.getPasswordConfirm())) {
        errors.rejectValue("passwordConfirm", "password.mismatch", "Passwords do not match");
      }
    }
  }

}
