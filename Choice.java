package com.example;

/**
 * 表示玩家的一个选择及其对应的动作。
 */
public class Choice {
    private String optionText;
    private Runnable action;

    public Choice(String optionText, Runnable action) {
        this.optionText = optionText;
        this.action = action;
    }

    // 获取选项文本
    public String getOptionText() {
        return optionText;
    }

    // 执行动作
    public Runnable getAction() {
        return action;
    }
}
