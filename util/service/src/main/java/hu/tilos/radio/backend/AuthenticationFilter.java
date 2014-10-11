package hu.tilos.radio.backend;

import com.google.gson.Gson;
import hu.radio.tilos.model.Role;
import hu.tilos.radio.backend.data.UserResponse;
import org.apache.deltaspike.core.api.config.ConfigProperty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.net.ssl.*;
import javax.servlet.ServletRequest;
import javax.servlet.http.Cookie;
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
    UserInfo userInfo;

    public AuthenticationFilter() {
        try {
            SSLContext sc = SSLContext.getInstance("TLS");
            sc.init(null, new TrustManager[]{new TrustAllX509TrustManager()}, new java.security.SecureRandom());
            HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
            HttpsURLConnection.setDefaultHostnameVerifier(new HostnameVerifier() {
                public boolean verify(String string, SSLSession ssls) {
                    return true;
                }
            });
        } catch (Exception ex) {
            LOG.error("Can't turn off SSL checking", ex);
        }
    }

    private String getAuthUrl() {
        return serverUrl;
    }

    public String getPhpSessionId(HttpServletRequest request) {
        //TODO: not an admin site
        if (!getAuthUrl().contains("admin") && !getAuthUrl().contains("tilosa")) {
            return null;
        }
        for (Cookie cookie : request.getCookies()) {
            if (cookie.getName().equals("PHPSESSID")) {
                return cookie.getValue();
            }
        }
        return null;
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


        Method m = resource.getResourceMethod();
        if (m.isAnnotationPresent(Security.class)) {
            Security s = m.getAnnotation(Security.class);
            if (s.role() != Role.GUEST) {
                String session = getPhpSessionId((HttpServletRequest) servletRequest);
                UserResponse user = getCurrentUser(session);
                if (user == null || (s.role().ordinal() > user.getRole().getId())) {
                    requestContext.abortWith(Response.status(Response.Status.FORBIDDEN).build());
                    return;
                } else {
                    userInfo.setUsername(user.getUsername());
                    userInfo.setRole(user.getRole().getId());
                }
            }
        } else {
            requestContext.abortWith(Response.status(Response.Status.FORBIDDEN).build());
        }
    }

}
