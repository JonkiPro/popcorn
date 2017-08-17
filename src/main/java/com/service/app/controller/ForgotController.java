package com.service.app.controller;

import com.service.app.dto.in.ForgotPasswordDTO;
import com.service.app.dto.in.ForgotUsernameDTO;
import com.service.app.entity.User;
import com.service.app.service.MailService;
import com.service.app.service.UserService;
import com.service.app.utils.EncryptUtils;
import com.service.app.utils.RandomUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import java.util.Optional;

@Controller
@PreAuthorize("hasRole('ROLE_ANONYMOUS')")
public class ForgotController {

    @Autowired
    private UserService userService;
    @Autowired
    private MailService mailService;

    /**
     * @param modelMap {@link ModelMap}
     * @return Returns the ModelAndView for the page that is responsible for sending the email with the username.
     */
    @GetMapping("/forgotUsername")
    public ModelAndView showFormForgotUsername(
            ModelMap modelMap
    ) {
        modelMap.addAttribute("forgotUsernameDTO", new ForgotUsernameDTO());

        return new ModelAndView("forgotUsername", modelMap);
    }

    /**
     * @param forgotUsernameDTO A form to remind the user's name.
     * @param result {@link BindingResult}
     * @param modelMap {@link ModelMap}
     * @return Returns the ModelAndView for the page that is responsible for sending the email with the username.
     */
    @PostMapping("/forgotUsername")
    public ModelAndView recoverUsername(
            @ModelAttribute("forgotUsernameDTO") @Valid ForgotUsernameDTO forgotUsernameDTO,
            BindingResult result,
            ModelMap modelMap
    ) {
        if(result.hasErrors()) {
            modelMap.addAttribute("error", true);

            return new ModelAndView("forgotUsername", modelMap);
        }

        Optional<User> userOptional = userService.findByEmail(forgotUsernameDTO.getEmail());

        if(userOptional.isPresent()) {
            User user = userOptional.get();

            mailService.sendMailWithUsername(user.getEmail(), user.getUsername());
        }

        modelMap.addAttribute("success", true);

        return new ModelAndView("forgotUsername", modelMap);
    }

    /**
     * @param modelMap {@link ModelMap}
     * @return Returns the ModelAndView for the page that is responsible for sending the email with the password.
     */
    @GetMapping("/forgotPassword")
    public ModelAndView showFormForgotPassword(
            ModelMap modelMap
    ) {
        modelMap.addAttribute("forgotPasswordDTO", new ForgotPasswordDTO());

        return new ModelAndView("forgotPassword", modelMap);
    }

    /**
     * @param forgotPasswordDTO A form to send a new password.
     * @param result {@link BindingResult}
     * @param modelMap {@link ModelMap}
     * @return Returns the ModelAndView for the page that is responsible for sending the email with the password.
     */
    @PutMapping("/forgotPassword")
    public ModelAndView recoverPassword(
            @ModelAttribute("forgotPasswordDTO") @Valid ForgotPasswordDTO forgotPasswordDTO,
            BindingResult result,
            ModelMap modelMap
    ) {
        if(result.hasErrors()) {
            return new ModelAndView("forgotPassword");
        }

        Optional<User> userOptional = userService.findByUsernameAndEmail(forgotPasswordDTO.getUsername(), forgotPasswordDTO.getEmail());

        if(!userOptional.isPresent()) {
            modelMap.addAttribute("error", true);

            return new ModelAndView("forgotPassword", modelMap);
        }

        User user = userOptional.get();

        String newPassword = RandomUtils.randomPassword();

        user.setPassword(EncryptUtils.encrypt(newPassword));

        mailService.sendMailWithNewPassword(user.getEmail(), newPassword);

        userService.saveUser(user);

        modelMap.addAttribute("success", true);

        return new ModelAndView("forgotPassword", modelMap);
    }
}
