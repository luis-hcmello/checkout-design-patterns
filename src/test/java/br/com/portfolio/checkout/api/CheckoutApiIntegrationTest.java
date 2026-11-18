package br.com.portfolio.checkout.api;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import br.com.portfolio.checkout.auditoria.RegistroAuditoria;

import static org.junit.jupiter.api.Assertions.assertSame;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
class CheckoutApiIntegrationTest {

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private RegistroAuditoria registroAuditoria;

    private MockMvc mockMvc;

    @BeforeEach
    void configurarMockMvc() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    @Test
    void deveFinalizarCheckoutComPix() throws Exception {
        String request = """
                {
                  "codigo": "API-TESTE-001",
                  "nomeCliente": "Cliente de Teste",
                  "formaPagamento": "pix",
                  "itens": [
                    {"descricao": "Livro", "quantidade": 2, "precoUnitario": 50.00}
                  ]
                }
                """;

        mockMvc.perform(post("/api/checkout")
                        .contentType(APPLICATION_JSON)
                        .content(request))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.codigoPedido").value("API-TESTE-001"))
                .andExpect(jsonPath("$.formaPagamento").value("PIX"))
                .andExpect(jsonPath("$.valorOriginal").value(100.00))
                .andExpect(jsonPath("$.valorFinal").value(95.00));
    }

    @Test
    void deveRetornarBadRequestQuandoQuantidadeForInvalida() throws Exception {
        String request = """
                {
                  "codigo": "API-TESTE-002",
                  "nomeCliente": "Cliente de Teste",
                  "formaPagamento": "pix",
                  "itens": [
                    {"descricao": "Livro", "quantidade": 0, "precoUnitario": 50.00}
                  ]
                }
                """;

        mockMvc.perform(post("/api/checkout")
                        .contentType(APPLICATION_JSON)
                        .content(request))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.mensagem").value("Verifique os dados enviados."))
                .andExpect(jsonPath("$.detalhes[0]").exists());
    }

    @Test
    void deveDisponibilizarOSingletonGoFComoBeanDoSpring() {
        assertSame(RegistroAuditoria.INSTANCE, registroAuditoria);
    }

    @Test
    void deveRejeitarFormaDePagamentoNaoSuportada() throws Exception {
        String request = """
                {
                  "codigo": "API-TESTE-003",
                  "nomeCliente": "Cliente de Teste",
                  "formaPagamento": "cripto",
                  "itens": [
                    {"descricao": "Livro", "quantidade": 1, "precoUnitario": 30.00}
                  ]
                }
                """;

        mockMvc.perform(post("/api/checkout")
                        .contentType(APPLICATION_JSON)
                        .content(request))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.mensagem").value("Forma de pagamento não suportada: cripto"));
    }

    @Test
    void deveListarAsFormasDePagamentoDisponiveis() throws Exception {
        mockMvc.perform(get("/api/pagamentos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].codigo").value("BOLETO"))
                .andExpect(jsonPath("$[1].codigo").value("CARTAO"))
                .andExpect(jsonPath("$[2].codigo").value("PIX"));
    }
}
