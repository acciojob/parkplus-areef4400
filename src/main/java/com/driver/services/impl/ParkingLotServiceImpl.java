package com.driver.services.impl;

import com.driver.model.ParkingLot;
import com.driver.model.Spot;
import com.driver.model.SpotType;
import com.driver.repository.ParkingLotRepository;
import com.driver.repository.SpotRepository;
import com.driver.services.ParkingLotService;
import lombok.RequiredArgsConstructor;
import org.hibernate.service.spi.ServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ParkingLotServiceImpl implements ParkingLotService {

    private final ParkingLotRepository parkingLotRepository1;
    private final SpotRepository spotRepository1;

    @Override
    public ParkingLot addParkingLot(String name, String address) {
        try{
            ParkingLot parkingLot = new ParkingLot();

            parkingLot.setAddress(address);
            parkingLot.setName(name);

            parkingLotRepository1.save(parkingLot);
            return parkingLot;
        } catch (Exception e) {
            throw new ServiceException(e.getMessage());
        }
    }

    @Override
    public Spot addSpot(int parkingLotId, Integer numberOfWheels, Integer pricePerHour) {
        try{

            Optional<ParkingLot> parkingLotOptional = parkingLotRepository1.findById(parkingLotId);
            if(parkingLotOptional.isPresent()){

                ParkingLot parkingLot = parkingLotOptional.get();

                Spot spot = new Spot();
                spot.setParkingLot(parkingLot);
                spot.setPricePerHour(pricePerHour);
                spot.setOccupied(false);
                if(numberOfWheels == 2){
                    spot.setSpotType(SpotType.TWO_WHEELER);
                }else if(numberOfWheels == 4){
                    spot.setSpotType(SpotType.FOUR_WHEELER);
                }else{
                    spot.setSpotType(SpotType.OTHERS);
                }

                parkingLot.getSpotList().add(spot);
                parkingLotRepository1.save(parkingLot);
                return spot;
            }
            throw new ServiceException("Parking lot not Found");
        } catch (Exception e) {
            throw new ServiceException(e.getMessage());
        }

    }

    @Override
    public void deleteSpot(int parkingLotId, int spotId) {
        try{
            Optional<ParkingLot> parkingLotOptional = parkingLotRepository1.findById(parkingLotId);
            if(parkingLotOptional.isPresent()){

                ParkingLot parkingLot = parkingLotOptional.get();
                List<Spot> spots = parkingLot.getSpotList();

                for(Spot spot : spots){
                    if(spot.getId() == spotId){
                        spots.remove(spot);
                        parkingLot.setSpotList(spots);
                        parkingLotRepository1.save(parkingLot);
                        return;
                    }
                }
                throw new ServiceException("Spot Not Found");
            }
            throw new ServiceException("Parking lot Not Found");
        } catch (Exception e) {
            throw new ServiceException(e.getMessage());
        }
    }

    @Override
    public Spot updateSpot(int parkingLotId, int spotId, int pricePerHour) {
        try{
            Optional<ParkingLot> parkingLotOptional = parkingLotRepository1.findById(parkingLotId);
            if(parkingLotOptional.isPresent()){

                ParkingLot parkingLot = parkingLotOptional.get();
                List<Spot> spots = parkingLot.getSpotList();

                for(Spot spot : spots){
                    if(spot.getId() == spotId){
                        spot.setPricePerHour(pricePerHour);
                        parkingLot.setSpotList(spots);
                        parkingLotRepository1.save(parkingLot);

                        return spot;
                    }
                }
            }
            throw new ServiceException("Spot Not Found");
        } catch (Exception e) {
            throw new ServiceException(e.getMessage());
        }
    }

    @Override
    public void deleteParkingLot(int parkingLotId) {
        try{
            Optional<ParkingLot> parkingLotOptional = parkingLotRepository1.findById(parkingLotId);
            if(parkingLotOptional.isPresent()) {

                ParkingLot parkingLot = parkingLotOptional.get();
                parkingLotRepository1.delete(parkingLot);
            }
            throw new ServiceException("Parking Lot Not Found");
        } catch (Exception e) {
            throw new ServiceException(e.getMessage());
        }

    }
}
