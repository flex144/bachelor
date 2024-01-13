package com.example.bachelor.services;

import com.example.bachelor.data.dto.GuardDayDto;
import com.example.bachelor.data.dto.JournalEntryDto;
import com.example.bachelor.data.dto.UserGuardingRelationDto;
import com.example.bachelor.data.enums.EntryType;
import com.example.bachelor.utility.helper.DateHelper;
import com.example.bachelor.utility.helper.TimePeriod;
import com.lowagie.text.Font;
import com.lowagie.text.*;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import org.hibernate.service.spi.InjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.awt.*;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class GuardDayPDFExportService {

    @Autowired
    GuardDayService guardDayService;

    private static Color DEFAULT_COLOR = Color.BLUE;
    private static Font DEFAULT_FONT = FontFactory.getFont(FontFactory.HELVETICA, -1.0F, Color.WHITE);
    private static DateFormat dateFormatter = new SimpleDateFormat("HH:mm");

    public void export(HttpServletResponse response, GuardDayDto guardDayDto) throws DocumentException, IOException {
        if (guardDayDto.getActualEndTime() == null) {
            throw new IllegalStateException("Wachtag kann nicht gedruckt werden, da er noch nicht beendet wurde.");
        }
        Document document = new Document(PageSize.A4);
        PdfWriter.getInstance(document, response.getOutputStream());

        document.open();
        Font font = FontFactory.getFont(FontFactory.HELVETICA_BOLD);
        font.setSize(18);
        font.setColor(DEFAULT_COLOR);

        Paragraph p = new Paragraph("Wachtag Export - " + guardDayDto.getGuardingDate(), font);
        p.setAlignment(Paragraph.ALIGN_CENTER);

        document.add(p);

        createGeneralData(document, guardDayDto);
        p = new Paragraph("Wetterdaten", font);
        document.add(p);
        createWeatherData(document, guardDayDto);
        p = new Paragraph("Wachpersonal", font);
        document.add(p);
        createUserData(document, guardDayDto);
        p = new Paragraph("Wachbuch", font);
        document.add(p);
        createGuardingData(document, guardDayDto);

        document.close();
    }

    private void createGeneralData(Document document, GuardDayDto guardDayDto) {
        PdfPTable table = new PdfPTable(4);
        table.setWidthPercentage(100f);
        table.setWidths(new float[]{3.0f, 3.0f, 3.0f, 3.0f});
        table.setSpacingBefore(10);

        fillTableHeader(table, "Datum", "Beginn", "Ende", "Dauer");
        fillGeneralDataTable(guardDayDto, table);

        document.add(table);
    }

    private void fillGeneralDataTable(GuardDayDto guardDayDto, PdfPTable table) {
        table.addCell(String.valueOf(guardDayDto.getGuardingDate()));
        table.addCell(dateFormatter.format(guardDayDto.getActualStartTime()));
        table.addCell(dateFormatter.format(guardDayDto.getActualEndTime()));

        TimePeriod timePeriod = DateHelper.getTimePeriod(guardDayDto.getActualStartTime(), guardDayDto.getActualEndTime());
        table.addCell(timePeriod.toString());
    }

    private void createWeatherData(Document document, GuardDayDto guardDayDto) {
        PdfPTable table = new PdfPTable(3);
        table.setWidthPercentage(100f);
        table.setWidths(new float[]{3.0f, 3.0f, 3.0f});
        table.setSpacingBefore(10);

        fillTableHeader(table, "Uhrzeit", "Typ", "Eintrag");
        fillWeatherDataTable(guardDayDto, table);

        document.add(table);
    }

    private void fillWeatherDataTable(GuardDayDto guardDayDto, PdfPTable table) {

        List<JournalEntryDto> weatherEntries = guardDayDto.getJournalEntries().stream().filter(n -> Arrays.asList(
                EntryType.WEATHER, EntryType.WATER_TEMP
        ).contains(n.getEntryType())).collect(Collectors.toList());
        Collections.reverse(weatherEntries);

        for (JournalEntryDto entry : weatherEntries) {
            table.addCell(dateFormatter.format(entry.getCreation()));
            table.addCell(EntryType.WATER_TEMP.equals(entry.getEntryType()) ? "Wassertemperatur" : "Wetter");
            table.addCell(entry.getDescription());
        }
    }

    private void createUserData(Document document, GuardDayDto guardDayDto) {
        PdfPTable table = new PdfPTable(4);
        table.setWidthPercentage(100f);
        table.setWidths(new float[]{3.0f, 3.0f, 3.0f, 3.0f});
        table.setSpacingBefore(10);

        fillTableHeader(table, "Name", "Von", "Bis", "Dauer");
        fillUserDataTable(guardDayDto, table);

        document.add(table);
    }

    private void fillUserDataTable(GuardDayDto guardDayDto, PdfPTable table) {
        List<UserGuardingRelationDto> allRelations = guardDayService.readUserGuardingRelations(guardDayDto.getGuardDayId()).stream().filter(n -> !n.isBooked()).collect(Collectors.toList());

        for (UserGuardingRelationDto relation : allRelations) {
            table.addCell(relation.getUserDto() != null ? relation.getUserDto().getFirstName() + " " + relation.getUserDto().getLastName() : relation.getUserFreetext());
            table.addCell(dateFormatter.format(relation.getGuardingStart()));
            table.addCell(dateFormatter.format(relation.getGuardingEnd()));

            TimePeriod timePeriod = DateHelper.getTimePeriod(relation.getGuardingStart(), relation.getGuardingEnd());
            table.addCell(timePeriod.toString());
        }
    }

    private void createGuardingData(Document document, GuardDayDto guardDayDto) {
        PdfPTable table = new PdfPTable(4);
        table.setWidthPercentage(100f);
        table.setWidths(new float[]{3.0f, 3.0f, 3.0f, 3.0f});
        table.setSpacingBefore(10);

        fillTableHeader(table, "Typ", "Uhrzeit", "Melder", "Ereignis");
        fillGuardingDataTable(guardDayDto, table);

        document.add(table);
    }

    private void fillGuardingDataTable(GuardDayDto guardDayDto, PdfPTable table) {
        List<JournalEntryDto> entries = guardDayDto.getJournalEntries().stream().filter(n -> !Arrays.asList(EntryType.WEATHER, EntryType.WATER_TEMP, EntryType.USER_GUARD_BEGIN, EntryType.USER_GUARD_END).contains(n.getEntryType())).collect(Collectors.toList());
        Collections.reverse(entries);

        for (JournalEntryDto entry : entries) {
            table.addCell(String.valueOf(entry.getEntryType().getDescription()));
            table.addCell(dateFormatter.format(entry.getCreation()));
            table.addCell(entry.getUserDto().getFirstName() + " " + entry.getUserDto().getLastName());
            table.addCell(entry.getDescription());
        }
    }


    private void fillTableHeader(PdfPTable table, String... header) {
        PdfPCell cell = new PdfPCell();
        cell.setBackgroundColor(DEFAULT_COLOR);
        cell.setPadding(5);

        for (String phrase : header) {
            cell.setPhrase(new Phrase(phrase, DEFAULT_FONT));
            table.addCell(cell);
        }
    }
}
