package ch.romix.ivk.resultarchiver.model;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class TableRanking {
  private int rank;
  private List<String> teamIds;

  public int getRank() {
    return rank;
  }

  public List<String> getTeamIds() {
    return teamIds;
  }
}
