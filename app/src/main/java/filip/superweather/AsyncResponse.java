package filip.superweather;

/**
 * Interface for retrieving the data from the weather AsyncTask.
 */
interface AsyncResponse {
    void processFinish(Weather weather);
}
