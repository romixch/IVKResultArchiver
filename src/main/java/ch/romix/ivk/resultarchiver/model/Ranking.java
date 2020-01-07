package ch.romix.ivk.resultarchiver.model;

import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Created by roman on 07.08.19.
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class Ranking {
  private List<Integer> rate;
  private int rank;
  private String teamId;
  private String team;
  private int points;
  private int games;
  private int played;
  private int won;
  private int lost;
  private int tie;

  public List<Integer> getRate() {
    return rate;
  }

  public int getRank() {
    return rank;
  }

  public String getTeam() {
    return team;
  }

  public int getPoints() {
    return points;
  }

  public int getGames() {
    return games;
  }

  public int getPlayed() {
    return played;
  }

  public int getWon() {
    return won;
  }

  public int getLost() {
    return lost;
  }

  public int getTie() {
    return tie;
  }


  public void setRate(List<Integer> rate) {
    this.rate = rate;
  }

  public void setRank(int rank) {
    this.rank = rank;
  }

  public void setTeamId(String teamId) {
    this.teamId = teamId;
  }

  public void setTeam(String team) {
    this.team = team;
  }

  public void setPoints(int points) {
    this.points = points;
  }

  public void setGames(int games) {
    this.games = games;
  }

  public void setPlayed(int played) {
    this.played = played;
  }

  public void setWon(int won) {
    this.won = won;
  }

  public void setLost(int lost) {
    this.lost = lost;
  }

  public void setTie(int tie) {
    this.tie = tie;
  }
}
