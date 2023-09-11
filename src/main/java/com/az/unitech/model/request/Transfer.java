package com.az.unitech.model.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class Transfer {
    private String receiverAccountNumber;
    private String senderAccountNumber;
    private String pin;
    private Double amount;
}
