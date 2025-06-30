package pojo.serialize;

import com.fasterxml.jackson.annotation.JsonProperty;

public class LocationResponse {
	@JsonProperty("latitude")
    private String lat;

    @JsonProperty("longitude")
    private String lng;

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLng() {
        return lng;
    }

    public void setLng(String lng) {
        this.lng = lng;
    }
}
