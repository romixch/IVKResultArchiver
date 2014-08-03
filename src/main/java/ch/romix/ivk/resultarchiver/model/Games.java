package ch.romix.ivk.resultarchiver.model;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class Games {

  private int roundId;
  private List<TeamOne> teamOnes;

  public int getRoundId() {
    return roundId;
  }

  public List<TeamOne> getTeamOnes() {
    return teamOnes;
  }
}
