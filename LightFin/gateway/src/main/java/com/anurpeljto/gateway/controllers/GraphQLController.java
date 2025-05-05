package com.anurpeljto.gateway.controllers;

import com.anurpeljto.gateway.domain.loan.Loan;
import com.anurpeljto.gateway.domain.fiscalization.Receipt;
import com.anurpeljto.gateway.domain.loan.LoanInput;
import com.anurpeljto.gateway.domain.subsidy.Subsidy;
import com.anurpeljto.gateway.domain.subsidy.SubsidyInput;
import com.anurpeljto.gateway.domain.user.User;
import com.anurpeljto.gateway.exceptions.InvalidReceiptException;
import com.anurpeljto.gateway.model.LoanStatus;
import com.anurpeljto.gateway.model.SubsidyStatus;
import com.anurpeljto.gateway.services.FiscalizationService;
import com.anurpeljto.gateway.services.LoanService;
import com.anurpeljto.gateway.services.SubsidyService;
import com.anurpeljto.gateway.services.UserService;
import org.apache.kafka.common.errors.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Optional;

@Controller
public class GraphQLController {

    private final LoanService loanService;

    private final FiscalizationService fiscalizationService;

    private final UserService userService;

    private final SubsidyService subsidyService;

    private final RestTemplate restTemplate;

    @Value("${spring.loans.url}")
    private String loanServiceUrl;

    public GraphQLController(final LoanService loanS, final FiscalizationService fiscalizationService, final UserService userService, final SubsidyService subsidyService, final RestTemplate restTemplate) {
        this.loanService = loanS;
        this.fiscalizationService = fiscalizationService;
        this.userService = userService;
        this.subsidyService = subsidyService;
        this.restTemplate = restTemplate;
    }

//    Loans and loan related methods

    @QueryMapping
    public List<Loan> listLoans(
            @Argument("page") final Integer page,
            @Argument("size") final Integer size) {

        String requestUrl = String.format("%s/list?page=%d&size=%d", loanServiceUrl, page, size);
        ResponseEntity<List> response = restTemplate.getForEntity(requestUrl, List.class);

        return response.getBody();
    }

    @QueryMapping
    public Loan getLoanById(
            @Argument("id") final Integer id
    ){
        return loanService.getLoanById(id);
    }

    @MutationMapping
    public Loan publishLoan(
            @Argument(name = "loan") LoanInput loan) {
        if(loan == null || loan.getBorrowerId() == null || loan.getAmount() == null || loan.getInterestRate() == null) {
            throw new InvalidReceiptException("loan is null or empty");
        }

        Loan newLoan = Loan.builder()
                        .amount(loan.getAmount())
                        .interestRate(loan.getInterestRate())
                        .borrowerId(loan.getBorrowerId())
                        .status(LoanStatus.PENDING)
                        .build();

        loanService.publishLoan(newLoan);
        return newLoan;
    }

    @MutationMapping
    public Loan approveLoan(
            @Argument(name = "id") final Integer id
    ){
        Loan loan = loanService.getLoanById(id);
        loanService.approveLoan(loan);
        return loan;
    }

    @MutationMapping
    public Loan rejectLoan(
            @Argument(name = "id") final Integer id
    ){
        Loan loan = loanService.getLoanById(id);
        loanService.rejectLoan(loan);
        return loan;
    }

//    Receipts and receipt related methods

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

//    Users and user related methods

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

    @QueryMapping
    public Iterable<Subsidy> listSubsidies(
            @Argument("page") final Integer page,
            @Argument("size") final Integer size
    ) {
        return subsidyService.listSubsidies(
                PageRequest.of(
                        Optional.ofNullable(page).orElse(0),
                        Optional.ofNullable(size).orElse(10)
                )
        );
    }

    @MutationMapping
    public Subsidy publishSubsidy(
            @Argument(name = "subsidy") final SubsidyInput subsidyInput
    ){
        Subsidy subsidy = Subsidy.builder()
                .grant(subsidyInput.getGrant())
                .amount(subsidyInput.getAmount())
                .recipientId(subsidyInput.getRecipientId())
                .status(SubsidyStatus.PENDING)
                .build();

        subsidyService.publishSubsidy(subsidy);
        return subsidy;
    }

    @MutationMapping
    public Subsidy approveSubsidy(
            @Argument(name = "id") final Integer id
    ) {
        Subsidy subsidy = subsidyService.getSubsidyById(id).orElseThrow(() -> new ResourceNotFoundException("Subsidy does not exist"));
        subsidyService.approveSubsidy(id);
        return subsidy;
    }

    @MutationMapping
    public Subsidy rejectSubsidy(
            @Argument(name = "id") final Integer id
    ) {
        Subsidy subsidy = subsidyService.getSubsidyById(id).orElseThrow(() -> new ResourceNotFoundException("Subsidy does not exist"));
        subsidyService.rejectSubsidy(id);
        return subsidy;
    }

}
