package io.quarkus.sample;

import java.util.List;
import java.util.Random;

import javax.transaction.Transactional;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.OPTIONS;
import javax.ws.rs.PATCH;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.bson.types.ObjectId;
import org.jboss.logging.Logger;

import io.quarkus.panache.common.Sort;


@Path("/api")
@Produces("application/json")
@Consumes("application/json")
public class TodoResource {
  private static final Logger LOG = Logger.getLogger(TodoResource.class);
  private static final double BOUND = 100;

  private String HOSTNAME =
  System.getenv().getOrDefault("HOSTNAME", "unknown");

  private int count = 0;
  private Random random = new Random();


  @OPTIONS
  public Response opt() {
      return Response.ok().build();
  }


  @POST // Create
  @Transactional
  public Response create(Todo item) {        
    item.anumber= random.nextDouble(BOUND);
    item.persist();
    return Response.status(Status.CREATED).entity(item).build();
  }

  @GET // Read
  public List<Todo> getAll() {
      return Todo.listAll(Sort.by("order"));
  }

  @GET
  @Path("/{id}")
  public Todo getOne(@PathParam("id") String id) {
      Todo entity = Todo.findById(new ObjectId(id));
      if (entity == null) {
          throw new WebApplicationException("Todo with id of " + id + " does not exist.", Status.NOT_FOUND);
      }
      return entity;
  }


  @PATCH // Update
  @Path("/{id}")
  @Transactional
  public Response update(Todo todo, @PathParam("id") String id) {
        Todo entity = Todo.findById(new ObjectId(id));
        entity.id = new ObjectId(id);
        entity.completed = todo.completed;
        entity.order = todo.order;
        entity.title = todo.title;
        entity.url = todo.url;
        entity.anumber = random.nextDouble(BOUND);
        entity.update(); // send to Mongo
        return Response.ok(entity).build();
  }


  @DELETE
  @Transactional
  @Path("/{id}")
  public Response deleteOne(@PathParam("id") String id) {
     Todo entity = Todo.findById(new ObjectId(id));
     if (entity == null) {
       throw new WebApplicationException("Todo with id of " + id + " does not exist.", Status.NOT_FOUND);
     }
     entity.delete();
     return Response.noContent().build();
  }

  /**
   * Useful for demos to better "see" the pod+JVM lifecycle
   * @return a string hold the podname/HOSTNAME and a counter
   */
  @GET
  @Path("/podhost")
  public String podID() {
    return HOSTNAME + " " + count++;
  }

  

}