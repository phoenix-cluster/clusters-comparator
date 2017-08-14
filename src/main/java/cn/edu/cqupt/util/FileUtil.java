package cn.edu.cqupt.util;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import cn.edu.cqupt.model.Cluster;
import cn.edu.cqupt.model.Spectrum;
import cn.edu.cqupt.object.ICluster;
import cn.edu.cqupt.object.ISpectrumReference;

public class FileUtil {
	private List<ICluster> cluster; // all cluster of a release
	private int totalRecord; // the number of all cluster

	public FileUtil() {

	}

	/**
	 * read cluster file by file name
	 * 
	 * @param clusteringFilePath
	 *            cluster file path
	 */
	public FileUtil(File clusteringFile) {
		ClusteringFileReader reader = new ClusteringFileReader(clusteringFile);
		try {
			this.cluster = reader.readAllClusters(); // ICluster
			this.totalRecord = this.cluster.size();
		} catch (Exception e) {
			throw new RuntimeException("Read the clustered result file with an exception", e);
		}
	}

	public int getTotalRecord() {
		return totalRecord;
	}

	public List<Cluster> findAllClusters() {
		List<Cluster> allClusters = assembleCluster(this.cluster);
		return allClusters;
	}

	/**
	 * get cluster data by scope
	 * 
	 * @param fromIndex
	 *            start index, 1-index system
	 * @param endIndex
	 *            end index, 1-index system
	 * @return the data in fromIdex(contained) to endIndex(contained)
	 */
	public List<Cluster> findClusterByScope(int fromIndex, int endIndex) {
		List<ICluster> subCluster = cluster.subList(fromIndex - 1, endIndex);
		return assembleCluster(subCluster);
	}

	private List<Cluster> assembleCluster(List<ICluster> clusterPart) {
		List<Cluster> allClusters = new ArrayList<Cluster>(); // return value
		
		// traverse and get data needed
		Iterator<ICluster> cluItr = clusterPart.iterator();
		while (cluItr.hasNext()) {
			ICluster tmpICluster = cluItr.next();

			// Clsuter
			Cluster tmpCluster = new Cluster();
			tmpCluster.setId(tmpICluster.getId());
			tmpCluster.setAvPrecursorIntens(tmpICluster.getAvPrecursorIntens());
			tmpCluster.setAvPrecursorMz(tmpICluster.getAvPrecursorMz());
			tmpCluster.setSpecCount(tmpICluster.getSpecCount());
			tmpCluster.setIntensValues(tmpICluster.getConsensusIntensValues());
			tmpCluster.setMzValues(tmpICluster.getConsensusMzValues());
//			
//			// sequences -> assemble sequence for each spectrum
//			Set<String> sequences = tmpICluster.getSequences();
//			Iterator<String> seqItr = sequences.iterator();
//			System.out.println("speNum: " + tmpICluster.getSpecCount());
//			System.out.println("Num: " + sequences.size());

			// Spectrum
			List<Spectrum> allSpectrums = new ArrayList<>();
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
				allSpectrums.add(tmpSpectrum);
			}

			tmpCluster.setSpectrums(allSpectrums); // set a cluster's spectrums
			allClusters.add(tmpCluster);
		}
		return allClusters;

	}

}
