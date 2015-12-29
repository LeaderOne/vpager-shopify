package com.fenrircyn.vpager.repos;

import com.fenrircyn.vpager.entities.Ticket;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

/**
 * Created by markelba on 12/28/15.
 */
public interface TicketRepository extends CrudRepository<Ticket, Long> {
    @Query("SELECT COUNT(*) FROM Ticket AS t INNER JOIN t.merchant m WHERE m.id = :merchantId AND t.id BETWEEN m.nowServing AND :ticketId GROUP BY t.createTs ORDER BY t.createTs")
    Integer findPlaceInLine(@Param("ticketId") long ticketId, @Param("merchantId") long merchantId);
}
