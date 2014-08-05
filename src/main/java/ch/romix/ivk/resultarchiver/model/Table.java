package ch.romix.ivk.resultarchiver.model;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class Table {

  private String groupName;
  private List<Round> rounds;
  private List<Team> teams;
  private List<Games> games;

  public String getGroupName() {
    return groupName;
  }

  public List<Round> getRounds() {
    return rounds;
  }

  public List<Team> getTeams() {
    return teams;
  }

  public List<Games> getGames() {
    return games;
  }

  public boolean hasData() {
    return games != null && teams != null && rounds != null;
  }
}
