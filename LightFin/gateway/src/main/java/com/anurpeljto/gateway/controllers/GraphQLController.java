package com.anurpeljto.gateway.controllers;

import com.anurpeljto.gateway.components.TransactionBuffer;
import com.anurpeljto.gateway.domain.loan.Loan;
import com.anurpeljto.gateway.domain.fiscalization.Receipt;
import com.anurpeljto.gateway.domain.loan.LoanInput;
import com.anurpeljto.gateway.domain.subsidy.Subsidy;
import com.anurpeljto.gateway.domain.subsidy.SubsidyInput;
import com.anurpeljto.gateway.domain.user.User;
import com.anurpeljto.gateway.dto.ReceiptResponse;
import com.anurpeljto.gateway.dto.SubsidyPageDTO;
import com.anurpeljto.gateway.dto.TodayDTOList;
import com.anurpeljto.gateway.dto.WeeklyByTypeDTO;
import com.anurpeljto.gateway.dto.loan.LoanResponseDto;
import com.anurpeljto.gateway.exceptions.InvalidReceiptException;
import com.anurpeljto.gateway.model.LoanStatus;
import com.anurpeljto.gateway.model.SubsidyStatus;
import com.anurpeljto.gateway.services.FiscalizationService;
import com.anurpeljto.gateway.services.LoanService;
import com.anurpeljto.gateway.services.SubsidyService;
import com.anurpeljto.gateway.services.UserService;
import org.apache.kafka.common.errors.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
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
    private final TransactionBuffer transactionBuffer;

    @Value("${spring.loans.url}")
    private String loanServiceUrl;

    public GraphQLController(final LoanService loanS, final FiscalizationService fiscalizationService, final UserService userService, final SubsidyService subsidyService, final RestTemplate restTemplate, TransactionBuffer transactionBuffer) {
        this.loanService = loanS;
        this.fiscalizationService = fiscalizationService;
        this.userService = userService;
        this.subsidyService = subsidyService;
        this.restTemplate = restTemplate;
        this.transactionBuffer = transactionBuffer;
    }

//    Loans and loan related methods

    @QueryMapping
    public List<Loan> listLoans(
            @Argument("page") final Integer page,
            @Argument("size") final Integer size,
            @Argument("filterBy") final String filterBy,
            @Argument("sortBy") final String sortBy
            ) {

        String requestUrl = String.format("%s/list?page=%d&size=%d&orderBy=%s&sortBy=%s", loanServiceUrl, page, size, filterBy, sortBy);
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

    @QueryMapping
    public LoanResponseDto getLoansByUserId(
            @Argument("id") final Integer id,
            @Argument("page") final Integer page,
            @Argument("size") final Integer size,
            @Argument("filterBy") final String filterBy,
            @Argument("sortBy") final String sortBy
    ) {
        return loanService.getLoansByUserId(id, page, size, filterBy, sortBy);
    }

//    Receipts and receipt related methods

    @QueryMapping
    public ReceiptResponse listReceipts(
            @Argument("page") final Integer page,
            @Argument("size") final Integer size,
            @Argument("filterBy") final String filterBy,
            @Argument("sortBy") final String sortBy) {
        return this.fiscalizationService.listReceipts(page, size, filterBy, sortBy);
    }

    @QueryMapping
    public Receipt getReceiptById(
            @Argument("id") final Integer id
    ) {
        return this.fiscalizationService.getReceiptById(id);
    }

    @MutationMapping
    public Receipt publishReceipt(
            @Argument(name = "receipt") final Receipt receipt
            ){
        if(receipt == null || receipt.getItems().isEmpty() || receipt.getPaymentType() == null) {
            throw new InvalidReceiptException("receipt is null or empty");
        }
        transactionBuffer.addTransaction(receipt);
        fiscalizationService.publishReceipt(receipt);
        return receipt;
    }

    @QueryMapping
    public ReceiptResponse getFiscalizedThisWeek(
            @Argument("page") final Integer page,
            @Argument("size") final Integer size,
            @Argument("filterBy") final String filterBy,
            @Argument("sortBy") final String sortBy
    ) {
        return fiscalizationService.getFiscalizedThisWeek(page, size, filterBy, sortBy);
    }

    @QueryMapping
    public ReceiptResponse getPendingThisWeek(
            @Argument("page") final Integer page,
            @Argument("size") final Integer size,
            @Argument("filterBy") final String filterBy,
            @Argument("sortBy") final String sortBy
    ) {
        return fiscalizationService.getPendingThisWeek(page, size, filterBy, sortBy);
    }

    @QueryMapping
    public ReceiptResponse getCancelledThisWeek(
            @Argument("page") final Integer page,
            @Argument("size") final Integer size,
            @Argument("filterBy") final String filterBy,
            @Argument("sortBy") final String sortBy
    ) {
        return fiscalizationService.getCancelledThisWeek(page, size, filterBy, sortBy);
    }

    @QueryMapping
    public TodayDTOList getTodaysTransactions(
            @Argument("page") final Integer page,
            @Argument("size") final Integer size,
            @Argument("filterBy") final String filterBy,
            @Argument("sortBy") final String sortBy
    ) {
        return fiscalizationService.getTodaysTransactions(page, size, filterBy, sortBy);
    }

    @QueryMapping
    public Integer getTodaysTransactionsCount(){
        return fiscalizationService.getTodaysTransactionsCount();
    }

    @QueryMapping
    public Integer getWeeklyTransactionsCount(){
        return fiscalizationService.getWeeklyTransactionsCount();
    }

    @QueryMapping
    public Integer getMonthlyTransactionsCount() {
        return fiscalizationService.getMonthlyTransactionsCount();
    }

    @QueryMapping
    public WeeklyByTypeDTO getWeeklyByType() {
        return fiscalizationService.getWeeklyByType();
    }

    @QueryMapping
    public List<Receipt> getLatestReceipts() {
        return transactionBuffer.getTransactions();
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
    public User getUserById(
            @Argument("id") final Integer id
    ) {
        return userService.getUserById(id);
    }

    @QueryMapping
    public SubsidyPageDTO listSubsidies(
            @Argument("page") final Integer page,
            @Argument("size") final Integer size
    ) {
        return subsidyService.listSubsidies(page, size);
    }

    @QueryMapping
    public SubsidyPageDTO getSubsidiesByUserId(
            @Argument("id") final Integer userId,
            @Argument("page") final Integer page,
            @Argument("size") final Integer size,
            @Argument("filterBy") final String filterBy,
            @Argument("sortBy") final String sortBy
    ){
        SubsidyPageDTO subsidies = subsidyService.getSubsidiesByUserId(userId, page, size, filterBy, sortBy);
        return subsidies;
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
        Subsidy subsidy = subsidyService.getSubsidyById(id);
        subsidyService.approveSubsidy(id);
        return subsidy;
    }

    @MutationMapping
    public Subsidy rejectSubsidy(
            @Argument(name = "id") final Integer id
    ) {
        Subsidy subsidy = subsidyService.getSubsidyById(id);
        subsidyService.rejectSubsidy(id);
        return subsidy;
    }

    @QueryMapping
    public SubsidyPageDTO getPendingSubsidies(
            @Argument("page") final Integer page,
            @Argument("size") final Integer size
    ) {
        return subsidyService.getPendingSubsidies(page, size);
    }

    @QueryMapping
    public SubsidyPageDTO getWeeklySubsidies(
            @Argument("page") final Integer page,
            @Argument("size") final Integer size
    ){
        return subsidyService.getWeeklySubsidies(page, size);
    }
}
