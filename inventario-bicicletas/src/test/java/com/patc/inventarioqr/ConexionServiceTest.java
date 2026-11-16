package com.patc.inventarioqr;

import static org.assertj.core.api.Assertions.*;
import com.patc.inventarioqr.service.ConexionService;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;

class ConexionServiceTest {
    @Test void respetaUnDominioConfiguradoYSuRuta() {
        ConexionService service = new ConexionService("https://inventario.ejemplo.com/tienda/");
        assertThat(service.baseUrl(new MockHttpServletRequest())).isEqualTo("https://inventario.ejemplo.com/tienda");
    }
    @Test void usaLaDireccionRecibidaDesdeLaRedLocal() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setServerName("192.168.10.25"); request.setServerPort(8080);
        assertThat(new ConexionService("").baseUrl(request)).isEqualTo("http://192.168.10.25:8080");
    }
    @Test void rechazaUnaConfiguracionQueNoSeaUnEnlaceWeb() {
        assertThatThrownBy(() -> new ConexionService("archivo-local"))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
