package filip.superweather;


import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;

class DayWeather implements Serializable{
    private int forecastsAmount;
    private String date, weekday, mainDescription, windDirection, description;
    private double rain, snow, windMax, windAverage, windDegreeAverage,
        temperatureAverage, temperatureMax, temperatureMin;
    private List<String> descriptions;
    private List<Integer> descriptionCounts;


    DayWeather(String date) {
        this.date = date;
        this.weekday = dateToWeekday(date);
        this.rain = 0;
        this.snow = 0;
        this.windMax = Integer.MIN_VALUE;
        this.windAverage = 0;
        this.windDegreeAverage = 0;
        this.mainDescription = "";
        this.windDirection = "";
        this.temperatureAverage = 0;
        this.temperatureMax = Integer.MIN_VALUE;
        this.temperatureMin = Integer.MAX_VALUE;

        this.forecastsAmount = 0;
        this.descriptions = new ArrayList<>();
        this.descriptionCounts = new ArrayList<>();
    }


    /**
     * Convert a date on the form yyyy-mm-dd to weekday.
     *
     * @param dateStr   String with the date.
     * @return          String with the weekday.
     */
    private String dateToWeekday(String dateStr) {
        SimpleDateFormat sdf1, sdf2;
        Date date;
        String day;

        try {
            sdf1 = new SimpleDateFormat("yyyy-MM-dd", Locale.UK);
            date = sdf1.parse(dateStr);
            sdf2 = new SimpleDateFormat("EEEE", Locale.UK);
            day = sdf2.format(date);
        } catch (ParseException e) {
            day = "";
            e.printStackTrace();
        }

        return day;
    }


    /**
     * Adds a forecast to the instance. The rain, snow, wind and temperature are
     * updated. The description is added to the description counter.
     *
     * @param fc    SingleForecast.
     */
    void addForecast(SingleForecast fc) {
        String dc;
        int index;

        forecastsAmount++;

        dc = fc.getMainWeather();
        if (!descriptions.contains(dc)) {
            descriptions.add(dc);
            descriptionCounts.add(0);
        }
        index = descriptions.indexOf(dc);
        descriptionCounts.set(index, descriptionCounts.get(index) + 1);

        rain += fc.getRain3h();
        snow += fc.getSnow3h();

        windAverage = newAverage(windAverage, fc.getWindSpeed());
        windMax = Math.max(windMax, fc.getWindSpeed());
        windDegreeAverage = newAverage(windDegreeAverage, fc.getWindDegree());

        temperatureAverage = newAverage(temperatureAverage, fc.getTemperature());
        temperatureMax = Math.max(temperatureMax, fc.getTemperature());
        temperatureMin = Math.min(temperatureMin, fc.getTemperature());

    }


    /**
     * Returns a new average. Assumes that the previous average was calculated
     * when the amount of forecasts were forecastsAmount - 1 and the current
     * amount is forecastsAmount.
     *
     * @param average   Current average.
     * @param value     New value to be added.
     * @return          New average.
     */
    private double newAverage(double average, double value) {
        double newAvg;

        newAvg = ((forecastsAmount - 1)*average + value)/forecastsAmount;

        return newAvg;
    }


    /**
     * Returns the description that occurs the most in the map
     * descriptionCounts.
     */
    private void generateMainDescription() {
        int max, index;

        max = Collections.max(descriptionCounts);
        index = descriptionCounts.indexOf(max);

        try {
            mainDescription = descriptions.get(index);
        } catch (Exception e) {
            mainDescription = "";
        }

    }


    /**
     * Generates the description of the day forecast.
     */
    private void generateDescription() {
        // TODO: Make methods for temperature, snow, wind etc.
        generateMainDescription();

        description = mainDescription;

        description += String.format(Locale.UK,
                "\nmin %.1f\u00b0C, max %.1f\u00b0C, avg %.1f\u00b0C\n",
                temperatureMin, temperatureMax, temperatureAverage);

        if (rain > 0.1) {
            description += String.format(Locale.UK,
                    "rain %.1f mm", rain);
            if (snow < 0.1) {
                description += "\n";
            }
            else {
                description += ", ";
            }
        }

        if (snow >= 0.1) {
            description += String.format(Locale.UK,
                    "snow %.1f mm\n", snow);
        }

        if (windAverage >= 11) {
            description += String.format(Locale.UK,
                    "strong wind: average %.1f m/s\n",
                    windAverage);
        }
        else if (windAverage >= 5.6) {
            description += String.format(Locale.UK,
                    "moderate wind: average %.1f m/s\n",
                    windAverage);
        }

    }


    /**
     * Converts a wind angle in degrees to N, NW, E, etc.
     *
     * @param degree    Wind angle in degrees.
     * @return          Wind direction.
     */
    private String degreeToDirection(double degree) {
        String direction = "";

        if ((337.5 <= degree && degree <= 360) ||
                (0 <= degree && degree <= 22.5)) {
            direction = "N";
        }
        else if (degree <= 67.5) {
            direction = "NE";
        }
        else if (degree <= 112.5) {
            direction = "E";
        }
        else if (degree <= 157.5) {
            direction = "SE";
        }
        else if (degree <= 202.5) {
            direction = "S";
        }
        else if (degree <= 247.5) {
            direction = "SW";
        }
        else if (degree <= 292.5) {
            direction = "W";
        }
        else if (degree <= 337.5) {
            direction = "NW";
        }

        return direction;
    }


    /**
     * Generates and returns the describion of the day forecast.
     *
     * @return  String.
     */
    String getDescription() {
        generateDescription();

        return description;
    }


    @Override
    public String toString() {
        return getDescription();
    }


    double getRain() {
        return rain;
    }


    double getSnow() {
        return snow;
    }


    double getWindMax() {
        return windMax;
    }


    double getWindAverage() {
        return windAverage;
    }


    String getWeekday() {
        return weekday;
    }


    String getDate() {
        return date;
    }


    String getWindDirection() {
        windDirection = degreeToDirection(windDegreeAverage);

        return windDirection;
    }



}
