package hu.tilos.radio.backend;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import hu.tilos.radio.backend.data.UserResponse;
import org.apache.cxf.jaxrs.ext.RequestHandler;
import org.apache.cxf.jaxrs.model.ClassResourceInfo;
import org.apache.cxf.message.Message;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Scanner;

public class AuthenticationFilter implements RequestHandler {
    @Override
    public Response handleRequest(Message m, ClassResourceInfo resourceClass) {
        UserResponse user = null;
        //String cookie = m.get(M)
        HttpServletRequest request = (HttpServletRequest) m.get("HTTP.REQUEST");
        for (Cookie cookie : request.getCookies()) {
            if (cookie.getName().equals("PHPSESSID")) {
                user = getResponse(cookie.getValue());
            }
        }
        m.put("hu.tilos.radio.user", user);
        return null;
    }

    private UserResponse getResponse(String value) {
        try {
            URL myUrl = new URL("http://tilosadmin/api/v0/user/me");
            URLConnection connection = myUrl.openConnection();
            connection.setRequestProperty("Cookie", "PHPSESSID=" + value);
            connection.connect();
            String responseTxt = new Scanner(connection.getInputStream()).useDelimiter("//Z").next();
            UserResponse response = new Gson().fromJson(responseTxt, UserResponse.class);
            return response;
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
