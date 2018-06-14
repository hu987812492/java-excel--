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
