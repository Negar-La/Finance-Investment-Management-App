package Final.project.controller;

import Final.project.dao.AccountDao;
import Final.project.dao.AssetDao;
import Final.project.dao.PortfolioDao;
import Final.project.dao.UserDao;
import Final.project.entities.Account;
import Final.project.entities.Asset;
import Final.project.entities.Portfolio;
import Final.project.entities.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
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
public class AssetController {
    @Autowired
    UserDao userDao;

    @Autowired
    AccountDao accountDao;

    @Autowired
    PortfolioDao portfolioDao;

    @Autowired
    AssetDao assetDao;

    Set<ConstraintViolation<Asset>> violations = new HashSet<>();

    @GetMapping("assets")
    public String displayAssets(Model model) {
        List<User> users = userDao.getAllUsers();
        List<Account> accounts = accountDao.getAllAccounts();
        List<Portfolio> portfolios = portfolioDao.getAllPortfolios();
        List<Asset> assets = assetDao.getAllAssets();
        model.addAttribute("users", users);
        model.addAttribute("accounts", accounts);
        model.addAttribute("portfolios", portfolios);
        model.addAttribute("assets", assets);
        model.addAttribute("errors", violations);
        return "assets";
    }

    @PostMapping("addAsset")
    public String addAsset(HttpServletRequest request) {
//        String[] accountIds = request.getParameterValues("accountID");
//
//        List<Account> accounts = new ArrayList<>();
//        if(accountIds != null) {
//            for(String accountId: accountIds) {
//                accounts.add(accountDao.getAccountById(Integer.parseInt(accountId)));
//            }
//        }

        String assetName = request.getParameter("assetName");
        String assetType = request.getParameter("assetType");


        Asset asset = new Asset();
        asset.setAssetName(assetName);
        asset.setAssetType(assetType);

        Validator validate = Validation.buildDefaultValidatorFactory().getValidator();
        violations = validate.validate(asset);

        if(violations.isEmpty()) {
            assetDao.addAsset(asset);
        }
        return "redirect:/assets";
    }

    @GetMapping("deleteAsset")
    public String deleteAsset(HttpServletRequest request) {
        int id = Integer.parseInt(request.getParameter("id"));
        assetDao.deleteAssetById(id);

        return "redirect:/assets";
    }

    @GetMapping("editAsset")
    public String editAsset(Integer id, Model model) {
        Asset asset = assetDao.getAssetById(id);

        model.addAttribute("asset", asset);
        return "editAsset";
    }

    @PostMapping("editAsset")
    public String performEditAsset(@Valid Asset asset, BindingResult result, HttpServletRequest request, Model model) {

        if(result.hasErrors()) {
            model.addAttribute("asset", asset);
            return "editAsset";
        }

        Validator validate = Validation.buildDefaultValidatorFactory().getValidator();
        violations = validate.validate(asset);

        if(violations.isEmpty()) {
            assetDao.updateAsset(asset);
        }

        return "redirect:/assets";
    }

}
