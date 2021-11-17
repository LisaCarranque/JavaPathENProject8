package gpsUtil.controller;

import gpsUtil.service.GpsUtilService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.UUID;

import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class GpsUtilControllerTest {

    @InjectMocks
    GpsUtilController gpsUtilController;

    @Mock
    GpsUtilService gpsUtilService;

    @Test
    public void getUserLocation() {
        UUID uuid = UUID.randomUUID();
        gpsUtilController.getUserLocation(uuid.toString());
        verify(gpsUtilService).getUserLocation(uuid.toString());
    }

    @Test
    public void getAttractions() {
        gpsUtilController.getAttractions();
        verify(gpsUtilService).getAttractions();
    }

}
