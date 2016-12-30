package com.fenrircyn.vpager.business;

import com.fenrircyn.vpager.dto.Order;
import com.fenrircyn.vpager.entities.Merchant;

/**
 * Created by markelba on 12/28/16.
 */
public interface MerchantBusiness {
    Iterable<Merchant> createMerchantsFromOrder(Order order);
}
