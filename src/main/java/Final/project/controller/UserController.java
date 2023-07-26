package Final.project.controller;

import Final.project.dao.AccountDao;
import Final.project.dao.UserDao;
import Final.project.entities.Account;
import Final.project.entities.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolation;
import javax.validation.Valid;
import javax.validation.Validation;
import javax.validation.Validator;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Controller
public class UserController {

    @Autowired
    UserDao userDao;

    @Autowired
    AccountDao accountDao;

    Set<ConstraintViolation<User>> violations = new HashSet<>();
    @GetMapping("users")
    public String displayOrganizations(Model model) {
        List<User> users = userDao.getAllUsers();
        List<Account> accounts = accountDao.getAllAccounts();
        model.addAttribute("users", users);
        model.addAttribute("accounts", accounts);
        model.addAttribute("errors", violations);
        return "users";
    }

    @PostMapping("addUser")
    public String addOrganization(HttpServletRequest request) {
        String[] accountIds = request.getParameterValues("accountID");

        List<Account> accounts = new ArrayList<>();
        if(accountIds != null) {
            for(String accountId: accountIds) {
                accounts.add(accountDao.getAccountById(Integer.parseInt(accountId)));
            }
        }

        String firstName = request.getParameter("userFirstName");
        String lastName = request.getParameter("userLastName");
        String email = request.getParameter("Email");
        String phone = request.getParameter("Phone");

        User user = new User();
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setEmail(email);
        user.setPhone(phone);

        Validator validate = Validation.buildDefaultValidatorFactory().getValidator();
        violations = validate.validate(user);

        if(violations.isEmpty()) {
            userDao.addUser(user);
        }
        return "redirect:/users";
    }

    @GetMapping("userDetail")
    public String userDetail(Integer id, Model model) {
        User user = userDao.getUserById(id);
        model.addAttribute("user", user);
        return "userDetail";
    }

    @GetMapping("deleteUser")
    public String deleteUser(HttpServletRequest request) {
        int id = Integer.parseInt(request.getParameter("id"));
        userDao.deleteUserById(id);

        return "redirect:/users";
    }

    @GetMapping("editUser")
    public String editUser(Integer id, Model model) {
        User user = userDao.getUserById(id);
        List<Account> accounts = accountDao.getAllAccounts();
        //Need to setPower and setOrganizations to null so they match the members from getOrganizationByID()
        //so "${organization.members.contains(hero)}" will work!
        for(Account account: accounts) {

        }
        model.addAttribute("user", user);
        model.addAttribute("accounts", accounts);
        return "editUser";
    }

    @PostMapping("editUser")
    public String performEditUser(@Valid User user, BindingResult result, HttpServletRequest request, Model model) {

//        String[] accountIds = request.getParameterValues("accountIds");
//
//        List<Account> accounts = new ArrayList<>();
//        if(accountIds != null) {
//            for(String heroId : accountIds) {
//                accounts.add(accountDao.getAccountById(Integer.parseInt(heroId)));
//            }
//            user.setAccounts(accounts);
//        }
//        else {
//            FieldError error = new FieldError("user", "accounts", "Must include one account");
//            result.addError(error);
//        }



        if(result.hasErrors()) {
            model.addAttribute("accounts", accountDao.getAllAccounts());
                model.addAttribute("user", user);
            return "editUser";
        }

        Validator validate = Validation.buildDefaultValidatorFactory().getValidator();
        violations = validate.validate(user);

        if(violations.isEmpty()) {
            userDao.updateUser(user);
        }

        return "redirect:/users";
    }


}
