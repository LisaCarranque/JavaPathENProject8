package gpsUtil.service;

import gpsUtil.GpsUtil;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.UUID;

import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class GpsUtilServiceTest {

    @InjectMocks
    GpsUtilService gpsUtilService;

    @Mock
    GpsUtil gpsUtil;

    @Test
    public void getAttractionTest() {
        gpsUtilService.getAttractions();
        verify(gpsUtil).getAttractions();
    }

    @Test
    public void getUserLocationTest() {
        UUID uuid = UUID.randomUUID();
        gpsUtilService.getUserLocation(uuid.toString());
        verify(gpsUtil).getUserLocation(uuid);
    }

}
