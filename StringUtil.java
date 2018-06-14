
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringUtil {
    /**
     * 查找一个字符串在数组中的索引值
     *
     * @param data
     * @param string
     * @return
     */
    public static int searchIndex(String[] data, String string) {
        if (data == null || data.length == 0)
            return -1;
        for (int i = 0; i < data.length; i++) {
            if ((data[i] != null && data[i].equals(string)) || (data[i] == null && string == null)) {
                return i;
            }
        }
        return -1;
    }

    public static boolean isNull(String string) {
        if (string == null || string.equals(""))
            return true;
        return false;
    }

    public static String arrayToString(List list, String split) {
        if (list == null || list.size() == 0)
            return null;

        String result = "";
        if (isNull(split))
            split = ",";

        for (int i = 0; i < list.size(); i++) {
            result += String.valueOf(list.get(i)) + split;
        }
        result = result.substring(0, result.length() - 1);
        return result;
    }

    public static String arrayToString(String[] list, String split) {
        if (list == null || list.length == 0)
            return null;

        String result = "";
        if (isNull(split))
            split = ",";

        for (int i = 0; i < list.length; i++) {
            result += list[i] + split;
        }
        result = result.substring(0, result.length() - 1);
        return result;
    }

    public static String getFileName(String name) {
        if (isNull(name))
            return null;

        if (name.lastIndexOf("\\") != -1) {
            name = name.substring(name.lastIndexOf("\\") + 1, name.length());
        } else if (name.lastIndexOf("/") != -1) {
            name = name.substring(name.lastIndexOf("/") + 1, name.length());
        }

        return name;
    }

    public static String getExtensionName(String name) {
        if (isNull(name))
            return null;
        if (name.lastIndexOf(".") != -1) {
            return name.substring(name.lastIndexOf(".") + 1);
        }
        return "";
    }

//    public static void main(String[] arg0) {
//        String a = "${bean.aid} sdfad ${xx.yy}sfdd";
//
//        String regex = "";
//
//        search(regex, a);
//    }

    public static String getUID() {
//        return UUID.randomUUID().toString().replaceAll("-", "");
        return "";
    }

    public static String replaceAll(String str, String regex, String replaceMent){
        StringBuffer sb = new StringBuffer();

        int index = str.indexOf(regex);
        sb.append(str.substring(0, index));
        sb.append(replaceMent);
        sb.append(str.substring(index+regex.length()));

//    	System.out.println(sb.toString());
        return sb.toString();
    }

    public static void main(String[] args) {
        String a = "${bean.aid} sdfad ${xx.yy}sfdd";

        a = StringUtil.replaceAll(a, " sdfa", "");

        System.out.println(a);
    }

    /**
     * 查找【正则表达式】描述的内容。
     * @param regex
     * @param string
     * @return
     */
    public static List search(String regex, String string) {
        List list = new ArrayList();
        Pattern p = Pattern.compile(regex);

        Matcher m = p.matcher(string);
        while (m.find()) {
            String g = m.group();
            list.add(g);
        }
        return list;
    }
}
