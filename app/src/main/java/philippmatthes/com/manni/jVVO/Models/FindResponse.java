package philippmatthes.com.manni.jVVO.Models;

import com.google.gson.annotations.SerializedName;

import java.util.Date;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import lombok.ToString;

@ToString
@AllArgsConstructor
public class FindResponse {
    @NonNull @SerializedName("Points") @Getter @Setter private List<Stop> stops;
    @NonNull @SerializedName("ExpirationTime") @Getter @Setter private Date expirationTime;
}
