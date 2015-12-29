package com.fenrircyn.vpager;

import com.fenrircyn.vpager.entities.Merchant;
import com.fenrircyn.vpager.repos.MerchantRepository;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.validation.constraints.NotNull;
import javax.websocket.server.PathParam;

/**
 * Created by markelba on 12/28/15.
 */
@RestController
public class MerchantController {
    @Resource
    private MerchantRepository merchantRepository;

    @RequestMapping(method = RequestMethod.PUT, value = "/merchant/create")
    public Merchant createMerchant() {
        Merchant m = new Merchant();

        m = merchantRepository.save(m);

        return m;
    }

    @RequestMapping(method = RequestMethod.POST, value = "/merchant/{merchantId}/serve")
    public long serveCustomer(@PathVariable long merchantId)
    {
        Merchant merchant = merchantRepository.findOne(merchantId);

        merchant.setNowServing(merchant.getNowServing() + 1);

        merchant = merchantRepository.save(merchant);

        return merchant.getNowServing();
    }

    @RequestMapping("/merchant/{merchantId}")
    public long getNowServing(@PathVariable long merchantId)
    {
        Merchant merchant = merchantRepository.findOne(merchantId);

        return merchant.getNowServing();
    }
}
