package com.service.app.controller.advice;

import com.service.app.exception.ActivationTokenNotFoundException;
import com.service.app.exception.EmailChangeTokenNotFoundException;
import com.service.app.exception.AccessToMessageForbiddenException;
import com.service.app.exception.ProfileNotFoundException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

@ControllerAdvice
public class ExceptionHandlerController {

    @ExceptionHandler(ProfileNotFoundException.class)
    public ModelAndView profileNotFound() {
        return new ModelAndView("redirect:/users");
    }

    @ExceptionHandler(ActivationTokenNotFoundException.class)
    public ModelAndView activationTokenNotFound() {
        return new ModelAndView("index");
    }

    @ExceptionHandler(EmailChangeTokenNotFoundException.class)
    public ModelAndView emailChangeTokenNotFound() {
        return new ModelAndView("index");
    }

    @ExceptionHandler(AccessToMessageForbiddenException.class)
    public ModelAndView forbiddenAccessToMessage() { return new ModelAndView("redirect:/messages"); }
}
