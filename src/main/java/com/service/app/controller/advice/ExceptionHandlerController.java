package com.service.app.controller.advice;

import com.service.app.exception.MessageNotFoundException;
import com.service.app.exception.AccountNotFoundException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

@ControllerAdvice
public class ExceptionHandlerController {

    @ExceptionHandler(AccountNotFoundException.class)
    public ModelAndView accountNotFound() {
        return new ModelAndView("redirect:/users");
    }

    @ExceptionHandler(MessageNotFoundException.class)
    public ModelAndView messageNotFound() { return new ModelAndView("redirect:/messages"); }
}
