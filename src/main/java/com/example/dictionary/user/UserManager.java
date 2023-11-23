package com.example.dictionary.user;

import com.example.dictionary.Application;
import com.example.dictionary.controller.Controller;
import com.example.dictionary.game.GameManager;

import java.io.*;
import java.time.LocalDate;
import java.util.ArrayList;

public class UserManager implements Serializable {
    private static final String DATA_PATH = "data/users.dat";
    private ArrayList<User> users;
    private static UserManager instance;

    private transient User currentUser;

    /**
     * Retrieves the currently logged-in user.
     *
     * @return The currently logged-in user.
     */
    public User getCurrentUser() {
        return currentUser;
    }

    /**
     * Sets the currently logged-in user.
     *
     * @param user The user to be set as the currently logged-in user.
     */
    private void setCurrentUser(User user) {
        if (currentUser != null) {
            currentUser.writeData();
        }
        currentUser = user;
        user.login();
        Controller.handleChangeUser();
        Application.getInstance().changeTheme(user.getTheme());
    }

    /**
     * Retrieves the instance of the UserManager.
     *
     * @return The instance of the UserManager.
     */
    public static UserManager getInstance() {
        if (instance == null) {
            try {
                ObjectInputStream is = new ObjectInputStream(new FileInputStream(DATA_PATH));
                instance = (UserManager) is.readObject();
                if (instance.users.size() != 0) {
                    User.lastUserId = instance.users.get(instance.users.size() - 1).getId();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return instance;
    }

    /**
     * Logs in a user with the provided username and password.
     *
     * @param username The username of the user trying to log in.
     * @param password The password of the user trying to log in.
     * @return True if login is successful, false otherwise.
     */
    public boolean login(String username, String password) {
        for (User user : users) {
            if (user.getPassword().equals(password) && user.getUsername().equals(username)) {
                setCurrentUser(user);
                return true;
            }
        }
        return false;
    }

    /**
     * Creates a new user with the provided username and password.
     *
     * @param username The username of the new user.
     * @param password The password of the new user.
     * @return True if user creation is successful, false if the username already exists.
     */
    public boolean create(String username, String password) {
        for (User user : users) {
            if (user.getUsername().equals(username)) {
                return false;
            }
        }
        User user = new User(username, password);
        users.add(user);
        setCurrentUser(user);
        return true;
    }

    /**
     * Removes the current user and their associated data.
     */
    public void remove() {
        currentUser.remove();
        users.remove(currentUser);
        currentUser = null;
    }

    /**
     * Provides a string representation of the UserManager object.
     *
     * @return A string representation of the UserManager object.
     */
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder("Users:");
        for (User user : users) {
            builder.append('\n');
            builder.append(user.toString());
        }
        return builder.toString();
    }

    /**
     * Writes data associated with the UserManager and the currently logged-in user to storage.
     */
    public void writeData() {
        try {
            if(currentUser != null)
                currentUser.writeData();
            ObjectOutputStream os = new ObjectOutputStream(new FileOutputStream(DATA_PATH));
            os.writeObject(instance);
            GameManager.writeData();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        try {
            UserManager userManager = new UserManager();
            userManager.users = new ArrayList<>();

            User user1 = new User("admin", "admin");
            userManager.users.add(user1);

            User user = new User("pmquy", "pmquy");
            user.getLoginDays().add(LocalDate.now().minusDays(1));
            user.getLoginDays().add(LocalDate.now().minusDays(2));
            user.getLoginDays().add(LocalDate.now().minusDays(3));
            user.getLoginDays().add(LocalDate.now().minusDays(4));
            user.setCoin(1000);
            userManager.users.add(user);
            System.out.println(user.getLoginDays());

            System.out.println(userManager);
            ObjectOutputStream os = new ObjectOutputStream(new FileOutputStream(DATA_PATH));
            os.writeObject(userManager);
            os.close();
            GameManager gameManager = new GameManager();
            os = new ObjectOutputStream(new FileOutputStream(GameManager.DATA_PATH));
            os.writeObject(gameManager);
            os.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public ArrayList<User> getUsers() {
        return users;
    }
}
