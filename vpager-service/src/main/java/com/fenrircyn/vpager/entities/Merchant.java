package com.fenrircyn.vpager.entities;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Created by markelba on 12/27/15.
 */
@Entity
@Table(name = "ticketing.merchant")
public class Merchant implements Serializable {
    private static final long serialVersionUID = 879636873487963L;

    @Id
    @Column(name = "merchant_id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Column(name = "now_serving")
    private long nowServing;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getNowServing() {
        return nowServing;
    }

    public void setNowServing(long nowServing) {
        this.nowServing = nowServing;
    }
}
