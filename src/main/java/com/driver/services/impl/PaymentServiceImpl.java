package com.driver.services.impl;

import com.driver.model.*;
import com.driver.repository.PaymentRepository;
import com.driver.repository.ReservationRepository;
import com.driver.repository.UserRepository;
import com.driver.services.PaymentService;
import lombok.RequiredArgsConstructor;
import org.hibernate.service.spi.ServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {

    private final UserRepository userRepository;
    private final ReservationRepository reservationRepository;
    private final PaymentRepository paymentRepository;

    @Override
    public Payment pay(Integer userId, Integer reservationId, int amountSent, String mode) throws Exception {
        try{
            mode = mode.toLowerCase();
            if(mode.equals("cash") || mode.equals("card") || mode.equals("upi") ){
                Optional<User> userOptional = userRepository.findById(userId);
                if(userOptional.isPresent()){
                    User user = userOptional.get();
                    List<Reservation> reservationList = user.getReservationList();

                    for(Reservation reservation : reservationList){
                        if(reservation.getId() == reservationId){
                            Spot spot = reservation.getSpot();

                            int billAmount = spot.getPricePerHour() * reservation.getNumberOfHours();
                            if(billAmount > amountSent){
                                throw new Exception("Insufficient Funds");
                            }

                            PaymentMode currMode;
                            if(mode.equals("upi"))currMode = PaymentMode.UPI;
                            else if(mode.equals("cash")) currMode = PaymentMode.CASH;
                            else currMode = PaymentMode.CARD;

                            Payment payment = new Payment();
                            payment.setPaymentMode(currMode);
                            payment.setReservation(reservation);
                            payment.setPaymentCompleted(true);

                            reservation.setPayment(payment);
                            user.setReservationList(reservationList);

                            userRepository.save(user);
                            return payment;
                        }
                    }
                }
            }
            throw new ServiceException("Mode of Payment is not present OR invalid");
        } catch (Exception e) {
            throw new ServiceException(e.getMessage());
        }
    }
}
