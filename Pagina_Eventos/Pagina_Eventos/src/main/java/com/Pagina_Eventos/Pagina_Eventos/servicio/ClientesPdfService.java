package com.Pagina_Eventos.Pagina_Eventos.servicio;

import com.Pagina_Eventos.Pagina_Eventos.Entidad.Usuario;
import com.Pagina_Eventos.Pagina_Eventos.Repositorio.UsuarioRepositorio;
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
public class ClientesPdfService {

    private final UsuarioRepositorio usuarioRepositorio;

    public ClientesPdfService(UsuarioRepositorio usuarioRepositorio) {
        this.usuarioRepositorio = usuarioRepositorio;
    }

    public byte[] generarReporteClientes() throws DocumentException {
        List<Usuario> clientes = usuarioRepositorio.findAll();

        Document document = new Document(PageSize.A4.rotate());
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        PdfWriter.getInstance(document, out);
        document.open();

        // Titulo
        Font titleFont = new Font(Font.HELVETICA, 18, Font.BOLD);
        Paragraph title = new Paragraph("REPORTE DE CLIENTES", titleFont);
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
        PdfPTable table = new PdfPTable(8);
        table.setWidthPercentage(100);
        table.setSpacingBefore(10f);
        table.setSpacingAfter(10f);

        // Configurar anchos de columnas
        float[] columnWidths = {0.8f, 2f, 2f, 1.5f, 1f, 2.5f, 1.5f, 1.5f};
        table.setWidths(columnWidths);

        // Headers
        Font headerFont = new Font(Font.HELVETICA, 9, Font.BOLD);
        addTableHeader(table, "ID", headerFont);
        addTableHeader(table, "NOMBRE", headerFont);
        addTableHeader(table, "APELLIDOS", headerFont);
        addTableHeader(table, "USUARIO", headerFont);
        addTableHeader(table, "DNI", headerFont);
        addTableHeader(table, "EMAIL", headerFont);
        addTableHeader(table, "TELEFONO", headerFont);
        addTableHeader(table, "ROL", headerFont);

        // Datos
        Font cellFont = new Font(Font.HELVETICA, 8, Font.NORMAL);

        for (Usuario cliente : clientes) {
            addTableCell(table, String.valueOf(cliente.getId()), cellFont);
            addTableCell(table, cliente.getNombre() != null ? cliente.getNombre() : "N/A", cellFont);
            addTableCell(table, cliente.getApellidos() != null ? cliente.getApellidos() : "N/A", cellFont);
            addTableCell(table, cliente.getNombreusuario() != null ? cliente.getNombreusuario() : "N/A", cellFont);
            addTableCell(table, cliente.getDni() != null ? cliente.getDni() : "N/A", cellFont);
            addTableCell(table, cliente.getEmail() != null ? cliente.getEmail() : "N/A", cellFont);
            addTableCell(table, cliente.getTelefono() != null ? cliente.getTelefono() : "N/A", cellFont);
            addTableCell(table, cliente.getRolUsuario() != null ? cliente.getRolUsuario().getRol() : "N/A", cellFont);
        }

        document.add(table);

        // Footer
        Font footerFont = new Font(Font.HELVETICA, 10, Font.ITALIC);
        Paragraph footer = new Paragraph("Total de clientes registrados: " + clientes.size(), footerFont);
        footer.setAlignment(Element.ALIGN_LEFT);
        footer.setSpacingBefore(20);
        document.add(footer);

        // Estadisticas por rol
        Paragraph estadisticas = new Paragraph("ESTADISTICAS POR ROL", titleFont);
        estadisticas.setAlignment(Element.ALIGN_CENTER);
        estadisticas.setSpacingBefore(30);
        estadisticas.setSpacingAfter(15);
        document.add(estadisticas);

        // Contar por roles
        long administradores = clientes.stream().filter(c -> c.getRolUsuario() != null && c.getRolUsuario().getId() == 1).count();
        long organizadores = clientes.stream().filter(c -> c.getRolUsuario() != null && c.getRolUsuario().getId() == 2).count();
        long clientesRegulares = clientes.stream().filter(c -> c.getRolUsuario() != null && c.getRolUsuario().getId() == 3).count();

        Font statsFont = new Font(Font.HELVETICA, 11, Font.NORMAL);
        document.add(new Paragraph("Administradores: " + administradores, statsFont));
        document.add(new Paragraph("Organizadores: " + organizadores, statsFont));
        document.add(new Paragraph("Clientes: " + clientesRegulares, statsFont));

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
        cell.setPadding(4);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        table.addCell(cell);
    }
}

