package ru.garskov.springcourse.ClimateSensorAPI.util;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ErrorMessageBuilderTest {

    private BindingResult bindingResult;
    private List<FieldError> fieldErrors;

    @BeforeEach
    public void setUp() {
        fieldErrors = new ArrayList<>();
        bindingResult = mock(BindingResult.class);
    }

    @Test
    public void shouldBuildErrorMessage_WhenFieldErrorsExist() {
        fieldErrors.add(new FieldError("objectName", "field1", "Field1 is required"));
        fieldErrors.add(new FieldError("objectName", "field2", "Field2 must be a valid email"));

        when(bindingResult.getFieldErrors()).thenReturn(fieldErrors);

        // Проверяем, что при вызове метода buildErrorMessage выбрасывается исключение
        SensorException exception = assertThrows(SensorException.class, () -> {
            ErrorMessageBuilder.buildErrorMessage(bindingResult);
        });

        // Проверяем, что сообщение об ошибке сформировано правильно
        String expectedMessage = "field1 - Field1 is required;field2 - Field2 must be a valid email;";
        assertEquals(expectedMessage, exception.getMessage());
    }
}