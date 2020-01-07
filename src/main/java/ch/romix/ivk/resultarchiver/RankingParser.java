package ch.romix.ivk.resultarchiver;

import ch.romix.ivk.resultarchiver.model.Ranking;
import ch.romix.ivk.resultarchiver.model.RankingModel;
import java.util.Arrays;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

/**
 * Created by roman on 07.08.19.
 */
public class RankingParser {

  private Client jaxrsClient;

  public RankingParser(Client jaxrsClient) {
    this.jaxrsClient = jaxrsClient;
  }

  public RankingModel getRankings(int id) {
    WebTarget target = jaxrsClient.target(RESTURI.getRankingsURI(id));
    Response rankingsResponse = target.request(MediaType.APPLICATION_JSON).get();
    try {
      String jsonString = rankingsResponse.readEntity(String.class);
      JSONObject jsonObject = new JSONObject(jsonString);
      RankingModel rankingModel = new RankingModel();
      jsonObject.keys().forEachRemaining(key -> {
        try {
          JSONObject rankingObject = jsonObject.getJSONObject(key.toString());
          Ranking ranking = new Ranking();
          ranking.setTeam(rankingObject.getString("team"));
          ranking.setTeamId(rankingObject.getString("teamId"));
          ranking.setGames(rankingObject.getInt("games"));
          ranking.setLost(rankingObject.getInt("lost"));
          ranking.setPlayed(rankingObject.getInt("played"));
          ranking.setWon(rankingObject.getInt("won"));
          ranking.setTie(rankingObject.getInt("tie"));
          ranking.setRank(rankingObject.getInt("rank"));
          ranking.setPoints(rankingObject.getInt("points"));
          JSONArray rateArray = rankingObject.getJSONArray("rate");
          ranking.setRate(Arrays.asList(rateArray.getInt(0), rateArray.getInt(1)));

          rankingModel.addRanking(ranking);
        } catch (JSONException e) {
          e.printStackTrace();
        }
      });

      return rankingModel;
    } catch (JSONException e) {
      throw new RuntimeException(e);
    } finally {
      rankingsResponse.close();
    }
  }
}
