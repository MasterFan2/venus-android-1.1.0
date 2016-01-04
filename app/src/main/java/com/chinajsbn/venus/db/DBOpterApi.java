package com.chinajsbn.venus.db;

/**
 * Created by chenjianjun on 15-4-17.
 * <p/>
 * Beyond their own, let the future
 */
public class DBOpterApi {

//    public static final String TAG = DBOpterApi.class.getSimpleName();
//    private static DBOpterApi ourInstance = new DBOpterApi();
//    private DbUtils _mdb;
//    private String nullStr = "";
//
//    /**
//     * 返回单例
//     *
//     * @return 返回单例
//     */
//    public static DBOpterApi getInstance() {
//        return ourInstance;
//    }
//
//    /**
//     * 构造函数
//     */
//    private DBOpterApi() {}
//
//    /**
//     * 初期化环境
//     *
//     * @param context 上下文
//     * @return false:初期化失败 true:初期化成功
//     */
//    public boolean initialize(Context context) {
//        _mdb = DbUtils.create(context);
//        return _mdb != null;
//    }
//
//    /**
//     * 删除数据库，在版本更新时使用
//     */
//    public void dropDB() {
//
//        try {
//            _mdb.dropTable(UserInfo.class);
//            //_mdb.dropDb();
//        } catch (DbException e) {
//            Log.e(TAG, e.getMessage());
//        }
//
//    }
//
//    /**
//     * 存储或者更新一条记录
//     * @param entity
//     * @return
//     */
//    public boolean saveOrUpdate(Object entity) {
//
//        try {
//            if (entity == null) {
//                return true;
//            }
//
//            // 存储新数据
//            _mdb.saveOrUpdate(entity);
//
//            return true;
//        } catch (DbException e) {
//            Log.e(TAG, e.getMessage());
//
//            return false;
//        }
//    }
//
//    /**
//     * 存储或者更新一组记录
//     * @param entities
//     * @return
//     */
//    public boolean saveOrUpdateAll(List<?> entities) {
//
//        try {
//            if(entities == null) {
//                return true;
//            }
//
//            _mdb.saveAll(entities);
//
//            return true;
//        } catch (DbException e) {
//            Log.e(TAG, e.getMessage());
//
//            return false;
//        }
//    }
//
//    /**
//     * 根据条件查询数据
//     * @param entityType
//     * @param sqlCondition 条件，比如时间段在00:00到23:00之间，版本号=“1.0”，node=“test1”，
//     *                     可以这样传输参数：“
//     *                     periodStart <= strftime('%H:%M','now') AND
//     *                     periodEnd >= strftime('%H:%M','now') AND
//     *                     version = ‘1.0’
//     *                     AND node = ‘test’
//     *                     ”
//     * @return
//     */
//    public <T> List<T> queryEntities(Class<?> entityType, String sqlCondition) {
//
//        try {
//            com.lidroid.xutils.db.sqlite.Selector selector;
//            if (sqlCondition == null || sqlCondition.equals(nullStr)) {
//                selector = Selector.from(entityType);
//            } else {
//                selector = Selector.from(entityType).expr(sqlCondition);
//            }
//
//            return _mdb.findAll(selector);
//        } catch (DbException e) {
//            Log.e(TAG, e.getMessage());
//
//            return null;
//        }
//    }
//
//    /**
//     * 根据条件查询数据 查询第一条
//     * @param entityType
//     * @param sqlCondition 条件，比如时间段在00:00到23:00之间，版本号=“1.0”，node=“test1”，
//     *                     可以这样传输参数：“
//     *                     periodStart <= strftime('%H:%M','now') AND
//     *                     periodEnd >= strftime('%H:%M','now') AND
//     *                     version = ‘1.0’
//     *                     AND node = ‘test’
//     *                     ”
//     * @return
//     */
//    public <T> T queryEntitie(Class<?> entityType, String sqlCondition) {
//
//        try {
//            com.lidroid.xutils.db.sqlite.Selector selector;
//            if (sqlCondition == null || sqlCondition.equals(nullStr)) {
//                selector = Selector.from(entityType);
//            } else {
//                selector = Selector.from(entityType).expr(sqlCondition);
//            }
//
//            return _mdb.findFirst(selector);
//        } catch (DbException e) {
//            Log.e(TAG, e.getMessage());
//
//            return null;
//        }
//    }
//
//    /**
//     * 根据条件删除
//     * @param entityType
//     * @param sqlCondition 条件，比如时间段在00:00到23:00之间，版本号=“1.0”，node=“test1”，
//     *                     可以这样传输参数：“
//     *                     periodStart <= strftime('%H:%M','now') AND
//     *                     periodEnd >= strftime('%H:%M','now') AND
//     *                     version = ‘1.0’
//     *                     AND node = ‘test’
//     *                     ”
//     */
//    public void delete(Class<?> entityType, String sqlCondition) {
//
//        try {
//            com.lidroid.xutils.db.sqlite.WhereBuilder whereBuilder;
//            if (sqlCondition == null || sqlCondition.equals(nullStr)) {
//                whereBuilder = WhereBuilder.b();
//            } else {
//                whereBuilder = WhereBuilder.b().expr(sqlCondition);
//            }
//
//            _mdb.delete(entityType, whereBuilder);
//        } catch (DbException e) {
//            Log.e(TAG, e.getMessage());
//        }
//    }
//
//    /**
//     * 清空数据表
//     * @param entityType
//     */
//    public void deleteAll(Class<?> entityType) {
//
//        try {
//            _mdb.deleteAll(entityType);
//        } catch (DbException e) {
//            Log.e(TAG, e.getMessage());
//        }
//    }
}

