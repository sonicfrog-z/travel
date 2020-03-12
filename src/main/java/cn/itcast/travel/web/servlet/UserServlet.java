package cn.itcast.travel.web.servlet;

import cn.itcast.travel.domain.ResultInfo;
import cn.itcast.travel.domain.User;
import cn.itcast.travel.service.UserService;
import cn.itcast.travel.service.impl.UserServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.beanutils.BeanUtils;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.Map;

@WebServlet("/user/*")
public class UserServlet extends BaseServlet {
    //define an UserService obj
    private UserService service = new UserServiceImpl();

    /**
     * register user
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    public void regist(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //verify code
        String checkCode = request.getParameter("check");
        //get code from session
        HttpSession session = request.getSession();
        String checkcode_server = (String) session.getAttribute("CHECKCODE_SERVER");
        session.removeAttribute("CHECKCODE_SERVER");
        //compare code
        if (checkcode_server == null || !checkcode_server.equalsIgnoreCase(checkCode)) {
            //code is wrong
            ResultInfo info = new ResultInfo();
            info.setFlag(false);
            info.setErrorMsg("Incorrect verification code");
            /*ObjectMapper mapper = new ObjectMapper();
            String json = mapper.writeValueAsString(info);
            response.setContentType("application/json");
            response.getWriter().write(json);*/
            writeValue(info,response);
            return;
        }


        //get data
        Map<String, String[]> map = request.getParameterMap();
        //encapsulate object
        User user = new User();
        try {
            BeanUtils.populate(user, map);
        } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
        //call service to register
        boolean flag = service.regist(user);
        ResultInfo info = new ResultInfo();

        //respond the result
        if (flag) {
            info.setFlag(true);
        } else {
            info.setFlag(false);
            info.setErrorMsg("Register failed!");
        }

        //serialize info into json
        /*ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString(info);*/

        //return json data to web client
        /*response.setContentType("application/json");
        response.getWriter().write(json);*/

        writeValue(info,response);
    }

    /**
     * login user
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    public void login(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //get username and password
        Map<String, String[]> map = request.getParameterMap();
        //encapsulate user
        User user = new User();
        try {
            BeanUtils.populate(user, map);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }

        //call service to query
        User u = service.login(user);

        ResultInfo info = new ResultInfo();
        //judge if u is null
        if (u == null) {
            //wrong username or password
            info.setFlag(false);
            info.setErrorMsg("Wrong username or password");
        }
        //judge if u is activated
        if (u != null && !"Y".equals(u.getStatus())) {
            //not activated
            info.setFlag(false);
            info.setErrorMsg("Please activate your account");
            //
        }
        //login successfully
        if (u != null && "Y".equals(u.getStatus())) {
            info.setFlag(true);
            request.getSession().setAttribute("user", u);
        }

        //return to client
        /*ObjectMapper mapper = new ObjectMapper();
        response.setContentType("application/json");
        mapper.writeValue(response.getOutputStream(), info);*/
        writeValue(info,response);
    }

    /**
     * query one user, send user obj to client
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    public void findOne(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //get user info from session
        Object user = request.getSession().getAttribute("user");
        //return user info to client
        /*ObjectMapper mapper = new ObjectMapper();
        response.setContentType("application/json");
        mapper.writeValue(response.getOutputStream(), user);*/
        writeValue(user, response);
    }

    /**
     * user log out
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    public void exit(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //remove session
        request.getSession().invalidate();
        //jump to login page
        response.sendRedirect(request.getContextPath() + "/login.html");
    }

    /**
     * activate user
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    public void active(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //get code
        String code = request.getParameter("code");
        if (code != null) {
            //call service to active
            boolean flag = service.active(code);
            //judge activation result
            String msg = null;
            if (flag) {
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

}
