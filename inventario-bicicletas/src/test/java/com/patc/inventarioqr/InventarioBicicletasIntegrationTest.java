package com.patc.inventarioqr;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.patc.inventarioqr.service.QrService;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import javax.imageio.ImageIO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest(properties = {
    "spring.datasource.url=jdbc:h2:mem:pruebas;DB_CLOSE_DELAY=-1",
    "spring.jpa.hibernate.ddl-auto=create-drop", "app.base-url="
})
@AutoConfigureMockMvc
@Transactional
class InventarioBicicletasIntegrationTest {
    @Autowired MockMvc mvc;
    @Autowired ObjectMapper json;
    @Autowired QrService qr;

    private JsonNode producto(long id) throws Exception {
        return json.readTree(mvc.perform(get("/api/productos/" + id)).andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString());
    }

    @Test void qrGeneradoAbreLaFichaConLaDireccionActualYSePuedeLeer() throws Exception {
        int saldo = producto(1).get("stockActual").asInt();
        byte[] imagen = mvc.perform(get("/api/productos/1/qr").with(r -> {
                    r.setServerName("192.168.50.20"); r.setServerPort(8080); return r;
                }))
                .andExpect(status().isOk()).andExpect(content().contentType(MediaType.IMAGE_PNG))
                .andExpect(header().string("Cache-Control", "no-store"))
                .andReturn().getResponse().getContentAsByteArray();
        MockMultipartFile archivo = new MockMultipartFile("archivo", "qr.png", "image/png", imagen);
        assertThat(qr.leerQrDesdeImagen(archivo)).isEqualTo("http://192.168.50.20:8080/producto.html?id=1");
        mvc.perform(multipart("/api/productos/leer-qr").file(archivo))
                .andExpect(status().isOk()).andExpect(jsonPath("id").value(1))
                .andExpect(jsonPath("sku").value("BIC-MTB-29-M"));
        assertThat(producto(1).get("stockActual").asInt()).isEqualTo(saldo);
        mvc.perform(get("/producto.html?id=1")).andExpect(status().isOk());
    }

    @Test void qrAbiertoDesdeLocalhostCoincideConElEnlaceMostrado() throws Exception {
        String base = json.readTree(mvc.perform(get("/api/conexion")).andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString()).get("url").asText();
        byte[] imagen = mvc.perform(get("/api/productos/1/qr")).andReturn().getResponse().getContentAsByteArray();
        assertThat(qr.leerQrDesdeImagen(new MockMultipartFile("archivo", imagen)))
                .isEqualTo(base + "/producto.html?id=1");
    }

    @Test void conservaCompatibilidadConLasEtiquetasPatcAnteriores() throws Exception {
        String codigo = producto(1).get("codigoQr").asText();
        byte[] imagen = qr.generarQrPng(codigo, 400, 400);
        mvc.perform(multipart("/api/productos/leer-qr").file(new MockMultipartFile("archivo", "anterior.png", "image/png", imagen)))
                .andExpect(status().isOk()).andExpect(jsonPath("id").value(1));
        mvc.perform(get("/api/productos/buscar-qr").param("codigo", codigo))
                .andExpect(status().isOk()).andExpect(jsonPath("id").value(1));
    }

    @Test void imagenIlegibleOArchivoVacioNoCambiaElInventario() throws Exception {
        mvc.perform(multipart("/api/productos/leer-qr").file(new MockMultipartFile("archivo", new byte[0])))
                .andExpect(status().isBadRequest());
        mvc.perform(multipart("/api/productos/leer-qr").file(new MockMultipartFile("archivo", "texto".getBytes())))
                .andExpect(status().isBadRequest());
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        ImageIO.write(new BufferedImage(100, 100, BufferedImage.TYPE_INT_RGB), "PNG", bytes);
        mvc.perform(multipart("/api/productos/leer-qr").file(new MockMultipartFile("archivo", bytes.toByteArray())))
                .andExpect(status().isBadRequest());
    }

    @Test void qrDesconocidoDevuelveUnErrorControlado() throws Exception {
        byte[] imagen = qr.generarQrPng("CODIGO-DESCONOCIDO", 400, 400);
        mvc.perform(multipart("/api/productos/leer-qr").file(new MockMultipartFile("archivo", imagen)))
                .andExpect(status().isNotFound());
        mvc.perform(get("/api/productos/999999/qr")).andExpect(status().isNotFound());
    }

    @Test void registraLosDatosEspecificosDeUnaBicicletaYRechazaSkuRepetido() throws Exception {
        String body = """
            {"sku":"BIC-PRUEBA-27-M","nombre":"Bicicleta de prueba","unidadMedida":"unidad",
             "precioUnitario":900000,"stockActual":2,"stockMinimo":1,"categoriaId":1,
             "marca":"Marca de prueba","modelo":"MTB","talla":"M","rin":"27.5"}
            """;
        mvc.perform(post("/api/productos").contentType(MediaType.APPLICATION_JSON).content(body))
                .andExpect(status().isCreated()).andExpect(jsonPath("marca").value("Marca de prueba"))
                .andExpect(jsonPath("modelo").value("MTB")).andExpect(jsonPath("talla").value("M"))
                .andExpect(jsonPath("rin").value("27.5"));
        mvc.perform(post("/api/productos").contentType(MediaType.APPLICATION_JSON).content(body))
                .andExpect(status().isConflict());
    }

    @Test void movimientosActualizanSaldoYRechazanUnaSalidaExcesiva() throws Exception {
        int inicial = producto(3).get("stockActual").asInt();
        mvc.perform(post("/api/productos/3/movimientos").contentType(MediaType.APPLICATION_JSON)
                .content("{\"tipo\":\"ENTRADA\",\"cantidad\":5}"))
                .andExpect(status().isCreated());
        mvc.perform(post("/api/productos/3/movimientos").contentType(MediaType.APPLICATION_JSON)
                .content("{\"tipo\":\"SALIDA\",\"cantidad\":3}"))
                .andExpect(status().isCreated());
        assertThat(producto(3).get("stockActual").asInt()).isEqualTo(inicial + 2);
        mvc.perform(post("/api/productos/3/movimientos").contentType(MediaType.APPLICATION_JSON)
                .content("{\"tipo\":\"SALIDA\",\"cantidad\":9999}"))
                .andExpect(status().isBadRequest());
        assertThat(producto(3).get("stockActual").asInt()).isEqualTo(inicial + 2);
        mvc.perform(get("/api/productos/3/movimientos")).andExpect(jsonPath("$.length()").value(2));
        mvc.perform(delete("/api/productos/3")).andExpect(status().isConflict());
    }

    @Test void admiteAjusteACeroYRechazaCantidadesNegativas() throws Exception {
        mvc.perform(post("/api/productos/2/movimientos").contentType(MediaType.APPLICATION_JSON)
                .content("{\"tipo\":\"AJUSTE\",\"cantidad\":0}"))
                .andExpect(status().isCreated());
        assertThat(producto(2).get("stockActual").asInt()).isZero();
        mvc.perform(post("/api/productos/2/movimientos").contentType(MediaType.APPLICATION_JSON)
                .content("{\"tipo\":\"ENTRADA\",\"cantidad\":-1}"))
                .andExpect(status().isBadRequest());
    }
}
