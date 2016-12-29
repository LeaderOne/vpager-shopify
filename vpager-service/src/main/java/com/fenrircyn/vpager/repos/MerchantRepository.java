package com.fenrircyn.vpager.repos;

import com.fenrircyn.vpager.entities.Merchant;
import org.springframework.data.repository.CrudRepository;

/**
 * Created by markelba on 12/28/15.
 */
public interface MerchantRepository extends CrudRepository<Merchant, Long> {
    Merchant getByShopifyShopUrl(String shopifyShopUrl);
    Merchant getByEmail(String email);
}
