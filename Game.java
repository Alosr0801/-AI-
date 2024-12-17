package com.example;

import java.io.*;
import java.text.MessageFormat;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.logging.*;

/**
 * 管理游戏的整体流程和逻辑。
 */
public class Game {
    private Player player;
    private Scanner scanner;
    private GameState currentState;
    private static final long PRINT_DELAY_MS = 30; // 每个字符的延迟时间
    private static final Logger logger = Logger.getLogger(Game.class.getName());

    public Game() {
        setupLogger();
        player = new Player();
        scanner = new Scanner(System.in);
        currentState = GameState.BEACH;
    }

    /**
     * 设置日志记录器
     */
    private void setupLogger() {
        try {
            LogManager.getLogManager().reset();
            logger.setLevel(Level.ALL);
            FileHandler fh = new FileHandler("game.log", true);
            fh.setLevel(Level.INFO);
            fh.setFormatter(new SimpleFormatter());
            logger.addHandler(fh);
        } catch (IOException e) {
            printWithDelay("无法设置日志记录。", PRINT_DELAY_MS);
        }
    }

    /**
     * 启动游戏
     */
    public void start() {
        boolean playAgain = true;

        while (playAgain) {
            currentState = GameState.BEACH;
            player = new Player(); // 每次重新开始游戏时重置玩家状态
            enterBeach();
            playAgain = promptPlayAgain();
        }

        printWithDelay("感谢游玩，再见！", PRINT_DELAY_MS);
        scanner.close();
    }

    /**
     * 进入沙滩场景
     */
    private void enterBeach() {
        logger.info("进入沙滩场景");
        printWithDelay("欢迎来到神秘岛屿的冒险游戏！", PRINT_DELAY_MS);
        printWithDelay("你是一名探险家，你的船在海上遇到了风暴，你被冲到了一个神秘的岛屿上。", PRINT_DELAY_MS);
        printWithDelay("你现在在岛屿的沙滩上，你看到了一个洞穴和一条小路。", PRINT_DELAY_MS);

        // 不手动打印选项，让presentChoices统一打印
        List<Choice> choices = Arrays.asList(
            new Choice("1. 进入洞穴", this::handleCavePath),
            new Choice("2. 沿着小路走", this::handleTrailPath)
        );

        presentChoices(choices);
    }

    /**
     * 处理进入洞穴的路径
     */
    private void handleCavePath() {
        logger.info("玩家选择进入洞穴");
        currentState = GameState.CAVE;
        printWithDelay("你进入了洞穴，里面很黑，你需要一个光源。", PRINT_DELAY_MS);
        printWithDelay("你发现地上有一个火把和一盒火柴。", PRINT_DELAY_MS);

        List<Choice> choices = Arrays.asList(
            new Choice("1. 拿起火把", () -> {
                player.addItem(new Item("火把", "可以用来照亮黑暗的洞穴"));
                exploreCaveWithTorch();
            }),
            new Choice("2. 拿起火柴", () -> {
                player.addItem(new Item("火柴", "可以用来点燃火把"));
                exploreCaveWithMatches();
            })
        );

        presentChoices(choices);
    }

    /**
     * 洞穴中玩家选择拿起火把的逻辑
     */
    private void exploreCaveWithTorch() {
        logger.info("玩家拿起火把探索洞穴");
        printWithDelay("你拿起了火把，点燃了它，洞穴里变得明亮起来。", PRINT_DELAY_MS);
        printWithDelay("你继续探索洞穴，发现了一个宝箱。", PRINT_DELAY_MS);
        printWithDelay("你打开了宝箱，里面有一张地图和一把钥匙。", PRINT_DELAY_MS);
        player.addItem(new Item("地图", "显示了岛屿的宝藏位置"));
        player.addItem(new Item("钥匙", "可能用于打开某些锁"));
        gameEnd("你成功地收集了宝藏，游戏结束！");
    }

    /**
     * 洞穴中玩家选择拿起火柴的逻辑
     */
    private void exploreCaveWithMatches() {
        logger.info("玩家拿起火柴但没有光源可用");
        printWithDelay("你拿起了火柴，但是火柴盒是空的。", PRINT_DELAY_MS);
        printWithDelay("你没有光源，只能在黑暗中摸索。", PRINT_DELAY_MS);
        printWithDelay("你不小心碰到了一个陷阱，游戏结束。", PRINT_DELAY_MS);
        gameEnd("游戏结束。");
    }

    /**
     * 处理沿着小路走的路径
     */
    private void handleTrailPath() {
        logger.info("玩家选择沿着小路走");
        currentState = GameState.TRAIL;
        printWithDelay("你沿着小路走，发现了一个废弃的小屋。", PRINT_DELAY_MS);
        printWithDelay("小屋的门是开着的，你决定进去看看。", PRINT_DELAY_MS);
        printWithDelay("你进入了小屋，发现里面有一张桌子和一把椅子。", PRINT_DELAY_MS);
        printWithDelay("桌子上有一张纸条，上面写着：'离开这个岛的唯一方法是找到隐藏在岛上的宝藏。'", PRINT_DELAY_MS);
        printWithDelay("你决定继续探索小屋，寻找更多的线索。", PRINT_DELAY_MS);
        printWithDelay("在小屋的角落里，你发现了一个暗门。", PRINT_DELAY_MS);
        printWithDelay("你决定打开暗门，看看里面有什么。", PRINT_DELAY_MS);
        printWithDelay("暗门后面是一条通往地下的通道。", PRINT_DELAY_MS);
        printWithDelay("你进入了通道，开始了新的冒险。", PRINT_DELAY_MS);
        printWithDelay("通道里很黑，你需要一个光源。", PRINT_DELAY_MS);
        printWithDelay("你发现地上有一个手电筒和一盒电池。", PRINT_DELAY_MS);

        List<Choice> choices = Arrays.asList(
            new Choice("1. 拿起手电筒", () -> {
                player.addItem(new Item("手电筒", "需要电池才能使用"));
                handleFlashlightChoice(true);
            }),
            new Choice("2. 拿起电池", () -> {
                player.addItem(new Item("电池", "可以为手电筒提供能量"));
                handleFlashlightChoice(false);
            })
        );

        presentChoices(choices);
    }

    /**
     * 根据玩家是否先拿手电筒还是电池处理逻辑
     */
    private void handleFlashlightChoice(boolean pickedFlashlightFirst) {
        if (pickedFlashlightFirst) {
            printWithDelay("你拿起了手电筒，但是没有电池，它无法工作。", PRINT_DELAY_MS);
            printWithDelay("你需要找到电池才能继续前进。", PRINT_DELAY_MS);
            printWithDelay("在通道的尽头，你发现了一个电池盒。", PRINT_DELAY_MS);
            player.addItem(new Item("电池盒", "包含多个电池"));
            printWithDelay("你将电池放入手电筒，手电筒亮了起来。", PRINT_DELAY_MS);
        } else {
            printWithDelay("你拿起了电池，但是没有手电筒，你无法使用它们。", PRINT_DELAY_MS);
            printWithDelay("你需要找到手电筒才能继续前进。", PRINT_DELAY_MS);
            printWithDelay("在通道的尽头，你发现了一个手电筒。", PRINT_DELAY_MS);
            player.addItem(new Item("手电筒", "需要电池才能使用"));
            printWithDelay("你将电池放入手电筒，手电筒亮了起来。", PRINT_DELAY_MS);
        }

        printWithDelay("你继续前进，发现了一个岔路口。", PRINT_DELAY_MS);
        printWithDelay("左边的路看起来很潮湿，右边的路看起来很干燥。", PRINT_DELAY_MS);

        List<Choice> choices = Arrays.asList(
            new Choice("1. 走左边的路", () -> {
                printWithDelay("你选择了左边的路，但是这条路很滑，你摔倒了。", PRINT_DELAY_MS);
                printWithDelay("你掉进了一个水坑里，游戏结束。", PRINT_DELAY_MS);
                gameEnd("游戏结束。");
            }),
            new Choice("2. 走右边的路", () -> {
                printWithDelay("你选择了右边的路，这条路很干燥，你走得很顺利。", PRINT_DELAY_MS);
                printWithDelay("你继续前进，发现了一个密室。", PRINT_DELAY_MS);
                printWithDelay("密室里有一个宝箱，你打开了它。", PRINT_DELAY_MS);
                printWithDelay("宝箱里有一张地图和一把钥匙。", PRINT_DELAY_MS);
                player.addItem(new Item("地图", "显示了岛屿的宝藏位置"));
                player.addItem(new Item("钥匙", "可能用于打开某些锁"));
                printWithDelay("你驾驶着小船离开了这个神秘的岛屿，游戏结束。", PRINT_DELAY_MS);
                gameEnd("恭喜你赢得了游戏！");
            })
        );

        presentChoices(choices);
    }

    /**
     * 游戏结束逻辑
     */
    private void gameEnd(String message) {
        logger.info("游戏结束：" + message);
        printWithDelay(message, PRINT_DELAY_MS);
        saveGame(); // 游戏结束自动保存
    }

    /**
     * 提示玩家是否重新开始游戏
     */
    private boolean promptPlayAgain() {
        printWithDelay("你想重新开始游戏吗？", PRINT_DELAY_MS);
        List<Choice> choices = Arrays.asList(
            new Choice("1. 是", this::start),
            new Choice("2. 否", () -> {
                printWithDelay("感谢游玩，再见！", PRINT_DELAY_MS);
                System.exit(0);
            })
        );
        presentChoices(choices);
        // 选择"1. 是"会重新调用start，程序循环开始
        // 选择"2. 否"会退出程序
        return false; // 此处返回值无实际影响，因为"是"会直接重启游戏,"否"会System.exit。
    }

    /**
     * 显示选项并处理玩家的选择
     */
    private void presentChoices(List<Choice> choices) {
        // 在这里打印选项，避免重复打印
        for (Choice choice : choices) {
            printWithDelay(choice.getOptionText(), PRINT_DELAY_MS);
        }

        int choiceNumber = getUserChoice(1, choices.size());
        Choice selectedChoice = choices.get(choiceNumber - 1);
        selectedChoice.getAction().run();
    }

    /**
     * 获取用户选择并验证
     */
    private int getUserChoice(int min, int max) {
        int choice;
        while (true) {
            System.out.print(MessageFormat.format("请输入你的选择（{0}到{1}）：", min, max));
            String input = scanner.nextLine();
            try {
                choice = Integer.parseInt(input);
                if (choice >= min && choice <= max) {
                    break;
                } else {
                    printWithDelay("请输入一个有效的选项（" + min + "到" + max + "）。", PRINT_DELAY_MS);
                }
            } catch (NumberFormatException e) {
                printWithDelay("无效输入，请输入一个数字。", PRINT_DELAY_MS);
            }
        }
        return choice;
    }

    /**
     * 延迟打印方法
     */
    private void printWithDelay(String text, long delayMillis) {
        for (char c : text.toCharArray()) {
            System.out.print(c);
            try {
                TimeUnit.MILLISECONDS.sleep(delayMillis);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt(); // 恢复中断状态
                System.out.println("\n打印被中断。");
                break;
            }
        }
        System.out.println();
    }

    /**
     * 保存游戏状态
     */
    private void saveGame() {
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream("savegame.dat"))) {
            out.writeObject(player);
            out.writeObject(currentState);
            logger.info("游戏已保存。");
            printWithDelay("游戏已保存。", PRINT_DELAY_MS);
        } catch (IOException e) {
            logger.severe("保存游戏时出错：" + e.getMessage());
            printWithDelay("保存游戏时出错。", PRINT_DELAY_MS);
        }
    }

    /**
     * 加载游戏状态
     * 如需要加载功能，可在游戏开始时增加加载选项
     */
    private void loadGame() {
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream("savegame.dat"))) {
            player = (Player) in.readObject();
            currentState = (GameState) in.readObject();
            logger.info("游戏已加载。");
            printWithDelay("游戏已加载。", PRINT_DELAY_MS);
            // 根据加载的状态继续游戏逻辑
            enterCurrentState();
        } catch (IOException | ClassNotFoundException e) {
            logger.severe("加载游戏时出错：" + e.getMessage());
            printWithDelay("加载游戏时出错。", PRINT_DELAY_MS);
            enterBeach();
        }
    }

    /**
     * 根据当前状态进入对应场景
     */
    private void enterCurrentState() {
        switch (currentState) {
            case BEACH:
                enterBeach();
                break;
            case CAVE:
                handleCavePath();
                break;
            case TRAIL:
                handleTrailPath();
                break;
            default:
                gameEnd("游戏状态未知，结束游戏。");
        }
    }
}
