package gpsUtil;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(MockitoExtension.class)
public class GpsUtilModuleTest {

    @InjectMocks
    GpsUtilModule gpsUtilModule;

    @Test
    public void loadGpsUtilModule() {
        assertNotNull(gpsUtilModule);
    }

    @Test
    public void loadGpsUtil() {
        assertNotNull(gpsUtilModule.getGpsUtil());
    }

}
