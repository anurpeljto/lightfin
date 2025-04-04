package com.anurpeljto.gateway.controllers;

import com.anurpeljto.gateway.domain.Loan;
import com.anurpeljto.gateway.services.LoanService;
import org.springframework.data.domain.PageRequest;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import java.util.Optional;

@Controller
public class GraphQLController {

    private final LoanService loanService;

    public GraphQLController(final LoanService loanS){
        this.loanService = loanS;
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
    public Loan loanById(
            @Argument("id") final Integer id
    ){
        return loanService.getLoanById(id).orElseThrow(() -> new RuntimeException("Loan not found"));
    }

}
