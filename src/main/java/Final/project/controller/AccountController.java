package Final.project.controller;

import Final.project.dao.AccountDao;
import Final.project.dao.PortfolioDao;
import Final.project.dao.UserDao;
import Final.project.entities.Account;
import Final.project.entities.Portfolio;
import Final.project.entities.User;
import Final.project.service.AccountService;
import Final.project.service.PortfolioService;
import Final.project.service.UserService;
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
public class AccountController {
    @Autowired
    UserService userService;

    @Autowired
    AccountService accountService;

    @Autowired
    PortfolioService portfolioService;

    Set<ConstraintViolation<Account>> violations = new HashSet<>();
    @GetMapping("accounts")
    public String displayAccounts(Model model) {
        List<User> users = userService.getAllUsers();
        List<Account> accounts = accountService.getAllAccounts();
        List<Portfolio> portfolios = portfolioService.getAllPortfolios();
        model.addAttribute("users", users);
        model.addAttribute("accounts", accounts);
        model.addAttribute("portfolios", portfolios);
        model.addAttribute("errors", violations);
        return "accounts";
    }

    @PostMapping("addAccount")
    public String addAccount(HttpServletRequest request) {
        String[] portfolioIds = request.getParameterValues("id");

        List<Portfolio> portfolios = new ArrayList<>();
        if(portfolioIds != null) {
            for(String portfolioId: portfolioIds) {
                portfolios.add(portfolioService.getPortfolioById(Integer.parseInt(portfolioId)));
            }
        }

        String userId = request.getParameter("accountUserID");
        String accountName = request.getParameter("accountName");
        String accountType = request.getParameter("accountType");

        Account account = new Account();
        account.setUserID(Integer.parseInt(userId));
        account.setAccountName(accountName);
        account.setAccountType(accountType);
        account.setPortfolios(portfolios);

        Validator validate = Validation.buildDefaultValidatorFactory().getValidator();
        violations = validate.validate(account);

        if(violations.isEmpty()) {
            accountService.addAccount(account);
        }
        return "redirect:/accounts";
    }

    @GetMapping("accountDetail")
    public String accountDetail(Integer id, Model model) {
        Account account = accountService.getAccountById(id);
        model.addAttribute("account", account);

        // Fetch the associated User based on the userID stored in the Account
        int userId = account.getUserID();
        User user = userService.getUserById(userId);
        model.addAttribute("user", user);

        return "accountDetail";
    }

    @GetMapping("deleteAccount")
    public String deleteAccount(HttpServletRequest request) {
        int id = Integer.parseInt(request.getParameter("id"));
        accountService.deleteAccountById(id);

        return "redirect:/accounts";
    }

    @GetMapping("editAccount")
    public String editAccount(Integer id, Model model) {
        Account account = accountService.getAccountById(id);

        List<Portfolio> portfolios = portfolioService.getAllPortfolios();

        model.addAttribute("account", account);
        model.addAttribute("portfolios", portfolios);
        return "editAccount";
    }

    @PostMapping("editAccount")
    public String performEditAccount(@Valid Account account, BindingResult result, HttpServletRequest request, Model model) {

        String[] portfolioIds = request.getParameterValues("portfolioIds");

        List<Portfolio> portfolios = new ArrayList<>();
        if(portfolioIds != null) {
            for(String portfolioId : portfolioIds) {
                portfolios.add(portfolioService.getPortfolioById(Integer.parseInt(portfolioId)));
            }
            account.setPortfolios(portfolios);
        }
        else {
            FieldError error = new FieldError("account", "portfolio", "Must include one portfolio");
            result.addError(error);
        }


        if(result.hasErrors()) {
            model.addAttribute("portfolios", portfolioService.getAllPortfolios());
            model.addAttribute("account", account);
            return "editAccount";
        }

        Validator validate = Validation.buildDefaultValidatorFactory().getValidator();
        violations = validate.validate(account);

        if(violations.isEmpty()) {
            accountService.updateAccount(account);
        }

        return "redirect:/accounts";
    }


}
