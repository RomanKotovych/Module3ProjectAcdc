package com.javarush.kotovych.controller;

import com.javarush.kotovych.constants.Constants;
import com.javarush.kotovych.constants.LoggerConstants;
import com.javarush.kotovych.constants.UriConstants;
import com.javarush.kotovych.entity.Quest;
import com.javarush.kotovych.service.QuestService;
import com.javarush.kotovych.service.UserService;
import com.javarush.kotovych.util.QuestParser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;


@Slf4j
@Controller
public class QuestCreationController {

    private final QuestService questService;

    @Autowired
    public QuestCreationController(QuestService questService) {
        this.questService = questService;
    }


    @GetMapping(UriConstants.CREATE_QUEST_URI)
    public ModelAndView getCreateQuestPage() {
        return new ModelAndView(Constants.CREATE_QUEST);
    }

    @PostMapping(UriConstants.CREATE_QUEST_URI)
    public ModelAndView createQuest(@RequestParam(Constants.JSON) String json,
                                    @SessionAttribute(value = Constants.ID, required = false) String id) {
        long author = Long.parseLong(id);
        Quest quest;
        try {
            quest = QuestParser.parseFromJson(json);
            quest.setAuthor(author);
            log.info(LoggerConstants.QUEST_CREATED_LOG, quest.getName());
        } catch (Exception e) {
            log.info(Constants.FAILED_TO_CREATE_QUEST);
            ModelAndView modelAndView = new ModelAndView(Constants.CREATE_QUEST);
            modelAndView.addObject(Constants.ERROR, true);
            return modelAndView;
        }
        if (!questService.checkIfExists(quest.getName())) {
            questService.create(quest);
        }
        return new ModelAndView(Constants.REDIRECT_QUEST_NAME + quest.getName());
    }
}
