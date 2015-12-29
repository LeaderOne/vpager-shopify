package com.fenrircyn.vpager.messages;

import java.io.Serializable;

/**
 * Created by markelba on 12/28/15.
 */
public class NowServingMessage implements Serializable {
    private static final long serialVersionUID = 687968234278642L;

    private long nowServingCustomer;
    private long merchantId;

    public NowServingMessage() {
        nowServingCustomer = 0;
        merchantId = 0;
    }

    public NowServingMessage(long merchantId, long ticketNumber) {
        this.merchantId = merchantId;
        nowServingCustomer = ticketNumber;
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
