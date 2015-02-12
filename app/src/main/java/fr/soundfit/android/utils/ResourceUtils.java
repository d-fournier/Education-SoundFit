package fr.soundfit.android.utils;

        import android.content.Context;
        import android.content.res.Resources;

        import java.lang.reflect.Array;

/**
 * Created by Donovan on 12/07/2014.
 */
public class ResourceUtils {

    public enum ResourceType{
        ID ("id"),
        STRING ("string"),
        DRAWABLE ("drawable"),
        ARRAY ("array");

        private String mName = "";
        ResourceType(String name){
            mName = name;
        }
        public String toString(){
            return mName;
        }
    }

    public static int getResourceId(ResourceType type, String baseName, Context context){
        if(context == null)
            return -1;
        Resources r = context.getResources();
        return r.getIdentifier(type + "_" + baseName, type.toString(), context.getPackageName());
    }


}
