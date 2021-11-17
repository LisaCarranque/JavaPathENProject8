package tripPricer.controller;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import tripPricer.service.TripPricerService;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class TripPricerControllerTest {

    @InjectMocks
    TripPricerController tripPricerController;

    @Mock
    TripPricerService tripPricerService;

    @Test
    public void loadController() {
        assertNotNull(tripPricerController);
    }

    @Test
    public void getPriceTest() {
        java.util.UUID uuid = java.util.UUID.randomUUID();
        tripPricerController.getPrice("key1", uuid, 1, 1, 1, 1);
        verify(tripPricerService).getPrice("key1", uuid, 1, 1, 1, 1);
    }

}
