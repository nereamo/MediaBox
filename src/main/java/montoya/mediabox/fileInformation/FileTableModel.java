package montoya.mediabox.fileInformation;

import java.util.List;
import javax.swing.table.AbstractTableModel;

/**
 * Difine que datos mostrar en la tabla de JFrame
 *
 * @author Nerea
 */
public class FileTableModel extends AbstractTableModel {

    private final String[] column = {"Name", "Size (MB)", "Type", "Date"};
    private List<FileInformation> fileList;

    public FileTableModel(List<FileInformation> fileList) { //Recibe lista del archivo .json
        this.fileList = fileList;
    }

    //Nº filas
    @Override
    public int getRowCount() {
        return fileList.size();
    }

    //Nº columnas
    @Override
    public int getColumnCount() {
        return column.length;
    }

    //Nombre columnas
    @Override
    public String getColumnName(int columns) {
        return column[columns];
    }

    //Valores asociados a las celdas
    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        FileInformation file = fileList.get(rowIndex);
        switch (columnIndex) {
            case 0:
                return file.name;
            case 1:
                return String.format("%.2f", file.size / (1024.0 * 1024.0)); // MB
            case 2:
                return file.type;
            case 3:
                return file.date;
            default:
                return null;
        }
    }

    //Devuelve fila seleccionada
    public FileInformation getFileAt(int rowIndex) {
        return fileList.get(rowIndex);
    }

    //Elimina archivo seleccionada
    public void removeFileAt(int rowIndex) {
        fileList.remove(rowIndex);
        fireTableRowsDeleted(rowIndex, rowIndex);
    }

    //Añade archivo
    public void addFile(FileInformation file) {
        fileList.add(file);
        fireTableRowsInserted(fileList.size() - 1, fileList.size() - 1);
    }
   
    //Actualiza los archivos mostrado en la tabla
    public void setFileList(List<FileInformation> newList) {
        fileList.clear();          
        fileList.addAll(newList);  
        fireTableDataChanged();
    }

    //Devuelve lista de archivos
    public List<FileInformation> getFileList() {
        return fileList;
    }
}
