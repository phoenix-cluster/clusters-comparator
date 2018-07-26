package cn.edu.cqupt.util;
import java.util.Collection;
import java.util.List;

import cn.edu.cqupt.object.ICluster;
/**
 * Created by Johnny on 2017/3/23.
 */
public interface IClusterSourceReader {
    /**
     * This function reads all clusters from the clustering source. Spectra
     * are never includeded in this output.
     * @return
     * @throws Exception
     */
    public List<ICluster> readAllClusters() throws Exception;

    public boolean supportsReadAllClusters();

    /**
     * This function includes spectra if they are available.
     * @param listeners
     * @throws Exception
     */
    public void readClustersIteratively(Collection<IClusterSourceListener> listeners) throws Exception;
}