package tripPricer;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(MockitoExtension.class)
public class TripPricerModuleTest {

    @InjectMocks
    TripPricerModule tripPricerModule;

    @Test
    public void loadTripPricerModule() {
        assertNotNull(tripPricerModule);
    }

    @Test
    public void loadTripPricer() {
        assertNotNull(tripPricerModule.getTripPricer());
    }

}
