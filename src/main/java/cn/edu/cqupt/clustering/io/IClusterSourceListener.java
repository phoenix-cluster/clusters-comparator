package cn.edu.cqupt.clustering.io;

import cn.edu.cqupt.object.ICluster;

/**
 * Created by Johnny on 2017/3/23.
 */
public interface IClusterSourceListener {
    public void onNewClusterRead(ICluster newCluster);
}

