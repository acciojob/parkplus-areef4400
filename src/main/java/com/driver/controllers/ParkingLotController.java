package com.driver.controllers;

import com.driver.services.ParkingLotService;
import com.driver.services.impl.ParkingLotServiceImpl;
import lombok.RequiredArgsConstructor;
import org.hibernate.service.spi.ServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.driver.model.*;

import java.util.concurrent.ExecutionException;

@RestController
@RequestMapping("/parking-lots")
@RequiredArgsConstructor
public class ParkingLotController {

    //findById and deleteById should be used wherever necessary
    //findAll should never be used

    private final ParkingLotServiceImpl parkingLotServiceI;

    @PostMapping("/add")
    public ResponseEntity<ParkingLot> addParkingLot(@RequestParam String name, @RequestParam String address) {
        try{
            return new ResponseEntity<>(parkingLotServiceI.addParkingLot(name, address), HttpStatus.CREATED);
        }catch (Exception e){
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/{parkingLotId}/spot/add")
    public ResponseEntity<Spot> addSpot(@PathVariable int parkingLotId, @RequestParam Integer numberOfWheels, @RequestParam Integer pricePerHour) {
        //create a new spot in the parkingLot with given id
        //the spot type should be the next biggest type in case the number of wheels are not 2 or 4, for 4+ wheels, it is others
        try{
            return new ResponseEntity<>(parkingLotServiceI.addSpot(parkingLotId, numberOfWheels,pricePerHour), HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/{parkingLotId}/spot/{spotId}/delete")
    public ResponseEntity<Void> deleteSpot(@PathVariable int parkingLotId, @PathVariable int spotId) {
        //delete a spot from given parking lot
        try{
            parkingLotServiceI.deleteSpot(parkingLotId, spotId);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/{parkingLotId}/spot/{spotId}/update")
    public ResponseEntity<Spot> updateSpot(@PathVariable int parkingLotId, @PathVariable int spotId, @RequestParam int pricePerHour) {
        //update the details of a spot
        try{
            return new ResponseEntity<>(parkingLotServiceI.updateSpot(parkingLotId,spotId, pricePerHour), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/{parkingLotId}/delete")
    public ResponseEntity<Void> deleteParkingLot(@PathVariable int parkingLotId) {
        //delete a parkingLot
        try{
            parkingLotServiceI.deleteParkingLot(parkingLotId);
            return new ResponseEntity<>(HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
