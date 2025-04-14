package com.anurpeljto.gateway.controllers;

import com.anurpeljto.gateway.domain.loan.Loan;
import com.anurpeljto.gateway.domain.fiscalization.Receipt;
import com.anurpeljto.gateway.domain.user.User;
import com.anurpeljto.gateway.exceptions.InvalidReceiptException;
import com.anurpeljto.gateway.services.FiscalizationService;
import com.anurpeljto.gateway.services.LoanService;
import com.anurpeljto.gateway.services.UserService;
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

    private final UserService userService;

    public GraphQLController(final LoanService loanS, final FiscalizationService fiscalizationService, final UserService userService) {
        this.loanService = loanS;
        this.fiscalizationService = fiscalizationService;
        this.userService = userService;
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

    @MutationMapping
    public User publishUser(
            @Argument(name="user") final User user
    ){
        if(user == null || user.getEmail() == null || user.getPassword() == null){
            throw new RuntimeException("user is null or empty");
        }
        userService.publishUser(user);
        return user;
    }

    @QueryMapping
    public Iterable<User> listUsers(
            @Argument("page") final Integer page,
            @Argument("size") final Integer size
    ) {
        return userService.getUsers(
                PageRequest.of(
                        Optional.ofNullable(page).orElse(0),
                        Optional.ofNullable(size).orElse(10)
                )
        );
    }

}
