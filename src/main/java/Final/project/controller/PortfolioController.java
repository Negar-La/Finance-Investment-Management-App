package Final.project.controller;

import Final.project.dao.AccountDao;
import Final.project.dao.AssetDao;
import Final.project.dao.PortfolioDao;
import Final.project.dao.UserDao;
import Final.project.entities.Account;
import Final.project.entities.Asset;
import Final.project.entities.Portfolio;
import Final.project.entities.User;
import Final.project.service.*;
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
public class PortfolioController {
    @Autowired
    UserService userService;

    @Autowired
    AccountService accountService;

    @Autowired
    PortfolioService portfolioService;

    @Autowired
    AssetService assetService;

    Set<ConstraintViolation<Portfolio>> violations = new HashSet<>();
    @GetMapping("portfolios")
    public String displayAccounts(Model model) {
        List<User> users = userService.getAllUsers();
        List<Account> accounts = accountService.getAllAccounts();
        List<Portfolio> portfolios = portfolioService.getAllPortfolios();
        List<Asset> assets = assetService.getAllAssets();
        model.addAttribute("users", users);
        model.addAttribute("accounts", accounts);
        model.addAttribute("portfolios", portfolios);
        model.addAttribute("assets", assets);
        model.addAttribute("errors", violations);
        return "portfolios";
    }

    @PostMapping("addPortfolio")
    public String addPortfolio(HttpServletRequest request) {

        String[] assetIds = request.getParameterValues("id");

        List<Asset> assets = new ArrayList<>();
        if(assetIds != null) {
            for(String  assetId: assetIds) {
                assets.add(assetService.getAssetById(Integer.parseInt(assetId)));
            }
        }

        String accountId = request.getParameter("accountID");
        String name = request.getParameter("portfolioName");


        Portfolio portfolio = new Portfolio();
        portfolio.setAccountID(Integer.parseInt(accountId));
        portfolio.setPortfolioName(name);
        portfolio.setAssets(assets);

        Validator validate = Validation.buildDefaultValidatorFactory().getValidator();
        violations = validate.validate(portfolio);

        if(violations.isEmpty()) {
            portfolioService.addPortfolio(portfolio);
        }
        return "redirect:/portfolios";
    }

    @GetMapping("portfolioDetail")
    public String portfolioDetail(Integer id, Model model) {
        Portfolio portfolio = portfolioService.getPortfolioById(id);
        model.addAttribute("portfolio", portfolio);

        // Fetch the associated account based on the accountID stored in the portfolio
        int accountId = portfolio.getAccountID();
        Account account = accountService.getAccountById(accountId);
        model.addAttribute("account", account);

        return "portfolioDetail";
    }

    @PostMapping("deletePortfolio")
    public String deletePortfolio(HttpServletRequest request) {
        int id = Integer.parseInt(request.getParameter("id"));
        portfolioService.deletePortfolioById(id);

        return "redirect:/portfolios";
    }

    @GetMapping("editPortfolio")
    public String editPortfolio(Integer id, Model model) {
        Portfolio portfolio = portfolioService.getPortfolioById(id);
        List<Asset> assets = assetService.getAllAssets();

        model.addAttribute("portfolio", portfolio);
        model.addAttribute("assets", assets);
        return "editPortfolio";
    }

    @PostMapping("editPortfolio")
    public String performEditPortfolio(@Valid Portfolio portfolio, BindingResult result, HttpServletRequest request, Model model) {

        String[] assetIds = request.getParameterValues("assetIds");

        List <Asset> assets = new ArrayList<>();
        if (assetIds != null){
            for (String assetId: assetIds){
                assets.add(assetService.getAssetById(Integer.parseInt(assetId)));
            }
            portfolio.setAssets(assets);
        }

        else {
            FieldError error = new FieldError("portfolio", "assets", "Must include one asset");
            result.addError(error);
        }

        if(result.hasErrors()) {
            model.addAttribute("assets", assetService.getAllAssets());
            model.addAttribute("portfolio", portfolio);
            return "editPortfolio";
        }

        Validator validate = Validation.buildDefaultValidatorFactory().getValidator();
        violations = validate.validate(portfolio);

        if(violations.isEmpty()) {
            portfolioService.updatePortfolio(portfolio);
        }

        return "redirect:/portfolios";
    }
}
