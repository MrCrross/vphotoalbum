package ru.mrcrross.vphotoalbum.wrappers;

import jakarta.servlet.http.HttpSession;
import ru.mrcrross.vphotoalbum.models.User;

public class ControllerWrapper {

    public boolean loginControl(HttpSession session) {
        User currentUser = (User) session.getAttribute("user");
        return currentUser != null;
    }

    public boolean paramsControl(HttpSession session, String techNameParam)
    {
        if (this.loginControl(session)) {
            return true;
        }
        User currentUser = (User) session.getAttribute("user");
        return !currentUser.getParams().contains(techNameParam);
    }
}
