package com.example.dictionary.achievement;

import com.example.dictionary.game.GameInfo;
import com.example.dictionary.game.GameManager;
import com.example.dictionary.user.UserManager;
import javafx.scene.image.Image;

public enum AchievementEnum {
    LOGIN1("Chăm chỉ LV1", "login.png", "Đăng nhập 1 ngày", 1),
    LOGIN2("Chăm chỉ LV2", "login.png", "Đăng nhập 7 ngày", 7),
    LOGIN3("Chăm chỉ LV3", "login.png", "Đăng nhập 21 ngày", 21),
    LOGIN4("Chăm chỉ LV4", "login.png", "Đăng nhập 100 ngày", 100),
    LOGIN5("Chăm chỉ LV5", "login.png", "Đăng nhập 365 ngày", 365),
    GAME1("Vua trò chơi LV1", "game.png", "Hoàn thành 1 game", 1),
    GAME2("Vua trò chơi LV2", "game.png", "Hoàn thành 10 game", 10),
    GAME3("Vua trò chơi LV3", "game.png", "Hoàn thành 100 game", 100),
    GAME4("Vua trò chơi LV4", "game.png", "Hoàn thành 1000 game", 1000),
    GAME5("Vua trò chơi LV5", "game.png", "Hoàn thành 5000 game", 5000),
    FIND1("Tìm kiếm LV1", "find.png", "Tìm kiếm 1 từ online", 1),
    FIND2("Tìm kiếm LV2", "find.png", "Tìm kiếm 10 từ online", 10),
    FIND3("Tìm kiếm LV3", "find.png", "Tìm kiếm 100 từ online", 100),
    FIND4("Tìm kiếm LV4", "find.png", "Tìm kiếm 1000 từ online", 1000),
    FIND5("Tìm kiếm LV5", "find.png", "Tìm kiếm 5000 từ online", 5000),
    LEARN1("Học tập LV1", "learn.png", "Thêm 5 từ vào danh sách", 5),
    LEARN2("Học tập LV2", "learn.png", "Thêm 10 từ vào danh sách", 10),
    LEARN3("Học tập LV3", "learn.png", "Thêm 100 từ vào danh sách", 100),
    LEARN4("Học tập LV4", "learn.png", "Thêm 1000 từ vào danh sách", 1000),
    LEARN5("Học tập LV4", "learn.png", "Thêm 1000 từ vào danh sách", 5000),
    PERSIST1("Kiên trì LV1", "persist.png", "Chơi 10 game bất kì, bất kể thắng thua", 10),
    PERSIST2("Kiên trì LV2", "persist.png", "Chơi 50 game bất kì, bất kể thắng thua", 50),
    PERSIST3("Kiên trì LV3", "persist.png", "Chơi 200 game bất kì, bất kể thắng thua", 200),
    PERSIST4("Kiên trì LV4", "persist.png", "Chơi 1000 game bất kì, bất kể thắng thua", 1000),
    PERSIST5("Kiên trì LV5", "persist.png", "Chơi 5000 game bất kì, bất kể thắng thua", 5000),
    CHALLENGE1("Kẻ hủy diệt trò choi LV1", "challenger.png", "Hoàn thành 1 game bất kì, thời gian dưới 5s", 1),
    CHALLENGE2("Kẻ hủy diệt trò choi LV2", "challenger.png", "Hoàn thành 5 game bất kì, thời gian dưới 5s", 5),
    CHALLENGE3("Kẻ hủy diệt trò choi LV3", "challenger.png", "Hoàn thành 10 game bất kì, thời gian dưới 5s", 10),
    CHALLENGE4("Kẻ hủy diệt trò choi LV4", "challenger.png", "Hoàn thành 50 game bất kì, thời gian dưới 5s", 50),
    CHALLENGE5("Kẻ hủy diệt trò choi LV5", "challenger.png", "Hoàn thành 500 game bất kì, thời gian dưới 5s", 500);

    private final String name;
    private final Image image;
    private final String description;
    private final int max;

    /**
     * Retrieves the current value based on the specific case.
     *
     * @return An integer representing different metrics based on the case:
     * - LOGIN: Returns the count of login days for the current user.
     * - GAME: Returns the count of games won by the current user.
     * - FIND: Returns the count of search words by the current user.
     * - LEARN: Returns the count of words added by the current user.
     * - PERSIST: Returns the count of games played by the current user.
     * - CHALLENGE: Returns the count of challenging games won by the current user within 5 minutes.
     */
    public int getCurrent() {
        switch (this) {
            case LOGIN1, LOGIN2, LOGIN3, LOGIN4, LOGIN5 -> {
                return UserManager.getInstance().getCurrentUser().getLoginDays().size();
            }
            case GAME1, GAME2, GAME3, GAME4, GAME5 -> {
                return GameManager.getInstance().getPlayersHistory().stream().filter(gameInfo ->
                        gameInfo.getStatus() == GameInfo.Status.WIN
                        && gameInfo.getPlayerId() == UserManager.getInstance().getCurrentUser().getId()
                ).toList().size();
            }
            case FIND1, FIND2, FIND3, FIND4, FIND5 -> {
                return UserManager.getInstance().getCurrentUser().getCountOfSearchWords();
            }
            case LEARN1, LEARN2, LEARN3, LEARN4, LEARN5 -> {
                return UserManager.getInstance().getCurrentUser().getCountOfAddWords();
            }
            case PERSIST1, PERSIST2, PERSIST3, PERSIST4, PERSIST5 -> {
                return GameManager.getInstance().getPlayersHistory().stream().filter(gameInfo ->
                        gameInfo.getPlayerId() == UserManager.getInstance().getCurrentUser().getId()
                ).toList().size();
            }
            case CHALLENGE1, CHALLENGE2, CHALLENGE3, CHALLENGE4, CHALLENGE5 -> {
                return GameManager.getInstance().getPlayersHistory().stream().filter(gameInfo ->
                        gameInfo.getPlayerId() == UserManager.getInstance().getCurrentUser().getId()
                        && gameInfo.getStatus() == GameInfo.Status.WIN
                        && gameInfo.getTime() <= 5
                ).toList().size();
            }
        }
        return 0;
    }

    /**
     * Retrieves the maximum threshold associated with achievement.
     *
     * @return An integer representing the maximum value.
     */
    public int getMax() {
        return max;
    }

    /**
     * Retrieves the description associated with achievement.
     *
     * @return A string containing the description of the achievement.
     */
    public String getDescription() {
        return description;
    }

    /**
     * Retrieves the name of achievement.
     *
     * @return A string representing the name of the achievement.
     */
    public String getName() {
        return name;
    }

    /**
     * Retrieves the image associated with achievement.
     *
     * @return An Image object representing the visual representation of the achievement.
     */
    public Image getImage() {
        return image;
    }

    /**
     * Constructor to create an AchievementEnum instance.
     *
     * @param name is the name/title of the achievement.
     * @param path is the path to the image file representing the achievement.
     * @param description is the description of the achievement.
     * @param max is the maximum threshold value associated with this achievement.
     */
    AchievementEnum(String name, String path, String description, int max) {
        this.name = name;
        this.image = new Image(getClass().getResourceAsStream(path));
        this.description = description;
        this.max = max;
    }
}
