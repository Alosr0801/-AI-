package com.example;

import java.util.List;

/**
 * 表示游戏中的一个房间或场景。
 */
public class Room {
    private String description;
    private List<Choice> choices;

    public Room(String description, List<Choice> choices) {
        this.description = description;
        this.choices = choices;
    }

    // 获取房间描述
    public String getDescription() {
        return description;
    }

    // 获取房间的所有选择
    public List<Choice> getChoices() {
        return choices;
    }
}
