package Final.project.controller;

import Final.project.dao.*;
import Final.project.entities.*;
import Final.project.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import javax.validation.ConstraintViolation;
import javax.validation.Valid;
import javax.validation.Validation;
import javax.validation.Validator;
import java.beans.PropertyEditorSupport;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Controller
public class TransactionController {

    @Autowired
    UserService userService;

    @Autowired
    AccountService accountService;

    @Autowired
    PortfolioService portfolioService;

    @Autowired
    AssetService assetService;

    @Autowired
    TransactionService transactionService;

    Set<ConstraintViolation<Transaction>> violations = new HashSet<>();

    @GetMapping("transactions")
    public String displayTransactions(Model model) {
        List<User> users = userService.getAllUsers();
        List<Account> accounts = accountService.getAllAccounts();
        List<Portfolio> portfolios = portfolioService.getAllPortfolios();
        List<Asset> assets = assetService.getAllAssets();
        List<Transaction> transactions = transactionService.getAllTransactions();
        model.addAttribute("users", users);
        model.addAttribute("accounts", accounts);
        model.addAttribute("portfolios", portfolios);
        model.addAttribute("assets", assets);
        model.addAttribute("transactions", transactions);
        model.addAttribute("errors", violations);
        return "transactions";
    }

    @PostMapping("addTransaction")
    public String addTransaction(@RequestParam("transactionDate") String transactionDate,
                                 @RequestParam("amount") String amount,
                                 @RequestParam("transactionType") String transactionType,
                                 @RequestParam("description") String description,
                                 @RequestParam("portfolioID") int portfolioID,
                                 @RequestParam("assetID") int assetID) {

        // Parse the inputs as needed (e.g., convert transactionDate to a valid date format)
        LocalDate parsedDate = LocalDate.parse(transactionDate, DateTimeFormatter.ISO_LOCAL_DATE);
        // Create a new Transaction object
        Transaction transaction = new Transaction();
        transaction.setTransactionDate(parsedDate);
        transaction.setAmount(new BigDecimal(amount));
        transaction.setTransactionType(transactionType);
        transaction.setDescription(description);

        // Get the corresponding Portfolio and Asset objects using their IDs
        Portfolio portfolio = portfolioService.getPortfolioById(portfolioID);
        Asset asset = assetService.getAssetById(assetID);

        // Set the Portfolio and Asset objects for the Transaction
        transaction.setPortfolio(portfolio);
        transaction.setAsset(asset);

        // Perform validation using Bean Validation (JSR 380)
        Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
        violations = validator.validate(transaction);

        if (violations.isEmpty()) {
            transactionService.addTransaction(transaction);
        }

        return "redirect:/transactions";
    }

    @PostMapping("deleteTransaction")
    public String deleteTransaction(@RequestParam("id") int id) {
        transactionService.deleteTransactionById(id);
        return "redirect:/transactions";
    }

    @GetMapping("editTransaction")
    public String editTransaction(@RequestParam("id") int id, Model model) {
        Transaction transaction = transactionService.getTransactionById(id);
        List<Portfolio> portfolios = portfolioService.getAllPortfolios();
        List<Asset> assets = assetService.getAllAssets();

        model.addAttribute("transaction", transaction);
        model.addAttribute("transactionDate", transaction.getTransactionDate().toString());
        model.addAttribute("portfolios", portfolios);
        model.addAttribute("assets", assets);
        return "editTransaction";
    }

    @PostMapping("editTransaction")
    public String performEditTransaction(@ModelAttribute("transaction") @Valid Transaction transaction,
                                    @RequestParam("transactionDate") String transactionDate,
                                    @RequestParam("amount") String amount,
                                    @RequestParam("transactionType") String transactionType,
                                    @RequestParam("description") String description,
                                    @RequestParam("portfolioID") int portfolioID,
                                    @RequestParam("assetID") int assetID) {

        // Parse the transactionDate input as needed (e.g., convert it to a valid date format)
        LocalDate parsedDate = LocalDate.parse(transactionDate, DateTimeFormatter.ISO_LOCAL_DATE);

        // Update the Transaction object with the edited data
        transaction.setTransactionDate(parsedDate);
        transaction.setAmount(new BigDecimal(amount));
        transaction.setTransactionType(transactionType);
        transaction.setDescription(description);

        // Get the corresponding Portfolio and Asset objects using their IDs
        Portfolio portfolio = portfolioService.getPortfolioById(portfolioID);
        Asset asset = assetService.getAssetById(assetID);

        // Set the Portfolio and Asset objects for the Transaction
        transaction.setPortfolio(portfolio);
        transaction.setAsset(asset);

        // Perform validation using Bean Validation (JSR 380)
        Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
        Set<ConstraintViolation<Transaction>> violations = validator.validate(transaction);

        if (violations.isEmpty()) {
            transactionService.updateTransaction(transaction);
        }

        return "redirect:/transactions";
    }


    @InitBinder
    public void initBinder(WebDataBinder binder) {
        binder.registerCustomEditor(LocalDate.class, new PropertyEditorSupport() {
            @Override
            public void setAsText(String text) throws IllegalArgumentException {
                // Define the date format to match the one used in the form (yyyy-MM-dd)
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                LocalDate date = LocalDate.parse(text, formatter);
                setValue(date);
            }
        });
    }
}
