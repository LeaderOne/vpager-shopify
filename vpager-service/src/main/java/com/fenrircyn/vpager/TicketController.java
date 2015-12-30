package com.fenrircyn.vpager;

import com.fenrircyn.vpager.entities.Merchant;
import com.fenrircyn.vpager.entities.Ticket;
import com.fenrircyn.vpager.repos.MerchantRepository;
import com.fenrircyn.vpager.repos.TicketRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.validation.constraints.NotNull;
import javax.websocket.server.PathParam;
import java.sql.Timestamp;

/**
 * Created by markelba on 12/28/15.
 */
@RestController
public class TicketController {
    @Resource
    private TicketRepository ticketRepository;

    @Resource
    private MerchantRepository merchantRepository;

    @RequestMapping(method = RequestMethod.PUT, value="/ticket/{merchantId}")
    public ResponseEntity<Ticket> getNewTicketForMerchant(@PathVariable long merchantId)
    {
        Merchant merchant = merchantRepository.findOne(merchantId);

        if(merchant == null) {
            return new ResponseEntity<Ticket>(HttpStatus.BAD_REQUEST);
        } else {
            Ticket ticket = new Ticket();

            ticket.setMerchant(merchant);

            ticket = ticketRepository.save(ticket);

            return new ResponseEntity<Ticket>(ticket, HttpStatus.OK);
        }
    }

    @RequestMapping("/ticket/{merchantId}/{ticketId}")
    public long findMyPlaceInLine(@PathVariable long merchantId, @PathVariable long ticketId)
    {
        Integer place = ticketRepository.findPlaceInLine(ticketId, merchantId);

        return place == null ? 0 : place;
    }
}
