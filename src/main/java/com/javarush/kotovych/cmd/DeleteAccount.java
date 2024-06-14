package com.javarush.kotovych.cmd;

import com.javarush.kotovych.constants.Constants;
import com.javarush.kotovych.constants.LoggerConstants;
import com.javarush.kotovych.constants.UriConstants;
import com.javarush.kotovych.service.UserService;
import com.javarush.kotovych.util.SessionAttributeSetter;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.SessionAttribute;
import org.springframework.web.servlet.ModelAndView;

@Slf4j
@Controller
public class DeleteAccount {
    private final UserService userService;

    public DeleteAccount(UserService userService) {
        this.userService = userService;
    }

    @PostMapping(UriConstants.DELETE_ACCOUNT_URI)
    public ModelAndView deleteAccount(@SessionAttribute(value = Constants.ID) long id,
                                      HttpServletRequest request) {
        userService.delete(userService.getIfExists(id));
        SessionAttributeSetter.addSessionAttribute(request, Constants.ID, Constants.DEFAULT_ID);
        log.info(LoggerConstants.USER_ID_DELETED_LOG, id);
        return new ModelAndView(Constants.MAIN_PAGE_REDIRECT);
    }
}
