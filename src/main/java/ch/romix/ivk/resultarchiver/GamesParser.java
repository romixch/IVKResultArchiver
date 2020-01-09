package ch.romix.ivk.resultarchiver;

import ch.romix.ivk.resultarchiver.model.Game;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

public class GamesParser {
  private Client jaxrsClient;

  public GamesParser(Client jaxrsClient) {
    this.jaxrsClient = jaxrsClient;
  }

  public List<Game> getGames(int groupId) {
    List<Game> games = new ArrayList<>();
    WebTarget target = jaxrsClient.target(RESTURI.getGamesURI(groupId));
    Response gamesResponse = target.request(MediaType.APPLICATION_JSON).get(Response.class);
    try {
      JSONArray jsonArray = new JSONArray(gamesResponse.readEntity(String.class));
      for (int i=0; i < jsonArray.length(); i++) {
        JSONObject jsonObject = jsonArray.getJSONObject(i);
        Game game = new Game();
        game.setTag(jsonObject.getString("tag"));
        game.setZeit(jsonObject.getString("zeit"));
        game.setRunde(jsonObject.getString("runde"));
        game.setTxtTeamA(jsonObject.getString("txtTeamA"));
        game.setTxtTeamB(jsonObject.getString("txtTeamB"));
        game.setResultatA(jsonObject.getString("resultatA"));
        game.setResultatB(jsonObject.getString("resultatB"));
        game.setPunkteA(jsonObject.getString("punkteA"));
        game.setPunkteB(jsonObject.getString("punkteB"));
        games.add(game);
      }
    } catch (JSONException | ParseException e) {
      e.printStackTrace();
    } finally {
      gamesResponse.close();
    }
    return games;
  }
}
