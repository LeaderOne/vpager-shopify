package com.fenrircyn.vpager;

import com.fenrircyn.vpager.messages.NowServingMessage;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

/**
 * Created by markelba on 12/28/15.
 */
@Controller
public class NowServingController {

    @MessageMapping("/nowserving")
    @SendTo("/topic/nowserving/{merchantId}")
    public NowServingMessage nowServingMessage(@DestinationVariable long merchantId, long ticketNumber,
                                               long lineLength) throws Exception {
        return new NowServingMessage(merchantId, ticketNumber, lineLength);
    }
}
