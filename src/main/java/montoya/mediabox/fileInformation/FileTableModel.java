package montoya.mediabox.fileInformation;

import java.util.List;
import javax.swing.table.AbstractTableModel;

/**
 * Define que datos mostrar en la tabla de JFrame
 *
 * @author Nerea
 */
public class FileTableModel extends AbstractTableModel {

    //Nombre de las columnas
    private final String[] column = {"Name", "Size (MB)", "Type", "Date", "Actions"};
    private List<FileInformation> fileList;

    /**
     * Constructor que recibe la lista de archivos
     * 
     * @param allFiles Lista de archivos cargados desde JSON
     */
    public FileTableModel(List<FileInformation> allFiles) {
        this.fileList = allFiles;
    }

    /** @return Número de filas de la tabla */
    @Override
    public int getRowCount() {
        return fileList.size();
    }

    /** @return Número de columnas de la tabla */
    @Override
    public int getColumnCount() {
        return column.length;
    }

    /** @return Nombre de las columnas de la tabla */
    @Override
    public String getColumnName(int columns) {
        return column[columns];
    }
    
    /**
     * Indica que solo la columna nº4 es editable.
     * 
     * @param rowIndex Índice de la fila
     * @param columnIndex Índice de la columna
     * @return True si la columna es editable, sino False
     */
    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return columnIndex == 4;
    }

    /**
     * Devuelve el valor de la fila.
     * 
     * @param rowIndex Índice de la fila
     * @param columnIndex Índice de la columna
     * @return El contenido de la fila, {@code null} si no hay nada
     */
    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        FileInformation file = fileList.get(rowIndex);
        switch (columnIndex) {
            case 0:
                return file.getName();
            case 1:
                return String.format("%.2f", file.getSize() / (1024.0 * 1024.0)); // MB
            case 2:
                return file.getType();
            case 3:
                return file.getDate();
            case 4:
                return "PlayOrDelete";
            default:
                return null;
        }
    }

    /**
     * Devuelve la fila seleccionada
     * 
     * @param rowIndex Índice de la fila
     * @return Objeto {@link FileInformation} correspondiente a la fila
     */
    public FileInformation getFileAt(int rowIndex) {
        return fileList.get(rowIndex);
    }

    /**
     * Elimina la fila seleccionada
     * 
     * @param rowIndex Índice de la fila
     */
    public void removeFileAt(int rowIndex) {
        fileList.remove(rowIndex);
        fireTableRowsDeleted(rowIndex, rowIndex);
    }

    /**
     * Añade el nuevo archivo al final de la tabla
     * 
     * @param file Archivo {@link FileInformation} a añadir
     */
    public void addFile(FileInformation file) {
        fileList.add(file);
        fireTableRowsInserted(fileList.size() - 1, fileList.size() - 1);
    }
   
    /**
     * Actualiza los archivos de la tabla
     * 
     * @param newList Nueva lista de archivos {@link FileInformation}
     */
    public void setFileList(List<FileInformation> newList) {
        fileList.clear();          
        fileList.addAll(newList);  
        fireTableDataChanged();
    }

    /**
     * Devuelve la lista completa de archivos mostrados en la tabla
     * 
     * @return Lista de {@link FileInformation} actualmente en la tabla
     */
    public List<FileInformation> getFileList() {
        return fileList;
    }
}
