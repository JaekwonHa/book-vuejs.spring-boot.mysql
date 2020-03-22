package com.taskagile.web.payload;

import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class RegistrationPayloadTests {
    private Validator validator;

    @BeforeEach
    public void setup() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    public void validate_blankPayload_shouldFail() {
        RegistrationPayload payload = new RegistrationPayload(null, null, null);
        Set<ConstraintViolation<RegistrationPayload>> violations = validator.validate(payload);
        assertEquals(3, violations.size());
    }

    @Test
    public void validate_payloadWithInvalidEmail_shouldFail() {
        RegistrationPayload payload = new RegistrationPayload("BadEmailAddress", "MyUsername", "MyPassword");

        Set<ConstraintViolation<RegistrationPayload>> violations = validator.validate(payload);
        assertEquals(1, violations.size());
    }

    @Test
    public void validate_payloadWithEmailAddressLongerThan100_shouldFail() {
        // The maximum allowed localPart is 64 characters
        // http://www.rfc-editor.org/errata_search.php?rfc=3696&eid=1690
        int maxLocalParthLength = 64;
        String localPart = RandomStringUtils.random(maxLocalParthLength, true, true);
        int usedLength = maxLocalParthLength + "@".length() + ".com".length();
        String domain = RandomStringUtils.random(101 - usedLength, true, true);

        RegistrationPayload payload = new RegistrationPayload("MyUsername", localPart + "@" + domain + ".com", "MyPassword");

        Set<ConstraintViolation<RegistrationPayload>> violations = validator.validate(payload);
        assertEquals(1, violations.size());
    }

    @Test
    public void validate_payloadWithUsernameShorterThan2_shouldFail() {
        String usernameTooShort = RandomStringUtils.random(1);
        RegistrationPayload payload = new RegistrationPayload(usernameTooShort, "sunny@taskagile.com", "MyPassword");

        Set<ConstraintViolation<RegistrationPayload>> violations = validator.validate(payload);
        assertEquals(1, violations.size());
    }

    @Test
    public void validate_payloadWithUsernameLongerThan50_shouldFail() {
        String usernameTooLong = RandomStringUtils.random(51);
        RegistrationPayload payload = new RegistrationPayload(usernameTooLong, "sunny@taskagile.com", "MyPassword");

        Set<ConstraintViolation<RegistrationPayload>> violations = validator.validate(payload);
        assertEquals(1, violations.size());
    }

    @Test
    public void validate_payloadWithPasswordShorterThan6_shouldFail() {
        String passwordTooShort = RandomStringUtils.random(5);
        RegistrationPayload payload = new RegistrationPayload(passwordTooShort, "MyUsername", "sunny@taskagile.com");

        Set<ConstraintViolation<RegistrationPayload>> violations = validator.validate(payload);
        assertEquals(1, violations.size());
    }

    @Test
    public void validate_payloadWithPasswordLongerThan30_shouldFail() {
        String passwordTooLong = RandomStringUtils.random(31);
        RegistrationPayload payload = new RegistrationPayload(passwordTooLong, "MyUsername", "sunny@taskagile.com");

        Set<ConstraintViolation<RegistrationPayload>> violations = validator.validate(payload);
        assertEquals(1, violations.size());
    }
}
