package com.example.library_management.validator;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;

import com.example.library_management.model.Account;
import com.example.library_management.model.ProfileUpdateDTO;


@Component
public class AccountValidator implements org.springframework.validation.Validator {

  @Override
  public boolean supports(Class<?> clazz) {
    return Account.class.equals(clazz);
  }

  private void validateAccount(Account account, Errors errors) {
    ValidationUtils.rejectIfEmptyOrWhitespace(errors, "username", "field.required");
    ValidationUtils.rejectIfEmptyOrWhitespace(errors, "password", "field.required");

    if (account.getFirstName() != null && 
        (account.getFirstName().length() < 2 || account.getFirstName().length() > 50)) {
      errors.rejectValue("firstName", "size.account.firstName");
        }

    if (account.getLastName() != null && 
        (account.getLastName().length() < 2 || account.getLastName().length() > 50)) {
      errors.rejectValue("lastName", "size.account.lastName");
        }
  }

  private void validateProfileDTO(ProfileUpdateDTO profile, Errors errors) {
    if (profile.getFirstName() != null && 
        (profile.getFirstName().length() < 2 || profile.getFirstName().length() > 50)) {
      errors.rejectValue("firstName", "size.profile.firstName");
        }

    if (profile.getLastName() != null && 
        (profile.getLastName().length() < 2 || profile.getLastName().length() > 50)) {
      errors.rejectValue("lastName", "size.profile.lastName");
        }
  }

  private void validateProfileUpdate(ProfileUpdateDTO profileUpdate, Errors errors) {
    if (profileUpdate.getFirstName() != null && profileUpdate.getFirstName().length() < 2) {
      errors.rejectValue("firstName", "firstName.tooShort",  "First name must be at least 2 characters");
    }
  }

  @Override
  public void validate(Object target, Errors errors) {
    
    System.out.println(target);

    if (target instanceof Account) {
      validateAccount((Account) target, errors);
    } else if (target instanceof ProfileUpdateDTO) {
      validateProfileDTO((ProfileUpdateDTO) target, errors);
    } 
  }

}
