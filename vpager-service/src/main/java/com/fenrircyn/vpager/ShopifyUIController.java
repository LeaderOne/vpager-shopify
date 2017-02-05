package com.fenrircyn.vpager;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class ShopifyUIController {

    @RequestMapping(value = "/shopify/console", method = RequestMethod.GET)
    public String console(@RequestParam("shop") String shop, Model model) {
        model.addAttribute("shop", shop);

        return "console";
    }
}
