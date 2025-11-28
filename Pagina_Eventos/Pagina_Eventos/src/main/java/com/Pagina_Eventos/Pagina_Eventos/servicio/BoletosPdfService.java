package com.Pagina_Eventos.Pagina_Eventos.servicio;

import com.Pagina_Eventos.Pagina_Eventos.Entidad.BoletaVenta;
import com.Pagina_Eventos.Pagina_Eventos.Repositorio.BoletaVentaRepositorio;
import com.lowagie.text.*;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
public class BoletosPdfService {

    private final BoletaVentaRepositorio boletaVentaRepositorio;

    public BoletosPdfService(BoletaVentaRepositorio boletaVentaRepositorio) {
        this.boletaVentaRepositorio = boletaVentaRepositorio;
    }

    public byte[] generarBoletaPdf(Integer boletaId) throws DocumentException {
        BoletaVenta boleta = boletaVentaRepositorio.findById(boletaId)
                .orElseThrow(() -> new RuntimeException("Boleta no encontrada"));

        Document document = new Document(PageSize.A4);
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        PdfWriter.getInstance(document, out);
        document.open();

        // ============ ENCABEZADO CON BORDE ============
        PdfPTable headerTable = new PdfPTable(1);
        headerTable.setWidthPercentage(100);
        headerTable.setSpacingAfter(15);

        PdfPCell headerCell = new PdfPCell();
        headerCell.setPadding(15);
        headerCell.setBorderWidth(2);
        headerCell.setBackgroundColor(new java.awt.Color(245, 245, 245));

        Font companyFont = new Font(Font.HELVETICA, 22, Font.BOLD);
        Font rucFont = new Font(Font.HELVETICA, 9, Font.NORMAL);
        Font boletaTypeFont = new Font(Font.HELVETICA, 14, Font.BOLD);

        Paragraph company = new Paragraph("EVENTIA S.A.C.", companyFont);
        company.setAlignment(Element.ALIGN_CENTER);

        Paragraph ruc = new Paragraph("RUC: 20123456789", rucFont);
        ruc.setAlignment(Element.ALIGN_CENTER);
        ruc.setSpacingBefore(5);

        Paragraph address = new Paragraph("Av. Principal 123 - Lima, Peru", rucFont);
        address.setAlignment(Element.ALIGN_CENTER);

        Paragraph contact = new Paragraph("Tel: (01) 234-5678 | ventas@eventia.com", rucFont);
        contact.setAlignment(Element.ALIGN_CENTER);
        contact.setSpacingAfter(10);

        Paragraph boletaType = new Paragraph("BOLETA DE VENTA ELECTRONICA", boletaTypeFont);
        boletaType.setAlignment(Element.ALIGN_CENTER);
        boletaType.setSpacingBefore(10);

        Paragraph boletaNumber = new Paragraph("B001-" + String.format("%08d", boleta.getId()), new Font(Font.HELVETICA, 12, Font.BOLD));
        boletaNumber.setAlignment(Element.ALIGN_CENTER);
        boletaNumber.setSpacingBefore(5);

        headerCell.addElement(company);
        headerCell.addElement(ruc);
        headerCell.addElement(address);
        headerCell.addElement(contact);
        headerCell.addElement(boletaType);
        headerCell.addElement(boletaNumber);
        headerTable.addCell(headerCell);

        document.add(headerTable);

        // ============ DATOS DEL CLIENTE ============
        Font sectionFont = new Font(Font.HELVETICA, 10, Font.BOLD);
        Font labelFont = new Font(Font.HELVETICA, 9, Font.BOLD);
        Font valueFont = new Font(Font.HELVETICA, 9, Font.NORMAL);

        Paragraph clientSection = new Paragraph("DATOS DEL CLIENTE", sectionFont);
        clientSection.setSpacingAfter(8);
        document.add(clientSection);

        PdfPTable clientTable = new PdfPTable(2);
        clientTable.setWidthPercentage(100);
        clientTable.setSpacingAfter(15);
        float[] clientWidths = {1.2f, 3f};
        clientTable.setWidths(clientWidths);

        addBorderedRow(clientTable, "Señor(es):", boleta.getUsuario().getNombre() + " " + boleta.getUsuario().getApellidos(), labelFont, valueFont);
        addBorderedRow(clientTable, "DNI/RUC:", boleta.getUsuario().getDni() != null ? boleta.getUsuario().getDni() : "00000000", labelFont, valueFont);
        addBorderedRow(clientTable, "Email:", boleta.getUsuario().getEmail(), labelFont, valueFont);
        addBorderedRow(clientTable, "Fecha Emision:", boleta.getFechaVenta().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")), labelFont, valueFont);

        document.add(clientTable);

        // ============ DETALLE DE LA VENTA ============
        Paragraph detalleSection = new Paragraph("DETALLE DE LA VENTA", sectionFont);
        detalleSection.setSpacingAfter(8);
        document.add(detalleSection);

        PdfPTable detalleTable = new PdfPTable(5);
        detalleTable.setWidthPercentage(100);
        detalleTable.setSpacingAfter(15);
        float[] detalleWidths = {0.8f, 3f, 1f, 1.3f, 1.3f};
        detalleTable.setWidths(detalleWidths);

        // Headers de la tabla
        Font tableHeaderFont = new Font(Font.HELVETICA, 9, Font.BOLD);
        addTableHeaderCell(detalleTable, "ITEM", tableHeaderFont);
        addTableHeaderCell(detalleTable, "DESCRIPCION", tableHeaderFont);
        addTableHeaderCell(detalleTable, "CANT.", tableHeaderFont);
        addTableHeaderCell(detalleTable, "P.UNIT", tableHeaderFont);
        addTableHeaderCell(detalleTable, "IMPORTE", tableHeaderFont);

        // Datos del producto/servicio
        double precioUnitario = boleta.getTotal().doubleValue() / boleta.getCantidad();

        addTableDataCell(detalleTable, "1", valueFont, Element.ALIGN_CENTER);

        String descripcion = "ENTRADA - " + boleta.getEvento().getNombre() + "\n" +
                           "Fecha: " + boleta.getEvento().getFechaHora().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")) + "\n" +
                           "Lugar: " + (boleta.getEvento().getUbicacion() != null ? boleta.getEvento().getUbicacion() : "Por confirmar");
        addTableDataCell(detalleTable, descripcion, valueFont, Element.ALIGN_LEFT);

        addTableDataCell(detalleTable, String.valueOf(boleta.getCantidad()), valueFont, Element.ALIGN_CENTER);
        addTableDataCell(detalleTable, String.format("%.2f", precioUnitario), valueFont, Element.ALIGN_RIGHT);
        addTableDataCell(detalleTable, String.format("%.2f", boleta.getTotal().doubleValue()), valueFont, Element.ALIGN_RIGHT);

        document.add(detalleTable);

        // ============ TOTALES ============
        PdfPTable totalesTable = new PdfPTable(2);
        totalesTable.setWidthPercentage(40);
        totalesTable.setHorizontalAlignment(Element.ALIGN_RIGHT);
        totalesTable.setSpacingBefore(10);
        totalesTable.setSpacingAfter(20);
        float[] totalesWidths = {2f, 1.5f};
        totalesTable.setWidths(totalesWidths);

        Font totalFont = new Font(Font.HELVETICA, 10, Font.BOLD);

        // Subtotal
        addTotalRow(totalesTable, "SUB TOTAL:", String.format("S/ %.2f", boleta.getTotal().doubleValue()), labelFont, valueFont);

        // IGV (0% para servicios)
        addTotalRow(totalesTable, "IGV (18%):", "S/ 0.00", labelFont, valueFont);

        // Total con fondo
        PdfPCell totalLabelCell = new PdfPCell(new Phrase("TOTAL:", totalFont));
        totalLabelCell.setBackgroundColor(new java.awt.Color(230, 230, 230));
        totalLabelCell.setPadding(8);
        totalLabelCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        totalLabelCell.setBorderWidth(1);
        totalesTable.addCell(totalLabelCell);

        PdfPCell totalValueCell = new PdfPCell(new Phrase(String.format("S/ %.2f", boleta.getTotal().doubleValue()), totalFont));
        totalValueCell.setBackgroundColor(new java.awt.Color(230, 230, 230));
        totalValueCell.setPadding(8);
        totalValueCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        totalValueCell.setBorderWidth(1);
        totalesTable.addCell(totalValueCell);

        document.add(totalesTable);

        // ============ INFORMACIÓN ADICIONAL ============
        PdfPTable infoTable = new PdfPTable(2);
        infoTable.setWidthPercentage(100);
        infoTable.setSpacingAfter(15);
        float[] infoWidths = {1.5f, 2f};
        infoTable.setWidths(infoWidths);

        addBorderedRow(infoTable, "Forma de Pago:", boleta.getMetodoPago() != null ? boleta.getMetodoPago().toUpperCase() : "NO ESPECIFICADO", labelFont, valueFont);
        addBorderedRow(infoTable, "Codigo de Pago:", boleta.getCodigoPago(), labelFont, valueFont);
        addBorderedRow(infoTable, "Organizador:", boleta.getEvento().getOrganizador().getNombreEmpresa(), labelFont, valueFont);

        document.add(infoTable);

        // ============ PIE DE PAGINA ============
        addDottedLine(document);

        Font footerFont = new Font(Font.HELVETICA, 8, Font.ITALIC);

        Paragraph condiciones = new Paragraph("TERMINOS Y CONDICIONES:", new Font(Font.HELVETICA, 8, Font.BOLD));
        condiciones.setSpacingBefore(10);
        condiciones.setSpacingAfter(5);
        document.add(condiciones);

        Paragraph cond1 = new Paragraph("1. Conserve esta boleta como comprobante de compra.", footerFont);
        document.add(cond1);
        Paragraph cond2 = new Paragraph("2. Debe presentar este documento o el codigo de pago el dia del evento.", footerFont);
        document.add(cond2);
        Paragraph cond3 = new Paragraph("3. No se aceptan cambios ni devoluciones salvo cancelacion del evento.", footerFont);
        document.add(cond3);
        Paragraph cond4 = new Paragraph("4. El ingreso esta sujeto a disponibilidad y horario indicado.", footerFont);
        cond4.setSpacingAfter(15);
        document.add(cond4);

        addDottedLine(document);

        // Agradecimiento
        Paragraph gracias = new Paragraph("Gracias por su preferencia", new Font(Font.HELVETICA, 10, Font.BOLD));
        gracias.setAlignment(Element.ALIGN_CENTER);
        gracias.setSpacingBefore(20);
        document.add(gracias);

        // Fecha de generación
        String fechaGeneracion = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss"));
        Paragraph fechaGen = new Paragraph("Documento generado electronicamente el " + fechaGeneracion, new Font(Font.HELVETICA, 7, Font.ITALIC));
        fechaGen.setAlignment(Element.ALIGN_CENTER);
        fechaGen.setSpacingBefore(10);
        document.add(fechaGen);

        Paragraph representacion = new Paragraph("Representacion impresa de la Boleta Electronica", new Font(Font.HELVETICA, 7, Font.ITALIC));
        representacion.setAlignment(Element.ALIGN_CENTER);
        document.add(representacion);

        document.close();

        return out.toByteArray();
    }

    private void addDottedLine(Document document) throws DocumentException {
        Paragraph separator = new Paragraph("- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -");
        separator.setAlignment(Element.ALIGN_CENTER);
        separator.setSpacingAfter(5);
        document.add(separator);
    }

    private void addBorderedRow(PdfPTable table, String label, String value, Font labelFont, Font valueFont) {
        PdfPCell labelCell = new PdfPCell(new Phrase(label, labelFont));
        labelCell.setPadding(5);
        labelCell.setBorderWidth(1);
        labelCell.setBackgroundColor(new java.awt.Color(250, 250, 250));
        table.addCell(labelCell);

        PdfPCell valueCell = new PdfPCell(new Phrase(value, valueFont));
        valueCell.setPadding(5);
        valueCell.setBorderWidth(1);
        table.addCell(valueCell);
    }

    private void addTableHeaderCell(PdfPTable table, String text, Font font) {
        PdfPCell cell = new PdfPCell(new Phrase(text, font));
        cell.setBackgroundColor(new java.awt.Color(52, 73, 94));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setPadding(6);
        cell.setBorderWidth(1);
        // Hacer el texto blanco
        cell.setPhrase(new Phrase(text, new Font(Font.HELVETICA, 9, Font.BOLD, new java.awt.Color(255, 255, 255))));
        table.addCell(cell);
    }

    private void addTableDataCell(PdfPTable table, String text, Font font, int alignment) {
        PdfPCell cell = new PdfPCell(new Phrase(text, font));
        cell.setHorizontalAlignment(alignment);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setPadding(6);
        cell.setBorderWidth(1);
        table.addCell(cell);
    }

    private void addTotalRow(PdfPTable table, String label, String value, Font labelFont, Font valueFont) {
        PdfPCell labelCell = new PdfPCell(new Phrase(label, labelFont));
        labelCell.setPadding(6);
        labelCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        labelCell.setBorderWidth(1);
        table.addCell(labelCell);

        PdfPCell valueCell = new PdfPCell(new Phrase(value, valueFont));
        valueCell.setPadding(6);
        valueCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        valueCell.setBorderWidth(1);
        table.addCell(valueCell);
    }

    // Métodos anteriores ya no necesarios, pero los dejo por compatibilidad
    private void addLineSeparator(Document document) throws DocumentException {
        addDottedLine(document);
    }

    private void addInfoRow(PdfPTable table, String label, String value, Font labelFont, Font valueFont) {
        addBorderedRow(table, label, value, labelFont, valueFont);
    }

    private void addPurchaseHeader(PdfPTable table, String headerTitle, Font font) {
        addTableHeaderCell(table, headerTitle, font);
    }

    private void addPurchaseCell(PdfPTable table, String text, Font font, int alignment) {
        addTableDataCell(table, text, font, alignment);
    }
}

