package cn.edu.cqupt.object;

/**
 * Created by Johnny on 2017/3/22.
 */
public interface IModification extends Comparable<IModification> {
    public int getPosition();

    public String getAccession();
}