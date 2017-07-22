package com.fenrircyn.vpager.business;

import com.fenrircyn.vpager.dto.Order;
import com.fenrircyn.vpager.entities.Merchant;

import java.util.List;

/**
 * Created by markelba on 12/28/16.
 */
public interface MerchantBusiness {
    Iterable<Merchant> createMerchantsFromOrder(Order order);

    Merchant createMerchantFromUrl(String url);

    List<Merchant> getMerchantsByEmail(String email);

    Merchant getByShopifyCustomerId(long shopifyCustomerId);
}
