package ch.romix.ivk.resultarchiver.model;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class TeamOne {

  private int teamOneId;
  private List<TeamTwo> teamTwos;

  public int getTeamOneId() {
    return teamOneId;
  }

  public List<TeamTwo> getTeamTwos() {
    return teamTwos;
  }
}
