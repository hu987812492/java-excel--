
import java.util.ArrayList;
import java.util.List;
import java.util.ArrayList;
import java.util.List;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Sheet;



public class PoiCells {
    public final static String regex = "\\$\\{[^\\}]+\\}";
    private Sheet sheet ;
    private Cell cell ;
    private int sheetIndex;

    public PoiCells(){}

    public PoiCells(Sheet sheet,Cell cell){
        this.sheet = sheet;
        this.cell = cell;
    }

    public Sheet getSheet() {
        return sheet;
    }
    public void setSheet(Sheet sheet) {
        this.sheet = sheet;
    }
    public Cell getCell() {
        return cell;
    }
    public void setCell(Cell cell) {
        this.cell = cell;
    }
    public int getSheetIndex() {
        return sheetIndex;
    }
    public void setSheetIndex(int sheetIndex) {
        this.sheetIndex = sheetIndex;
    }

    /**
     * ��ȡ��ǰ�е��С����ʽ������
     * @return
     */
    public List getExps(){
        List list = new ArrayList();
        if(this.cell!=null){
            String contents = this.cell.getStringCellValue();
            if(!"".equals(contents)){
                list = StringUtil.search(this.regex, contents);
            }
        }
        return list;
    }

    public String getFomatContext(){
        String contents = this.cell.getStringCellValue();
        //while(contents.)
        return contents;
    }
}
