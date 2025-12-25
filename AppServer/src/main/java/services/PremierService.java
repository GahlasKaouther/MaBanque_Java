package services;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("/premier")
public class PremierService {
    @Path("/test")
    @GET
    @Produces(MediaType.TEXT_PLAIN  )     public String message ()
    {
        return "Test du service avec succès...";
    }
}
