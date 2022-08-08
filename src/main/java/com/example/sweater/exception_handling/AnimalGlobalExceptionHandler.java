package com.example.sweater.exception_handling;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import java.text.ParseException;

@ControllerAdvice
public class AnimalGlobalExceptionHandler {

    @ExceptionHandler
    public ResponseEntity<AnimalIncorrectData> handleException(NoSuchAnimalException exception) {
        AnimalIncorrectData data = new AnimalIncorrectData();
        data.setInfo("1." + exception.getMessage());
        return new ResponseEntity<>(data, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler
    public ResponseEntity<AnimalIncorrectData> handleException(NumberFormatException exception) {
        AnimalIncorrectData data = new AnimalIncorrectData();
        data.setInfo("2.Введен некорректный Id.");
        return new ResponseEntity<>(data, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler
    public ResponseEntity<AnimalIncorrectData> handleException(Throwable throwable) {
        AnimalIncorrectData data = new AnimalIncorrectData();
        data.setInfo("3.Введены некорректные данные.");
        return new ResponseEntity<>(data, HttpStatus.EXPECTATION_FAILED);
    }

    @ExceptionHandler
    public ResponseEntity<AnimalIncorrectData> handleException(ParseException exception) {
        AnimalIncorrectData data = new AnimalIncorrectData();
        data.setInfo("4.Неверный формат даты. Введите в соответствии с форматом.");
        return new ResponseEntity<>(data, HttpStatus.EXPECTATION_FAILED);
    }


}
