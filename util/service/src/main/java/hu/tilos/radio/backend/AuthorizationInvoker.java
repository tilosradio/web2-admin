package hu.tilos.radio.backend;

import hu.radio.tilos.model.Role;
import hu.tilos.radio.backend.data.UserResponse;
import org.apache.cxf.jaxrs.JAXRSInvoker;
import org.apache.cxf.jaxrs.ext.RequestHandler;
import org.apache.cxf.jaxrs.model.ClassResourceInfo;
import org.apache.cxf.jaxrs.model.OperationResourceInfo;
import org.apache.cxf.message.Exchange;
import org.apache.cxf.message.Message;
import org.apache.cxf.message.MessageContentsList;

import javax.ws.rs.core.Response;
import java.lang.reflect.Method;

public class AuthorizationInvoker extends JAXRSInvoker {
    @Override
    public Object invoke(Exchange exchange, Object request, Object resourceObject) {
        OperationResourceInfo ori = exchange.get(OperationResourceInfo.class);
        Method m = ori.getAnnotatedMethod();

        if (m.isAnnotationPresent(Security.class)) {
            Security s = m.getAnnotation(Security.class);
            if (s.role() != Role.GUEST) {
                UserResponse user = (UserResponse) exchange.getInMessage().get("hu.tilos.radio.user");
                if (user == null || (s.role().ordinal() > user.getRole().getId())) {
                    return new MessageContentsList(Response.status(Response.Status.FORBIDDEN).build());
                }
            }
            return super.invoke(exchange, request, resourceObject);
        } else {
            return new MessageContentsList(Response.status(Response.Status.FORBIDDEN).build());
        }


    }
}
