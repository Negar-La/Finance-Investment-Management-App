package Final.project.controller;

import Final.project.dao.AccountDao;
import Final.project.dao.PortfolioDao;
import Final.project.dao.UserDao;
import Final.project.entities.Account;
import Final.project.entities.Portfolio;
import Final.project.entities.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Controller
public class AccountController {
    @Autowired
    UserDao userDao;

    @Autowired
    AccountDao accountDao;

    @Autowired
    PortfolioDao portfolioDao;

    Set<ConstraintViolation<Account>> violations = new HashSet<>();
    @GetMapping("accounts")
    public String displayAccounts(Model model) {
        List<User> users = userDao.getAllUsers();
        List<Account> accounts = accountDao.getAllAccounts();
        List<Portfolio> portfolios = portfolioDao.getAllPortfolios();
        model.addAttribute("users", users);
        model.addAttribute("accounts", accounts);
        model.addAttribute("portfolios", portfolios);
        model.addAttribute("errors", violations);
        return "accounts";
    }

    @PostMapping("addAccount")
    public String addAccount(HttpServletRequest request) {
        String[] portfolioIds = request.getParameterValues("portfolioID");

        List<Portfolio> portfolios = new ArrayList<>();
        if(portfolioIds != null) {
            for(String portfolioId: portfolioIds) {
                portfolios.add(portfolioDao.getPortfolioById(Integer.parseInt(portfolioId)));
            }
        }

        String userId = request.getParameter("accountUserID");
        String accountName = request.getParameter("accountName");
        String accountType = request.getParameter("accountType");

        Account account = new Account();
        account.setUserID(Integer.parseInt(userId));
        account.setAccountName(accountName);
        account.setAccountType(accountType);

        Validator validate = Validation.buildDefaultValidatorFactory().getValidator();
        violations = validate.validate(account);

        if(violations.isEmpty()) {
            accountDao.addAccount(account);
        }
        return "redirect:/accounts";
    }
}
