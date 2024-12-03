package com.example.demo.controller;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.FileInputStream;
import java.io.InputStream;
import java.nio.file.*;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@RestController
public class DataController {

    private final SimpMessagingTemplate messagingTemplate;
    private final String excelFilePath = "D:/iitjhtml.xlsx";
    private final ExecutorService executorService = Executors.newFixedThreadPool(2);

    @Autowired
    public DataController(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
        // Start file monitoring in the background
        startFileWatcher(excelFilePath);
    }

    // Endpoint to get data manually
    @GetMapping("/data")
    public CompletableFuture<List<Map<String, Object>>> getData() {
        return readExcelDataAsync(excelFilePath);
    }

    // Asynchronously read and process the Excel file
    private CompletableFuture<List<Map<String, Object>>> readExcelDataAsync(String filePath) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                return readExcelData(filePath);
            } catch (Exception e) {
                e.printStackTrace();
                return Collections.emptyList();
            }
        }, executorService);
    }

    // Synchronous method to read and process the Excel file
    private List<Map<String, Object>> readExcelData(String filePath) throws Exception {
        System.out.println("Reading Excel file: " + filePath);
        int retries = 5;
        int retryDelay = 2000; // 2 seconds delay between retries

        for (int i = 0; i < retries; i++) {
            try (InputStream inputStream = new FileInputStream(filePath);
                 Workbook workbook = new XSSFWorkbook(inputStream)) {

                Sheet sheet = workbook.getSheetAt(0);
                List<Map<String, Object>> dataList = new ArrayList<>();
                Row headerRow = sheet.getRow(0); // Assuming the first row is the header

                for (int rowIndex = 1; rowIndex <= sheet.getLastRowNum(); rowIndex++) {
                    Row row = sheet.getRow(rowIndex);
                    if (row == null) continue;

                    Map<String, Object> data = new HashMap<>();
                    for (int colIndex = 0; colIndex < row.getLastCellNum(); colIndex++) {
                        Cell cell = row.getCell(colIndex);
                        String header = headerRow.getCell(colIndex).getStringCellValue();
                        Object cellValue = getCellValue(cell);
                        data.put(header, cellValue);
                    }
                    dataList.add(data);
                }

                // Send updates to WebSocket clients asynchronously
                messagingTemplate.convertAndSend("/topic/data-updates", dataList);
                System.out.println("DataList: " + dataList);
                return dataList;

            } catch (Exception e) {
                System.out.println("File is currently locked or cannot be accessed. Retrying in 2 seconds...");
                Thread.sleep(retryDelay); // Retry after delay if file is locked
            }
        }

        throw new Exception("Failed to read Excel file after multiple attempts.");
    }

    // Helper to get the value of a cell based on its type
    private Object getCellValue(Cell cell) {
        if (cell == null) return null;
        switch (cell.getCellTypeEnum()) {
            case STRING:
                return cell.getStringCellValue();
            case NUMERIC:
                if (DateUtil.isCellDateFormatted(cell)) {
                    return cell.getDateCellValue();
                } else {
                    return cell.getNumericCellValue();
                }
            case BOOLEAN:
                return cell.getBooleanCellValue();
            default:
                return null;
        }
    }

    // Start the file watcher for the Excel file asynchronously
    private void startFileWatcher(String filePath) {
        CompletableFuture.runAsync(() -> {
            try {
                WatchService watchService = FileSystems.getDefault().newWatchService();
                Path path = Paths.get(filePath).getParent();
                path.register(watchService, StandardWatchEventKinds.ENTRY_MODIFY);

                WatchKey key;
                while ((key = watchService.take()) != null) {
                    for (WatchEvent<?> event : key.pollEvents()) {
                        if (event.kind() == StandardWatchEventKinds.ENTRY_MODIFY) {
                            System.out.println("File modified: " + event.context());
                            // Re-read the Excel file and send updated data to client asynchronously
                            readExcelDataAsync(filePath).thenAccept(dataList -> {
                                System.out.println("Updated data sent to WebSocket clients.");
                            });
                        }
                    }
                    key.reset();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }, executorService);
    }
}
