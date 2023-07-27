package Final.project.controller;

import Final.project.dao.*;
import Final.project.entities.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import javax.validation.ConstraintViolation;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Controller
public class TransactionController {

    @Autowired
    UserDao userDao;

    @Autowired
    AccountDao accountDao;

    @Autowired
    PortfolioDao portfolioDao;

    @Autowired
    AssetDao assetDao;

    @Autowired
    TransactionDao transactionDao;

    Set<ConstraintViolation<Transaction>> violations = new HashSet<>();

    @GetMapping("transactions")
    public String displayTransactions(Model model) {
        List<User> users = userDao.getAllUsers();
        List<Account> accounts = accountDao.getAllAccounts();
        List<Portfolio> portfolios = portfolioDao.getAllPortfolios();
        List<Asset> assets = assetDao.getAllAssets();
        List<Transaction> transactions = transactionDao.getAllTransactions();
        model.addAttribute("users", users);
        model.addAttribute("accounts", accounts);
        model.addAttribute("portfolios", portfolios);
        model.addAttribute("assets", assets);
        model.addAttribute("transactions", transactions);
        model.addAttribute("errors", violations);
        return "transactions";
    }
}
