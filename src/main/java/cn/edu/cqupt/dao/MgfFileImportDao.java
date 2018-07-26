package cn.edu.cqupt.dao;

import cn.edu.cqupt.score.calculate.MS;

import java.io.File;
import java.util.List;

public class MgfFileImportDao implements ImportDao<File, List<MS>> {
    @Override
    public List<MS> getResult(File file) throws Exception {
        return null;
    }
}
