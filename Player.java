package com.example;

import java.util.ArrayList;
import java.util.List;

/**
 * 表示玩家，跟踪玩家的物品清单。
 */
public class Player {
    private List<Item> inventory;

    public Player() {
        inventory = new ArrayList<>();
    }

    /**
     * 添加物品到玩家的清单中。
     */
    public void addItem(Item item) {
        inventory.add(item);
        System.out.println("你获得了：" + item.getName());
    }

    /**
     * 检查玩家是否拥有指定名称的物品。
     */
    public boolean hasItem(String itemName) {
        return inventory.stream().anyMatch(item -> item.getName().equalsIgnoreCase(itemName));
    }

    /**
     * 显示玩家的物品清单。
     */
    public void showInventory() {
        System.out.println("你的物品清单：");
        inventory.forEach(item -> System.out.println("- " + item.getName() + ": " + item.getDescription()));
    }
}
