package ch.romix.ivk.resultarchiver;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.List;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;

import org.junit.BeforeClass;
import org.junit.Test;

import ch.romix.ivk.resultarchiver.model.Games;
import ch.romix.ivk.resultarchiver.model.Round;
import ch.romix.ivk.resultarchiver.model.Table;
import ch.romix.ivk.resultarchiver.model.Team;
import ch.romix.ivk.resultarchiver.model.TeamOne;
import ch.romix.ivk.resultarchiver.model.TeamTwo;

public class TestTableParser {

  private static TableParser parser;
  private static Table table;

  @BeforeClass
  public static void beforeClass() {
    Client client = ClientBuilder.newClient();
    parser = new TableParser(client);
    parser.readTableOfGroup(1);
    table = parser.getTable();
  }

  @Test
  public void assertTableIsNotNull() {
    assertNotNull(table);
  }

  @Test
  public void assertTablePropertiesAreCorrect() {
    assertEquals("Herren 1. Liga", table.getGroupName());
  }

  @Test
  public void assertTwoRounds() {
    List<Round> rounds = table.getRounds();
    assertNotNull(rounds);
    assertEquals(2, rounds.size());
    for (Round r : rounds) {
      assertNotNull(r.getName());
      assertTrue(r.getId() > 0);
    }
  }

  @Test
  public void assertTeams() {
    List<Team> teams = table.getTeams();
    assertNotNull(teams);
    assertTrue(0 < teams.size());
    for (Team t : teams) {
      assertTrue(t.getId().length() > 0);
      assertNotNull(t.getName());
    }
  }

  @Test
  public void assertGames_Rounds() {
    List<Games> gamess = table.getGames();
    assertNotNull(gamess);
    assertEquals("Something is wrong with the amount of rounds in the table and in the games.",
        table.getRounds().size(), gamess.size());
    for (Games g : gamess) {
      int count = 0;
      for (Round r : table.getRounds()) {
        if (g.getRoundId() == r.getId()) {
          count++;
        }
      }
      assertEquals(1, count);
    }
  }

  @Test
  public void assertGames_Rounds_TeamOne() {
    for (Games g : table.getGames()) {
      List<TeamOne> teamOnes = g.getTeamOnes();
      assertEquals("There must be the same amount of TeamOnes as teams.", table.getTeams().size(),
          teamOnes.size());
      for (TeamOne t : teamOnes) {
        assertTrue(t.getTeamOneId().length() > 0);
      }
    }
  }

  @Test
  public void assertGames_Rounds_TeamOne_TeamTwo() {
    for (Games g : table.getGames()) {
      for (TeamOne ones : g.getTeamOnes()) {
        List<TeamTwo> teamTwos = ones.getTeamTwos();
        assertEquals(g.getTeamOnes().size(), teamTwos.size() + 1);
        for (TeamTwo two : teamTwos) {
          assertTrue(two.getTeamTwoId().length() > 0);
          assertNotNull(two.getResult());
        }
      }
    }
  }
}
