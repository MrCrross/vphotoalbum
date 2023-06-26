package ru.mrcrross.vphotoalbum.modules.user.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.mrcrross.vphotoalbum.models.HistoryAction;
import ru.mrcrross.vphotoalbum.models.Permission;
import ru.mrcrross.vphotoalbum.models.Role;
import ru.mrcrross.vphotoalbum.models.User;
import ru.mrcrross.vphotoalbum.modules.auth.services.AuthService;
import ru.mrcrross.vphotoalbum.modules.user.repositories.UserRepository;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Component
public class UserService {

    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User get(int id) {
        User user = userRepository.getByID(id);
        List<ArrayList<String>> paramsAndRoles = this.getParamsAndRoles(id);
        user.setRoles(paramsAndRoles.get(1));
        user.setParams(paramsAndRoles.get(0));
        return user;
    }

    public List<Role> getRolesById(int id) {
        return userRepository.getUserRoles(id);
    }

    public List<User> getAll() {
        List<User> users = userRepository.getAll();
        for (User user :
                users) {
            List<ArrayList<String>> paramsAndRoles = this.getParamsAndRoles(user.getId());
            user.setRoles(paramsAndRoles.get(1));
            user.setParams(paramsAndRoles.get(0));
        }
        return users;
    }

    public int add(User user, int[] roles) {
        String password = AuthService.passwordHashing(user.getPassword());
        user.setPassword(password);
        user.setDateAdd(LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
        int id = userRepository.registration(user);
        userRepository.addRolesUser(id, roles);
        return id;
    }

    public void update(int id, User user) {
        user.setDateEdit(LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
        List<Map.Entry<String, Object>> fields = this.getFields(user);
        userRepository.update(id, fields);
    }

    public void updateRoles(int id, int[] roles) {
        List<Role> currentRolesUser = userRepository.getUserRoles(id);
        List<Integer> currentRoles = new ArrayList<>();
        List<Integer> deleteRoles = new ArrayList<>();
        for (Role role : currentRolesUser) {
            currentRoles.add(role.getId());
            deleteRoles.add(role.getId());
        }
        List<Integer> newRoles = new ArrayList<>();
        List<Integer> createRoles = new ArrayList<>();
        for (int role : roles) {
            newRoles.add(role);
            createRoles.add(role);
        }
        createRoles.removeAll(currentRoles);
        deleteRoles.removeAll(newRoles);

        if (!createRoles.isEmpty()) {
            int[] rolesForCreate = new int[createRoles.size()];
            for(int i = 0; i < createRoles.size(); i++){
                rolesForCreate[i] = createRoles.get(i);
            }
            userRepository.addRolesUser(id, rolesForCreate);
        }
        if (!deleteRoles.isEmpty()) {
            String rolesForDelete = deleteRoles.stream().map(Object::toString).collect(Collectors.joining(","));
            userRepository.deleteRolesUser(id, rolesForDelete);
        }
    }

    private List<Map.Entry<String, Object>> getFields(User user) {
        Map<String, Object> map = new HashMap<>();
        if (user.getAvatar() != null && !user.getAvatar().trim().isEmpty()) {
            map.put("avatar", user.getAvatar());
        }
        if (user.getFio() != null && !user.getFio().trim().isEmpty()) {
            map.put("fio", user.getFio());
        }
        if (user.getLogin() != null && !user.getLogin().trim().isEmpty()) {
            map.put("login", user.getLogin());
        }
        if (user.getPassword() != null && !user.getPassword().trim().isEmpty()) {
            String password = AuthService.passwordHashing(user.getPassword());
            user.setPassword(password);
            map.put("password", password);
        }
        if (user.getDateEdit() != null) {
            map.put("date_edit", user.getDateEdit());
        }
        if (user.getDateDelete() != null) {
            map.put("date_delete", user.getDateDelete());
        }
        Set<Map.Entry<String, Object>> set
                = map.entrySet();
        return new ArrayList<>(set);
    }

    private List<ArrayList<String>> getParamsAndRoles(int id) {
        List<Permission> params = userRepository.getUserParamsForSession(id);
        List<Role> roles = userRepository.getUserRolesForSession(id);
        ArrayList<String> arrayParams = new ArrayList<>();
        ArrayList<String> arrayRoles = new ArrayList<>();
        for (Permission param : params) {
            arrayParams.add(param.getTechName());
        }
        for (Role role : roles) {
            arrayRoles.add(role.getTechName());
        }
        List <ArrayList<String>> result = new ArrayList<>();
        result.add(arrayParams);
        result.add(arrayRoles);
        return result;
    }

    public void delete(int id) {
        User user = new User();
        user.setDateDelete(LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
        List<Map.Entry<String, Object>> fields = this.getFields(user);
        userRepository.update(id, fields);
    }

    public void saveUserAction(User user, String path)
    {
        HistoryAction historyAction = new HistoryAction();
        historyAction.setUserID(user.getId());
        historyAction.setPath(path);
        historyAction.setDateAdd(LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
        userRepository.saveAction(historyAction);
    }
}
