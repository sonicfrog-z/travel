package cn.itcast.travel.web.servlet;

import cn.itcast.travel.domain.PageBean;
import cn.itcast.travel.domain.Route;
import cn.itcast.travel.domain.User;
import cn.itcast.travel.service.FavoriteService;
import cn.itcast.travel.service.RouteService;
import cn.itcast.travel.service.impl.FavoriteServiceImpl;
import cn.itcast.travel.service.impl.RouteServiceImpl;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/route/*")
public class RouteServlet extends BaseServlet {
    private RouteService routService = new RouteServiceImpl();
    private FavoriteService favoriteService = new FavoriteServiceImpl();

    /**
     * pages partition query
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    public void pageQuery(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //get query param
        String currentPageStr = request.getParameter("currentPage");
        String pageSizeStr = request.getParameter("pageSize");
        String cidStr = request.getParameter("cid");
        String rname = request.getParameter("rname");
        rname = new String(rname.getBytes("iso-8859-1"),"utf-8");

        //cast param
        //default cid is 0
        int cid=0;
        if (cidStr !=null && cidStr.length()>0 && !"null".equals(cidStr)){
            cid = Integer.parseInt(cidStr);
        }

        //default page is page 1
        int currentPage = 1;
        if (currentPageStr !=null && currentPageStr.length()>0){
            currentPage = Integer.parseInt(currentPageStr);
        }

        //default page size is 5
        int pageSize = 5;
        if (pageSizeStr !=null && pageSizeStr.length()>0){
            pageSize = Integer.parseInt(pageSizeStr);
        }

        //call service to get pageBean object
        PageBean<Route> pb = routService.pageQuery(cid, currentPage, pageSize, rname);
        //serialize pageBean to json and return to client
        writeValue(pb,response);
    }

    //fined one route detail by route id
    public void findOne(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //1 get id
        String rid = request.getParameter("rid");
        //2 call servlet to get route obj
        Route route = routService.findOne(rid);
        //3 convert to json and return to client
        writeValue(route, response);
    }

    //determine if the route is favorited by current user
    public void isFavorite (HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //1 get route id
        String rid = request.getParameter("rid");
        //2 get current user
        User user = (User) request.getSession().getAttribute("user");
        int uid;
        if (user == null){
            uid = 0;
        } else {
            uid = user.getUid();
        }

        //3 call FavoriteService to check if is favorite
        boolean flag = favoriteService.isFavorite(rid, uid);

        //return flag to client
        writeValue(flag, response);
    }

    public void addFavorite (HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //1 get rid
        String rid = request.getParameter("rid");
        //2 get user
        User user = (User) request.getSession().getAttribute("user");
        int uid;
        if (user == null){
            //not login
            return;
        } else {
            //is login
            uid = user.getUid();
        }

        //3 call service to add favorite
        favoriteService.add(rid, uid);
    }




    }
