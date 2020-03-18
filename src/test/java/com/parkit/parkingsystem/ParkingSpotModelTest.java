package com.parkit.parkingsystem;


import com.parkit.parkingsystem.constants.ParkingType;
import com.parkit.parkingsystem.model.ParkingSpot;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.assertj.core.api.Assertions.assertThat;


@ExtendWith(MockitoExtension.class)
public class ParkingSpotModelTest {

    private static ParkingSpot parkingSpot;
    private static ParkingSpot parkingSpot2;

    @BeforeEach
    private  void setUp() {
        parkingSpot = new ParkingSpot(3, ParkingType.CAR, true);
        parkingSpot2 = new ParkingSpot(4, ParkingType.BIKE, true);
    }


    @Test
    public void testSetterGetterId(){

        parkingSpot.setId(1);

        assertThat(parkingSpot.getId()).isEqualTo(1);
    }

    @Test
    public void testSetterGetParkingType(){

        parkingSpot.setParkingType(ParkingType.CAR);

        assertThat(parkingSpot.getParkingType()).isEqualTo(ParkingType.CAR);
    }


    @Test
    public void testSetterGetisAvalaibleTrue(){

        parkingSpot.setAvailable(true);

        assertThat(parkingSpot.isAvailable()).isEqualTo(true);
    }

    @Test
    public void testSetterGetisAvalaibleFalse(){

        parkingSpot.setAvailable(false);

        assertThat(parkingSpot.isAvailable()).isEqualTo(false);
    }

    @Test
    public void testHashCode(){

        parkingSpot.hashCode();
        System.out.println( parkingSpot.hashCode());

        assertThat(parkingSpot.hashCode()).isEqualTo(3);
    }

    @Test
    public void testEqualsTrue(){

        parkingSpot.equals(parkingSpot);

        assertThat(parkingSpot.equals(parkingSpot)).isEqualTo(true);
    }

    @Test
    public void testEqualsFalse(){

        parkingSpot.equals(null);

        assertThat(parkingSpot.equals(null)).isEqualTo(false);
    }


    @Test
    public void testEqualsReturn(){

        parkingSpot.equals(parkingSpot2);

        //System.out.println("Retour parkingSpot : "+parkingSpot.equals(parkingSpot2));

        assertThat(parkingSpot.equals(null)).isEqualTo(false);
    }
}
