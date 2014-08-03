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
    table = parser.getTable(1);
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
    rounds.forEach(r -> {
      assertNotNull(r.getName());
      assertTrue(r.getId() > 0);
    });
  }

  @Test
  public void assertTeams() {
    List<Team> teams = table.getTeams();
    assertNotNull(teams);
    assertTrue(0 < teams.size());
    teams.forEach(t -> {
      assertTrue(t.getId() > 0);
      assertNotNull(t.getName());
    });
  }

  @Test
  public void assertGames_Rounds() {
    List<Games> gamess = table.getGames();
    assertNotNull(gamess);
    assertEquals("Something is wrong with the amount of rounds in the table and in the games.",
        table.getRounds().size(), gamess.size());
    gamess.forEach(g -> {
      assertTrue(table.getRounds().stream().filter(r -> r.getId() == g.getRoundId()).count() == 1);
    });
  }

  @Test
  public void assertGames_Rounds_TeamOne() {
    table.getGames().forEach(
        g -> {
          List<TeamOne> teamOnes = g.getTeamOnes();
          assertEquals("There must be the same amount of TeamOnes as teams.", table.getTeams()
              .size(), teamOnes.size());
          teamOnes.forEach(t -> {
            assertTrue(t.getTeamOneId() > 0);
          });
        });
  }


  @Test
  public void assertGames_Rounds_TeamOne_TeamTwo() {
    table.getGames().forEach(g -> {
      g.getTeamOnes().forEach(ones -> {
        List<TeamTwo> teamTwos = ones.getTeamTwos();
        assertEquals(g.getTeamOnes().size(), teamTwos.size() + 1);
        teamTwos.forEach(two -> {
          assertTrue(two.getTeamTwoId() > 0);
          assertNotNull(two.getResult());
        });
      });
    });
  }
}
