package Chemistry.com.UpdatingChemicals;
import java.util.ArrayList;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Iterator;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;


public class ExcelReader {
    
    public ArrayList<ChemicalData> readExcelData(String excelFilePath) {
        ArrayList<ChemicalData> returnData = new ArrayList<>();
        try (FileInputStream fis = new FileInputStream(new File(excelFilePath));
             Workbook workbook = new XSSFWorkbook(fis)) {

            Sheet sheet = workbook.getSheetAt(0);
            Iterator<Row> rowIterator = sheet.rowIterator();
            
            int rowIndex = 0;
            while (rowIterator.hasNext()) {
            	 // Skip the first row (header)
                Row row = rowIterator.next();
                if (rowIndex == 0) {
                	
                    rowIndex++;
                    continue;
                }
                rowIndex++;
                try {

                } catch (IllegalStateException e) {
                	

                }
                String barcode; // This will hold the value of cell 0

                switch (row.getCell(0).getCellType()) {
                    case STRING:
                    	barcode = row.getCell(0).getStringCellValue();
                    	barcode= barcode.toUpperCase();
                        break;
                    case NUMERIC:
                    	barcode = String.valueOf((int) row.getCell(0).getNumericCellValue());
                        break;
                    default:
                    	barcode = ""; // Default value or throw an exception if needed
                        break;
                }
                String recordedinventoryNumber; // This will hold the value of cell 3

                switch (row.getCell(3).getCellType()) {
                    case STRING:
                    	recordedinventoryNumber = row.getCell(3).getStringCellValue();
                    	recordedinventoryNumber= recordedinventoryNumber.toUpperCase();
                        break;
                    case NUMERIC:
                    	recordedinventoryNumber = String.valueOf((int) row.getCell(3).getNumericCellValue());
                        break;
                    default:
                    	recordedinventoryNumber = ""; // Default value or throw an exception if needed
                        break;
                }
                String scannedinventoryNumber; // This will hold the value of cell 7
                
                switch (row.getCell(7).getCellType()) {
                case STRING:
                	scannedinventoryNumber = row.getCell(7).getStringCellValue();
                	scannedinventoryNumber = scannedinventoryNumber.toUpperCase();
                    break;
                case NUMERIC:
                	scannedinventoryNumber = String.valueOf((int) row.getCell(7).getNumericCellValue());
                    break;
                default:
                	scannedinventoryNumber = ""; // Default value or throw an exception if needed
                    break;
            }
                ChemicalData data = new ChemicalData(
                		// Get Barcode
                		barcode,
                    	//Get Chemical Description
                    row.getCell(1).getStringCellValue(),
                    	//get CAS Number
                    row.getCell(2).getStringCellValue(),
                    	// Gets scanned room number
                    recordedinventoryNumber,   
                    	// Get scanned cabinet location
                    row.getCell(4).getStringCellValue(),
                    	//Get scanned shelf location
                    row.getCell(5).getStringCellValue(),
                    	//Get scanned container 
                    row.getCell(6).getStringCellValue(),
                    	//gets updated room number 
                    scannedinventoryNumber,
                    	//gets updated cabinet location 
                    row.getCell(8).getStringCellValue(),
                    	//gets updated shelf number 
                    row.getCell(9).getStringCellValue(),
                    	//Gets updated container 
                    row.getCell(10).getStringCellValue()
                );
                returnData.add(data);
                
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
		return returnData;
        
    }

    public static class ChemicalData {
    	public String barcode;
    	public String chemicalDescription;
    	public String casNumber;
    	public String recordedRoom;
    	public String recordedStorageLocation;
    	public String recordedSubLocation;
    	public String recordedContainerMaterial;
    	public String scannedRoom;
    	public String scannedStorageLocation;
    	public String scannedSubLocation;
    	public String scannedContainerMaterial;

        public ChemicalData(String barcode, String chemicalDescription, String casNumber, String recordedRoom, 
                            String recordedStorageLocation, String recordedSubLocation, String recordedContainerMaterial, 
                            String scannedRoom, String scannedStorageLocation, String scannedSubLocation, 
                            String scannedContainerMaterial) {
            this.barcode = barcode;
            this.chemicalDescription = chemicalDescription;
            this.casNumber = casNumber;
            this.recordedRoom = recordedRoom;
            this.recordedStorageLocation = recordedStorageLocation;
            this.recordedSubLocation = recordedSubLocation;
            this.recordedContainerMaterial = recordedContainerMaterial;
            this.scannedRoom = scannedRoom;
            this.scannedStorageLocation = scannedStorageLocation;
            this.scannedSubLocation = scannedSubLocation;
            this.scannedContainerMaterial = scannedContainerMaterial;
        }
        @Override
        public String toString() {
            return "ChemicalData { " + 
                   "barcode='" + barcode + '\'' + 
                   ", chemicalDescription='" + chemicalDescription + '\'' +
                   ", casNumber='" + casNumber + '\'' +
                   ", recordedRoom='" + recordedRoom + '\'' +
                   ", recordedStorageLocation='" + recordedStorageLocation + '\'' +
                   ", recordedSubLocation='" + recordedSubLocation + '\'' +
                   ", recordedContainerMaterial='" + recordedContainerMaterial + '\'' +
                   ", scannedRoom='" + scannedRoom + '\'' +
                   ", scannedStorageLocation='" + scannedStorageLocation + '\'' +
                   ", scannedSubLocation='" + scannedSubLocation + '\'' +
                   ", scannedContainerMaterial='" + scannedContainerMaterial + '\'' +
                   '}';
        }
    }
    
}


