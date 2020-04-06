package cn.itcast.travel.service;

public interface FavoriteService {

    public boolean isFavorite(String rid, int uid);
    //add favorite
    public void add(String rid, int uid);
}
