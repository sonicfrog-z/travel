package cn.itcast.travel.web.servlet;

import cn.itcast.travel.service.UserService;
import cn.itcast.travel.service.impl.UserServiceImpl;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/activeUserServlet")
public class ActiveUserServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //get code
        String code = request.getParameter("code");
        if(code!=null){
            //call service to active
            UserService service = new UserServiceImpl();
            boolean flag = service.active(code);
            //judge activation result
            String msg =null;
            if(flag){
                //successful
                msg = "Activated! Please <a href='login.html'>log in</a>";
            } else {
                //failed
                msg = "Activation failed please contact administrator";
            }
            response.setContentType("text/html");
            response.getWriter().write(msg);

        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        this.doPost(request, response);
    }
}
