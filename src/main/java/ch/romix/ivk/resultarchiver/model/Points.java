package ch.romix.ivk.resultarchiver.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class Points {

  private String teamId;
  private int points;

  public String getTeamId() {
    return teamId;
  }

  public int getPoints() {
    return points;
  }
}
