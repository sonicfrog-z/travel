package cn.itcast.travel.dao;

import cn.itcast.travel.domain.Route;

import java.util.List;

public interface RouteDao {
    //query record number by cid
    public int findTotalCount(int cid, String rname);

    //query current page result set by cid, start, page size
    public List<Route> findByPage(int cid, int stat, int pageSize, String rname);

    //query route info by rid
    public Route findOne(int rid);
}
