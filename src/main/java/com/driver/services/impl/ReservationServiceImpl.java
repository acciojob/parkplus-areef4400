package com.driver.services.impl;

import com.driver.model.*;
import com.driver.repository.ParkingLotRepository;
import com.driver.repository.ReservationRepository;
import com.driver.repository.SpotRepository;
import com.driver.repository.UserRepository;
import com.driver.services.ReservationService;
import lombok.RequiredArgsConstructor;
import org.hibernate.service.spi.ServiceException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ReservationServiceImpl implements ReservationService {

    private final UserRepository userRepository;
    private final SpotRepository spotRepository;
    private final ReservationRepository reservationRepository;
    private final ParkingLotRepository parkingLotRepository;

    @Override
    public Reservation reserveSpot(Integer userId, Integer parkingLotId, Integer timeInHours, Integer numberOfWheels) throws Exception {
        try{
            Optional<User> userOptional = userRepository.findById(userId);

            if(userOptional.isPresent()){
                User user = userOptional.get();

                Optional<ParkingLot> parkingLotOptional = parkingLotRepository.findById(parkingLotId);
                if(parkingLotOptional.isPresent()){

                    ParkingLot parkingLot = parkingLotOptional.get();
                    List<Spot> spotList = parkingLot.getSpotList();
                    SpotType currType;
                    if(numberOfWheels > 4){
                        currType = SpotType.OTHERS;
                    }else if(numberOfWheels > 2){
                        currType = SpotType.FOUR_WHEELER;
                    }else{
                        currType = SpotType.TWO_WHEELER;
                    }

                    Spot minSpot = null;
                    int minAmount = 0;
                    for(Spot spot : spotList){

                        if(spot.getOccupied() == false && spot.getSpotType() == currType){
                            if(minSpot == null){

                                minSpot = spot;
                                minAmount = spot.getPricePerHour() * timeInHours;
                            }
                            else if((spot.getPricePerHour() * timeInHours) < minAmount){

                                minSpot = spot;
                                minAmount = spot.getPricePerHour() * timeInHours;
                            }
                        }
                    }

                    if(minSpot != null){
                        minSpot.setOccupied(true);
                        Reservation reservation = new Reservation();
                        reservation.setUser(user);
                        reservation.setSpot(minSpot);
                        reservation.setNumberOfHours(timeInHours);

                        List<Reservation> reservationList = user.getReservationList();
                        reservationList.add(reservation);

                        user.setReservationList(reservationList);
                        userRepository.save(user);
                        return reservation;
                    }
                    throw new ServiceException("No spot is available");
                }
                throw new ServiceException("Parking Lot not Found");
            }
            throw new ServiceException("User not Found");
        }catch (Exception e){
            throw new ServiceException("Internal Server Error");
        }
    }
}
