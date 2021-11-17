package tripPricer.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import tripPricer.TripPricer;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class TripPricerServiceTest {

    @InjectMocks
    TripPricerService tripPricerService;

    @Mock
    TripPricer tripPricer;

    @Test
    public void loadService() {
        assertNotNull(tripPricerService);
    }

    @Test
    public void getPriceTest() {
        java.util.UUID uuid = java.util.UUID.randomUUID();
        tripPricerService.getPrice("key1", uuid, 1, 1, 1, 1);
        verify(tripPricer, times(1)).getPrice("key1", uuid, 1, 1, 1, 1);
    }
}
