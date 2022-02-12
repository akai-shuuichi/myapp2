package club.bluetroy.crawler.util;

import lombok.experimental.UtilityClass;

import java.lang.reflect.Field;

/**
 * Created with IntelliJ IDEA.
 * Description:
 *
 * @author: heyixin
 * Date: 2018-08-14
 * Time: 下午3:14
 */
@UtilityClass
public class ObjectUtils {
    public static boolean isAllPropertyNull(Object o) {
        for (Field declaredField : o.getClass().getDeclaredFields()) {
            if (declaredField != null) {
                return false;
            }
        }
        return true;
    }
}
