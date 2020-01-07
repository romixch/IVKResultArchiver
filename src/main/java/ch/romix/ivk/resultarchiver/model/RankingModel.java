package ch.romix.ivk.resultarchiver.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by roman on 07.08.19.
 */
public class RankingModel {

  private List<Ranking> rankings = new ArrayList<>();

  public void addRanking(Ranking ranking) {
    rankings.add(ranking);
  }

  public boolean hasData() {
    return !rankings.isEmpty();
  }
  public List<Ranking> getRankings() {
    return Collections.unmodifiableList(rankings);
  }
}
