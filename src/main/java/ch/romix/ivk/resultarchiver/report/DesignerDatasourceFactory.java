package ch.romix.ivk.resultarchiver.report;

import java.util.ArrayList;
import java.util.Collection;

import ch.romix.ivk.resultarchiver.model.Team;

public class DesignerDatasourceFactory {

  public DesignerDatasourceFactory() {}

  public static Collection<Team> getTeams() {
    Team team = new Team();
    team.setId(1);
    team.setName("Korbball Fides Ruswil");
    ArrayList<Team> teams = new ArrayList<>();
    teams.add(team);
    team = new Team();
    team.setId(2);
    team.setName("STV Rickenbach");
    teams.add(team);
    return teams;
  }
}
