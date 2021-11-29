package tourGuide.dto;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import tourGuide.model.Attraction;
import tourGuide.user.User;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(MockitoExtension.class)
public class NearByDtoTest {

    @Test
    public void setAndGetNearByAttractionDto() {
        NearByAttractionDto nearByAttractionDto = NearByAttractionDto.builder().build();
        Attraction attraction = new Attraction("attraction", "city", "state", 1D, 1D);
        User user = new User(UUID.randomUUID(), "name", "phone", "email");
        nearByAttractionDto.setAttraction(attraction);
        nearByAttractionDto.setDistance(1D);
        nearByAttractionDto.setUser(user);
        assertNotNull(nearByAttractionDto.getAttraction());
        assertNotNull(nearByAttractionDto.getDistance());
        assertNotNull(nearByAttractionDto.getUser());
    }

    @Test
    public void createNearByAttractionDto() {
        NearByAttractionDto nearByAttractionDto = new NearByAttractionDto();
        assertNotNull(nearByAttractionDto);
    }

    @Test
    public void compareNearByAttractionDto() {
        NearByAttractionDto nearByAttractionDto1 = NearByAttractionDto.builder().build();
        Attraction attraction = new Attraction("attraction1", "city", "state", 1D, 1D);
        User user = new User(UUID.randomUUID(), "name", "phone", "email");
        nearByAttractionDto1.setAttraction(attraction);
        nearByAttractionDto1.setDistance(1D);
        nearByAttractionDto1.setUser(user);
        NearByAttractionDto nearByAttractionDto2 = NearByAttractionDto.builder().build();
        Attraction attraction2 = new Attraction("attraction2", "city", "state", 2D, 2D);
        User user2 = new User(UUID.randomUUID(), "name", "phone", "email");
        nearByAttractionDto2.setAttraction(attraction2);
        nearByAttractionDto2.setDistance(2D);
        nearByAttractionDto2.setUser(user2);
        assertEquals(-1, nearByAttractionDto1.compareTo(nearByAttractionDto2));
        assertEquals(1, nearByAttractionDto2.compareTo(nearByAttractionDto1));
    }
}
