package com.fenrircyn.vpager.business.impl;

import com.fenrircyn.vpager.business.MerchantBusiness;
import com.fenrircyn.vpager.dto.LineItem;
import com.fenrircyn.vpager.dto.Order;
import com.fenrircyn.vpager.entities.Merchant;
import com.fenrircyn.vpager.repos.MerchantRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;


@Component
public class MerchantBusinessImpl implements MerchantBusiness {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Resource
    private MerchantRepository merchantRepository;

    @Value("${vpager.url}")
    private String vpagerUrl;

    @Override
    public Iterable<Merchant> createMerchantsFromOrder(Order order) {
        String contactEmail = order.getContactEmail();

        List<Merchant> merchantList = new ArrayList<>(order.getLineItems().size());

        for(LineItem lineItem : order.getLineItems()) {
            for (int i = 0; i < lineItem.getQuantity(); i++) {
                logger.debug("Creating merchant from email {} for line item {} with {} tickets", contactEmail, lineItem.getId(), lineItem.getQuantity());
                Merchant merchant = new Merchant(contactEmail);
                merchantList.add(merchant);
            }
        }

        return merchantRepository.save(merchantList);
    }

    @Override
    public Merchant createMerchantFromUrl(String url) {
        Merchant merchant = new Merchant();
        merchant.setShopifyShopUrl(url);

        return merchantRepository.save(merchant);
    }

    @Override
    public List<Merchant> getMerchantsByEmail(String email) {
        return merchantRepository.getByEmail(email);
    }

    @Override
    public Merchant getByShopifyCustomerId(long shopifyCustomerId) {
        return merchantRepository.getByShopifyCustomerId(shopifyCustomerId);
    }
}
