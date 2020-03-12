package cn.itcast.travel.web.servlet;

import cn.itcast.travel.domain.Category;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;


public class BaseServlet extends HttpServlet {
    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //get request path
        String uri = req.getRequestURI(); //  /travel/user/add
        System.out.println(uri);
        //get method name
        String methodName = uri.substring(uri.lastIndexOf('/') + 1);
        System.out.println(methodName);
        //get method obj
        try {
            Method method = this.getClass().getMethod(methodName, HttpServletRequest.class, HttpServletResponse.class);
            //forced reflection & execute
//            method.setAccessible(true);
            method.invoke(this,req,resp);
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }

    }

    /**
     * Serialize the incoming object, then send json to client
     * @param obj
     * @param response
     * @throws IOException
     */
    public void writeValue(Object obj, HttpServletResponse response) throws IOException {
        //call service to find all
        ObjectMapper mapper = new ObjectMapper();
        response.setContentType("application/json");
        mapper.writeValue(response.getOutputStream(),obj);

    }

    /**
     * Serialize the incoming object, return to caller as String
     * @param obj
     * @return
     * @throws JsonProcessingException
     */
    public String  writeValueAsString(Object obj) throws JsonProcessingException {
        ObjectMapper mapper =  new ObjectMapper();
        return mapper.writeValueAsString(obj);
    }
}
