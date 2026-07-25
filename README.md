# SistemaCadastroVeiculos

# Tecnologia Usadas

    - Mermaid: Criação de Diagramas de Classes e Sequência;
    - Padrão MVC: Aproveitando a separação de responsabilidades para um futuro escalonamento;
    - Java 25;
    - Maven;
    - JavaFX: Renderização de telas e pop-ups;
    - PostgreSQL 18: Armazenamento de longa prazo


# Diagrama de Sequência


# Diagrama de Classes 

```mermaid
---
config:
  theme: dark
---
classDiagram
    namespace models {
        class Veiculo {
            -int id
            -String nome
            -String cor
            -int ano
            -String modelo
            -String numeroChassi
            -String placa
            -boolean unicoDono
            +Veiculo()
            +Veiculo(int, String, String, int, String, String, String, boolean)
            +getters_setters()
        }
    }
    namespace controller {
        class VeiculoController {
            -VeiculoController()
            +salvarOuAtualizar(Veiculo, String, String, String, String, String, String, boolean)\$ boolean
            +buscarComFiltro(String, String, FiltroDono, int) List~Veiculo~
            +excluirVeiculo(int) boolean
        }
    }
    namespace repository {
        class VeiculoRepository {
            -String INS
            -String UPT
            -String DEL
            -VeiculoRepository()
            +salvar(Veiculo) boolean
            +pesquisar(String, String, FiltroDono, int) List~Veiculo~
            +update(Veiculo) boolean
            +delete(int) boolean
        }
    }
    namespace view {
        class TelaPrincipal {
            +start(Stage stage) void
        }
        class TelaPesquisar {
            -TableColumn colunaId
            -TableColumn colunaNome
            -TextField campoBusca
            -Button btnBuscar
            -int offSet
            +TelaPesquisar()
            +telaPesquisar(BorderPane) Pane
            -atualizarTabela(ObservableList~Veiculo~) void
        }
    }

    namespace view{
        class TelaSalvar{
            -TextField campoNome
            -TextField campoCor
            -TextField campoAno
            -TextField campoModelo
            -TextField campoChassi
            -TextField campoPlaca
            -Button botaoSalvar
            -Button botaoVoltar
            -CheckBox caixaSimNao
            +TelaSalvar()
            +Pane getFormularioSalvar(BorderPane, Veiculo)
        }
    }

    TelaPrincipal ..> TelaPesquisar : Instancia e Inicia
    TelaPesquisar ..> VeiculoController : Envia dados da UI
    TelaPesquisar ..> TelaSalvar : Instancia Tela de salvamento
    TelaPesquisar ..> Veiculo : Exibe na Tabela
    VeiculoController ..> VeiculoRepository : Delega persistência
    VeiculoController ..> Veiculo : Valida e Instancia
    VeiculoRepository ..> Veiculo : Preenche e Retorna

```

