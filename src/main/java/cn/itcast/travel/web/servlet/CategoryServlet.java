package cn.itcast.travel.web.servlet;

import cn.itcast.travel.domain.Category;
import cn.itcast.travel.service.CategoryService;
import cn.itcast.travel.service.impl.CategoryServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet("/category/*")
public class CategoryServlet extends BaseServlet {
    CategoryService service = new CategoryServiceImpl();

    /**
     * find all category
     * @param request
     * @param response
     * @throws ServletException
     */
    public void findAll(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //call service to find all
        List<Category> cs = service.findAll();
//        ObjectMapper mapper = new ObjectMapper();
//        response.setContentType("application/json");
//        mapper.writeValue(response.getOutputStream(),cs);
        writeValue(cs, response);
    }
}
