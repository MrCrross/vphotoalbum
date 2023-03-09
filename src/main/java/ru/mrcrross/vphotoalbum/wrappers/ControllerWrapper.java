package ru.mrcrross.vphotoalbum.wrappers;

import jakarta.servlet.http.HttpSession;
import ru.mrcrross.vphotoalbum.models.User;

public class ControllerWrapper {

    public String loginControl(HttpSession session) {
        User currentUser = (User) session.getAttribute("user");
        if(currentUser == null)
        {
            return "redirect:/";
        }
        return "";
    }

    public boolean paramsControl(HttpSession session, String techNameParam)
    {
        User currentUser = (User) session.getAttribute("user");
        if (currentUser.getParams().contains(techNameParam)) {
            return true;
        }
        return false;
    }
}
