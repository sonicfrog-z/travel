package cn.itcast.travel.service.impl;

import cn.itcast.travel.dao.CategoryDao;
import cn.itcast.travel.dao.impl.CategoryDaoImpl;
import cn.itcast.travel.domain.Category;
import cn.itcast.travel.service.CategoryService;
import cn.itcast.travel.util.JedisUtil;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.Tuple;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class CategoryServiceImpl implements CategoryService {
    private CategoryDao categoryDao = new CategoryDaoImpl();

    @Override
    public List<Category> findAll() {
        //1.query from redis
        //1.1 get jedis
        Jedis jedis = JedisUtil.getJedis();
        //1.2 use sortedset to query
        //Set<String> categories = jedis.zrange("category", 0, -1);
        //1.3 query cid and cname in sortedset
        Set<Tuple> categories = jedis.zrangeWithScores("category", 0, -1);

        List<Category> cs = null;
        //2 judge if the query result is null
        if (categories == null || categories.size() == 0) {
            //3 if null, query from database, and save in redis
            //3.1 query from database
            System.out.println("query from database...");
            cs = categoryDao.findAll();
            //3.2 save in redis
            for (int i = 0; i < cs.size(); i++) {
                jedis.zadd("category", cs.get(i).getCid(), cs.get(i).getCname());
            }
        } else {
            System.out.println("query from redis");
            cs = new ArrayList<>();
            for (Tuple tuple : categories) {
                Category category = new Category();
                category.setCname(tuple.getElement());
                category.setCid((int)tuple.getScore());
                cs.add(category);
            }
        }

        //if not null, return result
        return cs;




    }
}
