package com.fenrircyn.vpager.messages;

import java.io.Serializable;

/**
 * Created by markelba on 12/28/15.
 */
public class NowServingMessage implements Serializable {
    private static final long serialVersionUID = 687968234278642L;

    private long nowServingCustomer;
    private long merchantId;
    private long lineLength;

    public NowServingMessage() {
        nowServingCustomer = 0;
        merchantId = 0;
        lineLength = 0;
    }

    public NowServingMessage(long merchantId, long ticketNumber, long lineLength) {
        this.merchantId = merchantId;
        nowServingCustomer = ticketNumber;
        this.lineLength = lineLength;
    }

    public long getLineLength() {
        return lineLength;
    }

    public void setLineLength(long lineLength) {
        this.lineLength = lineLength;
    }

    public long getNowServingCustomer() {
        return nowServingCustomer;
    }

    public void setNowServingCustomer(long nowServingCustomer) {
        this.nowServingCustomer = nowServingCustomer;
    }

    public long getMerchantId() {
        return merchantId;
    }

    public void setMerchantId(long merchantId) {
        this.merchantId = merchantId;
    }
}
