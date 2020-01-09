package ch.romix.ivk.resultarchiver;

import java.util.ArrayList;
import java.util.List;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;

public class AnnotationParser {

  private Client jaxrsClient;

  public AnnotationParser(Client jaxrsClient) {
    this.jaxrsClient = jaxrsClient;
  }

  public List<String> getAnnotations(int groupId) {
    List<String> annotations = new ArrayList<>();
    WebTarget target = jaxrsClient.target(RESTURI.getAnnotationsURI(groupId));
    Response groupsResponse = target.request(MediaType.APPLICATION_JSON).get(Response.class);
    try {
      JSONArray jsonArray = new JSONArray(groupsResponse.readEntity(String.class));
      for (int i=0; i < jsonArray.length(); i++) {
        String annotation = jsonArray.getString(i);
        annotations.add(annotation);
      }

    } catch (JSONException e) {
      e.printStackTrace();
    } finally {
      groupsResponse.close();
    }
    return annotations;
  }
}
