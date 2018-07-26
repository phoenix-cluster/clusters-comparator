package cn.edu.cqupt.dao;

public interface ImportDao<T, R>{
    /**
     * get result based on parameter t
     * @param t
     * @return
     */
    public R getResult(T t) throws Exception;
}
