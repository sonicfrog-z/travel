package cn.itcast.travel.dao;

import cn.itcast.travel.domain.Category;

import java.util.List;

public interface CategoryDao {
    /**
     * find all categories
     * @return
     */
    public List<Category> findAll();
}
