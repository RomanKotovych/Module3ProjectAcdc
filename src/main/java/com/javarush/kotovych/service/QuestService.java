package com.javarush.kotovych.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.javarush.kotovych.config.RedisConfig;
import com.javarush.kotovych.constants.Constants;
import com.javarush.kotovych.entity.Quest;
import com.javarush.kotovych.config.SessionCreator;
import com.javarush.kotovych.exception.AppException;
import com.javarush.kotovych.repository.QuestRepository;
import jakarta.transaction.Transactional;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
@Transactional
public class QuestService extends QuestRepository {

    private final SessionCreator sessionCreator;
    private final ObjectMapper objectMapper = new ObjectMapper();


    @Autowired
    public QuestService(SessionCreator sessionCreator) {
        super(Quest.class);
        this.sessionCreator = sessionCreator;
    }

    public boolean checkIfExists(String name) {
        Session session = sessionCreator.getSession();
        Query<Quest> query = session.createQuery("from Quest where name = :name", Quest.class);
        query.setParameter(Constants.NAME, name);

        return query.uniqueResult() != null;
    }


    public Quest getByName(String name) {
        Session session = sessionCreator.getSession();
        Query<Quest> query = session.createQuery("select e from Quest e where name = :name", Quest.class);
        query.setParameter(Constants.NAME, name);

        return query.uniqueResult();
    }

    public Quest getIfExists(String name) {
        Quest quest = getQuestFromCache(name);
        if (quest != null) {
            return quest;
        }
        quest = getByName(name);
        if (quest != null) {
            storeQuestInCache(quest);
        }
        return quest;

    }


    private void storeQuestInCache(Quest quest) {
        try {
            String questJson = objectMapper.writeValueAsString(quest);
            RedisConfig.getRedisCommands().set("quest:" + quest.getName(), questJson);
        } catch (JsonProcessingException e) {
            throw new AppException(e);
        }
    }

    private Quest getQuestFromCache(String name) {
        String questJson = RedisConfig.getRedisCommands().get("quest:" + name);
        if (questJson != null) {
            try {
                return objectMapper.readValue(questJson, Quest.class);
            } catch (IOException e) {
                throw new AppException(e);
            }
        }
        return null;
    }

}
