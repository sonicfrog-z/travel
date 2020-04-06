package cn.itcast.travel.dao;

import cn.itcast.travel.domain.Favorite;

public interface FavoriteDao {
    /**
     * find favorite info by rid and uid
     * @param rid
     * @param uid
     * @return
     */
    public Favorite findByRidAndUid(int rid, int uid);

    /**
     * find favorite count by rid
     * @param rid
     * @return
     */
    public int findCountByRid(int rid);

    /**
     * add favorite
     * @param rid
     * @param uid
     */
    public void add(int rid, int uid);
}
