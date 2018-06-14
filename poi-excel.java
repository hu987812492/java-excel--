import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.collections.map.HashedMap;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;


class excelText {

    public static void main(String[] args) throws Exception {

        //根据模板文件，$符号的形式，生成对应的excel
        //模板文件：
        String oldPath = "旧的文件路径";
        //   如果是服务器上的文件
        //   request.getSession().getServletContext().getRealPath("jsp/ww_bgglr/download/pzb/pzbInfo.xls");
        //接下来，我们伪造数据，一般这种情况是从数据库查到的

        //①，我们的模板文件，$占位符，存到数组里，用作测试数据
        //目前第一种模板，结果集为一个map第二种模板结果集为一个list
        Map datas = new HashMap();
        List<HashMap> list = new ArrayList<HashMap>();
        String[] str1 = {"ywrq1", "ywrq2", "cpdm", "cpmc", "dwjz", "ljjz",
                "glr1", "tgr1", "glr2", "ywrq3", "tgr2", "ywrq4"};
        String[] str2 = {"CS001", "CS002", "CS003", "CS004", "CS005", "CS006",
                "CS007", "CS008", "CS009", "CS010", "CS011", "CS012"};
        //如果是list ，我们就循环 造数，这里是map就不用了
        HashMap sMap = new HashMap();   //用于存储数据map
        for (int i = 0; i < str1.length; i++) {
            sMap.put(str1[i], str2[i]);
        }
        list.add(0, sMap);     //该list 作用等同于我们从数据库里面查出来的list
        datas.put("zcjzgg",list);     //把我们所得到的list 放在一个map 里面
        Workbook wb =   creatEx(oldPath, datas, "zcjzgg");  //创建excel 文件，
        // 参数 1.原始文静路径，2.数据map，3.我们占位符 的名字
        //占位符解释：${one.zcjzgg.ywrq1}   ${esch.zcjzgg.ywrq1}
        //如果是 数据是单个Map 则用 one ，若为List 则为 each
        //第二个为   map名字  ，第三个为标识符名字
        String newPath="新的文件路径";
        FileOutputStream out = null;
        try {
            out = new FileOutputStream(newPath);
            wb.write(out);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (out != null) {
                    out.flush();
                    out.close();
                    out = null;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }


    }



    /**
     * 用于  创建 excel ----wb的方法
     * @param path   模板文件路径
     * @param datas  需要插入的结果集
     * @param bs      标识，
     * @return
     */
    public Workbook creatEx(String path, Map datas, String bs) {
        Workbook wb = null;
        if (datas == null) {
            return wb;
        }
        try {
            wb = openWorkbook(path);
        } catch (Exception e) {
            e.printStackTrace();
        }
        generateExpData(wb, datas, bs);
        return wb;
    }


    /**
     * 用于根据模板路径打开模板文件
     * @param filePath
     * @return
     * @throws IOException
     */
    public static Workbook openWorkbook(String filePath) throws IOException {
        Workbook wb = null;
        InputStream fs = null;
        String fileName = "";
        try {
            File file = new File(filePath.trim());
            if(!file.exists()){
                System.out.println("模板文件:"+filePath+"不存在!");
            }
            fs = new FileInputStream(file);
            fileName = file.getName();
            if (fileName.endsWith(".xlsx")) {
                wb = new XSSFWorkbook(fs);// Excel 2007
            }else{
                wb = new HSSFWorkbook(fs);
            }
            fs.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("====poiExcel+openWorkbook================"+filePath+"================================"+wb);
        return wb;
    }

    /**
     * 用于获取 模板文件标识占位符
     * @param text  --占位符前部   我们定义的占位符是${},所以前部是${
     *                          也可以根据喜好自己定义，但是要保证excel与传入的一致
     * @param wb ---打开模板获得的wb
     * @return
     */
    private static List search(String text, Workbook wb) {
        List rcells = new ArrayList();
        Sheet sheet = null;
        Row row = null;
        Cell cell = null;
        // 循环获取所有的sheet页
        for (int i = 0; i < wb.getNumberOfSheets(); i++) {
            // 获取sheet工作薄
            sheet = wb.getSheetAt(i);
            // 获取当前工作薄共有多少有效行, 循环获取行
            for (int j = 0; j < sheet.getPhysicalNumberOfRows(); j++) {
                // 获取指定行
                row = sheet.getRow(j);
                // 获取当前行有多少单元格, 循环获取单元格数量
                for (int k = 0; k < row.getLastCellNum(); k++) {
                    // 获取指定单元格数
                    cell = row.getCell(k);
                    // 判断当前单元格是否为空
                    if (cell != null) {
                        // 判断当前单元格内容是否为空
                        if (!StringUtils.isNull(cell.getStringCellValue())) {
                            // 获取当前单元格内容
                            String contents = cell.getStringCellValue();
                            // 判断当前单元格里是否有指定的模板内容
                            if (contents.indexOf(text) != -1) {     //如果有出现了  ${,则
                                                                                    // 把该单元格内容放在list里
                                rcells.add(new PoiCells(sheet, cell));
                            }
                        }
                    }
                }
            }
        }

        return rcells;
    }


    /**
     * 切割标识符，获取数组，并根据标识调用写excel的方法
     * @param wb
     * @param datas
     * @param bs
     */
    public void generateExpData(Workbook wb, Map datas,String bs) {
        List expcells = search("${", wb); //搜索wb，获取所有标识符得到list
        PoiCells cells = null;

        for (int i = 0; i < expcells.size(); i++) { //遍历list，以列的形式循环
            cells = (PoiCells) expcells.get(i);    //定义了一个cell对象，获取到的标识符转化为
                                                                    //标识符对象


            // 将获取所有的标识符并切割成数组，只保留标识符名字，
            String[] array =cells.getCell().getStringCellValue().trim()
                    .replaceAll("[$]{1}[{]", "").replaceAll("}", "")
                    .split("\\.");


            //我们的下一步是建立工作簿，sheet，cell等，循环插入了，
            //这里我们因为  每个模板的样式都不一样，所以
            //我们用的标识符的形式
            if(bs.equals("pzb")){
               // writePzb(wb, datas, array, cells);
            }else {
               writeExNr(wb, datas, array, cells);
            }

        }
    }


    /**
     * 这里写了一个公用的，没有任何样式的方法
     * @param wb  我们根据路径打开的工作簿
     * @param map  需要写入的数据
     * @param array  切割好的标识符  array 因该长度为3，
     *         原标识符为 ${esch.zcjzgg.ywrq1}那么0:each  1:zcjzgg  2:ywrq1
     *         原标识符为 ${one.zcjzgg.ywrq1}那么0:one  1:zcjzgg  2:ywrq1
     * @param cells
     */
    public static void writeExNr(Workbook wb, Map map, String[] array,
                                 PoiCells cells) {
        Map xMap = new LinkedHashMap();
        Sheet sheet = null;
        Row row = null;
        Cell cell = null;

        List data = (List) map.get(array[1]);  //我们根据标识占位符第二个获取map中的
                                                                    //数据  list

        for (int i = 0; i < data.size(); i++) {
            xMap = (Map) data.get(i);
            // 获取工作薄
            sheet = wb.getSheet(cells.getSheet().getSheetName());
            // 获取行或创建行
            if (cells.getCell().getRowIndex() + i + 1 > sheet
                    .getPhysicalNumberOfRows()) {
                row = sheet.createRow(cells.getCell().getRowIndex() + i);
            } else {
                row = sheet.getRow(cells.getCell().getRowIndex() + i);
            }
            // 获取或生成单元格
            if (row.getPhysicalNumberOfCells() < cells.getCell()
                    .getColumnIndex() + 1) {
                cell = row.createCell(cells.getCell().getColumnIndex());
            } else {
                cell = row.getCell(cells.getCell().getColumnIndex());
            }

            //xMap.get(array[2]) 是根据  标识符名字，获取我们数据库查出来的数据对应的map  value
            cell.setCellValue(!"".equals(xMap.get(array[2]))?xMap.get(array[2]).toString():"");
            cell.setCellStyle(cells.getCell().getCellStyle());
            //*注意  如果这里报了空指针异常，请检查模板
            //*保证每个单元格里面只有标识符，如果想要
            // 原始数据+标识符+原始数据      这种的话
            //①拼接，②修改search方法



            // 循环了一次之后，这里判断我们标识占位符头，假如是one，只循环一次
            if ("one".equals(array[0])) {
                break;
            }


        }

    }

}







