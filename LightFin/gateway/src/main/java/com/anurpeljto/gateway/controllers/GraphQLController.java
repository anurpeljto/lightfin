package com.anurpeljto.gateway.controllers;

import com.anurpeljto.gateway.domain.loan.Loan;
import com.anurpeljto.gateway.domain.fiscalization.Receipt;
import com.anurpeljto.gateway.exceptions.InvalidReceiptException;
import com.anurpeljto.gateway.services.FiscalizationService;
import com.anurpeljto.gateway.services.LoanService;
import org.springframework.data.domain.PageRequest;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import java.util.Optional;

@Controller
public class GraphQLController {

    private final LoanService loanService;

    private final FiscalizationService fiscalizationService;

    public GraphQLController(final LoanService loanS, final FiscalizationService fiscalizationService){
        this.loanService = loanS;
        this.fiscalizationService = fiscalizationService;
    }

    @QueryMapping
    public Iterable<Loan> listLoans(
            @Argument("page") final Integer page,
            @Argument("size") final Integer size) {

            return loanService.getLoans(
                    PageRequest.of(
                            Optional.ofNullable(page).orElse(0),
                            Optional.ofNullable(size).orElse(10)
                    )
            );
    }

    @QueryMapping
    public Iterable<Receipt> listReceipts(
            @Argument("page") final Integer page,
            @Argument("size") final Integer size) {

        return fiscalizationService.listReceipts(
                PageRequest.of(
                        Optional.ofNullable(page).orElse(0),
                        Optional.ofNullable(size).orElse(10)
                )
        );
    }

    @MutationMapping
    public Receipt publishReceipt(
            @Argument(name = "receipt") final Receipt receipt
            ){
        if(receipt == null || receipt.getItems().isEmpty() || receipt.getPaymentType() == null) {
            throw new InvalidReceiptException("receipt is null or empty");
        }
        fiscalizationService.publishReceipt(receipt);
        return receipt;
    }

}
