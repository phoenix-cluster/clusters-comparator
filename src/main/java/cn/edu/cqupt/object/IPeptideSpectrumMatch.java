package cn.edu.cqupt.object;

/**
 * Created by Johnny on 2017/3/22.
 */
import cn.edu.cqupt.object.IModification;

import java.util.List;

/**
 * Created by jg on 24.09.14.
 */
public interface IPeptideSpectrumMatch {
	public String getSequence();

	public List<IModification> getModifications();

}