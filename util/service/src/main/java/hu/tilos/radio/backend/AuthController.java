package hu.tilos.radio.backend;

import com.auth0.jwt.Algorithm;
import com.auth0.jwt.ClaimSet;
import com.auth0.jwt.JwtProxy;
import com.auth0.jwt.impl.BasicPayloadHandler;
import com.auth0.jwt.impl.JwtProxyImpl;
import hu.radio.tilos.model.Role;
import hu.radio.tilos.model.User;
import hu.tilos.radio.backend.data.LoginData;
import hu.tilos.radio.backend.data.Token;
import org.apache.deltaspike.core.api.config.ConfigProperty;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.Query;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;


/**
 * Generate atom feed for the shows.
 */
@Path("/api/v1/auth")
public class AuthController {


    @Inject
    private EntityManager entityManager;

    @Inject
    @ConfigProperty(name = "jwt.secret")
    private String jwtToken;

    private JwtProxy jwtProxy = new JwtProxyImpl();

    public AuthController() {
        jwtProxy.setPayloadHandler(new BasicPayloadHandler());

    }

    @Path("/login")
    @Produces("application/json")
    @Security(role = Role.GUEST)
    @POST
    public Response login(LoginData loginData) {
        Query query = entityManager.createQuery("SELECT u FROM User u WHERE u.username=:username");
        query.setParameter("username", loginData.getUsername());
        try {
            User user = (User) query.getSingleResult();
            if (toSHA1(loginData.getPassword() + user.getSalt()).equals(user.getPassword())) {
                Token t = new Token();
                t.setUsername(loginData.getUsername());
                ClaimSet cs = new ClaimSet();

                try {
                    return Response.ok(jwtProxy.encode(Algorithm.HS256, t, jwtToken, cs)).build();
                } catch (Exception e) {
                    throw new RuntimeException("Can't encode the token", e);
                }
            } else {
                return Response.status(Response.Status.FORBIDDEN).build();
            }
        } catch (NoResultException ex) {
            return Response.status(Response.Status.FORBIDDEN).build();
        }
    }

    public static String toSHA1(String data) {
        MessageDigest md = null;
        try {
            md = MessageDigest.getInstance("SHA-1");
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
        return byteArrayToHexString(md.digest(data.getBytes()));
    }


    public static String byteArrayToHexString(byte[] b) {
        String result = "";
        for (int i = 0; i < b.length; i++) {
            result +=
                    Integer.toString((b[i] & 0xff) + 0x100, 16).substring(1);
        }
        return result;
    }
}
