package tourGuide.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import gpsUtil.location.Attraction;
import lombok.*;
import tourGuide.user.User;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class NearByAttractionDto  implements Comparable<NearByAttractionDto> {

    //private int id;
    private Attraction attraction;
    private User user;
    private double distance;

    //TODO : remove userpreferences from DTO display in view

    @Override
    public int compareTo(NearByAttractionDto other) {
        return distance < other.distance ? -1 : 1;
    }

}
