package com.driver.controllers;

import com.driver.model.Payment;
import com.driver.services.impl.PaymentServiceImpl;
import lombok.RequiredArgsConstructor;
import org.hibernate.service.spi.ServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping(value = "/payment")
@RequiredArgsConstructor
public class PaymentController {
	
    private final PaymentServiceImpl paymentService;

    @PostMapping("/pay")
    public Payment pay(@RequestParam Integer userId, @RequestParam Integer reservationId, @RequestParam Integer amountSent, @RequestParam String mode) throws Exception{
        //Attempt a payment of amountSent for reservationId using the given mode ("cASh", "card", or "upi")
        //If the amountSent is less than bill, throw "Insufficient Amount" exception, otherwise update payment attributes
        //If the mode contains a string other than "cash", "card", or "upi" (any character in uppercase or lowercase), throw "Payment mode not detected" exception.
        //Note that the reservationId always exists
        try{
            return paymentService.pay(userId, reservationId, amountSent, mode);
        }catch (Exception e){
            throw new ServiceException(e.getMessage());
        }
    }
}
