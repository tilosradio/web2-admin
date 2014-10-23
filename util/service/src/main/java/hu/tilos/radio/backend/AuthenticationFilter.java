package hu.tilos.radio.backend;

import com.auth0.jwt.Algorithm;
import com.auth0.jwt.JwtProxy;
import com.auth0.jwt.impl.BasicPayloadHandler;
import com.auth0.jwt.impl.JwtProxyImpl;
import com.google.gson.Gson;
import hu.radio.tilos.model.Role;
import hu.radio.tilos.model.User;
import hu.tilos.radio.backend.data.Token;
import hu.tilos.radio.backend.data.UserInfo;
import hu.tilos.radio.backend.data.UserResponse;
import org.apache.deltaspike.core.api.config.ConfigProperty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.container.ResourceInfo;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;
import java.io.IOException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Scanner;

/**
 * Workaround to syncrhoize PHP and java based authorization.
 */
@Provider
public class AuthenticationFilter implements ContainerRequestFilter {

    private static Logger LOG = LoggerFactory.getLogger(AuthenticationFilter.class);

    @Context
    ResourceInfo resource;

    @Context
    HttpServletRequest servletRequest;

    @Inject
    @ConfigProperty(name = "auth.url")
    private String serverUrl;

    @Inject
    Session session;

    @Inject
    private EntityManager entityManager;


    @Inject
    @ConfigProperty(name = "jwt.secret")
    private String jwtToken;

    private JwtProxy jwtProxy = new JwtProxyImpl();

    private Gson gson = new Gson();

    public AuthenticationFilter() {
        jwtProxy.setPayloadHandler(new BasicPayloadHandler());

    }

    private String getAuthUrl() {
        return serverUrl;
    }

    private UserResponse getCurrentUser(String value) {
        try {
            if (value == null) {
                return null;
            }
            URL myUrl = new URL(getAuthUrl() + "/api/v0/user/me");
            URLConnection connection = myUrl.openConnection();
            connection.setRequestProperty("Cookie", "PHPSESSID=" + value);
            connection.connect();
            String responseTxt = new Scanner(connection.getInputStream()).useDelimiter("//Z").next();
            if (responseTxt.equals("[]")) {
                return null;
            }
            UserResponse response = new Gson().fromJson(responseTxt, UserResponse.class);
            return response;
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }


    @Override
    public void filter(ContainerRequestContext requestContext) throws IOException {
        String bearer = servletRequest.getHeader("Bearer");
        if (bearer != null && bearer.length() > 10) {
            try {
                String content = (String) jwtProxy.decode(Algorithm.HS256, bearer, jwtToken);
                Token token = gson.fromJson(content, Token.class);

                User user = (User) entityManager.createNamedQuery("user.byUsername").setParameter("username", token.getUsername()).getSingleResult();

                UserInfo currentUserInfo = new UserInfo();
                currentUserInfo.setUsername(token.getUsername());
                currentUserInfo.setRole(user.getRole());
                currentUserInfo.setEmail(user.getEmail());
                currentUserInfo.setId(user.getId());

                session.setCurrentUser(currentUserInfo);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }


        Method m = resource.getResourceMethod();
        if (m.isAnnotationPresent(Security.class)) {
            Security s = m.getAnnotation(Security.class);
            if (s.role() != Role.GUEST && s.role() != Role.UNKNOWN && session.getCurrentUser() != null) {
                UserInfo user = session.getCurrentUser();
                if (user == null || (s.role().ordinal() > user.getRole().ordinal())) {
                    requestContext.abortWith(Response.status(Response.Status.FORBIDDEN).build());
                    return;
                }
            }
        } else {
            requestContext.abortWith(Response.status(Response.Status.FORBIDDEN).build());
        }
    }

}
