package discordBot;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Map;

@JsonIgnoreProperties(ignoreUnknown = true)
@SuppressWarnings("unchecked")
public class WeatherData {

    private String name;
    private String region;

    private double temp_c;
    private String text;

    @JsonProperty("location")
    private void unPackLocation(Map<String, Object> location) {
        this.name = (String) location.get("name");
        this.region = (String) location.get("region");
    }

    @JsonProperty("current")
    private void unPackCurrent(Map<String, Object> current) {
        this.temp_c = (Double) current.get("temp_c");
        Map<String, String> nestedCurrentCondition = (Map<String, String>) current.get("condition");
        this.text = nestedCurrentCondition.get("text");
    }

    @JsonProperty

    public String getName() {
        return name;
    }

    public String getRegion() {
        return region;
    }

    public Double getTemp_c() {
        return temp_c;
    }

    public String getText() {
        return text;
    }
}