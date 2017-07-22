package com.fenrircyn.vpager.repos;

import com.fenrircyn.vpager.entities.Merchant;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

/**
 * Created by markelba on 12/28/15.
 */
public interface MerchantRepository extends CrudRepository<Merchant, Long> {
    Merchant getByShopifyCustomerId(long shopifyCustomerId);
    Merchant getByShopifyShopUrl(String shopifyShopUrl);
    List<Merchant> getByEmail(String email);
}
