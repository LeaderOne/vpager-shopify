package com.fenrircyn.vpager.repos;

import com.fenrircyn.vpager.entities.Ticket;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Created by markelba on 12/28/15.
 */
public interface TicketRepository extends CrudRepository<Ticket, Long> {
    @Query("SELECT COUNT(*) FROM Ticket AS t INNER JOIN t.merchant m WHERE m.id = :merchantId AND t.id BETWEEN m.nowServing AND :ticketId")
    Integer findPlaceInLine(@Param("ticketId") long ticketId, @Param("merchantId") long merchantId);

    @Query("SELECT t FROM Ticket AS t INNER JOIN t.merchant m WHERE m.id = :merchantId AND t.id > m.nowServing ORDER BY t.createTs")
    List<Ticket> getNextTicketsForMerchant(@Param("merchantId") long merchantId);

    @Query("SELECT t FROM Ticket AS t INNER JOIN t.merchant m WHERE m.id = :merchantId AND t.id < m.nowServing ORDER BY t.createTs")
    List<Ticket> getPreviousTicketsForMerchant(@Param("merchantId") long merchantId);
}
