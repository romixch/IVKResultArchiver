package ch.romix.ivk.resultarchiver;

import ch.romix.ivk.resultarchiver.model.Season;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

public class SeasonParser {
  private Client jaxrsClient;

  public SeasonParser(Client jaxrsClient) {
    this.jaxrsClient = jaxrsClient;
  }

  public Season getSeason() {
    WebTarget target = jaxrsClient.target(RESTURI.getSeasonURI());
    Response seasonResponse = target.request(MediaType.APPLICATION_JSON).get(Response.class);
    try {
      String seasonString = seasonResponse.readEntity(String.class);
      JSONObject seasonJson = new JSONObject(seasonString);
      return new Season(seasonJson.getString("season"));
    } catch (JSONException e) {
      e.printStackTrace();
      return null;
    } finally {
      seasonResponse.close();
    }
  }
}
