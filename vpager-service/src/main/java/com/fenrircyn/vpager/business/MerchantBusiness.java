package com.fenrircyn.vpager.business;

import com.fenrircyn.vpager.dto.Order;
import com.fenrircyn.vpager.entities.Merchant;

import java.util.List;

/**
 * Created by markelba on 12/28/16.
 */
public interface MerchantBusiness {
    List<Merchant> createMerchantsFromOrder(Order order);
}
