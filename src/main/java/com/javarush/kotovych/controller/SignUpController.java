package com.javarush.kotovych.controller;

import com.javarush.kotovych.constants.Constants;
import com.javarush.kotovych.constants.UriConstants;
import com.javarush.kotovych.entity.User;
import com.javarush.kotovych.service.UserService;
import com.javarush.kotovych.util.SessionAttributeSetter;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

@Slf4j
@Controller
@RequiredArgsConstructor
public class SignUpController {

    private final UserService userService;


    @GetMapping(UriConstants.SIGNUP_URI)
    public ModelAndView getSignupPage() {
        return new ModelAndView(Constants.SIGNUP);
    }

    @PostMapping(UriConstants.SIGNUP_URI)
    public ModelAndView signUp(@RequestParam(Constants.USERNAME) String username,
                               @RequestParam(Constants.PASSWORD) String password,
                               HttpServletRequest request) {

        User userToCheck = userService.get(username);

        if (userToCheck == null) {
            User user = new User(username, password);
            userService.create(user);
            long id = user.getId();
            SessionAttributeSetter.addSessionAttribute(request, Constants.ID, String.valueOf(id));

            log.info(Constants.USER_WITH_USERNAME_CREATED, username);
            return new ModelAndView(Constants.MAIN_PAGE_REDIRECT);
        } else {
            log.info(Constants.FAILED_TO_CREATE_USER_BECAUSE_IT_ALREADY_EXISTS);
            ModelAndView modelAndView = new ModelAndView(Constants.SIGNUP);
            modelAndView.addObject(Constants.ERROR, Constants.FAILED_TO_CREATE_USER_BECAUSE_IT_ALREADY_EXISTS);
            return modelAndView;
        }
    }
}
