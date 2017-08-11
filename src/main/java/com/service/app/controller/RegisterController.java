package com.service.app.controller;

import com.service.app.dto.in.RegisterDTO;
import com.service.app.exception.ActivationTokenNotFoundException;
import com.service.app.utils.EncryptUtils;
import com.service.app.entity.User;
import com.service.app.converter.UnidirectionalConverter;
import com.service.app.service.MailService;
import com.service.app.service.UserService;
import com.service.app.validator.component.RegisterPasswordsValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import java.util.Optional;

@Controller
@PreAuthorize("hasRole('ROLE_ANONYMOUS')")
@RequestMapping("/register")
public class RegisterController {

    @Autowired
    private UserService userService;
    @Autowired
    private MailService mailService;
    @Autowired
    private RegisterPasswordsValidator registerPasswordsValidator;
    @Autowired
    private UnidirectionalConverter<RegisterDTO, User> converterRegisterDTOToUser;

    @InitBinder
    private void initBinder(WebDataBinder binder) {
        binder.addValidators(registerPasswordsValidator);
    }

    @GetMapping
    public ModelAndView showRegister(
            ModelMap modelMap
    ) {
        modelMap.addAttribute("registerDTO", new RegisterDTO());

        return new ModelAndView("register", modelMap);
    }

    @PostMapping
    public ModelAndView register(
            @ModelAttribute("registerDTO") @Valid RegisterDTO registerDTO,
            BindingResult result,
            ModelMap modelMap
    ) {
        if(result.hasErrors()) {
            return new ModelAndView("register");
        }

        User user = converterRegisterDTOToUser.convert(registerDTO);
        user.setActivationToken(EncryptUtils.encrypt(user.getUsername()));
        user.setAuthorities("ROLE_USER");

        mailService.sendMailWithActivationToken(user.getEmail(), user.getActivationToken());

        userService.saveUser(user);

        return new ModelAndView("redirect:/signIn", modelMap);
    }

    @GetMapping("/thanks")
    public ModelAndView confirmAccount(
            @RequestParam("token") String token
    ) {
        Optional<User> userOptional = userService.findByActivationToken(token);

        if(userOptional.isPresent()) {
            User user = userOptional.get();

            user.setActivationToken(null);
            user.setEnabled(true);

            userService.saveUser(user);
        } else {
            throw new ActivationTokenNotFoundException();
        }

        return new ModelAndView("redirect:/signIn");
    }
}
