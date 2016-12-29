package com.fenrircyn.vpager.business.impl;

import com.fenrircyn.vpager.business.FulfillShopifyBusiness;
import com.fenrircyn.vpager.business.MerchantBusiness;
import com.fenrircyn.vpager.dto.LineItem;
import com.fenrircyn.vpager.dto.Order;
import com.fenrircyn.vpager.entities.Merchant;
import com.fenrircyn.vpager.repos.MerchantRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Created by markelba on 12/28/16.
 */
@Component
public class MerchantBusinessImpl implements MerchantBusiness {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Resource
    private MerchantRepository merchantRepository;

    @Resource
    private FulfillShopifyBusiness fulfillShopifyBusiness;

    @Override
    public List<Merchant> createMerchantsFromOrder(Order order) {
        String contactEmail = order.getContactEmail();
        List<Merchant> merchantList = new ArrayList<>(order.getLineItems().size());

        for(LineItem lineItem : order.getLineItems()) {
            for (int i = 0; i < lineItem.getQuantity(); i++) {
                logger.debug("Creating merchant from email %s for line item %d with %d tickets", contactEmail, lineItem.getId(), lineItem.getQuantity());
                Merchant merchant = new Merchant(contactEmail);
                merchant = merchantRepository.save(merchant);

                fulfillShopifyBusiness.createFulfillmentFromMerchant(merchant, order, lineItem);

                merchantList.add(merchant);
            }
        }

        logger.debug("Created " + merchantList.size() + " merchants");

        return merchantList;
    }
}
