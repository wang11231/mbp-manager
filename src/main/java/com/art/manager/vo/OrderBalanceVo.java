package com.art.manager.vo;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @author hao.chang
 */
@Data
public class OrderBalanceVo {
    private BigDecimal dealPrice;
    private BigDecimal balance;
    private String specialName;
    private boolean success;
}