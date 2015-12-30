package com.fenrircyn.vpager;

import com.fenrircyn.vpager.entities.Merchant;
import com.fenrircyn.vpager.entities.Ticket;
import com.fenrircyn.vpager.messages.NowServingMessage;
import com.fenrircyn.vpager.repos.MerchantRepository;
import com.fenrircyn.vpager.repos.TicketRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by markelba on 12/28/15.
 */
@RestController
public class MerchantController {
    @Resource
    private MerchantRepository merchantRepository;

    @Resource
    private TicketRepository ticketRepository;

    @Resource
    private SimpMessagingTemplate simpMessagingTemplate;

    @RequestMapping(method = RequestMethod.PUT, value = "/merchant/create")
    public Merchant createMerchant() {
        Merchant m = new Merchant();

        m = merchantRepository.save(m);

        return m;
    }

    @RequestMapping(method = RequestMethod.POST, value = "/merchant/{merchantId}/serve")
    public ResponseEntity<Long> serveCustomer(@PathVariable long merchantId)
    {
        Merchant merchant = merchantRepository.findOne(merchantId);

        List<Ticket> nextTicketsForMerchant = ticketRepository.getNextTicketsForMerchant(merchantId);

        return getNowServingChangeEntity(merchantId, merchant, nextTicketsForMerchant);
    }

    @RequestMapping(method = RequestMethod.POST, value = "/merchant/{merchantId}/rewind")
    public ResponseEntity<Long> rewindTicket(@PathVariable long merchantId)
    {
        Merchant merchant = merchantRepository.findOne(merchantId);

        List<Ticket> prevTicketsForMerchant = ticketRepository.getPreviousTicketsForMerchant(merchantId);

        return getNowServingChangeEntity(merchantId, merchant, prevTicketsForMerchant);
    }

    private ResponseEntity<Long> getNowServingChangeEntity(@PathVariable long merchantId, Merchant merchant, List<Ticket> availTicketsForMerchant) {
        if(availTicketsForMerchant.isEmpty()) {
            return new ResponseEntity<Long>(HttpStatus.BAD_REQUEST);
        } else {
            Ticket nxTicket = availTicketsForMerchant.get(0);

            merchant.setNowServing(nxTicket.getId());

            merchant = merchantRepository.save(merchant);

            NowServingMessage messageBroadcast = new NowServingMessage(merchantId, merchant.getNowServing());

            simpMessagingTemplate.convertAndSend("/topic/nowserving/" + merchantId, messageBroadcast);

            long nowServing = merchant.getNowServing();

            return new ResponseEntity<Long>(nowServing, HttpStatus.OK);
        }
    }

    @RequestMapping("/merchant/{merchantId}")
    public ResponseEntity<Long> getNowServing(@PathVariable long merchantId)
    {
        Merchant merchant = merchantRepository.findOne(merchantId);

        if(merchant == null) {
            return new ResponseEntity<Long>(HttpStatus.BAD_REQUEST);
        } else {
            return new ResponseEntity<Long>(merchant.getNowServing(), HttpStatus.OK);
        }
    }
}
