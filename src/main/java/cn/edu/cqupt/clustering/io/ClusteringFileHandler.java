package cn.edu.cqupt.clustering.io;

import cn.edu.cqupt.clustering.io.ClusteringFileReader;
import cn.edu.cqupt.model.Cluster;
import cn.edu.cqupt.model.Spectrum;
import cn.edu.cqupt.object.ICluster;
import cn.edu.cqupt.object.ISpectrumReference;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * read clustering file and reassemble object Cluster
 */
public class ClusteringFileHandler {

    /**
     * get object Cluster list
     *
     * @param clusteringFile cluster file path
     */
    public static List<Cluster> getAllClusters(File clusteringFile) throws Exception {
        return reassembleCluster(new ClusteringFileReader(clusteringFile).readAllClusters());
    }

    @Deprecated
    public static List<Cluster> oldReassembleCluster(List<ICluster> clusterList) {
        List<Cluster> allClusters = new ArrayList<Cluster>(); // return value

        // traverse and get data needed
        Iterator<ICluster> cluItr = clusterList.iterator();
        while (cluItr.hasNext()) {
            ICluster tmpICluster = cluItr.next();

            // reassemble my object Clsuter
            Cluster tmpCluster = new Cluster();
            tmpCluster.setId(tmpICluster.getId());
            tmpCluster.setRatio(tmpICluster.getMaxRatio());
            tmpCluster.setAvPrecursorIntens(tmpICluster.getAvPrecursorIntens());
            tmpCluster.setAvPrecursorMz(tmpICluster.getAvPrecursorMz());
            tmpCluster.setSpecCount(tmpICluster.getSpecCount());
            tmpCluster.setIntensValues(tmpICluster.getConsensusIntensValues());
            tmpCluster.setMzValues(tmpICluster.getConsensusMzValues());

            // Spectrum
            List<Spectrum> allSpectra = new ArrayList<>();
            List<ISpectrumReference> tmpSpectrumReferencesList = tmpICluster.getSpectrumReferences();
            Iterator<ISpectrumReference> specItr = tmpSpectrumReferencesList.iterator();
            while (specItr.hasNext()) {
                Spectrum tmpSpectrum = new Spectrum();
                ISpectrumReference tmpSpecTrumReferences = specItr.next();
                tmpSpectrum.setId(tmpSpecTrumReferences.getSpectrumId());
                tmpSpectrum.setCharge(tmpSpecTrumReferences.getCharge());
                tmpSpectrum.setPrecursorMz(tmpSpecTrumReferences.getPrecursorMz());
                tmpSpectrum.setSpecies(tmpSpecTrumReferences.getSpecies());

                // add sequence
                tmpSpectrum.setSequence(tmpSpecTrumReferences.getSequence());
                allSpectra.add(tmpSpectrum);
            }
            tmpCluster.setSpectra(allSpectra); // set a cluster's spectra
            allClusters.add(tmpCluster);
        }
        return allClusters;
    }

    public static List<Cluster> reassembleCluster(List<ICluster> clusterList) {

        // traverse and get data needed
        return clusterList.stream()
                .map(iCluster -> {
                    List<Spectrum> spectra = iCluster.getSpectrumReferences().stream()
                            .map(iSpectrumReference ->
                                    new Spectrum(iSpectrumReference.getSpectrumId(),
                                            iSpectrumReference.getSequence(),
                                            iSpectrumReference.getCharge(),
                                            iSpectrumReference.getPrecursorMz(),
                                            iSpectrumReference.getSpecies()))
                            .collect(Collectors.toList());

                    return new Cluster(iCluster.getId(),
                            iCluster.getAvPrecursorMz(),
                            iCluster.getAvPrecursorIntens(),
                            iCluster.getSpecCount(),
                            iCluster.getMaxRatio(),
                            spectra,
                            iCluster.getConsensusMzValues(),
                            iCluster.getConsensusIntensValues());
                }).collect(Collectors.toList());
    }
}
