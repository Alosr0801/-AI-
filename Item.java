package com.example;

/**
 * 表示游戏中的物品。
 */
public class Item {
    private String name;
    private String description;

    public Item(String name, String description) {
        this.name = name;
        this.description = description;
    }

    // 获取物品名称
    public String getName() {
        return name;
    }

    // 获取物品描述
    public String getDescription() {
        return description;
    }
}
