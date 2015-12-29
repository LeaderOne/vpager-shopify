package com.fenrircyn.vpager;

import com.fenrircyn.vpager.entities.Merchant;
import com.fenrircyn.vpager.entities.Ticket;
import com.fenrircyn.vpager.repos.MerchantRepository;
import com.fenrircyn.vpager.repos.TicketRepository;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.validation.constraints.NotNull;
import javax.websocket.server.PathParam;

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
    public Ticket getNewTicketForMerchant(@PathVariable long merchantId)
    {
        Merchant merchant = merchantRepository.findOne(merchantId);

        Ticket ticket = new Ticket();

        ticket.setMerchant(merchant);

        ticket = ticketRepository.save(ticket);

        return ticket;
    }

    @RequestMapping("/ticket/{merchantId}/{ticketId}")
    public long findMyPlaceInLine(@PathVariable long merchantId, @PathVariable long ticketId)
    {
        Integer place = ticketRepository.findPlaceInLine(ticketId, merchantId);

        return place == null ? 0 : place;
    }
}
