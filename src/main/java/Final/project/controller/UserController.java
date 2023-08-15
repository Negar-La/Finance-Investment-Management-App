package Final.project.controller;

import Final.project.dao.AccountDao;
import Final.project.dao.PortfolioDao;
import Final.project.dao.TransactionDao;
import Final.project.dao.UserDao;
import Final.project.entities.*;
import Final.project.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.Mapping;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolation;
import javax.validation.Valid;
import javax.validation.Validation;
import javax.validation.Validator;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Controller
public class UserController {

    @Autowired
    UserService userService;

    @Autowired
    AccountService accountService;

    @Autowired
    TransactionService transactionService;

    @Autowired
    PortfolioService portfolioService;

    Set<ConstraintViolation<User>> violations = new HashSet<>();
    @GetMapping("users")
    public String displayUsers(Model model) {
        List<User> users = userService.getAllUsers();
        List<Account> accounts = accountService.getAllAccounts();
        model.addAttribute("users", users);
        model.addAttribute("accounts", accounts);
        model.addAttribute("errors", violations);
        return "users";
    }

    @PostMapping("addUser")
    public String addUser(HttpServletRequest request) {
        String[] accountIds = request.getParameterValues("accountID");

        List<Account> accounts = new ArrayList<>();
        if(accountIds != null) {
            for(String accountId: accountIds) {
                accounts.add(accountService.getAccountById(Integer.parseInt(accountId)));
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
            userService.addUser(user);
        }
        return "redirect:/users";
    }

    @GetMapping("userDetail")
    public String userDetail(Integer id, Model model) {
        try{
            User user = userService.getUserById(id);
            if (user != null) {
                List<Transaction> transactions = transactionService.getTransactionsByUserId(id);
                BigDecimal balance = transactionService.getUserBalance(id);

                if (user.getAccounts() != null) {
                    List<Portfolio> portfolios;

                    // For each account, fetch portfolios and assets
                    for (Account account : user.getAccounts()) {
                        portfolios = portfolioService.getPortfoliosByUserId(user.getUserID());
                        account.setPortfolios(portfolios);
                    }
                }

                model.addAttribute("user", user);
                model.addAttribute("transactions", transactions);
                // Pass the calculated balance to the view
                model.addAttribute("balance", balance);
                return "userDetail";
            } else {
                // Handle the case where the user is null
                // For example, you can show an error message or redirect to a different page
                return "error";
            }
        } catch (InsufficientFundsException e) {
            model.addAttribute("errorMessage", e.getMessage());
            return "error-page";
        }
    }

    @PostMapping("deleteUser")
    public String deleteUser(HttpServletRequest request) {
        int id = Integer.parseInt(request.getParameter("id"));
        userService.deleteUserById(id);

        return "redirect:/users";
    }

    @GetMapping("editUser")
    public String editUser(Integer id, Model model) {
        User user = userService.getUserById(id);
        List<Account> accounts = accountService.getAllAccounts();
        for(Account account: accounts) {

        }
        model.addAttribute("user", user);
        model.addAttribute("accounts", accounts);
        return "editUser";
    }

    @PostMapping("editUser")
    public String performEditUser(@Valid User user, BindingResult result, Model model) {
        if(result.hasErrors()) {
            model.addAttribute("accounts", accountService.getAllAccounts());
                model.addAttribute("user", user);
            return "editUser";
        }

        Validator validate = Validation.buildDefaultValidatorFactory().getValidator();
        violations = validate.validate(user);

        if(violations.isEmpty()) {
            userService.updateUser(user);
        }

        return "redirect:/users";
    }


}
