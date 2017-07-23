package com.fenrircyn.vpager;

import com.fenrircyn.vpager.security.ShopOwnerUser;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Created by markelba on 6/24/17.
 */
@Controller
public class ShopifyUserController {
    @RequestMapping(value = "/shopify/user/console")
    public String getUserConsole(Model model) {
        ShopOwnerUser user = (ShopOwnerUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        model.addAttribute("username", user.getUsername());
        model.addAttribute("shop", user.getShopUrl());

        return "console";
    }
}
