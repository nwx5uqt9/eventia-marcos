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
import java.util.List;

@Service
public class VentasPdfService {

    private final BoletaVentaRepositorio boletaVentaRepositorio;

    public VentasPdfService(BoletaVentaRepositorio boletaVentaRepositorio) {
        this.boletaVentaRepositorio = boletaVentaRepositorio;
    }

    public byte[] generarReporteVentas() throws DocumentException {
        List<BoletaVenta> ventas = boletaVentaRepositorio.findAll();

        Document document = new Document(PageSize.A4.rotate());
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        PdfWriter.getInstance(document, out);
        document.open();

        // Titulo
        Font titleFont = new Font(Font.HELVETICA, 18, Font.BOLD);
        Paragraph title = new Paragraph("REPORTE DE VENTAS", titleFont);
        title.setAlignment(Element.ALIGN_CENTER);
        title.setSpacingAfter(10);
        document.add(title);

        // Fecha de generacion
        Font dateFont = new Font(Font.HELVETICA, 10, Font.NORMAL);
        String fechaActual = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss"));
        Paragraph date = new Paragraph("Generado el: " + fechaActual, dateFont);
        date.setAlignment(Element.ALIGN_RIGHT);
        date.setSpacingAfter(20);
        document.add(date);

        // Tabla
        PdfPTable table = new PdfPTable(7);
        table.setWidthPercentage(100);
        table.setSpacingBefore(10f);
        table.setSpacingAfter(10f);

        // Configurar anchos de columnas
        float[] columnWidths = {1f, 2.5f, 2.5f, 2.5f, 2.5f, 2f, 2f};
        table.setWidths(columnWidths);

        // Headers
        Font headerFont = new Font(Font.HELVETICA, 10, Font.BOLD);
        addTableHeader(table, "ID", headerFont);
        addTableHeader(table, "USUARIO", headerFont);
        addTableHeader(table, "EVENTO", headerFont);
        addTableHeader(table, "UBICACION", headerFont);
        addTableHeader(table, "CODIGO PAGO", headerFont);
        addTableHeader(table, "FECHA", headerFont);
        addTableHeader(table, "TOTAL", headerFont);

        // Datos
        Font cellFont = new Font(Font.HELVETICA, 9, Font.NORMAL);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        double totalGeneral = 0.0;

        for (BoletaVenta venta : ventas) {
            addTableCell(table, String.valueOf(venta.getId()), cellFont);
            addTableCell(table, venta.getUsuario() != null ? venta.getUsuario().getNombreusuario() : "N/A", cellFont);
            addTableCell(table, venta.getEvento() != null ? venta.getEvento().getNombre() : "N/A", cellFont);
            addTableCell(table, venta.getUbicacion() != null ? venta.getUbicacion().getNombre() : "N/A", cellFont);
            addTableCell(table, venta.getCodigoPago(), cellFont);
            addTableCell(table, venta.getFechaVenta() != null ? venta.getFechaVenta().format(formatter) : "N/A", cellFont);
            addTableCell(table, "S/ " + String.format("%.2f", venta.getTotal()), cellFont);

            if (venta.getTotal() != null) {
                totalGeneral += venta.getTotal().doubleValue();
            }
        }

        document.add(table);

        // Total general
        Paragraph total = new Paragraph("TOTAL GENERAL: S/ " + String.format("%.2f", totalGeneral), titleFont);
        total.setAlignment(Element.ALIGN_RIGHT);
        total.setSpacingBefore(20);
        document.add(total);

        // Footer
        Font footerFont = new Font(Font.HELVETICA, 8, Font.ITALIC);
        Paragraph footer = new Paragraph("Total de registros: " + ventas.size(), footerFont);
        footer.setAlignment(Element.ALIGN_LEFT);
        footer.setSpacingBefore(20);
        document.add(footer);

        document.close();

        return out.toByteArray();
    }

    private void addTableHeader(PdfPTable table, String headerTitle, Font font) {
        PdfPCell header = new PdfPCell();
        header.setBackgroundColor(new java.awt.Color(100, 126, 234));
        header.setBorderWidth(1);
        header.setPhrase(new Phrase(headerTitle, font));
        header.setHorizontalAlignment(Element.ALIGN_CENTER);
        header.setVerticalAlignment(Element.ALIGN_MIDDLE);
        header.setPadding(5);
        table.addCell(header);
    }

    private void addTableCell(PdfPTable table, String text, Font font) {
        PdfPCell cell = new PdfPCell(new Phrase(text, font));
        cell.setPadding(5);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        table.addCell(cell);
    }
}

