package tourGuide.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import tourGuide.model.Attraction;
import tourGuide.user.User;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class NearByAttractionDto  implements Comparable<NearByAttractionDto> {

    private Attraction attraction;
    private User user;
    private double distance;

    @Override
    public int compareTo(NearByAttractionDto other) {
        return distance < other.distance ? -1 : 1;
    }

}
