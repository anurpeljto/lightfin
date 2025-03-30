package com.anurpeljto.loanlistener.services;

import com.anurpeljto.loanlistener.TestData;
import com.anurpeljto.loanlistener.domain.Loan;
import com.anurpeljto.loanlistener.model.LoanStatus;
import com.anurpeljto.loanlistener.repositories.LoanRepository;
import com.anurpeljto.loanlistener.services.impl.LoanServiceImpl;
import org.checkerframework.checker.units.qual.A;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class LoanServiceImplTest {

    @Mock
    private LoanRepository loanRepository;

    @InjectMocks
    private LoanServiceImpl underTest;

    @Test
    public void testThatSaveWorksWhenBodyNotEmpty(){
        final Loan testLoan = TestData.getTestLoan();

        when(loanRepository.save(eq(testLoan))).thenReturn(testLoan);

        final Loan result = underTest.saveLoan(testLoan);
        assertEquals(result, testLoan);
    }

    @Test
    public void testThatUpdateReturnsIfLoanNotExist() {
        final Loan testLoan = TestData.getTestLoan();

        when(loanRepository.findById(eq(testLoan.getId()))).thenReturn(Optional.empty());

        Loan result = underTest.updateLoan(testLoan);

        assertNull(result);
        verify(loanRepository, never()).save(testLoan);
    }

    @Test
    public void testThatUpdateUpdatesWhenLoanPresent() {
        final Loan testLoan = TestData.getTestLoan();
        final Loan newLoan = TestData.getTestLoan();
        newLoan.setAmount(300.00);
        newLoan.setStatus(LoanStatus.CANCELLED);
        newLoan.setInterest_rate(0.1);

        when(loanRepository.findById(eq(testLoan.getId()))).thenReturn(Optional.of(testLoan));
        when(loanRepository.save(eq(testLoan))).thenReturn(newLoan);

        final Loan result = underTest.updateLoan(testLoan);

        assertNotNull(result);
        assertEquals(result.getId(), newLoan.getId());
        assertEquals(newLoan.getAmount(), result.getAmount());
        assertEquals(newLoan.getStatus(), result.getStatus());
        assertEquals(newLoan.getBorrower(), result.getBorrower());
        assertEquals(newLoan.getInterest_rate(), result.getInterest_rate());

        verify(loanRepository).save(eq(testLoan));
    }
}
