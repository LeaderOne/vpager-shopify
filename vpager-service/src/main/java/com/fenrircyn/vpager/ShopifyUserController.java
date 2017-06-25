package com.fenrircyn.vpager;

import com.fenrircyn.vpager.filters.MerchantUser;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.authentication.OAuth2AuthenticationDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;
import java.util.Map;

/**
 * Created by markelba on 6/24/17.
 */
@Controller
public class ShopifyUserController {
    @RequestMapping(value = "/shopify/user/console")
    public String getUserConsole(Model model) {
        MerchantUser user = (MerchantUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        model.addAttribute("username", user.getUsername());
        model.addAttribute("shop", user.getShopUrl());

        return "console";
    }
}
