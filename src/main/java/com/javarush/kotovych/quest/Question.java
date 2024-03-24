package com.javarush.kotovych.quest;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Question {
    private String situation;
    private String text;

    private String firstAnswer;
    private String secondAnswer;

    private String firstNextQuestion;
    private String secondNextQuestion;
}