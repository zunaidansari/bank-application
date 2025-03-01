package com.zunaidansari.accounts.service.impl;

import com.zunaidansari.accounts.dto.AccountsDto;
import com.zunaidansari.accounts.dto.CardsDto;
import com.zunaidansari.accounts.dto.CustomerDetailsDto;
import com.zunaidansari.accounts.dto.LoansDto;
import com.zunaidansari.accounts.entity.Accounts;
import com.zunaidansari.accounts.entity.Customer;
import com.zunaidansari.accounts.exception.ResourceNotFoundException;
import com.zunaidansari.accounts.mapper.AccountsMapper;
import com.zunaidansari.accounts.mapper.CustomerMapper;
import com.zunaidansari.accounts.repository.AccountsRepository;
import com.zunaidansari.accounts.repository.CustomerRepository;
import com.zunaidansari.accounts.service.ICustomersService;
import com.zunaidansari.accounts.service.client.CardsFeignClient;
import com.zunaidansari.accounts.service.client.LoansFeignClient;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class CustomersServiceImpl implements ICustomersService {

    private AccountsRepository accountsRepository;
    private CustomerRepository customerRepository;
    private CardsFeignClient cardsFeignClient;
    private LoansFeignClient loansFeignClient;

    /**
     * @param mobileNumber - Input Mobile Number
     *  @param correlationId - Correlation ID value generated at Edge server
     * @return Customer Details based on a given mobileNumber
     */
    @Override
    public CustomerDetailsDto fetchCustomerDetails(String mobileNumber, String correlationId) {
        Customer customer = customerRepository.findByMobileNumber(mobileNumber).orElseThrow(
                () -> new ResourceNotFoundException("Customer", "mobileNumber", mobileNumber)
        );
        Accounts accounts = accountsRepository.findByCustomerId(customer.getCustomerId()).orElseThrow(
                () -> new ResourceNotFoundException("Account", "customerId", customer.getCustomerId().toString())
        );

        CustomerDetailsDto customerDetailsDto = CustomerMapper.mapToCustomerDetailsDto(customer, new CustomerDetailsDto());
        customerDetailsDto.setAccountsDto(AccountsMapper.mapToAccountsDto(accounts, new AccountsDto()));

        ResponseEntity<LoansDto> loansDtoResponseEntity = loansFeignClient.fetchLoanDetails(correlationId, mobileNumber);
        if(null != loansDtoResponseEntity) {
            customerDetailsDto.setLoansDto(loansDtoResponseEntity.getBody());
        }

        ResponseEntity<CardsDto> cardsDtoResponseEntity = cardsFeignClient.fetchCardDetails(correlationId, mobileNumber);
        if(null != cardsDtoResponseEntity) {
            customerDetailsDto.setCardsDto(cardsDtoResponseEntity.getBody());
        }


        return customerDetailsDto;

    }
}
