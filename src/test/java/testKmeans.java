
import com.recsystem.model.KmeansCos;

/**
 * Created by yanqing on 2016/1/8.
 */
public class testKmeans {
    public static void main(String[] args){
        KmeansCos kmeansClustrer = new KmeansCos(6);
        kmeansClustrer.execute();
    }
}
